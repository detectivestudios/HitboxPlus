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
import com.detectivestudios.hitboxplus.geometry.Bounds;
import com.detectivestudios.hitboxplus.geometry.OrientedBox;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import java.util.Optional;

/** Continuous linear center/size motion and shortest-path quaternion rotation over one interval. */
public class MovingHitboxTracer {

  /** Maximum local-face distance admitted by the conservative rotating-box solver, in blocks. */
  public static final double CONTACT_TOLERANCE = 1.0E-5D;

  public Optional<BodyHit> trace(
      HitboxPose first, HitboxPose second, Vector3 start, Vector3 end, double inflation) {
    if (!Double.isFinite(inflation) || inflation < 0.0D) {
      throw new IllegalArgumentException("Inflation must be finite and nonnegative");
    }
    if (first.hitboxes().size() != second.hitboxes().size()) {
      throw new IllegalArgumentException("Motion endpoints must have matching body parts");
    }
    BodyHit nearest = null;
    for (int index = 0; index < first.hitboxes().size(); index++) {
      BodyHitbox a = first.hitboxes().get(index);
      BodyHitbox b = second.hitboxes().get(index);
      if (!a.part().equals(b.part())) {
        throw new IllegalArgumentException("Motion endpoints must have matching body parts");
      }
      var motion = new Motion(a.box(), b.box(), start, end, inflation);
      double limit = nearest == null ? 1.0D : nearest.fraction();
      double radius =
          Math.max(this.length(a.box().halfExtents()), this.length(b.box().halfExtents()))
              + Math.sqrt(3.0D) * inflation;
      if (!Bounds.between(a.box().center(), b.box().center())
          .inflate(radius)
          .intersects(Bounds.between(start, start.lerp(end, limit)))) {
        continue;
      }
      double fraction;
      if (motion.angle == 0.0D) {
        fraction = this.linear(motion, limit);
      } else {
        double[] interval = this.rotationInterval(motion, radius, limit);
        fraction = interval == null ? Double.NaN : this.rotating(motion, interval[0], interval[1]);
      }
      if (Double.isFinite(fraction) && (nearest == null || fraction < nearest.fraction())) {
        nearest =
            new BodyHit(
                a.part(),
                start.lerp(end, fraction),
                fraction == 0.0D ? Vector3.ZERO : this.normal(motion, fraction),
                fraction);
      }
    }
    return Optional.ofNullable(nearest);
  }

  private double linear(Motion motion, double limit) {
    Vector3 start = motion.first.rotation().inverseTransform(motion.relativeStart);
    Vector3 end = motion.first.rotation().inverseTransform(motion.relativeEnd);
    double enter = 0.0D;
    double exit = limit;
    for (int axis = 0; axis < 3; axis++) {
      double movement = end.component(axis) - start.component(axis);
      double initialExtent = motion.first.halfExtents().component(axis) + motion.inflation;
      double extentDelta =
          motion.second.halfExtents().component(axis) - motion.first.halfExtents().component(axis);
      for (int face = 0; face < 2; face++) {
        double distance =
            initialExtent + (face == 0 ? start.component(axis) : -start.component(axis));
        double speed = extentDelta + (face == 0 ? movement : -movement);
        if (speed == 0.0D) {
          if (distance < 0.0D) {
            return Double.NaN;
          }
        } else if (speed > 0.0D) {
          enter = Math.max(enter, -distance / speed);
        } else {
          exit = Math.min(exit, -distance / speed);
        }
        if (enter > exit) {
          return Double.NaN;
        }
      }
    }
    return enter;
  }

  private double rotating(Motion motion, double first, double last) {
    if (this.gap(motion, first) <= 0.0D) {
      return first;
    }
    double middle = (first + last) * 0.5D;
    double possibleChange = motion.speedBound * (last - first) * 0.5D;
    if (this.gap(motion, middle) > possibleChange) {
      return Double.NaN;
    }
    // The Lipschitz bound proves this interval stays within the declared contact tolerance.
    // Splitting uncertain intervals, rather than fixed substeps, preserves brief crossing hits.
    if (possibleChange * 2.0D <= CONTACT_TOLERANCE || middle == first || middle == last) {
      return first;
    }
    double left = this.rotating(motion, first, middle);
    return Double.isFinite(left) ? left : this.rotating(motion, middle, last);
  }

