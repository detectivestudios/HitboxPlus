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

public class AquaticMobProfiles {

  private final ProfileVolumes volumes = new ProfileVolumes();

  public void addTo(Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles) {
    profiles.put(new VanillaProfileKey("minecraft:axolotl", "base"), this.axolotl());
    profiles.put(new VanillaProfileKey("minecraft:cod", "base"), this.cod());
    profiles.put(new VanillaProfileKey("minecraft:dolphin", "base"), this.dolphin());
    profiles.put(new VanillaProfileKey("minecraft:elder_guardian", "base"), this.elderGuardian());
    profiles.put(new VanillaProfileKey("minecraft:glow_squid", "base"), this.glowSquid());
    profiles.put(new VanillaProfileKey("minecraft:guardian", "base"), this.guardian());
    profiles.put(new VanillaProfileKey("minecraft:pufferfish", "base"), this.pufferfish());
    profiles.put(new VanillaProfileKey("minecraft:pufferfish", "full"), this.pufferfishFull());
    profiles.put(new VanillaProfileKey("minecraft:pufferfish", "medium"), this.pufferfishMedium());
    profiles.put(new VanillaProfileKey("minecraft:salmon", "base"), this.salmon());
    profiles.put(new VanillaProfileKey("minecraft:squid", "base"), this.squid());
    profiles.put(new VanillaProfileKey("minecraft:tropical_fish", "base"), this.tropicalFish());
    profiles.put(
        new VanillaProfileKey("minecraft:tropical_fish", "large"), this.tropicalFishLarge());
    profiles.put(new VanillaProfileKey("minecraft:turtle", "base"), this.turtle());
    profiles.put(new VanillaProfileKey("minecraft:turtle", "gravid"), this.turtleGravid());
  }

