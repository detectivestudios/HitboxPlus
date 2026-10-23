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

import com.detectivestudios.hitboxplus.api.BodyPart;
import com.detectivestudios.hitboxplus.api.NamedHitboxPart;
import com.detectivestudios.hitboxplus.profile.EntityHitboxProfile;
import com.detectivestudios.hitboxplus.profile.vanilla.v118.ProfileVolumes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DragonModelDefinition {

  private final Map<String, float[]> bones = new LinkedHashMap<>();
  private final List<EntityHitboxProfile.Volume<String>> volumes = new ArrayList<>();
  private final ProfileVolumes boxes = new ProfileVolumes();
  private final EntityHitboxProfile<String> profile;

  public DragonModelDefinition() {
    this.bone("$root", 0, 0, 0);
    this.bone("head", 0, 0, 0);
    this.bone("head/jaw", 0, 4, -8);
    this.box("head", BodyPart.HEAD, -6, -1, -24, 12, 5, 16);
    this.box("head", BodyPart.HEAD, -8, -8, -10, 16, 16, 16);
    this.box("head", BodyPart.HEAD, -5, -12, -4, 2, 4, 6);
    this.box("head", BodyPart.HEAD, -5, -3, -22, 2, 2, 4);
    this.box("head", BodyPart.HEAD, 3, -12, -4, 2, 4, 6);
    this.box("head", BodyPart.HEAD, 3, -3, -22, 2, 2, 4);
    this.box("head/jaw", BodyPart.HEAD, -6, 0, -16, 12, 4, 16);
    this.bone("body", 0, 4, 8);
    this.box("body", BodyPart.TORSO, -12, 0, -16, 24, 24, 64);
    for (int z : new int[] {-10, 10, 30}) {
      this.box("body", BodyPart.TORSO, -1, -6, z, 2, 6, 12);
    }
    for (String side : new String[] {"left", "right"}) {
      boolean left = side.equals("left");
      float sign = left ? 1 : -1;
      String wing = side + "_wing";
      this.bone(wing, sign * 12, 5, 2);
      this.bone(wing + "/" + side + "_wing_tip", sign * 56, 0, 0);
      var region = left ? BodyPart.LEFT_WING : BodyPart.RIGHT_WING;
      this.box(wing, region, left ? 0 : -56, -4, -4, 56, 8, 8);
      this.box(wing, region, left ? 0 : -56, 0, 2, 56, 0, 56);
      this.box(wing + "/" + side + "_wing_tip", region, left ? 0 : -56, -2, -2, 56, 4, 4);
      this.box(wing + "/" + side + "_wing_tip", region, left ? 0 : -56, 0, 2, 56, 0, 56);
      String front = side + "_front_leg";
      String frontTip = front + "/" + front + "_tip";
      String frontFoot = frontTip + "/" + side + "_front_foot";
      this.bone(front, sign * 12, 20, 2);
      this.bone(frontTip, 0, 20, -1);
      this.bone(frontFoot, 0, 23, 0);
      region = left ? BodyPart.FRONT_LEFT_LEG : BodyPart.FRONT_RIGHT_LEG;
      this.box(front, region, -4, -4, -4, 8, 24, 8);
      this.box(frontTip, region, -3, -1, -3, 6, 24, 6);
      this.box(frontFoot, region, -4, 0, -12, 8, 4, 16);
      String hind = side + "_hind_leg";
      String hindTip = hind + "/" + hind + "_tip";
      String hindFoot = hindTip + "/" + side + "_hind_foot";
      this.bone(hind, sign * 16, 16, 42);
      this.bone(hindTip, 0, 32, -4);
      this.bone(hindFoot, 0, 31, 4);
      region = left ? BodyPart.HIND_LEFT_LEG : BodyPart.HIND_RIGHT_LEG;
      this.box(hind, region, -8, -4, -8, 16, 32, 16);
      this.box(hindTip, region, -6, -2, 0, 12, 32, 12);
      this.box(hindFoot, region, -9, 0, -20, 18, 6, 24);
    }
    for (int index = 0; index < 17; index++) {
      String name = index < 5 ? "neck" + index : "tail" + (index - 5);
      this.bone(name, 0, 0, 0);
      var region = index < 5 ? BodyPart.NECK : BodyPart.TAIL;
      this.box(name, region, -5, -5, -5, 10, 10, 10);
      this.box(name, region, -1, -9, -3, 2, 4, 6);
    }
    this.profile = new EntityHitboxProfile<>(this.volumes);
  }

  public VanillaRig rig() {
    return new VanillaRig(this.bones);
  }

  public EntityHitboxProfile<String> profile() {
    return this.profile;
  }

  private void bone(String name, float x, float y, float z) {
    this.bones.put(name, new float[] {x, y, z, 0, 0, 0});
  }

  private void box(
      String bone,
      BodyPart region,
      double x,
      double y,
      double z,
      double width,
      double height,
      double depth) {
    var part =
        new NamedHitboxPart("minecraft:ender_dragon/" + bone + "/" + this.volumes.size(), region);
    this.volumes.add(this.boxes.box(part, bone, x, y, z, x + width, y + height, z + depth));
  }
}
