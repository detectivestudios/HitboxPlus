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

package com.detectivestudios.hitboxplus.math;

/** Coordinates in blocks. */
public record Vector3(double x, double y, double z) {

  public static final Vector3 ZERO = new Vector3(0.0D, 0.0D, 0.0D);

  public Vector3 {
    if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) {
      throw new IllegalArgumentException("Coordinates must be finite");
    }
  }

  public Vector3 add(Vector3 other) {
    return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
  }

  public Vector3 subtract(Vector3 other) {
    return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
  }

  public Vector3 scale(double scale) {
    return new Vector3(this.x * scale, this.y * scale, this.z * scale);
  }

  public Vector3 lerp(Vector3 other, double fraction) {
    return this.add(other.subtract(this).scale(fraction));
  }

  public double component(int axis) {
    return switch (axis) {
      case 0 -> this.x;
      case 1 -> this.y;
      case 2 -> this.z;
      default -> throw new IllegalArgumentException("Axis must be 0, 1 or 2");
    };
  }
}