  private EntityHitboxProfile<String> axolotl() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, -2.0D, -9.0D, 4.0D, 2.0D, 1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/axolotl/body/1", BodyPart.TORSO),
                "body",
                0.0D,
                -3.0D,
                -8.0D,
                0.0D,
                2.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/axolotl/body/head/0", BodyPart.HEAD),
                "body/head",
                -4.001D,
                -3.001D,
                -5.001D,
                4.001D,
                2.001D,
                0.001D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/axolotl/body/head/left_gills/0", BodyPart.HEAD),
                "body/head/left_gills",
                -3.001D,
                -5.001D,
                -0.001D,
                0.001D,
                2.001D,
                0.001D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/axolotl/body/head/right_gills/0", BodyPart.HEAD),
                "body/head/right_gills",
                -0.001D,
                -5.001D,
                -0.001D,
                3.001D,
                2.001D,
                0.001D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/axolotl/body/head/top_gills/0", BodyPart.HEAD),
                "body/head/top_gills",
                -4.001D,
                -3.001D,
                -0.001D,
                4.001D,
                0.001D,
                0.001D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/axolotl/body/left_front_leg/0", BodyPart.FRONT_LEFT_LEG),
                "body/left_front_leg",
                -1.001D,
                -0.001D,
                -0.001D,
                2.001D,
                5.001D,
                0.001D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/axolotl/body/left_hind_leg/0", BodyPart.HIND_LEFT_LEG),
                "body/left_hind_leg",
                -1.001D,
                -0.001D,
                -0.001D,
                2.001D,
                5.001D,
                0.001D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/axolotl/body/right_front_leg/0", BodyPart.FRONT_RIGHT_LEG),
                "body/right_front_leg",
                -2.001D,
                -0.001D,
                -0.001D,
                1.001D,
                5.001D,
                0.001D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/axolotl/body/right_hind_leg/0", BodyPart.HIND_RIGHT_LEG),
                "body/right_hind_leg",
                -2.001D,
                -0.001D,
                -0.001D,
                1.001D,
                5.001D,
                0.001D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/axolotl/body/tail/0", BodyPart.TAIL),
                "body/tail",
                0.0D,
                -3.0D,
                0.0D,
                0.0D,
                2.0D,
                12.0D)));
  }

  private EntityHitboxProfile<String> cod() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -1.0D, -2.0D, 0.0D, 1.0D, 2.0D, 7.0D),
            this.volumes.box(BodyPart.HEAD, "head", -1.0D, -2.0D, -3.0D, 1.0D, 2.0D, 0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cod/left_fin/0", BodyPart.LEFT_FIN),
                "left_fin",
                0.0D,
                0.0D,
                -1.0D,
                2.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cod/nose/0", BodyPart.HEAD),
                "nose",
                -1.0D,
                -2.0D,
                -1.0D,
                1.0D,
                1.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cod/right_fin/0", BodyPart.RIGHT_FIN),
                "right_fin",
                -2.0D,
                0.0D,
                -1.0D,
                0.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cod/tail_fin/0", BodyPart.TAIL),
                "tail_fin",
                0.0D,
                -2.0D,
                0.0D,
                0.0D,
                2.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/cod/top_fin/0", BodyPart.FIN),
                "top_fin",
                0.0D,
                -1.0D,
                -1.0D,
                0.0D,
                0.0D,
                5.0D)));
  }

  private EntityHitboxProfile<String> dolphin() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, -7.0D, 0.0D, 4.0D, 0.0D, 13.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/dolphin/body/back_fin/0", BodyPart.FIN),
                "body/back_fin",
                -0.5D,
                0.0D,
                8.0D,
                0.5D,
                4.0D,
                13.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/dolphin/body/head/0", BodyPart.HEAD),
                "body/head",
                -4.0D,
                -3.0D,
                -3.0D,
                4.0D,
                4.0D,
                3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/dolphin/body/head/nose/0", BodyPart.HEAD),
                "body/head/nose",
                -1.0D,
                2.0D,
                -7.0D,
                1.0D,
                4.0D,
                -3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/dolphin/body/left_fin/0", BodyPart.LEFT_FIN),
                "body/left_fin",
                -0.5D,
                -4.0D,
                0.0D,
                0.5D,
                0.0D,
                7.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/dolphin/body/right_fin/0", BodyPart.RIGHT_FIN),
                "body/right_fin",
                -0.5D,
                -4.0D,
                0.0D,
                0.5D,
                0.0D,
                7.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/dolphin/body/tail/0", BodyPart.TAIL),
                "body/tail",
                -2.0D,
                -2.5D,
                0.0D,
                2.0D,
                2.5D,
                11.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/dolphin/body/tail/tail_fin/0", BodyPart.TAIL),
                "body/tail/tail_fin",
                -5.0D,
                -0.5D,
                0.0D,
                5.0D,
                0.5D,
                6.0D)));
  }

  private EntityHitboxProfile<String> elderGuardian() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.HEAD, "head", -6.0D, 10.0D, -8.0D, 6.0D, 22.0D, 8.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/1", BodyPart.HEAD),
                "head",
                -8.0D,
                10.0D,
                -6.0D,
                -6.0D,
                22.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/2", BodyPart.HEAD),
                "head",
                6.0D,
                10.0D,
                -6.0D,
                8.0D,
                22.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/3", BodyPart.HEAD),
                "head",
                -6.0D,
                8.0D,
                -6.0D,
                6.0D,
                10.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/4", BodyPart.HEAD),
                "head",
                -6.0D,
                22.0D,
                -6.0D,
                6.0D,
                24.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/eye/0", BodyPart.HEAD),
                "head/eye",
                -1.0D,
                15.0D,
                0.0D,
                1.0D,
                17.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike0/0", BodyPart.HEAD),
                "head/spike0",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike1/0", BodyPart.HEAD),
                "head/spike1",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/elder_guardian/head/spike10/0", BodyPart.HEAD),
                "head/spike10",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/elder_guardian/head/spike11/0", BodyPart.HEAD),
                "head/spike11",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike2/0", BodyPart.HEAD),
                "head/spike2",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike3/0", BodyPart.HEAD),
                "head/spike3",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike4/0", BodyPart.HEAD),
                "head/spike4",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike5/0", BodyPart.HEAD),
                "head/spike5",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike6/0", BodyPart.HEAD),
                "head/spike6",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike7/0", BodyPart.HEAD),
                "head/spike7",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike8/0", BodyPart.HEAD),
                "head/spike8",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/spike9/0", BodyPart.HEAD),
                "head/spike9",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/elder_guardian/head/tail0/0", BodyPart.TAIL),
                "head/tail0",
                -2.0D,
                14.0D,
                7.0D,
                2.0D,
                18.0D,
                15.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/elder_guardian/head/tail0/tail1/0", BodyPart.TAIL),
                "head/tail0/tail1",
                0.0D,
                14.0D,
                0.0D,
                3.0D,
                17.0D,
                7.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/elder_guardian/head/tail0/tail1/tail2/0", BodyPart.TAIL),
                "head/tail0/tail1/tail2",
                0.0D,
                14.0D,
                0.0D,
                2.0D,
                16.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/elder_guardian/head/tail0/tail1/tail2/1", BodyPart.TAIL),
                "head/tail0/tail1/tail2",
                1.0D,
                10.5D,
                3.0D,
                2.0D,
                19.5D,
                12.0D)));
  }

  private EntityHitboxProfile<String> glowSquid() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -6.0D, -8.0D, -6.0D, 6.0D, 8.0D, 6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/glow_squid/tentacle0/0", BodyPart.TENTACLE),
                "tentacle0",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/glow_squid/tentacle1/0", BodyPart.TENTACLE),
                "tentacle1",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/glow_squid/tentacle2/0", BodyPart.TENTACLE),
                "tentacle2",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/glow_squid/tentacle3/0", BodyPart.TENTACLE),
                "tentacle3",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/glow_squid/tentacle4/0", BodyPart.TENTACLE),
                "tentacle4",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/glow_squid/tentacle5/0", BodyPart.TENTACLE),
                "tentacle5",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/glow_squid/tentacle6/0", BodyPart.TENTACLE),
                "tentacle6",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/glow_squid/tentacle7/0", BodyPart.TENTACLE),
                "tentacle7",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> guardian() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.HEAD, "head", -6.0D, 10.0D, -8.0D, 6.0D, 22.0D, 8.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/1", BodyPart.HEAD),
                "head",
                -8.0D,
                10.0D,
                -6.0D,
                -6.0D,
                22.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/2", BodyPart.HEAD),
                "head",
                6.0D,
                10.0D,
                -6.0D,
                8.0D,
                22.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/3", BodyPart.HEAD),
                "head",
                -6.0D,
                8.0D,
                -6.0D,
                6.0D,
                10.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/4", BodyPart.HEAD),
                "head",
                -6.0D,
                22.0D,
                -6.0D,
                6.0D,
                24.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/eye/0", BodyPart.HEAD),
                "head/eye",
                -1.0D,
                15.0D,
                0.0D,
                1.0D,
                17.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike0/0", BodyPart.HEAD),
                "head/spike0",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike1/0", BodyPart.HEAD),
                "head/spike1",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike10/0", BodyPart.HEAD),
                "head/spike10",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike11/0", BodyPart.HEAD),
                "head/spike11",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike2/0", BodyPart.HEAD),
                "head/spike2",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike3/0", BodyPart.HEAD),
                "head/spike3",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike4/0", BodyPart.HEAD),
                "head/spike4",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike5/0", BodyPart.HEAD),
                "head/spike5",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike6/0", BodyPart.HEAD),
                "head/spike6",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike7/0", BodyPart.HEAD),
                "head/spike7",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike8/0", BodyPart.HEAD),
                "head/spike8",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/spike9/0", BodyPart.HEAD),
                "head/spike9",
                -1.0D,
                -4.5D,
                -1.0D,
                1.0D,
                4.5D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/tail0/0", BodyPart.TAIL),
                "head/tail0",
                -2.0D,
                14.0D,
                7.0D,
                2.0D,
                18.0D,
                15.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/guardian/head/tail0/tail1/0", BodyPart.TAIL),
                "head/tail0/tail1",
                0.0D,
                14.0D,
                0.0D,
                3.0D,
                17.0D,
                7.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/guardian/head/tail0/tail1/tail2/0", BodyPart.TAIL),
                "head/tail0/tail1/tail2",
                0.0D,
                14.0D,
                0.0D,
                2.0D,
                16.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/guardian/head/tail0/tail1/tail2/1", BodyPart.TAIL),
                "head/tail0/tail1/tail2",
                1.0D,
                10.5D,
                3.0D,
                2.0D,
                19.5D,
                12.0D)));
  }

  private EntityHitboxProfile<String> pufferfish() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/back_fin/0", BodyPart.FIN),
                "back_fin",
                -1.5D,
                0.0D,
                0.0D,
                1.5D,
                0.0D,
                3.0D),
            this.volumes.box(BodyPart.TORSO, "body", -1.5D, -2.0D, -1.5D, 1.5D, 0.0D, 1.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/left_eye/0", BodyPart.HEAD),
                "left_eye",
                0.5D,
                0.0D,
                -1.5D,
                1.5D,
                1.0D,
                -0.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/left_fin/0", BodyPart.LEFT_FIN),
                "left_fin",
                0.0D,
                0.0D,
                0.0D,
                1.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/right_eye/0", BodyPart.HEAD),
                "right_eye",
                -1.5D,
                0.0D,
                -1.5D,
                -0.5D,
                1.0D,
                -0.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/right_fin/0", BodyPart.RIGHT_FIN),
                "right_fin",
                -1.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                2.0D)));
  }

  private EntityHitboxProfile<String> pufferfishFull() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -4.0D, -8.0D, -4.0D, 4.0D, 0.0D, 4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/bottom_back_fin/0", BodyPart.FIN),
                "bottom_back_fin",
                -4.0D,
                0.0D,
                0.0D,
                4.0D,
                1.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/bottom_front_fin/0", BodyPart.FIN),
                "bottom_front_fin",
                -4.0D,
                0.0D,
                0.0D,
                4.0D,
                1.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/bottom_middle_fin/0", BodyPart.FIN),
                "bottom_middle_fin",
                -4.0D,
                0.0D,
                0.0D,
                4.0D,
                1.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/left_back_fin/0", BodyPart.LEFT_FIN),
                "left_back_fin",
                0.0D,
                -8.0D,
                0.0D,
                1.0D,
                0.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/left_blue_fin/0", BodyPart.LEFT_FIN),
                "left_blue_fin",
                0.0D,
                0.0D,
                -1.0D,
                2.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/left_front_fin/0", BodyPart.LEFT_FIN),
                "left_front_fin",
                0.0D,
                -8.0D,
                0.0D,
                1.0D,
                0.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/right_back_fin/0", BodyPart.RIGHT_FIN),
                "right_back_fin",
                -1.0D,
                -8.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/right_blue_fin/0", BodyPart.RIGHT_FIN),
                "right_blue_fin",
                -2.0D,
                0.0D,
                -1.0D,
                0.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/right_front_fin/0", BodyPart.RIGHT_FIN),
                "right_front_fin",
                -1.0D,
                -8.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/top_back_fin/0", BodyPart.FIN),
                "top_back_fin",
                -4.0D,
                -1.0D,
                0.0D,
                4.0D,
                0.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/top_front_fin/0", BodyPart.FIN),
                "top_front_fin",
                -4.0D,
                -1.0D,
                0.0D,
                4.0D,
                0.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/top_middle_fin/0", BodyPart.FIN),
                "top_middle_fin",
                -4.0D,
                -1.0D,
                0.0D,
                4.0D,
                0.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> pufferfishMedium() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -2.5D, -5.0D, -2.5D, 2.5D, 0.0D, 2.5D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/bottom_back_fin/0", BodyPart.FIN),
                "bottom_back_fin",
                0.0D,
                0.0D,
                0.0D,
                1.0D,
                1.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/bottom_front_fin/0", BodyPart.FIN),
                "bottom_front_fin",
                -2.5D,
                0.0D,
                0.0D,
                2.5D,
                1.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/left_back_fin/0", BodyPart.LEFT_FIN),
                "left_back_fin",
                0.0D,
                -5.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/left_blue_fin/0", BodyPart.LEFT_FIN),
                "left_blue_fin",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/left_front_fin/0", BodyPart.LEFT_FIN),
                "left_front_fin",
                0.0D,
                -5.0D,
                0.0D,
                1.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/right_back_fin/0", BodyPart.RIGHT_FIN),
                "right_back_fin",
                -1.0D,
                -5.0D,
                0.0D,
                0.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/right_blue_fin/0", BodyPart.RIGHT_FIN),
                "right_blue_fin",
                -2.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/pufferfish/right_front_fin/0", BodyPart.RIGHT_FIN),
                "right_front_fin",
                -1.0D,
                -5.0D,
                0.0D,
                0.0D,
                0.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/top_back_fin/0", BodyPart.FIN),
                "top_back_fin",
                -2.5D,
                -1.0D,
                -1.0D,
                2.5D,
                0.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/pufferfish/top_front_fin/0", BodyPart.FIN),
                "top_front_fin",
                -2.5D,
                -1.0D,
                0.0D,
                2.5D,
                0.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> salmon() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/salmon/body_back/0", BodyPart.TORSO),
                "body_back",
                -1.5D,
                -2.5D,
                0.0D,
                1.5D,
                2.5D,
                8.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/salmon/body_back/back_fin/0", BodyPart.FIN),
                "body_back/back_fin",
                0.0D,
                -2.5D,
                0.0D,
                0.0D,
                2.5D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/salmon/body_back/top_back_fin/0", BodyPart.FIN),
                "body_back/top_back_fin",
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/salmon/body_front/0", BodyPart.TORSO),
                "body_front",
                -1.5D,
                -2.5D,
                0.0D,
                1.5D,
                2.5D,
                8.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/salmon/body_front/top_front_fin/0", BodyPart.FIN),
                "body_front/top_front_fin",
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                3.0D),
            this.volumes.box(BodyPart.HEAD, "head", -1.0D, -2.0D, -3.0D, 1.0D, 2.0D, 0.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/salmon/left_fin/0", BodyPart.LEFT_FIN),
                "left_fin",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                0.0D,
                2.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/salmon/right_fin/0", BodyPart.RIGHT_FIN),
                "right_fin",
                -2.0D,
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                2.0D)));
  }

  private EntityHitboxProfile<String> squid() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -6.0D, -8.0D, -6.0D, 6.0D, 8.0D, 6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/squid/tentacle0/0", BodyPart.TENTACLE),
                "tentacle0",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/squid/tentacle1/0", BodyPart.TENTACLE),
                "tentacle1",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/squid/tentacle2/0", BodyPart.TENTACLE),
                "tentacle2",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/squid/tentacle3/0", BodyPart.TENTACLE),
                "tentacle3",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/squid/tentacle4/0", BodyPart.TENTACLE),
                "tentacle4",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/squid/tentacle5/0", BodyPart.TENTACLE),
                "tentacle5",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/squid/tentacle6/0", BodyPart.TENTACLE),
                "tentacle6",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/squid/tentacle7/0", BodyPart.TENTACLE),
                "tentacle7",
                -1.0D,
                0.0D,
                -1.0D,
                1.0D,
                18.0D,
                1.0D)));
  }

  private EntityHitboxProfile<String> tropicalFish() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -1.0D, -1.5D, -3.0D, 1.0D, 1.5D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/tropical_fish/left_fin/0", BodyPart.LEFT_FIN),
                "left_fin",
                0.0D,
                -1.0D,
                0.0D,
                2.0D,
                1.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/tropical_fish/right_fin/0", BodyPart.RIGHT_FIN),
                "right_fin",
                -2.0D,
                -1.0D,
                0.0D,
                0.0D,
                1.0D,
                0.0D),
            this.volumes.box(BodyPart.TAIL, "tail", 0.0D, -1.5D, 0.0D, 0.0D, 1.5D, 6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/tropical_fish/top_fin/0", BodyPart.FIN),
                "top_fin",
                0.0D,
                -3.0D,
                0.0D,
                0.0D,
                0.0D,
                6.0D)));
  }

  private EntityHitboxProfile<String> tropicalFishLarge() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -1.0D, -3.0D, -3.0D, 1.0D, 3.0D, 3.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/tropical_fish/bottom_fin/0", BodyPart.FIN),
                "bottom_fin",
                0.0D,
                0.0D,
                0.0D,
                0.0D,
                4.0D,
                6.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/tropical_fish/left_fin/0", BodyPart.LEFT_FIN),
                "left_fin",
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                2.0D,
                0.0D),
            this.volumes.box(
                new NamedHitboxPart(
                    "minecraft:hitbox/tropical_fish/right_fin/0", BodyPart.RIGHT_FIN),
                "right_fin",
                -2.0D,
                0.0D,
                0.0D,
                0.0D,
                2.0D,
                0.0D),
            this.volumes.box(BodyPart.TAIL, "tail", 0.0D, -3.0D, 0.0D, 0.0D, 3.0D, 5.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/tropical_fish/top_fin/0", BodyPart.FIN),
                "top_fin",
                0.0D,
                -4.0D,
                0.0D,
                0.0D,
                0.0D,
                6.0D)));
  }

  private EntityHitboxProfile<String> turtle() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -9.5D, 3.0D, -10.0D, 9.5D, 23.0D, -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/turtle/body/1", BodyPart.TORSO),
                "body",
                -5.5D,
                3.0D,
                -13.0D,
                5.5D,
                21.0D,
                -10.0D),
            this.volumes.box(BodyPart.HEAD, "head", -3.0D, -1.0D, -3.0D, 3.0D, 4.0D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", 0.0D, 0.0D, -2.0D, 13.0D, 1.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, 0.0D, 2.0D, 1.0D, 10.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -13.0D, 0.0D, -2.0D, 0.0D, 1.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, 0.0D, 2.0D, 1.0D, 10.0D)));
  }

  private EntityHitboxProfile<String> turtleGravid() {
    return new EntityHitboxProfile<>(
        List.of(
            this.volumes.box(BodyPart.TORSO, "body", -9.5D, 3.0D, -10.0D, 9.5D, 23.0D, -4.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/turtle/body/1", BodyPart.TORSO),
                "body",
                -5.5D,
                3.0D,
                -13.0D,
                5.5D,
                21.0D,
                -10.0D),
            this.volumes.box(
                new NamedHitboxPart("minecraft:hitbox/turtle/egg_belly/0", BodyPart.TORSO),
                "egg_belly",
                -4.5D,
                3.0D,
                -14.0D,
                4.5D,
                21.0D,
                -13.0D),
            this.volumes.box(BodyPart.HEAD, "head", -3.0D, -1.0D, -3.0D, 3.0D, 4.0D, 3.0D),
            this.volumes.box(
                BodyPart.FRONT_LEFT_LEG, "left_front_leg", 0.0D, 0.0D, -2.0D, 13.0D, 1.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_LEFT_LEG, "left_hind_leg", -2.0D, 0.0D, 0.0D, 2.0D, 1.0D, 10.0D),
            this.volumes.box(
                BodyPart.FRONT_RIGHT_LEG, "right_front_leg", -13.0D, 0.0D, -2.0D, 0.0D, 1.0D, 3.0D),
            this.volumes.box(
                BodyPart.HIND_RIGHT_LEG, "right_hind_leg", -2.0D, 0.0D, 0.0D, 2.0D, 1.0D, 10.0D)));
  }
}
