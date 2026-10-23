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

package com.detectivestudios.hitboxplus.pose.geckolib;

import com.detectivestudios.hitboxplus.math.BoneTransform;
import java.util.Map;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;

/** Addon hook for server-owned controller state and custom renderer/root transforms. */
@FunctionalInterface
public interface GeckoLibAnimationSource {

  Sample sample(LivingEntity entity, GeckoLibRig rig, float partialTick);

  /** Optional procedural corrections, with fully composed model-space transforms for all bones. */
  default Map<String, BoneTransform> adjustBones(
      LivingEntity entity, Map<String, BoneTransform> bones, float partialTick) {
    return bones;
  }

  record Sample(String clip, double seconds, Optional<BoneTransform> worldRoot) {

    public Sample(String clip, double seconds) {
      this(clip, seconds, Optional.empty());
    }
  }
}
