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

import java.util.Objects;

/** Rigid transform with positive uniform scale; compose parent and child transforms explicitly. */
public record BoneTransform(Vector3 translation, Rotation rotation, double scale) {

  public static final BoneTransform IDENTITY =
      new BoneTransform(Vector3.ZERO, Rotation.IDENTITY, 1.0D);

  public BoneTransform {
    Objects.requireNonNull(translation, "translation");
    Objects.requireNonNull(rotation, "rotation");
    if (!Double.isFinite(scale) || scale <= 0.0D) {
      throw new IllegalArgumentException("Bone scale must be finite and positive");
    }
  }

  public Vector3 transform(Vector3 point) {
    return this.translation.add(this.rotation.transform(point.scale(this.scale)));
  }

  /** Returns this parent transform composed with the child's local transform. */
  public BoneTransform compose(BoneTransform child) {
    if (this == IDENTITY) {
      return child;
    }
    if (child == IDENTITY) {
      return this;
    }
    return new BoneTransform(
        this.transform(child.translation),
        this.rotation.multiply(child.rotation),
        this.scale * child.scale);
  }
}
