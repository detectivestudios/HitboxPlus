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

import com.detectivestudios.hitboxplus.pose.HumanoidAnimation;
import com.detectivestudios.hitboxplus.pose.HumanoidPose;

/**
 * Samples on the entity's game thread without advancing animation state. Treat the input as
 * read-only and return a fresh mutable pose: enclosing animations may modify it before geometry is
 * created.
 */
@FunctionalInterface
public interface HumanoidPoseAnimation {

  HumanoidPose evaluate(HumanoidAnimation.Input input);
}
