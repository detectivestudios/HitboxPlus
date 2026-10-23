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

import java.util.List;

/**
 * Trusted model definitions. List order identifies volumes across motion observations and breaks
 * equal-distance ties. Keep the same profile throughout a traced motion interval.
 */
public record HumanoidHitboxProfile(List<BodyVolume> volumes) {

  public HumanoidHitboxProfile {
    volumes = List.copyOf(volumes);
  }
}
