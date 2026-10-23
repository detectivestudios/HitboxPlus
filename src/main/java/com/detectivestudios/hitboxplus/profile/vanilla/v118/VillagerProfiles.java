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

public class VillagerProfiles {

  private final ProfileVolumes volumes = new ProfileVolumes();

  public void addTo(Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles) {
    profiles.put(new VanillaProfileKey("minecraft:evoker", "base"), this.evoker());
    profiles.put(
        new VanillaProfileKey("minecraft:evoker", "separate_arms"), this.evokerSeparateArms());
    profiles.put(new VanillaProfileKey("minecraft:illusioner", "base"), this.illusioner());
    profiles.put(
        new VanillaProfileKey("minecraft:illusioner", "separate_arms"),
        this.illusionerSeparateArms());
    profiles.put(new VanillaProfileKey("minecraft:pillager", "base"), this.pillager());
    profiles.put(
        new VanillaProfileKey("minecraft:pillager", "separate_arms"), this.pillagerSeparateArms());
    profiles.put(new VanillaProfileKey("minecraft:ravager", "base"), this.ravager());
    profiles.put(new VanillaProfileKey("minecraft:villager", "base"), this.villager());
    profiles.put(new VanillaProfileKey("minecraft:vindicator", "base"), this.vindicator());
    profiles.put(
        new VanillaProfileKey("minecraft:vindicator", "separate_arms"),
        this.vindicatorSeparateArms());
    profiles.put(
        new VanillaProfileKey("minecraft:wandering_trader", "base"), this.wanderingTrader());
    profiles.put(new VanillaProfileKey("minecraft:witch", "base"), this.witch());
  }

