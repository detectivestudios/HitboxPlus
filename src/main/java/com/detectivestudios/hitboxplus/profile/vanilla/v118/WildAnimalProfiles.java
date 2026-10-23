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

public class WildAnimalProfiles {

  private final ProfileVolumes volumes = new ProfileVolumes();

  public void addTo(Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles) {
    profiles.put(new VanillaProfileKey("minecraft:cat", "base"), this.cat());
    profiles.put(new VanillaProfileKey("minecraft:fox", "base"), this.fox());
    profiles.put(new VanillaProfileKey("minecraft:hoglin", "base"), this.hoglin());
    profiles.put(new VanillaProfileKey("minecraft:ocelot", "base"), this.ocelot());
    profiles.put(new VanillaProfileKey("minecraft:panda", "base"), this.panda());
    profiles.put(new VanillaProfileKey("minecraft:polar_bear", "base"), this.polarBear());
    profiles.put(new VanillaProfileKey("minecraft:rabbit", "base"), this.rabbit());
    profiles.put(new VanillaProfileKey("minecraft:strider", "base"), this.strider());
    profiles.put(new VanillaProfileKey("minecraft:wolf", "base"), this.wolf());
    profiles.put(new VanillaProfileKey("minecraft:zoglin", "base"), this.zoglin());
  }

