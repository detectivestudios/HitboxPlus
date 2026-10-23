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

public class MonsterProfiles {

  private final ProfileVolumes volumes = new ProfileVolumes();

  public void addTo(Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles) {
    profiles.put(new VanillaProfileKey("minecraft:blaze", "base"), this.blaze());
    profiles.put(new VanillaProfileKey("minecraft:cave_spider", "base"), this.caveSpider());
    profiles.put(new VanillaProfileKey("minecraft:creeper", "base"), this.creeper());
    profiles.put(new VanillaProfileKey("minecraft:endermite", "base"), this.endermite());
    profiles.put(new VanillaProfileKey("minecraft:ghast", "base"), this.ghast());
    profiles.put(new VanillaProfileKey("minecraft:iron_golem", "base"), this.ironGolem());
    profiles.put(new VanillaProfileKey("minecraft:magma_cube", "base"), this.magmaCube());
    profiles.put(new VanillaProfileKey("minecraft:shulker", "base"), this.shulker());
    profiles.put(new VanillaProfileKey("minecraft:silverfish", "base"), this.silverfish());
    profiles.put(new VanillaProfileKey("minecraft:slime", "base"), this.slime());
    profiles.put(new VanillaProfileKey("minecraft:slime", "inner"), this.slimeInner());
    profiles.put(new VanillaProfileKey("minecraft:snow_golem", "base"), this.snowGolem());
    profiles.put(new VanillaProfileKey("minecraft:spider", "base"), this.spider());
    profiles.put(new VanillaProfileKey("minecraft:wither", "base"), this.wither());
  }

