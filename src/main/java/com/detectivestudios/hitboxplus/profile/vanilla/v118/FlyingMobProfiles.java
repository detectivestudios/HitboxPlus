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

public class FlyingMobProfiles {

  private final ProfileVolumes volumes = new ProfileVolumes();

  public void addTo(Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles) {
    profiles.put(new VanillaProfileKey("minecraft:bat", "base"), this.bat());
    profiles.put(new VanillaProfileKey("minecraft:bee", "base"), this.bee());
    profiles.put(new VanillaProfileKey("minecraft:chicken", "base"), this.chicken());
    profiles.put(new VanillaProfileKey("minecraft:parrot", "base"), this.parrot());
    profiles.put(new VanillaProfileKey("minecraft:phantom", "base"), this.phantom());
  }

  private EntityHitboxProfile<String> bat() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -3.0D, 4.0D, -3.0D, 3.0D, 16.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bat/body/1", BodyPart.TORSO),
                "body",
                -5.0D,
                16.0D,
                0.0D,
                5.0D,
                22.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bat/body/left_wing/0", BodyPart.LEFT_WING),
                "body/left_wing",
                2.0D,
                1.0D,
                1.5D,
                12.0D,
                17.0D,
                2.5D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/bat/body/left_wing/left_wing_tip/0", BodyPart.LEFT_WING),
                "body/left_wing/left_wing_tip",
                0.0D,
                1.0D,
                0.0D,
                8.0D,
                13.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bat/body/right_wing/0", BodyPart.RIGHT_WING),
                "body/right_wing",
                -12.0D,
                1.0D,
                1.5D,
                -2.0D,
                17.0D,
                2.5D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/bat/body/right_wing/right_wing_tip/0", BodyPart.RIGHT_WING),
                "body/right_wing/right_wing_tip",
                -8.0D,
                1.0D,
                0.0D,
                0.0D,
                13.0D,
                1.0D),
            this.volumes.box(BodyPart.HEAD, "head", -3.0D, -3.0D, -3.0D, 3.0D, 3.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bat/head/left_ear/0", BodyPart.HEAD),
                "head/left_ear",
                1.0D,
                -6.0D,
                -2.0D,
                4.0D,
                -2.0D,
                -1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bat/head/right_ear/0", BodyPart.HEAD),
                "head/right_ear",
                -4.0D,
                -6.0D,
                -2.0D,
                -1.0D,
                -2.0D,
                -1.0D)));
  }

  private EntityHitboxProfile<String> bee() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bee/bone/back_legs/0", BodyPart.OTHER),
                "bone/back_legs",
                -5.0D,
                0.0D,
                0.0D,
                2.0D,
                2.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bee/bone/body/0", BodyPart.TORSO),
                "bone/body",
                -3.5D,
                -4.0D,
                -5.0D,
                3.5D,
                3.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/bee/bone/body/left_antenna/0", BodyPart.TORSO),
                "bone/body/left_antenna",
                1.5D,
                -2.0D,
                -3.0D,
                2.5D,
                0.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/bee/bone/body/right_antenna/0", BodyPart.TORSO),
                "bone/body/right_antenna",
                -2.5D,
                -2.0D,
                -3.0D,
                -1.5D,
                0.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bee/bone/body/stinger/0", BodyPart.TORSO),
                "bone/body/stinger",
                0.0D,
                -1.0D,
                5.0D,
                0.0D,
                0.0D,
                7.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bee/bone/front_legs/0", BodyPart.OTHER),
                "bone/front_legs",
                -5.0D,
                0.0D,
                0.0D,
                2.0D,
                2.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bee/bone/left_wing/0", BodyPart.LEFT_WING),
                "bone/left_wing",
                -0.001D,
                -0.001D,
                -0.001D,
                9.001D,
                0.001D,
                6.001D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bee/bone/middle_legs/0", BodyPart.OTHER),
                "bone/middle_legs",
                -5.0D,
                0.0D,
                0.0D,
                2.0D,
                2.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/bee/bone/right_wing/0", BodyPart.RIGHT_WING),
                "bone/right_wing",
                -9.001D,
                -0.001D,
                -0.001D,
                0.001D,
                0.001D,
                6.001D)));
  }

  private EntityHitboxProfile<String> chicken() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/chicken/beak/0", BodyPart.HEAD),
                "beak",
                -2.0D,
                -4.0D,
                -4.0D,
                2.0D,
                -2.0D,
                -2.0D),
            this.volumes.box(BodyPart.TORSO, "body", -3.0D, -4.0D, -3.0D, 3.0D, 4.0D, 3.0D),
            this.volumes.box(BodyPart.HEAD, "head", -2.0D, -6.0D, -2.0D, 2.0D, 0.0D, 1.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -1.0D, 0.0D, -3.0D, 2.0D, 5.0D, 0.0D),
            this.volumes.box(BodyPart.LEFT_WING, "left_wing", -1.0D, 0.0D, -3.0D, 0.0D, 4.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/chicken/red_thing/0", BodyPart.HEAD),
                "red_thing",
                -1.0D,
                -2.0D,
                -3.0D,
                1.0D,
                0.0D,
                -1.0D),
            this.volumes.box(BodyPart.RIGHT_LEG, "right_leg", -1.0D, 0.0D, -3.0D, 2.0D, 5.0D, 0.0D),
            this.volumes.box(
                BodyPart.RIGHT_WING, "right_wing", 0.0D, 0.0D, -3.0D, 1.0D, 4.0D, 3.0D)));
  }

  private EntityHitboxProfile<String> parrot() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -1.5D, 0.0D, -1.5D, 1.5D, 6.0D, 1.5D),
            this.volumes.box(BodyPart.HEAD, "head", -1.0D, -1.5D, -1.0D, 1.0D, 1.5D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/parrot/head/beak1/0", BodyPart.HEAD),
                "head/beak1",
                -0.5D,
                -1.0D,
                -0.5D,
                0.5D,
                1.0D,
                0.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/parrot/head/beak2/0", BodyPart.HEAD),
                "head/beak2",
                -0.5D,
                0.0D,
                -0.5D,
                0.5D,
                2.0D,
                0.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/parrot/head/feather/0", BodyPart.HEAD),
                "head/feather",
                0.0D,
                -4.0D,
                -2.0D,
                0.0D,
                1.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/parrot/head/head2/0", BodyPart.HEAD),
                "head/head2",
                -1.0D,
                -0.5D,
                -2.0D,
                1.0D,
                0.5D,
                2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -0.5D, 0.0D, -0.5D, 0.5D, 2.0D, 0.5D),
            this.volumes.box(BodyPart.LEFT_WING, "left_wing", -0.5D, 0.0D, -1.5D, 0.5D, 5.0D, 1.5D),
            this.volumes.box(BodyPart.RIGHT_LEG, "right_leg", -0.5D, 0.0D, -0.5D, 0.5D, 2.0D, 0.5D),
            this.volumes.box(
                BodyPart.RIGHT_WING, "right_wing", -0.5D, 0.0D, -1.5D, 0.5D, 5.0D, 1.5D),
            this.volumes.box(BodyPart.TAIL, "tail", -1.5D, -1.0D, -1.0D, 1.5D, 3.0D, 0.0D)));
  }

  private EntityHitboxProfile<String> phantom() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -3.0D, -2.0D, -8.0D, 2.0D, 1.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/phantom/body/head/0", BodyPart.HEAD),
                "body/head",
                -4.0D,
                -2.0D,
                -5.0D,
                3.0D,
                1.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/phantom/body/left_wing_base/0", BodyPart.LEFT_WING),
                "body/left_wing_base",
                0.0D,
                0.0D,
                0.0D,
                6.0D,
                2.0D,
                9.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/phantom/body/left_wing_base/left_wing_tip/0",
                    BodyPart.LEFT_WING),
                "body/left_wing_base/left_wing_tip",
                0.0D,
                0.0D,
                0.0D,
                13.0D,
                1.0D,
                9.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/phantom/body/right_wing_base/0", BodyPart.RIGHT_WING),
                "body/right_wing_base",
                -6.0D,
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                9.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/phantom/body/right_wing_base/right_wing_tip/0",
                    BodyPart.RIGHT_WING),
                "body/right_wing_base/right_wing_tip",
                -13.0D,
                0.0D,
                0.0D,
                0.0D,
                1.0D,
                9.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/phantom/body/tail_base/0", BodyPart.TAIL),
                "body/tail_base",
                -2.0D,
                0.0D,
                0.0D,
                1.0D,
                2.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/phantom/body/tail_base/tail_tip/0", BodyPart.TAIL),
                "body/tail_base/tail_tip",
                -1.0D,
                0.0D,
                0.0D,
                0.0D,
                1.0D,
                6.0D)));
  }
}
