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

/** A normalized quaternion. Axis-angle input uses right-handed rotations in radians. */
public record Rotation(double x, double y, double z, double w) {

  public static final Rotation IDENTITY = new Rotation(0.0D, 0.0D, 0.0D, 1.0D);

  public Rotation {
    double squared = x * x + y * y + z * z + w * w;
    double length =
        squared >= Double.MIN_NORMAL && Double.isFinite(squared)
            ? Math.sqrt(squared)
            : Math.hypot(Math.hypot(x, y), Math.hypot(z, w));
    if (!Double.isFinite(length) || length == 0.0D) {
      throw new IllegalArgumentException("Rotation must have a finite nonzero length");
    }
    x /= length;
    y /= length;
    z /= length;
    w /= length;
  }

  public static Rotation axisAngle(Vector3 axis, double radians) {
    double length = Math.hypot(Math.hypot(axis.x(), axis.y()), axis.z());
    if (length == 0.0D || !Double.isFinite(length) || !Double.isFinite(radians)) {
      throw new IllegalArgumentException("Axis and angle must define a finite rotation");
    }
    double sine = Math.sin(radians * 0.5D);
    return new Rotation(
        axis.x() / length * sine,
        axis.y() / length * sine,
        axis.z() / length * sine,
        Math.cos(radians * 0.5D));
  }

  public Vector3 transform(Vector3 vector) {
    return this.rotate(vector, 1.0D);
  }

  /** Composition in matrix order: other is applied first. */
  public Rotation multiply(Rotation other) {
    if (this == IDENTITY) {
      return other;
    }
    if (other == IDENTITY) {
      return this;
    }
    return new Rotation(
        this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y,
        this.w * other.y - this.x * other.z + this.y * other.w + this.z * other.x,
        this.w * other.z + this.x * other.y - this.y * other.x + this.z * other.w,
        this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z);
  }

  public static Rotation euler(double x, double y, double z) {
    if (x == 0.0D && y == 0.0D && z == 0.0D) {
      return IDENTITY;
    }
    double sx = Math.sin(x * 0.5D);
    double cx = Math.cos(x * 0.5D);
    double sy = Math.sin(y * 0.5D);
    double cy = Math.cos(y * 0.5D);
    double sz = Math.sin(z * 0.5D);
    double cz = Math.cos(z * 0.5D);
    return new Rotation(
        sx * cy * cz - cx * sy * sz,
        cx * sy * cz + sx * cy * sz,
        cx * cy * sz - sx * sy * cz,
        cx * cy * cz + sx * sy * sz);
  }

  public double angularDistance(Rotation other) {
    double dot = this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    return 2.0D * Math.acos(Math.min(1.0D, Math.abs(dot)));
  }

  public Rotation interpolate(Rotation other, double fraction) {
    double dot = this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    double sign = dot < 0.0D ? -1.0D : 1.0D;
    double angle = Math.acos(Math.min(1.0D, Math.abs(dot)));
    double first = 1.0D - fraction;
    double second = fraction;
    if (angle > 1.0E-8D) {
      first = Math.sin(first * angle) / Math.sin(angle);
      second = Math.sin(second * angle) / Math.sin(angle);
    }
    return new Rotation(
        this.x * first + other.x * second * sign,
        this.y * first + other.y * second * sign,
        this.z * first + other.z * second * sign,
        this.w * first + other.w * second * sign);
  }

  public Vector3 inverseTransform(Vector3 vector) {
    return this.rotate(vector, -1.0D);
  }

  private Vector3 rotate(Vector3 vector, double sign) {
    double qx = this.x * sign;
    double qy = this.y * sign;
    double qz = this.z * sign;
    double tx = 2.0D * (qy * vector.z() - qz * vector.y());
    double ty = 2.0D * (qz * vector.x() - qx * vector.z());
    double tz = 2.0D * (qx * vector.y() - qy * vector.x());
    return new Vector3(
        vector.x() + this.w * tx + qy * tz - qz * ty,
        vector.y() + this.w * ty + qz * tx - qx * tz,
        vector.z() + this.w * tz + qx * ty - qy * tx);
  }
}