  private EntityHitboxProfile<String> blaze() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -4.0D, -4.0D, 4.0D, 4.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part0/0", BodyPart.TORSO),
                "part0",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part1/0", BodyPart.TORSO),
                "part1",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part10/0", BodyPart.TORSO),
                "part10",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part11/0", BodyPart.TORSO),
                "part11",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part2/0", BodyPart.TORSO),
                "part2",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part3/0", BodyPart.TORSO),
                "part3",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part4/0", BodyPart.TORSO),
                "part4",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part5/0", BodyPart.TORSO),
                "part5",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part6/0", BodyPart.TORSO),
                "part6",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part7/0", BodyPart.TORSO),
                "part7",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part8/0", BodyPart.TORSO),
                "part8",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/blaze/part9/0", BodyPart.TORSO),
                "part9",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                8.0D,
                2.0D)));
  }

  private EntityHitboxProfile<String> caveSpider() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cave_spider/body0/0", BodyPart.TORSO),
                "body0",
                -3.0D,
                -3.0D,
                -3.0D,
                3.0D,
                3.0D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cave_spider/body1/0", BodyPart.TORSO),
                "body1",
                -5.0D,
                -4.0D,
                -6.0D,
                5.0D,
                4.0D,
                6.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -4.0D, -8.0D, 4.0D, 4.0D, 0.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -1.0D, -1.0D, -1.0D, 15.0D, 1.0D, 1.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -1.0D, -1.0D, -1.0D, 15.0D, 1.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/cave_spider/left_middle_front_leg/0",
                    BodyPart.FRONT_LEFT_LEG),
                "left_middle_front_leg",
                -1.0D,
                -1.0D,
                -1.0D,
                15.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/cave_spider/left_middle_hind_leg/0", BodyPart.HIND_LEFT_LEG),
                "left_middle_hind_leg",
                -1.0D,
                -1.0D,
                -1.0D,
                15.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG,
                "right_front_leg",
                -15.0D,
                -1.0D,
                -1.0D,
                1.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -15.0D, -1.0D, -1.0D, 1.0D, 1.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/cave_spider/right_middle_front_leg/0",
                    BodyPart.FRONT_RIGHT_LEG),
                "right_middle_front_leg",
                -15.0D,
                -1.0D,
                -1.0D,
                1.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/cave_spider/right_middle_hind_leg/0",
                    BodyPart.HIND_RIGHT_LEG),
                "right_middle_hind_leg",
                -15.0D,
                -1.0D,
                -1.0D,
                1.0D,
                1.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> creeper() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, 0.0D, -2.0D, 4.0D, 12.0D, 2.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 6.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 6.0D, 2.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 6.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 6.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> endermite() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/endermite/segment0/0", BodyPart.HEAD),
                "segment0",
                -2.0D,
                0.0D,
                -1.0D,
                2.0D,
                3.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/endermite/segment1/0", BodyPart.TORSO),
                "segment1",
                -3.0D,
                0.0D,
                -2.5D,
                3.0D,
                4.0D,
                2.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/endermite/segment2/0", BodyPart.TORSO),
                "segment2",
                -1.5D,
                0.0D,
                -0.5D,
                1.5D,
                3.0D,
                0.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/endermite/segment3/0", BodyPart.TORSO),
                "segment3",
                -0.5D,
                0.0D,
                -0.5D,
                0.5D,
                2.0D,
                0.5D)));
  }

  private EntityHitboxProfile<String> ghast() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -8.0D, -8.0D, -8.0D, 8.0D, 8.0D, 8.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ghast/tentacle0/0", BodyPart.TENTACLE),
                "tentacle0",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                8.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ghast/tentacle1/0", BodyPart.TENTACLE),
                "tentacle1",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                13.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ghast/tentacle2/0", BodyPart.TENTACLE),
                "tentacle2",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                9.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ghast/tentacle3/0", BodyPart.TENTACLE),
                "tentacle3",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                11.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ghast/tentacle4/0", BodyPart.TENTACLE),
                "tentacle4",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                11.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ghast/tentacle5/0", BodyPart.TENTACLE),
                "tentacle5",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                10.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ghast/tentacle6/0", BodyPart.TENTACLE),
                "tentacle6",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                12.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ghast/tentacle7/0", BodyPart.TENTACLE),
                "tentacle7",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                9.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/ghast/tentacle8/0", BodyPart.TENTACLE),
                "tentacle8",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                12.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> ironGolem() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -9.0D, -2.0D, -6.0D, 9.0D, 10.0D, 5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/iron_golem/body/1", BodyPart.TORSO),
                "body",
                -5.0D,
                9.5D,
                -3.5D,
                5.0D,
                15.5D,
                3.5D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -12.0D, -5.5D, 4.0D, -2.0D, 2.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/iron_golem/head/1", BodyPart.HEAD),
                "head",
                -1.0D,
                -5.0D,
                -7.5D,
                1.0D,
                -1.0D,
                -5.5D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", 9.0D, -2.5D, -3.0D, 13.0D, 27.5D, 3.0D),
            this.volumes.box(BodyPart.LEFT_LEG, "left_leg", -3.5D, -3.0D, -3.0D, 2.5D, 13.0D, 2.0D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -13.0D, -2.5D, -3.0D, -9.0D, 27.5D, 3.0D),
            this.volumes.box(
                BodyPart.RIGHT_LEG, "right_leg", -3.5D, -3.0D, -3.0D, 2.5D, 13.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> magmaCube() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/magma_cube/cube0/0", BodyPart.TORSO),
                "cube0",
                -4.0D,
                16.0D,
                -4.0D,
                4.0D,
                17.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/magma_cube/cube1/0", BodyPart.TORSO),
                "cube1",
                -4.0D,
                17.0D,
                -4.0D,
                4.0D,
                18.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/magma_cube/cube2/0", BodyPart.TORSO),
                "cube2",
                -4.0D,
                18.0D,
                -4.0D,
                4.0D,
                19.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/magma_cube/cube3/0", BodyPart.TORSO),
                "cube3",
                -4.0D,
                19.0D,
                -4.0D,
                4.0D,
                20.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/magma_cube/cube4/0", BodyPart.TORSO),
                "cube4",
                -4.0D,
                20.0D,
                -4.0D,
                4.0D,
                21.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/magma_cube/cube5/0", BodyPart.TORSO),
                "cube5",
                -4.0D,
                21.0D,
                -4.0D,
                4.0D,
                22.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/magma_cube/cube6/0", BodyPart.TORSO),
                "cube6",
                -4.0D,
                22.0D,
                -4.0D,
                4.0D,
                23.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/magma_cube/cube7/0", BodyPart.TORSO),
                "cube7",
                -4.0D,
                23.0D,
                -4.0D,
                4.0D,
                24.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/magma_cube/inside_cube/0", BodyPart.TORSO),
                "inside_cube",
                -2.0D,
                18.0D,
                -2.0D,
                2.0D,
                22.0D,
                2.0D)));
  }

  private EntityHitboxProfile<String> shulker() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/shulker/base/0", BodyPart.TORSO),
                "base",
                -8.0D,
                -8.0D,
                -8.0D,
                8.0D,
                0.0D,
                8.0D),
            this.volumes.box(BodyPart.HEAD, "head", -3.0D, 0.0D, -3.0D, 3.0D, 6.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/shulker/lid/0", BodyPart.TORSO),
                "lid",
                -8.0D,
                -16.0D,
                -8.0D,
                8.0D,
                -4.0D,
                8.0D)));
  }

  private EntityHitboxProfile<String> silverfish() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/layer0/0", BodyPart.TORSO),
                "layer0",
                -5.0D,
                0.0D,
                -1.5D,
                5.0D,
                8.0D,
                1.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/layer1/0", BodyPart.TORSO),
                "layer1",
                -3.0D,
                0.0D,
                -1.5D,
                3.0D,
                4.0D,
                1.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/layer2/0", BodyPart.TORSO),
                "layer2",
                -3.0D,
                0.0D,
                -1.5D,
                3.0D,
                5.0D,
                0.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/segment0/0", BodyPart.HEAD),
                "segment0",
                -1.5D,
                0.0D,
                -1.0D,
                1.5D,
                2.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/segment1/0", BodyPart.TORSO),
                "segment1",
                -2.0D,
                0.0D,
                -1.0D,
                2.0D,
                3.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/segment2/0", BodyPart.TORSO),
                "segment2",
                -3.0D,
                0.0D,
                -1.5D,
                3.0D,
                4.0D,
                1.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/segment3/0", BodyPart.TORSO),
                "segment3",
                -1.5D,
                0.0D,
                -1.5D,
                1.5D,
                3.0D,
                1.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/segment4/0", BodyPart.TORSO),
                "segment4",
                -1.0D,
                0.0D,
                -1.5D,
                1.0D,
                2.0D,
                1.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/segment5/0", BodyPart.TORSO),
                "segment5",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/silverfish/segment6/0", BodyPart.TORSO),
                "segment6",
                -0.5D,
                0.0D,
                -1.0D,
                0.5D,
                1.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> slime() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/slime/cube/0", BodyPart.TORSO),
                "cube",
                -4.0D,
                16.0D,
                -4.0D,
                4.0D,
                24.0D,
                4.0D)));
  }

  private EntityHitboxProfile<String> slimeInner() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/slime/cube/0", BodyPart.TORSO),
                "cube",
                -3.0D,
                17.0D,
                -3.0D,
                3.0D,
                23.0D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/slime/left_eye/0", BodyPart.HEAD),
                "left_eye",
                1.25D,
                18.0D,
                -3.5D,
                3.25D,
                20.0D,
                -1.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/slime/mouth/0", BodyPart.HEAD),
                "mouth",
                0.0D,
                21.0D,
                -3.5D,
                1.0D,
                22.0D,
                -2.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/slime/right_eye/0", BodyPart.HEAD),
                "right_eye",
                -3.25D,
                18.0D,
                -3.5D,
                -1.25D,
                20.0D,
                -1.5D)));
  }

  private EntityHitboxProfile<String> snowGolem() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.HEAD, "head", -3.5D, -7.5D, -3.5D, 3.5D, -0.5D, 3.5D),
            this.volumes.box(BodyPart.LEFT_ARM, "left_arm", -0.5D, 0.5D, -0.5D, 10.5D, 1.5D, 0.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/snow_golem/lower_body/0", BodyPart.TORSO),
                "lower_body",
                -5.5D,
                -11.5D,
                -5.5D,
                5.5D,
                -0.5D,
                5.5D),
            this.volumes.box(
                BodyPart.RIGHT_ARM, "right_arm", -0.5D, 0.5D, -0.5D, 10.5D, 1.5D, 0.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/snow_golem/upper_body/0", BodyPart.TORSO),
                "upper_body",
                -4.5D,
                -9.5D,
                -4.5D,
                4.5D,
                -0.5D,
                4.5D)));
  }

  private EntityHitboxProfile<String> spider() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/spider/body0/0", BodyPart.TORSO),
                "body0",
                -3.0D,
                -3.0D,
                -3.0D,
                3.0D,
                3.0D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/spider/body1/0", BodyPart.TORSO),
                "body1",
                -5.0D,
                -4.0D,
                -6.0D,
                5.0D,
                4.0D,
                6.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -4.0D, -8.0D, 4.0D, 4.0D, 0.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -1.0D, -1.0D, -1.0D, 15.0D, 1.0D, 1.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -1.0D, -1.0D, -1.0D, 15.0D, 1.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/spider/left_middle_front_leg/0", BodyPart.FRONT_LEFT_LEG),
                "left_middle_front_leg",
                -1.0D,
                -1.0D,
                -1.0D,
                15.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/spider/left_middle_hind_leg/0", BodyPart.HIND_LEFT_LEG),
                "left_middle_hind_leg",
                -1.0D,
                -1.0D,
                -1.0D,
                15.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG,
                "right_front_leg",
                -15.0D,
                -1.0D,
                -1.0D,
                1.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -15.0D, -1.0D, -1.0D, 1.0D, 1.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/spider/right_middle_front_leg/0", BodyPart.FRONT_RIGHT_LEG),
                "right_middle_front_leg",
                -15.0D,
                -1.0D,
                -1.0D,
                1.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/spider/right_middle_hind_leg/0", BodyPart.HIND_RIGHT_LEG),
                "right_middle_hind_leg",
                -15.0D,
                -1.0D,
                -1.0D,
                1.0D,
                1.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> wither() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wither/center_head/0", BodyPart.HEAD),
                "center_head",
                -4.0D,
                -4.0D,
                -4.0D,
                4.0D,
                4.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wither/left_head/0", BodyPart.HEAD),
                "left_head",
                -4.0D,
                -4.0D,
                -4.0D,
                2.0D,
                2.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wither/ribcage/0", BodyPart.TORSO),
                "ribcage",
                0.0D,
                0.0D,
                0.0D,
                3.0D,
                10.0D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wither/ribcage/1", BodyPart.TORSO),
                "ribcage",
                -4.0D,
                1.5D,
                0.5D,
                7.0D,
                3.5D,
                2.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wither/ribcage/2", BodyPart.TORSO),
                "ribcage",
                -4.0D,
                4.0D,
                0.5D,
                7.0D,
                6.0D,
                2.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wither/ribcage/3", BodyPart.TORSO),
                "ribcage",
                -4.0D,
                6.5D,
                0.5D,
                7.0D,
                8.5D,
                2.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wither/right_head/0", BodyPart.HEAD),
                "right_head",
                -4.0D,
                -4.0D,
                -4.0D,
                2.0D,
                2.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/wither/shoulders/0", BodyPart.TORSO),
                "shoulders",
                -10.0D,
                3.9D,
                -0.5D,
                10.0D,
                6.9D,
                2.5D),
            this.volumes.box(BodyPart.TAIL, "tail", 0.0D, 0.0D, 0.0D, 3.0D, 6.0D, 3.0D)));
  }
}
