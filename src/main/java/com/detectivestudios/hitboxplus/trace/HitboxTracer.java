/*
 * Hitbox+ by DetectiveStudios
 * Copyright (c) 2026 DetectiveStudios and the respective contributors.
 *
 * Licensed under the DetectiveStudios Source-Available Contribution License.
 * See LICENSE.txt for the complete terms and third-party exceptions.
 *
 * Contribution forks and pull requests to the official GitHub repository are
 * permitted. Reuse in other projects and redistribution require separate written
 * permission, except as expressly provided in the license.
 */

package com.detectivestudios.hitboxplus.trace;

import com.detectivestudios.hitboxplus.api.BodyHit;
import com.detectivestudios.hitboxplus.api.BodyHitbox;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.math.Vector3;
import java.util.Optional;

/** Nearest body-part tracing against one pose. World occlusion belongs to the caller. */
public class HitboxTracer {

  /** Includes segment endpoints. Equal-distance body parts use pose list order. */
  public Optional<BodyHit> trace(HitboxPose pose, Vector3 start, Vector3 end, double inflation) {
    return this.trace(pose, start, end, inflation, 1.0D, false);
  }

  /**
   * obstructionFraction is the earliest world obstruction on the same start/end segment.
   * Obstructions win ties, including at fraction zero or one. Empty space between body parts never
   * selects a fallback part.
   */
  public Optional<BodyHit> trace(
      HitboxPose pose, Vector3 start, Vector3 end, double inflation, double obstructionFraction) {
    return this.trace(pose, start, end, inflation, obstructionFraction, true);
  }

  private Optional<BodyHit> trace(
      HitboxPose pose,
      Vector3 start,
      Vector3 end,
      double inflation,
      double maxFraction,
      boolean obstructed) {
    if (!Double.isFinite(maxFraction) || maxFraction < 0.0D || maxFraction > 1.0D) {
      throw new IllegalArgumentException("Maximum fraction must lie between zero and one");
    }
    if (!Double.isFinite(inflation) || inflation < 0.0D) {
      throw new IllegalArgumentException("Inflation must be finite and nonnegative");
    }
    if (pose.bounds().isEmpty()) {
      return Optional.empty();
    }
    // Inflating local OBB faces can enlarge a world axis by up to sqrt(3) times the padding.
    if (!pose.bounds()
        .orElseThrow()
        .intersectsSegment(start, end, maxFraction, inflation * Math.sqrt(3.0D))) {
      return Optional.empty();
    }
    BodyHitbox nearest = null;
    double nearestFraction = maxFraction;
    for (BodyHitbox hitbox : pose.hitboxes()) {
      double fraction = hitbox.box().clipFraction(start, end, inflation);
      if (Double.isNaN(fraction)) {
        continue;
      }
      if (fraction > maxFraction || (obstructed && fraction == maxFraction)) {
        continue;
      }
      if (nearest == null || fraction < nearestFraction) {
        nearest = hitbox;
        nearestFraction = fraction;
      }
    }
    if (nearest == null) {
      return Optional.empty();
    }
    var contact = nearest.box().clip(start, end, inflation).orElseThrow();
    return Optional.of(
        new BodyHit(nearest.part(), contact.position(), contact.normal(), contact.fraction()));
  }
}
