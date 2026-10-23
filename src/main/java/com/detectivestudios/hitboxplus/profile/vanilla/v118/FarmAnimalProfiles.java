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

public class FarmAnimalProfiles {

  private final ProfileVolumes volumes = new ProfileVolumes();

  public void addTo(Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles) {
    profiles.put(new VanillaProfileKey("minecraft:cow", "base"), this.cow());
    profiles.put(new VanillaProfileKey("minecraft:donkey", "base"), this.donkey());
    profiles.put(new VanillaProfileKey("minecraft:donkey", "young"), this.donkeyYoung());
    profiles.put(new VanillaProfileKey("minecraft:goat", "base"), this.goat());
    profiles.put(new VanillaProfileKey("minecraft:goat", "young"), this.goatYoung());
    profiles.put(new VanillaProfileKey("minecraft:horse", "base"), this.horse());
    profiles.put(new VanillaProfileKey("minecraft:horse", "young"), this.horseYoung());
    profiles.put(new VanillaProfileKey("minecraft:llama", "base"), this.llama());
    profiles.put(new VanillaProfileKey("minecraft:mooshroom", "base"), this.mooshroom());
    profiles.put(new VanillaProfileKey("minecraft:mule", "base"), this.mule());
    profiles.put(new VanillaProfileKey("minecraft:mule", "young"), this.muleYoung());
    profiles.put(new VanillaProfileKey("minecraft:pig", "base"), this.pig());
    profiles.put(new VanillaProfileKey("minecraft:sheep", "base"), this.sheep());
    profiles.put(new VanillaProfileKey("minecraft:sheep", "wool"), this.sheepWool());
    profiles.put(new VanillaProfileKey("minecraft:skeleton_horse", "base"), this.skeletonHorse());
    profiles.put(
        new VanillaProfileKey("minecraft:skeleton_horse", "young"), this.skeletonHorseYoung());
    profiles.put(new VanillaProfileKey("minecraft:trader_llama", "base"), this.traderLlama());
    profiles.put(new VanillaProfileKey("minecraft:zombie_horse", "base"), this.zombieHorse());
    profiles.put(new VanillaProfileKey("minecraft:zombie_horse", "young"), this.zombieHorseYoung());
  }