  private EntityHitboxProfile<String> cat() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -2.0D, 3.0D, -8.0D, 2.0D, 19.0D, -2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -2.5D, -2.0D, -3.0D, 2.5D, 2.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cat/head/1", BodyPart.HEAD),
                "head",
                -1.5D,
                0.0D,
                -4.0D,
                1.5D,
                2.0D,
                -2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cat/head/2", BodyPart.HEAD),
                "head",
                -2.0D,
                -3.0D,
                0.0D,
                -1.0D,
                -2.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cat/head/3", BodyPart.HEAD),
                "head",
                1.0D,
                -3.0D,
                0.0D,
                2.0D,
                -2.0D,
                2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -1.0D, 0.0D, 0.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -1.0D, 0.0D, 1.0D, 1.0D, 6.0D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -1.0D, 0.0D, 0.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -1.0D, 0.0D, 1.0D, 1.0D, 6.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cat/tail1/0", BodyPart.TAIL),
                "tail1",
                -0.5D,
                0.0D,
                0.0D,
                0.5D,
                8.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cat/tail2/0", BodyPart.TAIL),
                "tail2",
                -0.5D,
                0.0D,
                0.0D,
                0.5D,
                8.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> fox() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -3.0D, 3.999D, -3.5D, 3.0D, 14.999001D, 2.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/fox/body/tail/0", BodyPart.TAIL),
                "body/tail",
                2.0D,
                0.0D,
                -1.0D,
                6.0D,
                9.0D,
                4.0D),
            this.volumes.box(BodyPart.HEAD, "head", -3.0D, -2.0D, -5.0D, 5.0D, 4.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/fox/head/left_ear/0", BodyPart.HEAD),
                "head/left_ear",
                3.0D,
                -4.0D,
                -4.0D,
                5.0D,
                -2.0D,
                -3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/fox/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                2.01D,
                -8.0D,
                3.0D,
                4.01D,
                -5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/fox/head/right_ear/0", BodyPart.HEAD),
                "head/right_ear",
                -3.0D,
                -4.0D,
                -4.0D,
                -1.0D,
                -2.0D,
                -3.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG,
                "left_front_leg",
                1.999D,
                0.499D,
                -1.001D,
                4.001D,
                6.501D,
                1.001D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG,
                "left_hind_leg",
                1.999D,
                0.499D,
                -1.001D,
                4.001D,
                6.501D,
                1.001D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG,
                "right_front_leg",
                1.999D,
                0.499D,
                -1.001D,
                4.001D,
                6.501D,
                1.001D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG,
                "right_hind_leg",
                1.999D,
                0.499D,
                -1.001D,
                4.001D,
                6.501D,
                1.001D)));
  }

  private EntityHitboxProfile<String> hoglin() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -8.0D, -7.0D, -13.0D, 8.0D, 7.0D, 13.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/hoglin/body/mane/0", BodyPart.NECK),
                "body/mane",
                -0.001D,
                -0.001D,
                -9.001D,
                0.001D,
                10.001D,
                10.001D),
            this.volumes.box(BodyPart.HEAD, "head", -7.0D, -3.0D, -19.0D, 7.0D, 3.0D, 0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/hoglin/head/left_ear/0", BodyPart.HEAD),
                "head/left_ear",
                0.0D,
                -1.0D,
                -2.0D,
                6.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/hoglin/head/left_horn/0", BodyPart.HEAD),
                "head/left_horn",
                -1.0D,
                -11.0D,
                -1.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/hoglin/head/right_ear/0", BodyPart.HEAD),
                "head/right_ear",
                -6.0D,
                -1.0D,
                -2.0D,
                0.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/hoglin/head/right_horn/0", BodyPart.HEAD),
                "head/right_horn",
                -1.0D,
                -11.0D,
                -1.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -3.0D, 0.0D, -3.0D, 3.0D, 14.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.5D, 0.0D, -2.5D, 2.5D, 11.0D, 2.5D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -3.0D, 0.0D, -3.0D, 3.0D, 14.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.5D, 0.0D, -2.5D, 2.5D, 11.0D, 2.5D)));
  }

  private EntityHitboxProfile<String> ocelot() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -2.0D, 3.0D, -8.0D, 2.0D, 19.0D, -2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -2.5D, -2.0D, -3.0D, 2.5D, 2.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ocelot/head/1", BodyPart.HEAD),
                "head",
                -1.5D,
                0.0D,
                -4.0D,
                1.5D,
                2.0D,
                -2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ocelot/head/2", BodyPart.HEAD),
                "head",
                -2.0D,
                -3.0D,
                0.0D,
                -1.0D,
                -2.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ocelot/head/3", BodyPart.HEAD),
                "head",
                1.0D,
                -3.0D,
                0.0D,
                2.0D,
                -2.0D,
                2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -1.0D, 0.0D, 0.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -1.0D, 0.0D, 1.0D, 1.0D, 6.0D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -1.0D, 0.0D, 0.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -1.0D, 0.0D, 1.0D, 1.0D, 6.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ocelot/tail1/0", BodyPart.TAIL),
                "tail1",
                -0.5D,
                0.0D,
                0.0D,
                0.5D,
                8.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ocelot/tail2/0", BodyPart.TAIL),
                "tail2",
                -0.5D,
                0.0D,
                0.0D,
                0.5D,
                8.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> panda() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -9.5D, -13.0D, -6.5D, 9.5D, 13.0D, 6.5D),
            this.volumes.box(BodyPart.HEAD, "head", -6.5D, -5.0D, -4.0D, 6.5D, 5.0D, 5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/panda/head/1", BodyPart.HEAD),
                "head",
                -3.5D,
                0.0D,
                -6.0D,
                3.5D,
                5.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/panda/head/2", BodyPart.HEAD),
                "head",
                3.5D,
                -8.0D,
                -1.0D,
                8.5D,
                -4.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/panda/head/3", BodyPart.HEAD),
                "head",
                -8.5D,
                -8.0D,
                -1.0D,
                -3.5D,
                -4.0D,
                0.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -3.0D, 0.0D, -3.0D, 3.0D, 9.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -3.0D, 0.0D, -3.0D, 3.0D, 9.0D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -3.0D, 0.0D, -3.0D, 3.0D, 9.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -3.0D, 0.0D, -3.0D, 3.0D, 9.0D, 3.0D)));
  }

  private EntityHitboxProfile<String> polarBear() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.0D, -13.0D, -7.0D, 9.0D, 1.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/polar_bear/body/1", BodyPart.TORSO),
                "body",
                -4.0D,
                -25.0D,
                -7.0D,
                8.0D,
                -13.0D,
                3.0D),
            this.volumes.box(BodyPart.HEAD, "head", -3.5D, -3.0D, -3.0D, 3.5D, 4.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/polar_bear/head/1", BodyPart.HEAD),
                "head",
                -2.5D,
                1.0D,
                -6.0D,
                2.5D,
                4.0D,
                -3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/polar_bear/head/2", BodyPart.HEAD),
                "head",
                -4.5D,
                -4.0D,
                -1.0D,
                -2.5D,
                -2.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/polar_bear/head/3", BodyPart.HEAD),
                "head",
                2.5D,
                -4.0D,
                -1.0D,
                4.5D,
                -2.0D,
                0.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 10.0D, 4.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 10.0D, 6.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 10.0D, 4.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 10.0D, 6.0D)));
  }

  private EntityHitboxProfile<String> rabbit() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -3.0D, -2.0D, -10.0D, 3.0D, 3.0D, 0.0D),
            this.volumes.box(BodyPart.HEAD, "head", -2.5D, -4.0D, -5.0D, 2.5D, 0.0D, 0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/rabbit/left_ear/0", BodyPart.HEAD),
                "left_ear",
                0.5D,
                -9.0D,
                -1.0D,
                2.5D,
                -4.0D,
                0.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -1.0D, 0.0D, -1.0D, 1.0D, 7.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/rabbit/left_haunch/0", BodyPart.HIND_LEFT_LEG),
                "left_haunch",
                -1.0D,
                0.0D,
                0.0D,
                1.0D,
                4.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/rabbit/left_hind_foot/0", BodyPart.HIND_LEFT_LEG),
                "left_hind_foot",
                -1.0D,
                5.5D,
                -3.7D,
                1.0D,
                6.5D,
                3.3D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/rabbit/nose/0", BodyPart.HEAD),
                "nose",
                -0.5D,
                -2.5D,
                -5.5D,
                0.5D,
                -1.5D,
                -4.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/rabbit/right_ear/0", BodyPart.HEAD),
                "right_ear",
                -2.5D,
                -9.0D,
                -1.0D,
                -0.5D,
                -4.0D,
                0.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -1.0D, 0.0D, -1.0D, 1.0D, 7.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/rabbit/right_haunch/0", BodyPart.HIND_RIGHT_LEG),
                "right_haunch",
                -1.0D,
                0.0D,
                0.0D,
                1.0D,
                4.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/rabbit/right_hind_foot/0", BodyPart.HIND_RIGHT_LEG),
                "right_hind_foot",
                -1.0D,
                5.5D,
                -3.7D,
                1.0D,
                6.5D,
                3.3D),
            this.volumes.box(BodyPart.TAIL, "tail", -1.5D, -1.5D, 0.0D, 1.5D, 1.5D, 2.0D)));
  }

  private EntityHitboxProfile<String> strider() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -8.0D, -6.0D, -8.0D, 8.0D, 8.0D, 8.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/strider/body/left_bottom_bristle/0", BodyPart.TORSO),
                "body/left_bottom_bristle",
                0.0D,
                0.0D,
                0.0D,
                12.0D,
                0.0D,
                16.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/strider/body/left_middle_bristle/0", BodyPart.TORSO),
                "body/left_middle_bristle",
                0.0D,
                0.0D,
                0.0D,
                12.0D,
                0.0D,
                16.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/strider/body/left_top_bristle/0", BodyPart.TORSO),
                "body/left_top_bristle",
                0.0D,
                0.0D,
                0.0D,
                12.0D,
                0.0D,
                16.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/strider/body/right_bottom_bristle/0", BodyPart.TORSO),
                "body/right_bottom_bristle",
                -12.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                16.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/strider/body/right_middle_bristle/0", BodyPart.TORSO),
                "body/right_middle_bristle",
                -12.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                16.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/strider/body/right_top_bristle/0", BodyPart.TORSO),
                "body/right_top_bristle",
                -12.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                16.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 16.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 16.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> wolf() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -3.0D, -2.0D, -3.0D, 3.0D, 7.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wolf/head/real_head/0", BodyPart.HEAD),
                "head/real_head",
                -2.0D,
                -3.0D,
                -2.0D,
                4.0D,
                3.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wolf/head/real_head/1", BodyPart.HEAD),
                "head/real_head",
                -2.0D,
                -5.0D,
                0.0D,
                0.0D,
                -3.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wolf/head/real_head/2", BodyPart.HEAD),
                "head/real_head",
                2.0D,
                -5.0D,
                0.0D,
                4.0D,
                -3.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wolf/head/real_head/3", BodyPart.HEAD),
                "head/real_head",
                -0.5D,
                0.0D,
                -5.0D,
                2.5D,
                3.0D,
                -1.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", 0.0D, 0.0D, -1.0D, 2.0D, 8.0D, 1.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", 0.0D, 0.0D, -1.0D, 2.0D, 8.0D, 1.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", 0.0D, 0.0D, -1.0D, 2.0D, 8.0D, 1.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", 0.0D, 0.0D, -1.0D, 2.0D, 8.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wolf/tail/real_tail/0", BodyPart.TAIL),
                "tail/real_tail",
                0.0D,
                0.0D,
                -1.0D,
                2.0D,
                8.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wolf/upper_body/0", BodyPart.TORSO),
                "upper_body",
                -3.0D,
                -3.0D,
                -3.0D,
                5.0D,
                3.0D,
                4.0D)));
  }

  private EntityHitboxProfile<String> zoglin() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -8.0D, -7.0D, -13.0D, 8.0D, 7.0D, 13.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zoglin/body/mane/0", BodyPart.NECK),
                "body/mane",
                -0.001D,
                -0.001D,
                -9.001D,
                0.001D,
                10.001D,
                10.001D),
            this.volumes.box(BodyPart.HEAD, "head", -7.0D, -3.0D, -19.0D, 7.0D, 3.0D, 0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zoglin/head/left_ear/0", BodyPart.HEAD),
                "head/left_ear",
                0.0D,
                -1.0D,
                -2.0D,
                6.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zoglin/head/left_horn/0", BodyPart.HEAD),
                "head/left_horn",
                -1.0D,
                -11.0D,
                -1.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zoglin/head/right_ear/0", BodyPart.HEAD),
                "head/right_ear",
                -6.0D,
                -1.0D,
                -2.0D,
                0.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zoglin/head/right_horn/0", BodyPart.HEAD),
                "head/right_horn",
                -1.0D,
                -11.0D,
                -1.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -3.0D, 0.0D, -3.0D, 3.0D, 14.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.5D, 0.0D, -2.5D, 2.5D, 11.0D, 2.5D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -3.0D, 0.0D, -3.0D, 3.0D, 14.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.5D, 0.0D, -2.5D, 2.5D, 11.0D, 2.5D)));
  }
}