  private double[] rotationInterval(Motion motion, double radius, double limit) {
    double[] interval = {0.0D, limit};
    Vector3 movement = motion.relativeEnd.subtract(motion.relativeStart);
    for (int axis = 0; axis < 3; axis++) {
      if (this.outsideProjection(
          motion.relativeStart.component(axis),
          movement.component(axis),
          radius,
          radius,
          interval)) {
        return null;
      }
    }
    Rotation first = motion.first.rotation();
    Rotation delta =
        motion
            .second
            .rotation()
            .multiply(new Rotation(-first.x(), -first.y(), -first.z(), first.w()));
    var axis = new Vector3(delta.x(), delta.y(), delta.z());
    double length = this.length(axis);
    if (length > 1.0E-12D) {
      axis = axis.scale(1.0D / length);
      Vector3 local = first.inverseTransform(axis);
      // Projection onto the slerp rotation axis is invariant, giving an exact cheap rejection
      // for shots skimming a rotating face parallel to that axis.
      double padding =
          (motion.inflation + CONTACT_TOLERANCE)
              * (Math.abs(local.x()) + Math.abs(local.y()) + Math.abs(local.z()));
      double start = this.support(local, motion.first.halfExtents()) + padding;
      double end = this.support(local, motion.second.halfExtents()) + padding;
      if (this.outsideProjection(
          this.dot(axis, motion.relativeStart), this.dot(axis, movement), start, end, interval)) {
        return null;
      }
    }
    return interval;
  }

  private boolean outsideProjection(
      double origin, double movement, double firstExtent, double lastExtent, double[] interval) {
    for (int face = 0; face < 2; face++) {
      double distance = firstExtent + (face == 0 ? origin : -origin);
      double speed = lastExtent - firstExtent + (face == 0 ? movement : -movement);
      if (speed == 0.0D) {
        if (distance < 0.0D) {
          return true;
        }
      } else if (speed > 0.0D) {
        interval[0] = Math.max(interval[0], -distance / speed);
      } else {
        interval[1] = Math.min(interval[1], -distance / speed);
      }
      if (interval[0] > interval[1]) {
        return true;
      }
    }
    return false;
  }

  private double support(Vector3 axis, Vector3 extent) {
    return Math.abs(axis.x()) * extent.x()
        + Math.abs(axis.y()) * extent.y()
        + Math.abs(axis.z()) * extent.z();
  }

  private double dot(Vector3 first, Vector3 second) {
    return first.x() * second.x() + first.y() * second.y() + first.z() * second.z();
  }

  private double gap(Motion motion, double fraction) {
    Rotation rotation = motion.first.rotation().interpolate(motion.second.rotation(), fraction);
    Vector3 point =
        rotation.inverseTransform(motion.relativeStart.lerp(motion.relativeEnd, fraction));
    Vector3 extent = motion.first.halfExtents().lerp(motion.second.halfExtents(), fraction);
    return Math.max(
            Math.abs(point.x()) - extent.x(),
            Math.max(Math.abs(point.y()) - extent.y(), Math.abs(point.z()) - extent.z()))
        - motion.inflation;
  }

  private Vector3 normal(Motion motion, double fraction) {
    Rotation rotation = motion.first.rotation().interpolate(motion.second.rotation(), fraction);
    Vector3 point =
        rotation.inverseTransform(motion.relativeStart.lerp(motion.relativeEnd, fraction));
    Vector3 extent = motion.first.halfExtents().lerp(motion.second.halfExtents(), fraction);
    double greatest = Double.NEGATIVE_INFINITY;
    int face = 0;
    for (int axis = 0; axis < 3; axis++) {
      double gap = Math.abs(point.component(axis)) - extent.component(axis);
      if (gap > greatest) {
        face = axis;
        greatest = gap;
      }
    }
    double sign = point.component(face) < 0.0D ? -1.0D : 1.0D;
    Vector3 normal =
        switch (face) {
          case 0 -> new Vector3(sign, 0.0D, 0.0D);
          case 1 -> new Vector3(0.0D, sign, 0.0D);
          default -> new Vector3(0.0D, 0.0D, sign);
        };
    return rotation.transform(normal);
  }

  private double length(Vector3 vector) {
    return Math.hypot(Math.hypot(vector.x(), vector.y()), vector.z());
  }

  private class Motion {

    private final OrientedBox first;
    private final OrientedBox second;
    private final Vector3 relativeStart;
    private final Vector3 relativeEnd;
    private final double inflation;
    private final double angle;
    private final double speedBound;

    public Motion(
        OrientedBox first, OrientedBox second, Vector3 start, Vector3 end, double inflation) {
      this.first = first;
      this.second = second;
      this.relativeStart = start.subtract(first.center());
      this.relativeEnd = end.subtract(second.center());
      this.inflation = inflation;
      this.angle =
          first.rotation().equals(second.rotation())
              ? 0.0D
              : first.rotation().angularDistance(second.rotation());
      this.speedBound =
          MovingHitboxTracer.this.length(this.relativeEnd.subtract(this.relativeStart))
              + this.angle
                  * Math.max(
                      MovingHitboxTracer.this.length(this.relativeStart),
                      MovingHitboxTracer.this.length(this.relativeEnd))
              + MovingHitboxTracer.this.length(second.halfExtents().subtract(first.halfExtents()));
    }
  }
}