  private EntityHitboxProfile<String> cow() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -6.0D, -10.0D, -7.0D, 6.0D, 8.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:udder", BodyPart.TORSO),
                "body",
                -2.0D,
                2.0D,
                -8.0D,
                2.0D,
                8.0D,
                -7.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -4.0D, -6.0D, 4.0D, 4.0D, 0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:right_horn", BodyPart.HEAD),
                "head",
                -5.0D,
                -5.0D,
                -4.0D,
                -4.0D,
                -2.0D,
                -3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:left_horn", BodyPart.HEAD),
                "head",
                4.0D,
                -5.0D,
                -4.0D,
                5.0D,
                -2.0D,
                -3.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> donkey() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/donkey/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/donkey/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/donkey/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                -1.0D,
                -7.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -1.0D,
                -7.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/donkey/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -3.0D, -1.01D, -1.9D, 1.0D, 9.99D, 2.1D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -3.0D, -1.01D, -1.0D, 1.0D, 9.99D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG,
                "right_front_leg",
                -1.0D,
                -1.01D,
                -1.9D,
                3.0D,
                9.99D,
                2.1D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG,
                "right_hind_leg",
                -1.0D,
                -1.01D,
                -1.0D,
                3.0D,
                9.99D,
                3.0D)));
  }

  private EntityHitboxProfile<String> donkeyYoung() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/donkey/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/donkey/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/donkey/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                -1.0D,
                -7.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -1.0D,
                -7.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/donkey/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/left_front_baby_leg/0", BodyPart.FRONT_LEFT_LEG),
                "left_front_baby_leg",
                -3.0D,
                -6.51D,
                -1.9D,
                1.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/left_hind_baby_leg/0", BodyPart.HIND_LEFT_LEG),
                "left_hind_baby_leg",
                -3.0D,
                -6.51D,
                -1.0D,
                1.0D,
                15.49D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/right_front_baby_leg/0", BodyPart.FRONT_RIGHT_LEG),
                "right_front_baby_leg",
                -1.0D,
                -6.51D,
                -1.9D,
                3.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/donkey/right_hind_baby_leg/0", BodyPart.HIND_RIGHT_LEG),
                "right_hind_baby_leg",
                -1.0D,
                -6.51D,
                -1.0D,
                3.0D,
                15.49D,
                3.0D)));
  }

  private EntityHitboxProfile<String> goat() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, -17.0D, -7.0D, 5.0D, -6.0D, 9.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/body/1", BodyPart.TORSO),
                "body",
                -5.0D,
                -18.0D,
                -8.0D,
                6.0D,
                -4.0D,
                3.0D),
            this.volumes.box(BodyPart.HEAD, "head", -6.0D, -11.0D, -10.0D, -3.0D, -9.0D, -9.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/head/1", BodyPart.HEAD),
                "head",
                2.0D,
                -11.0D,
                -10.0D,
                5.0D,
                -9.0D,
                -9.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/head/2", BodyPart.HEAD),
                "head",
                -0.5D,
                -3.0D,
                -14.0D,
                -0.5D,
                4.0D,
                -9.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/head/left_horn/0", BodyPart.HEAD),
                "head/left_horn",
                -0.01D,
                -16.0D,
                -10.0D,
                1.99D,
                -9.0D,
                -8.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -3.0D,
                -4.0D,
                -8.0D,
                2.0D,
                3.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/head/right_horn/0", BodyPart.HEAD),
                "head/right_horn",
                -2.99D,
                -16.0D,
                -10.0D,
                -0.99D,
                -9.0D,
                -8.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", 0.0D, 0.0D, 0.0D, 3.0D, 10.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", 0.0D, 4.0D, 0.0D, 3.0D, 10.0D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", 0.0D, 0.0D, 0.0D, 3.0D, 10.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", 0.0D, 4.0D, 0.0D, 3.0D, 10.0D, 3.0D)));
  }

  private EntityHitboxProfile<String> goatYoung() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, -17.0D, -7.0D, 5.0D, -6.0D, 9.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/body/1", BodyPart.TORSO),
                "body",
                -5.0D,
                -18.0D,
                -8.0D,
                6.0D,
                -4.0D,
                3.0D),
            this.volumes.box(BodyPart.HEAD, "head", -6.0D, -11.0D, -10.0D, -3.0D, -9.0D, -9.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/head/1", BodyPart.HEAD),
                "head",
                2.0D,
                -11.0D,
                -10.0D,
                5.0D,
                -9.0D,
                -9.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/head/2", BodyPart.HEAD),
                "head",
                -0.5D,
                -3.0D,
                -14.0D,
                -0.5D,
                4.0D,
                -9.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/goat/head/nose/0", BodyPart.HEAD),
                "head/nose",
                -3.0D,
                -4.0D,
                -8.0D,
                2.0D,
                3.0D,
                2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", 0.0D, 0.0D, 0.0D, 3.0D, 10.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", 0.0D, 4.0D, 0.0D, 3.0D, 10.0D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", 0.0D, 0.0D, 0.0D, 3.0D, 10.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", 0.0D, 4.0D, 0.0D, 3.0D, 10.0D, 3.0D)));
  }

  private EntityHitboxProfile<String> horse() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/horse/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/horse/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/horse/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                0.551D,
                -12.999D,
                4.001D,
                2.549D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -2.549D,
                -12.999D,
                4.001D,
                -0.55099994D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/horse/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -3.0D, -1.01D, -1.9D, 1.0D, 9.99D, 2.1D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -3.0D, -1.01D, -1.0D, 1.0D, 9.99D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG,
                "right_front_leg",
                -1.0D,
                -1.01D,
                -1.9D,
                3.0D,
                9.99D,
                2.1D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG,
                "right_hind_leg",
                -1.0D,
                -1.01D,
                -1.0D,
                3.0D,
                9.99D,
                3.0D)));
  }

  private EntityHitboxProfile<String> horseYoung() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/horse/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/horse/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/horse/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                0.551D,
                -12.999D,
                4.001D,
                2.549D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -2.549D,
                -12.999D,
                4.001D,
                -0.55099994D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/horse/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/left_front_baby_leg/0", BodyPart.FRONT_LEFT_LEG),
                "left_front_baby_leg",
                -3.0D,
                -6.51D,
                -1.9D,
                1.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/left_hind_baby_leg/0", BodyPart.HIND_LEFT_LEG),
                "left_hind_baby_leg",
                -3.0D,
                -6.51D,
                -1.0D,
                1.0D,
                15.49D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/right_front_baby_leg/0", BodyPart.FRONT_RIGHT_LEG),
                "right_front_baby_leg",
                -1.0D,
                -6.51D,
                -1.9D,
                3.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/horse/right_hind_baby_leg/0", BodyPart.HIND_RIGHT_LEG),
                "right_hind_baby_leg",
                -1.0D,
                -6.51D,
                -1.0D,
                3.0D,
                15.49D,
                3.0D)));
  }

  private EntityHitboxProfile<String> llama() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -6.0D, -10.0D, -7.0D, 6.0D, 8.0D, 3.0D),
            this.volumes.box(BodyPart.HEAD, "head", -2.0D, -14.0D, -10.0D, 2.0D, -10.0D, -1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/llama/head/1", BodyPart.HEAD),
                "head",
                -4.0D,
                -16.0D,
                -6.0D,
                4.0D,
                2.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/llama/head/2", BodyPart.HEAD),
                "head",
                -4.0D,
                -19.0D,
                -4.0D,
                -1.0D,
                -16.0D,
                -2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/llama/head/3", BodyPart.HEAD),
                "head",
                1.0D,
                -19.0D,
                -4.0D,
                4.0D,
                -16.0D,
                -2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 14.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 14.0D, 2.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 14.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 14.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> mooshroom() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -6.0D, -10.0D, -7.0D, 6.0D, 8.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:udder", BodyPart.TORSO),
                "body",
                -2.0D,
                2.0D,
                -8.0D,
                2.0D,
                8.0D,
                -7.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -4.0D, -6.0D, 4.0D, 4.0D, 0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:right_horn", BodyPart.HEAD),
                "head",
                -5.0D,
                -5.0D,
                -4.0D,
                -4.0D,
                -2.0D,
                -3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:left_horn", BodyPart.HEAD),
                "head",
                4.0D,
                -5.0D,
                -4.0D,
                5.0D,
                -2.0D,
                -3.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> mule() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/mule/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/mule/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/mule/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                -1.0D,
                -7.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -1.0D,
                -7.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/mule/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -3.0D, -1.01D, -1.9D, 1.0D, 9.99D, 2.1D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -3.0D, -1.01D, -1.0D, 1.0D, 9.99D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG,
                "right_front_leg",
                -1.0D,
                -1.01D,
                -1.9D,
                3.0D,
                9.99D,
                2.1D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG,
                "right_hind_leg",
                -1.0D,
                -1.01D,
                -1.0D,
                3.0D,
                9.99D,
                3.0D)));
  }

  private EntityHitboxProfile<String> muleYoung() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/mule/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/mule/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/mule/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                -1.0D,
                -7.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -1.0D,
                -7.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/mule/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/left_front_baby_leg/0", BodyPart.FRONT_LEFT_LEG),
                "left_front_baby_leg",
                -3.0D,
                -6.51D,
                -1.9D,
                1.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/left_hind_baby_leg/0", BodyPart.HIND_LEFT_LEG),
                "left_hind_baby_leg",
                -3.0D,
                -6.51D,
                -1.0D,
                1.0D,
                15.49D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/right_front_baby_leg/0", BodyPart.FRONT_RIGHT_LEG),
                "right_front_baby_leg",
                -1.0D,
                -6.51D,
                -1.9D,
                3.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/mule/right_hind_baby_leg/0", BodyPart.HIND_RIGHT_LEG),
                "right_hind_baby_leg",
                -1.0D,
                -6.51D,
                -1.0D,
                3.0D,
                15.49D,
                3.0D)));
  }

  private EntityHitboxProfile<String> pig() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.0D, -10.0D, -7.0D, 5.0D, 6.0D, 1.0D),
            this.volumes.box(BodyPart.HEAD, "head", -4.0D, -4.0D, -8.0D, 4.0D, 4.0D, 0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:snout", BodyPart.HEAD),
                "head",
                -2.0D,
                0.0D,
                -9.0D,
                2.0D,
                3.0D,
                -8.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 6.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 6.0D, 2.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 6.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 6.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> sheep() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, -10.0D, -7.0D, 4.0D, 6.0D, -1.0D),
            this.volumes.box(BodyPart.HEAD, "head", -3.0D, -4.0D, -6.0D, 3.0D, 2.0D, 2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> sheepWool() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, -10.0D, -7.0D, 4.0D, 6.0D, -1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/sheep/body/1", BodyPart.TORSO),
                "body",
                -5.75D,
                -11.75D,
                -8.75D,
                5.75D,
                7.75D,
                0.75D),
            this.volumes.box(BodyPart.HEAD, "head", -3.0D, -4.0D, -6.0D, 3.0D, 2.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/sheep/head/1", BodyPart.HEAD),
                "head",
                -3.6D,
                -4.6D,
                -4.6D,
                3.6D,
                2.6D,
                2.6D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/sheep/left_front_leg/1", BodyPart.FRONT_LEFT_LEG),
                "left_front_leg",
                -2.5D,
                -0.5D,
                -2.5D,
                2.5D,
                6.5D,
                2.5D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/sheep/left_hind_leg/1", BodyPart.HIND_LEFT_LEG),
                "left_hind_leg",
                -2.5D,
                -0.5D,
                -2.5D,
                2.5D,
                6.5D,
                2.5D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/sheep/right_front_leg/1", BodyPart.FRONT_RIGHT_LEG),
                "right_front_leg",
                -2.5D,
                -0.5D,
                -2.5D,
                2.5D,
                6.5D,
                2.5D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 12.0D, 2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/sheep/right_hind_leg/1", BodyPart.HIND_RIGHT_LEG),
                "right_hind_leg",
                -2.5D,
                -0.5D,
                -2.5D,
                2.5D,
                6.5D,
                2.5D)));
  }

  private EntityHitboxProfile<String> skeletonHorse() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/skeleton_horse/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/skeleton_horse/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                0.551D,
                -12.999D,
                4.001D,
                2.549D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -2.549D,
                -12.999D,
                4.001D,
                -0.55099994D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -3.0D, -1.01D, -1.9D, 1.0D, 9.99D, 2.1D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -3.0D, -1.01D, -1.0D, 1.0D, 9.99D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG,
                "right_front_leg",
                -1.0D,
                -1.01D,
                -1.9D,
                3.0D,
                9.99D,
                2.1D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG,
                "right_hind_leg",
                -1.0D,
                -1.01D,
                -1.0D,
                3.0D,
                9.99D,
                3.0D)));
  }

  private EntityHitboxProfile<String> skeletonHorseYoung() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/skeleton_horse/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/skeleton_horse/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                0.551D,
                -12.999D,
                4.001D,
                2.549D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -2.549D,
                -12.999D,
                4.001D,
                -0.55099994D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/left_front_baby_leg/0",
                    BodyPart.FRONT_LEFT_LEG),
                "left_front_baby_leg",
                -3.0D,
                -6.51D,
                -1.9D,
                1.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/left_hind_baby_leg/0", BodyPart.HIND_LEFT_LEG),
                "left_hind_baby_leg",
                -3.0D,
                -6.51D,
                -1.0D,
                1.0D,
                15.49D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/right_front_baby_leg/0",
                    BodyPart.FRONT_RIGHT_LEG),
                "right_front_baby_leg",
                -1.0D,
                -6.51D,
                -1.9D,
                3.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/skeleton_horse/right_hind_baby_leg/0",
                    BodyPart.HIND_RIGHT_LEG),
                "right_hind_baby_leg",
                -1.0D,
                -6.51D,
                -1.0D,
                3.0D,
                15.49D,
                3.0D)));
  }

  private EntityHitboxProfile<String> traderLlama() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -6.0D, -10.0D, -7.0D, 6.0D, 8.0D, 3.0D),
            this.volumes.box(BodyPart.HEAD, "head", -2.0D, -14.0D, -10.0D, 2.0D, -10.0D, -1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/trader_llama/head/1", BodyPart.HEAD),
                "head",
                -4.0D,
                -16.0D,
                -6.0D,
                4.0D,
                2.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/trader_llama/head/2", BodyPart.HEAD),
                "head",
                -4.0D,
                -19.0D,
                -4.0D,
                -1.0D,
                -16.0D,
                -2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/trader_llama/head/3", BodyPart.HEAD),
                "head",
                1.0D,
                -19.0D,
                -4.0D,
                4.0D,
                -16.0D,
                -2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 14.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 14.0D, 2.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -2.0D, 0.0D, -2.0D, 2.0D, 14.0D, 2.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, -2.0D, 2.0D, 14.0D, 2.0D)));
  }

  private EntityHitboxProfile<String> zombieHorse() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombie_horse/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombie_horse/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                0.551D,
                -12.999D,
                4.001D,
                2.549D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -2.549D,
                -12.999D,
                4.001D,
                -0.55099994D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", -3.0D, -1.01D, -1.9D, 1.0D, 9.99D, 2.1D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -3.0D, -1.01D, -1.0D, 1.0D, 9.99D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG,
                "right_front_leg",
                -1.0D,
                -1.01D,
                -1.9D,
                3.0D,
                9.99D,
                2.1D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG,
                "right_hind_leg",
                -1.0D,
                -1.01D,
                -1.0D,
                3.0D,
                9.99D,
                3.0D)));
  }

  private EntityHitboxProfile<String> zombieHorseYoung() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -5.05D, -8.05D, -17.05D, 5.05D, 2.05D, 5.05D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombie_horse/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                14.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/zombie_horse/head_parts/0", BodyPart.NECK),
                "head_parts",
                -2.05D,
                -6.0D,
                -2.0D,
                1.95D,
                6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/head/0", BodyPart.HEAD),
                "head_parts/head",
                -3.0D,
                -11.0D,
                -2.0D,
                3.0D,
                -6.0D,
                5.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/head/left_ear/0", BodyPart.HEAD),
                "head_parts/head/left_ear",
                0.551D,
                -12.999D,
                4.001D,
                2.549D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/head/right_ear/0", BodyPart.HEAD),
                "head_parts/head/right_ear",
                -2.549D,
                -12.999D,
                4.001D,
                -0.55099994D,
                -10.001D,
                4.999D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/mane/0", BodyPart.NECK),
                "head_parts/mane",
                -1.0D,
                -11.0D,
                5.01D,
                1.0D,
                5.0D,
                7.01D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/head_parts/upper_mouth/0", BodyPart.HEAD),
                "head_parts/upper_mouth",
                -2.0D,
                -11.0D,
                -7.0D,
                2.0D,
                -6.0D,
                -2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/left_front_baby_leg/0", BodyPart.FRONT_LEFT_LEG),
                "left_front_baby_leg",
                -3.0D,
                -6.51D,
                -1.9D,
                1.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/left_hind_baby_leg/0", BodyPart.HIND_LEFT_LEG),
                "left_hind_baby_leg",
                -3.0D,
                -6.51D,
                -1.0D,
                1.0D,
                15.49D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/right_front_baby_leg/0",
                    BodyPart.FRONT_RIGHT_LEG),
                "right_front_baby_leg",
                -1.0D,
                -6.51D,
                -1.9D,
                3.0D,
                15.49D,
                2.1D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/zombie_horse/right_hind_baby_leg/0", BodyPart.HIND_RIGHT_LEG),
                "right_hind_baby_leg",
                -1.0D,
                -6.51D,
                -1.0D,
                3.0D,
                15.49D,
                3.0D)));
  }
}
