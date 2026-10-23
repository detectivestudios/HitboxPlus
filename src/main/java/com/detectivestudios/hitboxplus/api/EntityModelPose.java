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

import com.detectivestudios.hitboxplus.math.BoneTransform;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.pose.HumanoidPose;
import com.detectivestudios.hitboxplus.pose.PlayerBodyGeometry;
import com.detectivestudios.hitboxplus.profile.HumanoidBone;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Server-evaluated bone transforms in model pixels and a root that maps pixels into world blocks.
 */
public record EntityModelPose(
    Map<String, BoneTransform> bones, BoneTransform root, String variant) {

  public EntityModelPose {
    bones = Map.copyOf(bones);
    Objects.requireNonNull(root, "root");
    Objects.requireNonNull(variant, "variant");
  }

  public static EntityModelPose humanoid(HumanoidPose pose, BoneTransform root, String variant) {
    var bones = new LinkedHashMap<String, BoneTransform>();
    bones.put("$root", BoneTransform.IDENTITY);
    for (HumanoidBone key : HumanoidBone.values()) {
      var part = pose.part(key);
      bones.put(
          key.name().toLowerCase(Locale.ROOT),
          new BoneTransform(
              new Vector3(part.x, part.y, part.z),
              Rotation.euler(part.xRot, part.yRot, part.zRot),
              1));
    }
    bones.put("hat", bones.get("head"));
    return new EntityModelPose(bones, root, variant);
  }

  public static String playerVariant(boolean slim, PlayerBodyGeometry.Layers layers) {
    boolean outer =
        layers.hat()
            && layers.jacket()
            && layers.rightSleeve()
            && layers.leftSleeve()
            && layers.rightPants()
            && layers.leftPants();
    return (slim ? "slim" : "standard") + (outer ? "_layers" : "");
  }
}
