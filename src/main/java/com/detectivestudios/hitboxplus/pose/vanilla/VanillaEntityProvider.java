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

import com.detectivestudios.hitboxplus.api.EntityHitboxProvider;
import com.detectivestudios.hitboxplus.api.EntityModelPose;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.pose.EntityBodyGeometry;
import com.detectivestudios.hitboxplus.pose.HumanoidInputs;
import com.detectivestudios.hitboxplus.pose.LivingModelTransform;
import com.detectivestudios.hitboxplus.profile.EntityHitboxProfile;
import com.detectivestudios.hitboxplus.profile.VanillaMobProfiles;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Slime;

public class VanillaEntityProvider implements EntityHitboxProvider {

  private final LivingEntity entity;
  private final String kind;
  private final VanillaMobProfiles profiles;
  private final VanillaRigs rigs;
  private final HumanoidInputs inputs = new HumanoidInputs();
  private final VanillaModelRoot root = new VanillaModelRoot();
  private final VanillaHumanoidAnimation humanoid = new VanillaHumanoidAnimation();
  private final VanillaMonsterAnimation monsters = new VanillaMonsterAnimation();
  private final VanillaAnimalAnimation animals = new VanillaAnimalAnimation();
  private final VanillaFlyingAnimation flying = new VanillaFlyingAnimation();
  private final VanillaAquaticAnimation aquatic = new VanillaAquaticAnimation();
  private final EntityBodyGeometry geometry = new EntityBodyGeometry();
  private final SlimeDeformation deformation = new SlimeDeformation();
  private EntityHitboxProfile<String> visibleSource;
  private EntityHitboxProfile<String> visibleProfile;
  private int visibility = -1;
  private VanillaRig rig;
  private String rigVariant;

  public VanillaEntityProvider(
      LivingEntity entity, String kind, VanillaMobProfiles profiles, VanillaRigs rigs) {
    this.entity = entity;
    this.kind = kind;
    this.profiles = profiles;
    this.rigs = rigs;
  }

  @Override
  public Optional<HitboxPose> getPose(float partialTick) {
    return this.getModelPose(partialTick)
        .map(
            pose -> {
              var profile =
                  this.profiles.find("minecraft:" + this.kind, pose.variant()).orElseThrow();
              var visible = this.visibleProfile(profile);
              var body = this.geometry.create(visible, pose.bones()::get, pose.root());
              return this.entity instanceof Slime slime
                  ? this.deformation.apply(body, pose.root(), slime, partialTick)
                  : body;
            });
  }

