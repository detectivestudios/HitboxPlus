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

public class HumanoidProfiles {

  private final ProfileVolumes volumes = new ProfileVolumes();

  public void addTo(Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles) {
    profiles.put(new VanillaProfileKey("minecraft:drowned", "base"), this.drowned());
    profiles.put(new VanillaProfileKey("minecraft:enderman", "base"), this.enderman());
    profiles.put(new VanillaProfileKey("minecraft:giant", "base"), this.giant());
    profiles.put(new VanillaProfileKey("minecraft:husk", "base"), this.husk());
    profiles.put(new VanillaProfileKey("minecraft:piglin", "base"), this.piglin());
    profiles.put(new VanillaProfileKey("minecraft:piglin_brute", "base"), this.piglinBrute());
    profiles.put(new VanillaProfileKey("minecraft:skeleton", "base"), this.skeleton());
    profiles.put(new VanillaProfileKey("minecraft:stray", "base"), this.stray());
    profiles.put(new VanillaProfileKey("minecraft:vex", "base"), this.vex());
    profiles.put(new VanillaProfileKey("minecraft:wither_skeleton", "base"), this.witherSkeleton());
    profiles.put(new VanillaProfileKey("minecraft:zombie", "base"), this.zombie());
    profiles.put(new VanillaProfileKey("minecraft:zombie_villager", "base"), this.zombieVillager());
    profiles.put(
        new VanillaProfileKey("minecraft:zombified_piglin", "base"), this.zombifiedPiglin());
  }

