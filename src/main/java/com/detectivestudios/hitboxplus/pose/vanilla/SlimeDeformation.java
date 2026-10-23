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

package com.detectivestudios.hitboxplus.pose.vanilla;

import com.detectivestudios.hitboxplus.api.BodyHitbox;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.geometry.OrientedBox;
import com.detectivestudios.hitboxplus.math.BoneTransform;
import com.detectivestudios.hitboxplus.math.Vector3;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;

public class SlimeDeformation {

  public HitboxPose apply(HitboxPose pose, BoneTransform root, Slime slime, float partialTick) {
    float amount =
        Mth.lerp(partialTick, slime.oSquish, slime.squish) / (slime.getSize() * 0.5F + 1);
    double horizontal = 1 / (amount + 1);
    double vertical = amount + 1;
    var anchor = root.transform(new Vector3(0, 1.501 * 16, 0));
    return new HitboxPose(
        pose.hitboxes().stream()
            .map(
                hitbox -> {
                  var box = hitbox.box();
                  var local = root.rotation().inverseTransform(box.center().subtract(anchor));
                  var center =
                      anchor.add(
                          root.rotation()
                              .transform(
                                  new Vector3(
                                      local.x() * horizontal,
                                      local.y() * vertical,
                                      local.z() * horizontal)));
                  var extent = box.halfExtents();
                  return new BodyHitbox(
                      hitbox.part(),
                      new OrientedBox(
                          center,
                          new Vector3(
                              extent.x() * horizontal,
                              extent.y() * vertical,
                              extent.z() * horizontal),
                          box.rotation()));
                })
            .toList());
  }
}
