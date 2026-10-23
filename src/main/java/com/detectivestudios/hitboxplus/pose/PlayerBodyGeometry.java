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

package com.detectivestudios.hitboxplus.pose;

import com.detectivestudios.hitboxplus.api.BodyHitbox;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.geometry.OrientedBox;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.profile.BodyVolume;
import com.detectivestudios.hitboxplus.profile.HumanoidHitboxProfile;
import com.detectivestudios.hitboxplus.profile.PlayerHitboxProfiles;
import com.detectivestudios.hitboxplus.profile.SkinLayer;
import java.util.ArrayList;

public class PlayerBodyGeometry {

  private final PlayerHitboxProfiles profiles = new PlayerHitboxProfiles();

  public HitboxPose create(
      HumanoidPose pose,
      Vector3 origin,
      Rotation rootRotation,
      double scale,
      boolean slim,
      Layers layers) {
    return this.create(this.profiles.select(slim), pose, origin, rootRotation, scale, layers);
  }

  public HitboxPose create(
      HumanoidHitboxProfile profile,
      HumanoidPose pose,
      Vector3 origin,
      Rotation rootRotation,
      double scale,
      Layers layers) {
    if (!Double.isFinite(scale) || scale <= 0.0D) {
      throw new IllegalArgumentException("Model scale must be finite and positive");
    }
    var boxes = new ArrayList<BodyHitbox>(profile.volumes().size());
    for (BodyVolume volume : profile.volumes()) {
      var part = pose.part(volume.bone());
      var bounds = layers.enabled(volume.layer()) ? volume.layeredBounds() : volume.bounds();
      Vector3 center = bounds.min().add(bounds.max()).scale(0.5D);
      Vector3 halfExtents = bounds.max().subtract(bounds.min()).scale(0.5D);
      Rotation local = Rotation.euler(part.xRot, part.yRot, part.zRot);
      Vector3 transformed =
          new Vector3(part.x, part.y, part.z).add(local.transform(center)).scale(scale);
      boxes.add(
          new BodyHitbox(
              volume.part(),
              new OrientedBox(
                  origin.add(rootRotation.transform(transformed)),
                  halfExtents.scale(scale),
                  rootRotation.multiply(local))));
    }
    return new HitboxPose(boxes);
  }

  public record Layers(
      boolean hat,
      boolean jacket,
      boolean rightSleeve,
      boolean leftSleeve,
      boolean rightPants,
      boolean leftPants) {

    public boolean enabled(SkinLayer layer) {
      return switch (layer) {
        case NONE -> false;
        case HAT -> this.hat;
        case JACKET -> this.jacket;
        case RIGHT_SLEEVE -> this.rightSleeve;
        case LEFT_SLEEVE -> this.leftSleeve;
        case RIGHT_PANTS -> this.rightPants;
        case LEFT_PANTS -> this.leftPants;
      };
    }
  }
}
