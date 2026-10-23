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

import com.detectivestudios.hitboxplus.api.BodyPart;
import com.detectivestudios.hitboxplus.api.NamedHitboxPart;
import com.detectivestudios.hitboxplus.profile.EntityHitboxProfile;
import com.detectivestudios.hitboxplus.profile.VanillaProfileKey;
import java.util.List;
import java.util.Map;

public class ArmorStandProfiles {

  private final ProfileVolumes volumes = new ProfileVolumes();

  public void addTo(Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles) {
    profiles.put(new VanillaProfileKey("minecraft:armor_stand", "arms"), this.armorStandArms());
    profiles.put(new VanillaProfileKey("minecraft:armor_stand", "base"), this.armorStand());
  }

  private EntityHitboxProfile<String> armorStandArms() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/armor_stand/base_plate/0", BodyPart.OTHER),
                "base_plate",
                -6.0D,
                11.0D,
                -6.0D,
                6.0D,
                12.0D,
                6.0D),
            this.volumes.box(BodyPart.TORSO, "body", -6.0D, 0.0D, -1.5D, 6.0D, 3.0D, 1.5D),
            this.volumes.box(BodyPart.HEAD, "head", -1.0D, -7.0D, -1.0D, 1.0D, 0.0D, 1.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", 0.0D, -2.0D, -1.0D, 2.0D, 10.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/armor_stand/left_body_stick/0", BodyPart.TORSO),
                "left_body_stick",
                1.0D,
                3.0D,
                -1.0D,
                3.0D,
                10.0D,
                1.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -1.0D, 0.0D, -1.0D, 1.0D, 11.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -2.0D, -2.0D, -1.0D, 0.0D, 10.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/armor_stand/right_body_stick/0", BodyPart.TORSO),
                "right_body_stick",
                -3.0D,
                3.0D,
                -1.0D,
                -1.0D,
                10.0D,
                1.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -1.0D, 0.0D, -1.0D, 1.0D, 11.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/armor_stand/shoulder_stick/0", BodyPart.TORSO),
                "shoulder_stick",
                -4.0D,
                10.0D,
                -1.0D,
                4.0D,
                12.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> armorStand() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/armor_stand/base_plate/0", BodyPart.OTHER),
                "base_plate",
                -6.0D,
                11.0D,
                -6.0D,
                6.0D,
                12.0D,
                6.0D),
            this.volumes.box(BodyPart.TORSO, "body", -6.0D, 0.0D, -1.5D, 6.0D, 3.0D, 1.5D),
            this.volumes.box(BodyPart.HEAD, "head", -1.0D, -7.0D, -1.0D, 1.0D, 0.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/armor_stand/left_body_stick/0", BodyPart.TORSO),
                "left_body_stick",
                1.0D,
                3.0D,
                -1.0D,
                3.0D,
                10.0D,
                1.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -1.0D, 0.0D, -1.0D, 1.0D, 11.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/armor_stand/right_body_stick/0", BodyPart.TORSO),
                "right_body_stick",
                -3.0D,
                3.0D,
                -1.0D,
                -1.0D,
                10.0D,
                1.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -1.0D, 0.0D, -1.0D, 1.0D, 11.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/armor_stand/shoulder_stick/0", BodyPart.TORSO),
                "shoulder_stick",
                -4.0D,
                10.0D,
                -1.0D,
                4.0D,
                12.0D,
                1.0D)));
  }
}
