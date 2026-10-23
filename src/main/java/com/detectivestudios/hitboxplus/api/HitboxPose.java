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

package com.detectivestudios.hitboxplus.api;

import com.detectivestudios.hitboxplus.geometry.Bounds;
import java.util.List;
import java.util.Optional;

/** Immutable observed geometry suitable for ownership by a server snapshot/history. */
public class HitboxPose {

  private final List<BodyHitbox> hitboxes;
  private final Optional<Bounds> bounds;

  public HitboxPose(List<BodyHitbox> hitboxes) {
    this.hitboxes = List.copyOf(hitboxes);
    double minX = Double.POSITIVE_INFINITY;
    double minY = Double.POSITIVE_INFINITY;
    double minZ = Double.POSITIVE_INFINITY;
    double maxX = Double.NEGATIVE_INFINITY;
    double maxY = Double.NEGATIVE_INFINITY;
    double maxZ = Double.NEGATIVE_INFINITY;
    for (BodyHitbox hitbox : this.hitboxes) {
      var box = hitbox.box().bounds();
      minX = Math.min(minX, box.min().x());
      minY = Math.min(minY, box.min().y());
      minZ = Math.min(minZ, box.min().z());
      maxX = Math.max(maxX, box.max().x());
      maxY = Math.max(maxY, box.max().y());
      maxZ = Math.max(maxZ, box.max().z());
    }
    this.bounds =
        this.hitboxes.isEmpty()
            ? Optional.empty()
            : Optional.of(
                new Bounds(
                    new com.detectivestudios.hitboxplus.math.Vector3(minX, minY, minZ),
                    new com.detectivestudios.hitboxplus.math.Vector3(maxX, maxY, maxZ)));
  }

  public List<BodyHitbox> hitboxes() {
    return this.hitboxes;
  }

  /** Whether a continuous trace can pair every volume with the same part in the other pose. */
  public boolean matchesTopology(HitboxPose other) {
    if (this.hitboxes.size() != other.hitboxes.size()) {
      return false;
    }
    for (int i = 0; i < this.hitboxes.size(); i++) {
      if (!this.hitboxes.get(i).part().equals(other.hitboxes.get(i).part())) {
        return false;
      }
    }
    return true;
  }

  public Optional<Bounds> bounds() {
    return this.bounds;
  }
}