  private EntityHitboxProfile<String> evoker() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/evoker/arms/0", BodyPart.OTHER),
                "arms",
                -8.0D,
                -2.0D,
                -2.0D,
                -4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/evoker/arms/1", BodyPart.OTHER),
                "arms",
                -4.0D,
                2.0D,
                -2.0D,
                4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/evoker/arms/left_shoulder/0", BodyPart.LEFT_ARM),
                "arms/left_shoulder",
                4.0D,
                -2.0D,
                -2.0D,
                8.0D,
                6.0D,
                2.0D),
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/evoker/body/1", BodyPart.TORSO),
                "body",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/evoker/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> evokerSeparateArms() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/evoker/body/1", BodyPart.TORSO),
                "body",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/evoker/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> illusioner() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/illusioner/arms/0", BodyPart.OTHER),
                "arms",
                -8.0D,
                -2.0D,
                -2.0D,
                -4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/illusioner/arms/1", BodyPart.OTHER),
                "arms",
                -4.0D,
                2.0D,
                -2.0D,
                4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/illusioner/arms/left_shoulder/0", BodyPart.LEFT_ARM),
                "arms/left_shoulder",
                4.0D,
                -2.0D,
                -2.0D,
                8.0D,
                6.0D,
                2.0D),
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/illusioner/body/1", BodyPart.TORSO),
                "body",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/illusioner/head/hat/0", BodyPart.HEAD),
                "head/hat",
                -4.45D,
                -10.45D,
                -4.45D,
                4.45D,
                2.45D,
                4.45D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/illusioner/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> illusionerSeparateArms() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/illusioner/body/1", BodyPart.TORSO),
                "body",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/illusioner/head/hat/0", BodyPart.HEAD),
                "head/hat",
                -4.45D,
                -10.45D,
                -4.45D,
                4.45D,
                2.45D,
                4.45D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/illusioner/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> pillager() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pillager/arms/0", BodyPart.OTHER),
                "arms",
                -8.0D,
                -2.0D,
                -2.0D,
                -4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pillager/arms/1", BodyPart.OTHER),
                "arms",
                -4.0D,
                2.0D,
                -2.0D,
                4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pillager/arms/left_shoulder/0", BodyPart.LEFT_ARM),
                "arms/left_shoulder",
                4.0D,
                -2.0D,
                -2.0D,
                8.0D,
                6.0D,
                2.0D),
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pillager/body/1", BodyPart.TORSO),
                "body",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pillager/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> pillagerSeparateArms() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pillager/body/1", BodyPart.TORSO),
                "body",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pillager/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> ravager() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -7.0D, -10.0D, -7.0D, 7.0D, 6.0D, 13.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ravager/body/1", BodyPart.TORSO),
                "body",
                -6.0D,
                6.0D,
                -7.0D,
                6.0D,
                19.0D,
                11.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -4.0D, 0.0D, -4.0D, 4.0D, 37.0D, 4.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -4.0D, 0.0D, -4.0D, 4.0D, 37.0D, 4.0D),
            this.volumes.box(BodyPart.NECK, "neck", -5.0D, -1.0D, -18.0D, 5.0D, 9.0D, 0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ravager/neck/head/0", BodyPart.HEAD),
                "neck/head",
                -8.0D,
                -20.0D,
                -14.0D,
                8.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ravager/neck/head/1", BodyPart.HEAD),
                "neck/head",
                -2.0D,
                -6.0D,
                -18.0D,
                2.0D,
                2.0D,
                -14.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/ravager/neck/head/left_horn/0", BodyPart.HEAD),
                "neck/head/left_horn",
                0.0D,
                -14.0D,
                -2.0D,
                2.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ravager/neck/head/mouth/0", BodyPart.HEAD),
                "neck/head/mouth",
                -8.0D,
                0.0D,
                -16.0D,
                8.0D,
                3.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/ravager/neck/head/right_horn/0", BodyPart.HEAD),
                "neck/head/right_horn",
                0.0D,
                -14.0D,
                -2.0D,
                2.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -4.0D, 0.0D, -4.0D, 4.0D, 37.0D, 4.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -4.0D, 0.0D, -4.0D, 4.0D, 37.0D, 4.0D)));
  }

  private EntityHitboxProfile<String> villager() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/villager/arms/0", BodyPart.OTHER),
                "arms",
                -8.0D,
                -2.0D,
                -2.0D,
                -4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/villager/arms/1", BodyPart.OTHER),
                "arms",
                4.0D,
                -2.0D,
                -2.0D,
                8.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/villager/arms/2", BodyPart.OTHER),
                "arms",
                -4.0D,
                2.0D,
                -2.0D,
                4.0D,
                6.0D,
                2.0D),
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/villager/body/jacket/0", BodyPart.TORSO),
                "body/jacket",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/villager/head/hat/0", BodyPart.HEAD),
                "head/hat",
                -4.5D,
                -10.5D,
                -4.5D,
                4.5D,
                0.5D,
                4.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/villager/head/hat/hat_rim/0", BodyPart.HEAD),
                "head/hat/hat_rim",
                -8.0D,
                -8.0D,
                -6.0D,
                8.0D,
                8.0D,
                -5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/villager/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> vindicator() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/vindicator/arms/0", BodyPart.OTHER),
                "arms",
                -8.0D,
                -2.0D,
                -2.0D,
                -4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/vindicator/arms/1", BodyPart.OTHER),
                "arms",
                -4.0D,
                2.0D,
                -2.0D,
                4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/vindicator/arms/left_shoulder/0", BodyPart.LEFT_ARM),
                "arms/left_shoulder",
                4.0D,
                -2.0D,
                -2.0D,
                8.0D,
                6.0D,
                2.0D),
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/vindicator/body/1", BodyPart.TORSO),
                "body",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/vindicator/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> vindicatorSeparateArms() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/vindicator/body/1", BodyPart.TORSO),
                "body",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/vindicator/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> wanderingTrader() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wandering_trader/arms/0", BodyPart.OTHER),
                "arms",
                -8.0D,
                -2.0D,
                -2.0D,
                -4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wandering_trader/arms/1", BodyPart.OTHER),
                "arms",
                4.0D,
                -2.0D,
                -2.0D,
                8.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wandering_trader/arms/2", BodyPart.OTHER),
                "arms",
                -4.0D,
                2.0D,
                -2.0D,
                4.0D,
                6.0D,
                2.0D),
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/wandering_trader/body/jacket/0", BodyPart.TORSO),
                "body/jacket",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wandering_trader/head/hat/0", BodyPart.HEAD),
                "head/hat",
                -4.5D,
                -10.5D,
                -4.5D,
                4.5D,
                0.5D,
                4.5D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/wandering_trader/head/hat/hat_rim/0", BodyPart.HEAD),
                "head/hat/hat_rim",
                -8.0D,
                -8.0D,
                -6.0D,
                8.0D,
                8.0D,
                -5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wandering_trader/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> witch() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/arms/0", BodyPart.OTHER),
                "arms",
                -8.0D,
                -2.0D,
                -2.0D,
                -4.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/arms/1", BodyPart.OTHER),
                "arms",
                4.0D,
                -2.0D,
                -2.0D,
                8.0D,
                6.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/arms/2", BodyPart.OTHER),
                "arms",
                -4.0D,
                2.0D,
                -2.0D,
                4.0D,
                6.0D,
                2.0D),
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/body/jacket/0", BodyPart.TORSO),
                "body/jacket",
                -4.5D,
                -0.5D,
                -3.5D,
                4.5D,
                18.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/head/hat/0", BodyPart.HEAD),
                "head/hat",
                0.0D,
                0.0D,
                0.0D,
                10.0D,
                2.0D,
                10.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/head/hat/hat2/0", BodyPart.HEAD),
                "head/hat/hat2",
                0.0D,
                0.0D,
                0.0D,
                7.0D,
                4.0D,
                7.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/head/hat/hat2/hat3/0", BodyPart.HEAD),
                "head/hat/hat2/hat3",
                0.0D,
                0.0D,
                0.0D,
                4.0D,
                4.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/witch/head/hat/hat2/hat3/hat4/0", BodyPart.HEAD),
                "head/hat/hat2/hat3/hat4",
                -0.25D,
                -0.25D,
                -0.25D,
                1.25D,
                2.25D,
                1.25D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/head/hat/hat_rim/0", BodyPart.HEAD),
                "head/hat/hat_rim",
                -8.0D,
                -8.0D,
                -6.0D,
                8.0D,
                8.0D,
                -5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -1.0D,
                -1.0D,
                -6.0D,
                1.0D,
                3.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/witch/head/nose/mole/0", BodyPart.HEAD),
                "head/nose/mole",
                0.25D,
                3.25D,
                -6.5D,
                0.75D,
                3.75D,
                -6.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }
}
