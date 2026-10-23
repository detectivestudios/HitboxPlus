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

package com.detectivestudios.hitboxplus.profile;

import com.detectivestudios.hitboxplus.api.HitboxPart;
import com.detectivestudios.hitboxplus.geometry.Bounds;
import com.detectivestudios.hitboxplus.math.BoneTransform;
import java.util.List;
import java.util.Objects;

/**
 * Trusted model definition independent of any entity class or skeleton. Bone keys may be enums,
 * strings, or adapter-owned immutable values. Preserve volume order across motion observations.
 */
public record EntityHitboxProfile<B>(List<Volume<B>> volumes) {

  public EntityHitboxProfile {
    volumes = List.copyOf(volumes);
  }

  /** Bounds are relative to the resolved bone, in the adapter's model units. */
  public record Volume<B>(HitboxPart part, B bone, Bounds bounds, BoneTransform localTransform) {

    public Volume(HitboxPart part, B bone, Bounds bounds) {
      this(part, bone, bounds, BoneTransform.IDENTITY);
    }

    public Volume {
      Objects.requireNonNull(part, "part");
      Objects.requireNonNull(bone, "bone");
      Objects.requireNonNull(bounds, "bounds");
      Objects.requireNonNull(localTransform, "localTransform");
    }
  }
}
