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

import java.util.Optional;

@FunctionalInterface
public interface EntityHitboxProvider {

  /**
   * Sample on the entity's game thread; immutable results can then be traced on other threads.
   * Empty keeps ordinary collision for an unsupported model or state.
   */
  Optional<HitboxPose> getPose(float partialTick);

  /** Optional animated skeleton used to apply geometry overrides without replacing movement. */
  default Optional<EntityModelPose> getModelPose(float partialTick) {
    return Optional.empty();
  }
}
