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

package com.detectivestudios.hitboxplus.profile.vanilla.v118;

import com.detectivestudios.hitboxplus.api.HitboxPart;
import com.detectivestudios.hitboxplus.geometry.Bounds;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.profile.EntityHitboxProfile;

public class ProfileVolumes {

  public EntityHitboxProfile.Volume<String> box(
      HitboxPart part,
      String bone,
      double minX,
      double minY,
      double minZ,
      double maxX,
      double maxY,
      double maxZ) {
    return new EntityHitboxProfile.Volume<>(
        part, bone, new Bounds(new Vector3(minX, minY, minZ), new Vector3(maxX, maxY, maxZ)));
  }
}
