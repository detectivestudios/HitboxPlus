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

package com.detectivestudios.hitboxplus.geometry;

import com.detectivestudios.hitboxplus.math.Vector3;
import java.util.Objects;

/** An axis-aligned envelope for candidate selection, not a body-part hit result. */
public record Bounds(Vector3 min, Vector3 max) {

  public Bounds {
    Objects.requireNonNull(min, "min");
    Objects.requireNonNull(max, "max");
    if (min.x() > max.x() || min.y() > max.y() || min.z() > max.z()) {
      throw new IllegalArgumentException("Minimum bounds must not exceed maximum bounds");
    }
  }

  public static Bounds between(Vector3 first, Vector3 second) {
    return new Bounds(
        new Vector3(
            Math.min(first.x(), second.x()),
            Math.min(first.y(), second.y()),
            Math.min(first.z(), second.z())),
        new Vector3(
            Math.max(first.x(), second.x()),
            Math.max(first.y(), second.y()),
            Math.max(first.z(), second.z())));
  }

  public Bounds union(Bounds other) {
    return new Bounds(
        new Vector3(
            Math.min(this.min.x(), other.min.x()),
            Math.min(this.min.y(), other.min.y()),
            Math.min(this.min.z(), other.min.z())),
        new Vector3(
            Math.max(this.max.x(), other.max.x()),
            Math.max(this.max.y(), other.max.y()),
            Math.max(this.max.z(), other.max.z())));
  }

  public Bounds inflate(double amount) {
    if (!Double.isFinite(amount) || amount < 0.0D) {
      throw new IllegalArgumentException("Inflation must be finite and nonnegative");
    }
    var padding = new Vector3(amount, amount, amount);
    return new Bounds(this.min.subtract(padding), this.max.add(padding));
  }

  public boolean intersects(Bounds other) {
    return this.min.x() <= other.max.x()
        && this.max.x() >= other.min.x()
        && this.min.y() <= other.max.y()
        && this.max.y() >= other.min.y()
        && this.min.z() <= other.max.z()
        && this.max.z() >= other.min.z();
  }

  /**
   * Conservatively tests the segment portion from zero through maxFraction, including face contact.
   * A small rounding allowance keeps transformed corner contacts in the candidate set.
   */
  public boolean intersectsSegment(Vector3 start, Vector3 end, double maxFraction, double padding) {
    if (!Double.isFinite(maxFraction)
        || maxFraction < 0
        || maxFraction > 1
        || !Double.isFinite(padding)
        || padding < 0) {
      throw new IllegalArgumentException("Invalid segment fraction or padding");
    }
    double enter = 0;
    double exit = maxFraction;
    for (int axis = 0; axis < 3; axis++) {
      double min = this.min.component(axis) - padding;
      double max = this.max.component(axis) + padding;
      if (!Double.isFinite(min) || !Double.isFinite(max)) {
        throw new IllegalArgumentException("Inflated bounds must be finite");
      }
      double origin = start.component(axis);
      double endpoint = end.component(axis);
      double rounding =
          16
              * Math.ulp(
                  Math.max(
                      Math.max(Math.abs(min), Math.abs(max)),
                      Math.max(Math.abs(origin), Math.abs(endpoint))));
      min -= rounding;
      max += rounding;
      double delta = endpoint - origin;
      if (delta == 0) {
        if (origin < min || origin > max) {
          return false;
        }
      } else {
        double first = (min - origin) / delta;
        double second = (max - origin) / delta;
        enter = Math.max(enter, Math.min(first, second));
        exit = Math.min(exit, Math.max(first, second));
        if (enter > exit) {
          return false;
        }
      }
    }
    return true;
  }
}
