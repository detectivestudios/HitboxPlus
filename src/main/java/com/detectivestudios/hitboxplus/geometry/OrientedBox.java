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

import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import java.util.Objects;
import java.util.Optional;

public record OrientedBox(Vector3 center, Vector3 halfExtents, Rotation rotation) {

  public OrientedBox {
    Objects.requireNonNull(center, "center");
    Objects.requireNonNull(halfExtents, "halfExtents");
    Objects.requireNonNull(rotation, "rotation");
    if (halfExtents.x() < 0.0D || halfExtents.y() < 0.0D || halfExtents.z() < 0.0D) {
      throw new IllegalArgumentException("Half extents must be nonnegative");
    }
  }

  public Bounds bounds() {
    double x = this.rotation.x();
    double y = this.rotation.y();
    double z = this.rotation.z();
    double w = this.rotation.w();
    double ex = this.halfExtents.x();
    double ey = this.halfExtents.y();
    double ez = this.halfExtents.z();
    var worldExtents =
        new Vector3(
            Math.abs(1 - 2 * (y * y + z * z)) * ex
                + Math.abs(2 * (x * y - z * w)) * ey
                + Math.abs(2 * (x * z + y * w)) * ez,
            Math.abs(2 * (x * y + z * w)) * ex
                + Math.abs(1 - 2 * (x * x + z * z)) * ey
                + Math.abs(2 * (y * z - x * w)) * ez,
            Math.abs(2 * (x * z - y * w)) * ex
                + Math.abs(2 * (y * z + x * w)) * ey
                + Math.abs(1 - 2 * (x * x + y * y)) * ez);
    return new Bounds(this.center.subtract(worldExtents), this.center.add(worldExtents));
  }

  /**
   * Clips a finite segment, including endpoints and face contact. Inflation expands each local box
   * face; it does not represent a spherical projectile. A start inside the box has fraction zero
   * and a zero normal because no entry face was crossed.
   */
  public Optional<Intersection> clip(Vector3 start, Vector3 end, double inflation) {
    if (!Double.isFinite(inflation) || inflation < 0.0D) {
      throw new IllegalArgumentException("Inflation must be finite and nonnegative");
    }
    Vector3 localStart = this.rotation.inverseTransform(start.subtract(this.center));
    Vector3 localEnd = this.rotation.inverseTransform(end.subtract(this.center));
    Vector3 movement = localEnd.subtract(localStart);
    double enter = 0.0D;
    double exit = 1.0D;
    int entryAxis = -1;
    double entrySign = 0.0D;
    for (int axis = 0; axis < 3; axis++) {
      double extent = this.halfExtents.component(axis) + inflation;
      if (!Double.isFinite(extent)) {
        throw new IllegalArgumentException("Inflated half extents must be finite");
      }
      double origin = localStart.component(axis);
      double delta = movement.component(axis);
      if (delta == 0.0D) {
        if (origin < -extent || origin > extent) {
          return Optional.empty();
        }
        continue;
      }
      double first = (-extent - origin) / delta;
      double second = (extent - origin) / delta;
      double near = Math.min(first, second);
      double far = Math.max(first, second);
      if (near > enter) {
        enter = near;
        entryAxis = axis;
        entrySign = delta > 0.0D ? -1.0D : 1.0D;
      }
      exit = Math.min(exit, far);
      if (enter > exit) {
        return Optional.empty();
      }
    }
    Vector3 normal =
        switch (entryAxis) {
          case 0 -> new Vector3(entrySign, 0.0D, 0.0D);
          case 1 -> new Vector3(0.0D, entrySign, 0.0D);
          case 2 -> new Vector3(0.0D, 0.0D, entrySign);
          default -> Vector3.ZERO;
        };
    return Optional.of(
        new Intersection(start.lerp(end, enter), this.rotation.transform(normal), enter));
  }

  public record Intersection(Vector3 position, Vector3 normal, double fraction) {}

  /**
   * Returns the first segment contact in [0, 1], or NaN on a miss, without creating hit objects.
   */
  public double clipFraction(Vector3 start, Vector3 end, double inflation) {
    if (!Double.isFinite(inflation) || inflation < 0) {
      throw new IllegalArgumentException("Inflation must be finite and nonnegative");
    }
    double qx = -this.rotation.x();
    double qy = -this.rotation.y();
    double qz = -this.rotation.z();
    double qw = this.rotation.w();
    double x = start.x() - this.center.x();
    double y = start.y() - this.center.y();
    double z = start.z() - this.center.z();
    double tx = 2 * (qy * z - qz * y);
    double ty = 2 * (qz * x - qx * z);
    double tz = 2 * (qx * y - qy * x);
    double sx = x + qw * tx + qy * tz - qz * ty;
    double sy = y + qw * ty + qz * tx - qx * tz;
    double sz = z + qw * tz + qx * ty - qy * tx;
    x = end.x() - this.center.x();
    y = end.y() - this.center.y();
    z = end.z() - this.center.z();
    tx = 2 * (qy * z - qz * y);
    ty = 2 * (qz * x - qx * z);
    tz = 2 * (qx * y - qy * x);
    double ex = x + qw * tx + qy * tz - qz * ty;
    double ey = y + qw * ty + qz * tx - qx * tz;
    double ez = z + qw * tz + qx * ty - qy * tx;
    if (!Double.isFinite(sx)
        || !Double.isFinite(sy)
        || !Double.isFinite(sz)
        || !Double.isFinite(ex)
        || !Double.isFinite(ey)
        || !Double.isFinite(ez)) {
      throw new IllegalArgumentException("Local segment coordinates must be finite");
    }
    double enter = 0;
    double exit = 1;
    for (int axis = 0; axis < 3; axis++) {
      double extent = this.halfExtents.component(axis) + inflation;
      if (!Double.isFinite(extent)) {
        throw new IllegalArgumentException("Inflated half extents must be finite");
      }
      double origin = axis == 0 ? sx : axis == 1 ? sy : sz;
      double delta = axis == 0 ? ex - sx : axis == 1 ? ey - sy : ez - sz;
      if (delta == 0) {
        if (origin < -extent || origin > extent) {
          return Double.NaN;
        }
      } else {
        double first = (-extent - origin) / delta;
        double second = (extent - origin) / delta;
        enter = Math.max(enter, Math.min(first, second));
        exit = Math.min(exit, Math.max(first, second));
        if (enter > exit) {
          return Double.NaN;
        }
      }
    }
    return enter;
  }
}