  private EntityHitboxProfile<String> drowned() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> enderman() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -1.0D, 1.0D, 28.0D, 1.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -1.0D, 0.0D, -1.0D, 1.0D, 30.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -1.0D, -2.0D, -1.0D, 1.0D, 28.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -1.0D, 0.0D, -1.0D, 1.0D, 30.0D, 1.0D)));
  }

  private EntityHitboxProfile<String> giant() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> husk() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> piglin() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/ear/0", BodyPart.HEAD),
                "ear",
                -3.0D,
                -6.0D,
                -1.0D,
                3.0D,
                0.0D,
                0.0D),
            this.volumes.box(BodyPart.HEAD, "head", -5.0D, -8.0D, -4.0D, 5.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/head/1", BodyPart.HEAD),
                "head",
                -2.0D,
                -4.0D,
                -5.0D,
                2.0D,
                0.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/head/2", BodyPart.HEAD),
                "head",
                2.0D,
                -2.0D,
                -5.0D,
                3.0D,
                0.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/head/3", BodyPart.HEAD),
                "head",
                -3.0D,
                -2.0D,
                -5.0D,
                -2.0D,
                0.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/head/left_ear/0", BodyPart.HEAD),
                "head/left_ear",
                0.0D,
                0.0D,
                -2.0D,
                1.0D,
                5.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/head/right_ear/0", BodyPart.HEAD),
                "head/right_ear",
                -1.0D,
                0.0D,
                -2.0D,
                0.0D,
                5.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/jacket/0", BodyPart.TORSO),
                "jacket",
                -4.25D,
                -0.25D,
                -2.25D,
                4.25D,
                12.25D,
                2.25D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/left_pants/0", BodyPart.LEFT_LEG),
                "left_pants",
                -2.25D,
                -0.25D,
                -2.25D,
                2.25D,
                12.25D,
                2.25D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/left_sleeve/0", BodyPart.LEFT_ARM),
                "left_sleeve",
                -1.25D,
                -2.25D,
                -2.25D,
                3.25D,
                10.25D,
                2.25D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/right_pants/0", BodyPart.RIGHT_LEG),
                "right_pants",
                -2.25D,
                -0.25D,
                -2.25D,
                2.25D,
                12.25D,
                2.25D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin/right_sleeve/0", BodyPart.RIGHT_ARM),
                "right_sleeve",
                -3.25D,
                -2.25D,
                -2.25D,
                1.25D,
                10.25D,
                2.25D)));
  }

  private EntityHitboxProfile<String> piglinBrute() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin_brute/ear/0", BodyPart.HEAD),
                "ear",
                -3.0D,
                -6.0D,
                -1.0D,
                3.0D,
                0.0D,
                0.0D),
            this.volumes.box(BodyPart.HEAD, "head", -5.0D, -8.0D, -4.0D, 5.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin_brute/head/1", BodyPart.HEAD),
                "head",
                -2.0D,
                -4.0D,
                -5.0D,
                2.0D,
                0.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin_brute/head/2", BodyPart.HEAD),
                "head",
                2.0D,
                -2.0D,
                -5.0D,
                3.0D,
                0.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin_brute/head/3", BodyPart.HEAD),
                "head",
                -3.0D,
                -2.0D,
                -5.0D,
                -2.0D,
                0.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin_brute/head/left_ear/0", BodyPart.HEAD),
                "head/left_ear",
                0.0D,
                0.0D,
                -2.0D,
                1.0D,
                5.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/piglin_brute/head/right_ear/0", BodyPart.HEAD),
                "head/right_ear",
                -1.0D,
                0.0D,
                -2.0D,
                0.0D,
                5.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/piglin_brute/jacket/0", BodyPart.TORSO),
                "jacket",
                -4.25D,
                -0.25D,
                -2.25D,
                4.25D,
                12.25D,
                2.25D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/piglin_brute/left_pants/0", BodyPart.LEFT_LEG),
                "left_pants",
                -2.25D,
                -0.25D,
                -2.25D,
                2.25D,
                12.25D,
                2.25D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/piglin_brute/left_sleeve/0", BodyPart.LEFT_ARM),
                "left_sleeve",
                -1.25D,
                -2.25D,
                -2.25D,
                3.25D,
                10.25D,
                2.25D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/piglin_brute/right_pants/0", BodyPart.RIGHT_LEG),
                "right_pants",
                -2.25D,
                -0.25D,
                -2.25D,
                2.25D,
                12.25D,
                2.25D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/piglin_brute/right_sleeve/0", BodyPart.RIGHT_ARM),
                "right_sleeve",
                -3.25D,
                -2.25D,
                -2.25D,
                1.25D,
                10.25D,
                2.25D)));
  }

  private EntityHitboxProfile<String> skeleton() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -1.0D, 1.0D, 10.0D, 1.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -1.0D, 0.0D, -1.0D, 1.0D, 12.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -1.0D, -2.0D, -1.0D, 1.0D, 10.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -1.0D, 0.0D, -1.0D, 1.0D, 12.0D, 1.0D)));
  }

  private EntityHitboxProfile<String> stray() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -1.0D, 1.0D, 10.0D, 1.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -1.0D, 0.0D, -1.0D, 1.0D, 12.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -1.0D, -2.0D, -1.0D, 1.0D, 10.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -1.0D, 0.0D, -1.0D, 1.0D, 12.0D, 1.0D)));
  }

  private EntityHitboxProfile<String> vex() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_WING, "left_wing", 0.0D, 0.0D, 0.0D, 20.0D, 12.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -1.0D, -1.0D, -2.0D, 5.0D, 9.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_WING, "right_wing", -20.0D, 0.0D, 0.0D, 0.0D, 12.0D, 1.0D)));
  }

  private EntityHitboxProfile<String> witherSkeleton() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -1.0D, 1.0D, 10.0D, 1.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -1.0D, 0.0D, -1.0D, 1.0D, 12.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -1.0D, -2.0D, -1.0D, 1.0D, 10.0D, 1.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -1.0D, 0.0D, -1.0D, 1.0D, 12.0D, 1.0D)));
  }

  private EntityHitboxProfile<String> zombie() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> zombieVillager() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -3.0D, 4.0D, 12.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombie_villager/body/1", BodyPart.TORSO),
                "body",
                -4.05D,
                -0.05D,
                -3.05D,
                4.05D,
                18.05D,
                3.05D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -10.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombie_villager/head/1", BodyPart.HEAD),
                "head",
                -1.0D,
                -3.0D,
                -6.0D,
                1.0D,
                1.0D,
                -4.0D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> zombifiedPiglin() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombified_piglin/ear/0", BodyPart.HEAD),
                "ear",
                -3.0D,
                -6.0D,
                -1.0D,
                3.0D,
                0.0D,
                0.0D),
            this.volumes.box(BodyPart.HEAD, "head", -5.0D, -8.0D, -4.0D, 5.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombified_piglin/head/1", BodyPart.HEAD),
                "head",
                -2.0D,
                -4.0D,
                -5.0D,
                2.0D,
                0.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombified_piglin/head/2", BodyPart.HEAD),
                "head",
                2.0D,
                -2.0D,
                -5.0D,
                3.0D,
                0.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombified_piglin/head/3", BodyPart.HEAD),
                "head",
                -3.0D,
                -2.0D,
                -5.0D,
                -2.0D,
                0.0D,
                -4.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombified_piglin/head/left_ear/0", BodyPart.HEAD),
                "head/left_ear",
                0.0D,
                0.0D,
                -2.0D,
                1.0D,
                5.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombified_piglin/head/right_ear/0", BodyPart.HEAD),
                "head/right_ear",
                -1.0D,
                0.0D,
                -2.0D,
                0.0D,
                5.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombified_piglin/jacket/0", BodyPart.TORSO),
                "jacket",
                -4.25D,
                -0.25D,
                -2.25D,
                4.25D,
                12.25D,
                2.25D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -1.0D, -2.0D, -2.0D, 3.0D, 10.0D, 2.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombified_piglin/left_pants/0", BodyPart.LEFT_LEG),
                "left_pants",
                -2.25D,
                -0.25D,
                -2.25D,
                2.25D,
                12.25D,
                2.25D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombified_piglin/left_sleeve/0", BodyPart.LEFT_ARM),
                "left_sleeve",
                -1.25D,
                -2.25D,
                -2.25D,
                3.25D,
                10.25D,
                2.25D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -3.0D, -2.0D, -2.0D, 1.0D, 10.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombified_piglin/right_pants/0", BodyPart.RIGHT_LEG),
                "right_pants",
                -2.25D,
                -0.25D,
                -2.25D,
                2.25D,
                12.25D,
                2.25D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombified_piglin/right_sleeve/0", BodyPart.RIGHT_ARM),
                "right_sleeve",
                -3.25D,
                -2.25D,
                -2.25D,
                1.25D,
                10.25D,
                2.25D)));
  }
}
