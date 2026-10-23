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

/** Common damage regions. Left and right refer to the target entity's own sides. */
public enum BodyPart implements HitboxPart {

  HEAD,
  TORSO,
  LEFT_ARM,
  RIGHT_ARM,
  LEFT_LEG,
  RIGHT_LEG,
  LEFT_FOOT,
  RIGHT_FOOT,
  NECK,
  FRONT_LEFT_LEG,
  FRONT_RIGHT_LEG,
  HIND_LEFT_LEG,
  HIND_RIGHT_LEG,
  TAIL,
  LEFT_WING,
  RIGHT_WING,
  LEFT_FIN,
  RIGHT_FIN,
  FIN,
  TENTACLE,
  OTHER;

  @Override
  public BodyPart region() {
    return this;
  }
}