  @Override
  public Optional<EntityModelPose> getModelPose(float partialTick) {
    String variant = this.variant();
    if (this.rig == null || !variant.equals(this.rigVariant)) {
      this.rig = this.rigs.pose(this.kind, variant);
      this.rigVariant = variant;
    } else {
      this.rig.reset();
    }
    var rig = this.rig;
    var input =
        this.inputs.sample(this.entity, new LivingModelTransform(this.entity, 1), partialTick);
    var context = new VanillaPoseContext(this.entity, this.kind, input, partialTick);
    switch (this.kind) {
      case "drowned",
          "zombie_villager",
          "piglin",
          "piglin_brute",
          "zombified_piglin",
          "enderman",
          "vex" ->
          this.humanoid.animate(rig, context);
      case "villager", "wandering_trader", "witch" -> this.monsters.villager(rig, context);
      case "evoker", "illusioner", "pillager", "vindicator" -> this.monsters.illager(rig, context);
      case "spider", "cave_spider" -> this.monsters.spider(rig, context);
      case "endermite", "silverfish" -> this.monsters.segmented(rig, context);
      case "blaze" -> this.monsters.blaze(rig, context);
      case "iron_golem", "snow_golem" -> this.monsters.golem(rig, context);
      case "wither" -> this.monsters.wither(rig, context);
      case "shulker" -> this.monsters.shulker(rig, context);
      case "magma_cube" -> this.monsters.magmaCube(rig, context);
      case "armor_stand" -> this.monsters.armorStand(rig, context);
      case "ravager" -> this.monsters.ravager(rig, context);
      case "goat" -> this.animals.goat(rig, context);
      case "hoglin", "zoglin" -> this.animals.hoglin(rig, context);
      case "polar_bear" -> this.animals.bear(rig, context);
      case "rabbit" -> this.animals.rabbit(rig, context);
      case "wolf" -> this.animals.wolf(rig, context);
      case "cat", "ocelot" -> this.animals.cat(rig, context);
      case "fox" -> this.animals.fox(rig, context);
      case "horse", "donkey", "mule", "skeleton_horse", "zombie_horse" ->
          this.animals.horse(rig, context);
      case "panda" -> this.animals.panda(rig, context);
      case "strider" -> this.animals.strider(rig, context);
      case "llama", "trader_llama" -> {
        this.animals.quadruped(rig, context);
        if (this.entity.isBaby()) {
          rig.young(0.7, 21, 3.52, 0.45454544, 33, "head");
        }
      }
      case "bat" -> this.flying.bat(rig, context);
      case "bee" -> this.flying.bee(rig, context);
      case "phantom" -> this.flying.phantom(rig, context);
      case "ghast" -> this.flying.ghast(rig, context);
      case "parrot" -> this.flying.parrot(rig, context);
      case "cod", "salmon", "tropical_fish", "pufferfish" -> this.aquatic.fish(rig, context);
      case "squid", "glow_squid" -> this.aquatic.squid(rig, context);
      case "dolphin" -> this.aquatic.dolphin(rig, context);
      case "guardian", "elder_guardian" -> this.aquatic.guardian(rig, context);
      case "turtle" -> this.aquatic.turtle(rig, context);
      case "axolotl" -> this.aquatic.axolotl(rig, context);
      case "slime" -> {}
      default -> throw new IllegalArgumentException("No vanilla animation: " + this.kind);
    }
    return Optional.of(new EntityModelPose(rig.compose(), this.root.sample(context), variant));
  }

  private String variant() {
    if (this.entity instanceof Pufferfish fish) {
      return switch (fish.getPuffState()) {
        case 1 -> "medium";
        case 2 -> "full";
        default -> "base";
      };
    }
    if (this.entity instanceof TropicalFish fish && fish.getBaseVariant() != 0) {
      return "large";
    }
    if (this.entity instanceof AbstractHorse
        && this.entity.isBaby()
        && this.profiles.find("minecraft:" + this.kind, "young").isPresent()) {
      return "young";
    }
    if (this.kind.equals("goat") && this.entity.isBaby()) {
      return "young";
    }
    if (this.entity instanceof AbstractIllager illager
        && illager.getArmPose() != AbstractIllager.IllagerArmPose.CROSSED) {
      return "separate_arms";
    }
    if (this.entity instanceof Turtle turtle && !turtle.isBaby() && turtle.hasEgg()) {
      return "gravid";
    }
    if (this.entity instanceof ArmorStand stand && stand.isShowArms()) {
      return "arms";
    }
    return "base";
  }

  private boolean visible(String bone) {
    if (this.kind.equals("vex") && bone.equals("left_leg")) {
      return false;
    }
    if (this.entity instanceof ArmorStand stand
        && (stand.isMarker() || bone.equals("base_plate") && stand.isNoBasePlate())) {
      return false;
    }
    if (this.entity instanceof Bee bee && bee.hasStung() && bone.equals("bone/body/stinger")) {
      return false;
    }
    return !(this.entity instanceof Fox fox && fox.isSleeping() && bone.endsWith("_leg"));
  }

  private EntityHitboxProfile<String> visibleProfile(EntityHitboxProfile<String> profile) {
    int flags =
        (this.entity instanceof ArmorStand stand
                ? (stand.isMarker() ? 1 : 0) | (stand.isNoBasePlate() ? 2 : 0)
                : 0)
            | (this.entity instanceof Bee bee && bee.hasStung() ? 4 : 0)
            | (this.entity instanceof Fox fox && fox.isSleeping() ? 8 : 0);
    if (this.visibleSource != profile || this.visibility != flags) {
      this.visibleSource = profile;
      this.visibility = flags;
      this.visibleProfile =
          this.kind.equals("vex") || flags != 0
              ? new EntityHitboxProfile<>(
                  profile.volumes().stream().filter(volume -> this.visible(volume.bone())).toList())
              : profile;
    }
    return this.visibleProfile;
  }
}
