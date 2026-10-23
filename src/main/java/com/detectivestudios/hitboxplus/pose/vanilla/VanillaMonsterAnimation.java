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

import net.minecraft.core.Rotations;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.CrossbowItem;

public class VanillaMonsterAnimation {

  public void villager(VanillaRig rig, VanillaPoseContext context) {
    context.look(rig, "head");
    float step = context.swing() * 0.6662F;
    rig.part("right_leg").pitch = Mth.cos(step) * 0.7F * context.amount();
    rig.part("left_leg").pitch = Mth.cos(step + Mth.PI) * 0.7F * context.amount();
    if (context.entity() instanceof AbstractVillager villager && villager.getUnhappyCounter() > 0) {
      rig.part("head").pitch = 0.4F;
      rig.part("head").roll = 0.3F * Mth.sin(context.age() * 0.45F);
    }
    if (context.kind().equals("witch")) {
      var nose = rig.part("head/nose");
      float phase = context.entity().tickCount * 0.01F * (context.entity().getId() % 10);
      nose.pitch = Mth.sin(phase) * 4.5F * Mth.DEG_TO_RAD;
      nose.roll = Mth.cos(phase) * 2.5F * Mth.DEG_TO_RAD;
      if (!context.entity().getMainHandItem().isEmpty()) {
        nose.set(0, 1, -1.5F);
        nose.pitch = -0.9F;
      }
    }
  }

  public void illager(VanillaRig rig, VanillaPoseContext context) {
    this.villager(rig, context);
    if (!rig.has("right_arm")) {
      return;
    }
    var right = rig.part("right_arm");
    var left = rig.part("left_arm");
    var head = rig.part("head");
    right.pitch = Mth.cos(context.swing() * 0.6662F + Mth.PI) * context.amount();
    left.pitch = Mth.cos(context.swing() * 0.6662F) * context.amount();
    if (context.input().riding) {
      right.pitch = left.pitch = -Mth.PI / 5;
      rig.part("right_leg").rotate(-1.4137167F, Mth.PI / 10, 0.07853982F);
      rig.part("left_leg").rotate(-1.4137167F, -Mth.PI / 10, -0.07853982F);
    }
    var illager = (AbstractIllager) context.entity();
    switch (illager.getArmPose()) {
      case ATTACKING -> {
        float swing = Mth.sin(context.input().attack * Mth.PI);
        float reach =
            Mth.sin((1 - (1 - context.input().attack) * (1 - context.input().attack)) * Mth.PI);
        if (illager.getMainHandItem().isEmpty()) {
          right.pitch = left.pitch = -Mth.PI / 2 - swing * 1.2F + reach * 0.4F;
          right.yaw = -(0.1F - swing * 0.6F);
          left.yaw = -right.yaw;
        } else {
          var main = context.input().rightHanded ? right : left;
          var off = context.input().rightHanded ? left : right;
          main.pitch =
              -1.8849558F + Mth.cos(context.age() * 0.09F) * 0.15F + swing * 2.2F - reach * 0.4F;
          off.pitch = Mth.cos(context.age() * 0.19F) * 0.5F + swing * 1.2F - reach * 0.4F;
          right.yaw = 0.15707964F;
          left.yaw = -0.15707964F;
        }
        right.roll = Mth.cos(context.age() * 0.09F) * 0.05F + 0.05F;
        left.roll = -right.roll;
        right.pitch += Mth.sin(context.age() * 0.067F) * 0.05F;
        left.pitch -= Mth.sin(context.age() * 0.067F) * 0.05F;
      }
      case SPELLCASTING, CELEBRATING -> {
        boolean spell = illager.getArmPose() == AbstractIllager.IllagerArmPose.SPELLCASTING;
        right.pitch = left.pitch = Mth.cos(context.age() * 0.6662F) * (spell ? 0.25F : 0.05F);
        right.roll = spell ? 2.3561945F : 2.670354F;
        left.roll = -2.3561945F;
      }
      case BOW_AND_ARROW -> {
        right.yaw = head.yaw - 0.1F;
        right.pitch = -Mth.PI / 2 + head.pitch;
        left.pitch = -0.9424779F + head.pitch;
        left.yaw = head.yaw - 0.4F;
        left.roll = Mth.PI / 2;
      }
      case CROSSBOW_HOLD -> {
        right.yaw = head.yaw - 0.3F;
        left.yaw = head.yaw + 0.6F;
        right.pitch = -Mth.PI / 2 + head.pitch + 0.1F;
        left.pitch = -1.5F + head.pitch;
      }
      case CROSSBOW_CHARGE -> {
        right.yaw = -0.8F;
        right.pitch = -0.97079635F;
        float charge =
            Mth.clamp(
                (float) illager.getTicksUsingItem()
                    / Math.max(1, CrossbowItem.getChargeDuration(illager.getUseItem())),
                0,
                1);
        left.yaw = Mth.lerp(charge, 0.4F, 0.85F);
        left.pitch = Mth.lerp(charge, right.pitch, -Mth.PI / 2);
      }
      default -> {}
    }
  }

  public void spider(VanillaRig rig, VanillaPoseContext context) {
    context.look(rig, "head");
    String[] legs = {"hind_leg", "middle_hind_leg", "middle_front_leg", "front_leg"};
    float[] yaw = {Mth.PI / 4, Mth.PI / 8, -Mth.PI / 8, -Mth.PI / 4};
    float[] phases = {0, Mth.PI, Mth.PI / 2, Mth.PI * 1.5F};
    float phase = context.swing() * 0.6662F;
    for (int index = 0; index < legs.length; index++) {
      float rotation = -Mth.cos(phase * 2 + phases[index]) * 0.4F * context.amount();
      float lift = Math.abs(Mth.sin(phase + phases[index]) * 0.4F) * context.amount();
      float spread = index == 0 || index == 3 ? Mth.PI / 4 : 0.58119464F;
      rig.part("right_" + legs[index]).rotate(0, yaw[index] + rotation, -spread + lift);
      rig.part("left_" + legs[index]).rotate(0, -yaw[index] - rotation, spread - lift);
    }
  }

  public void segmented(VanillaRig rig, VanillaPoseContext context) {
    boolean silverfish = context.kind().equals("silverfish");
    for (int index = 0; index < (silverfish ? 7 : 4); index++) {
      float phase = context.age() * 0.9F + index * 0.15F * Mth.PI;
      rig.part("segment" + index).yaw =
          Mth.cos(phase) * Mth.PI * (silverfish ? 0.05F : 0.01F) * (1 + Math.abs(index - 2));
      rig.part("segment" + index).x =
          Mth.sin(phase) * Mth.PI * (silverfish ? 0.2F : 0.1F) * Math.abs(index - 2);
    }
    if (silverfish) {
      int[] segments = {2, 4, 1};
      for (int index = 0; index < segments.length; index++) {
        var segment = rig.part("segment" + segments[index]);
        rig.part("layer" + index).yaw = segment.yaw;
        if (index > 0) {
          rig.part("layer" + index).x = segment.x;
        }
      }
    }
  }

  public void blaze(VanillaRig rig, VanillaPoseContext context) {
    context.look(rig, "head");
    for (int group = 0; group < 3; group++) {
      float radius = group == 0 ? 9 : group == 1 ? 7 : 5;
      float phase =
          group == 0
              ? -context.age() * Mth.PI * 0.1F
              : group == 1
                  ? Mth.PI / 4 + context.age() * Mth.PI * 0.03F
                  : 0.47123894F - context.age() * Mth.PI * 0.05F;
      for (int index = 0; index < 4; index++) {
        int ordinal = group * 4 + index;
        float vertical =
            group == 0
                ? -2 + Mth.cos((ordinal * 2 + context.age()) * 0.25F)
                : group == 1
                    ? 2 + Mth.cos((ordinal * 2 + context.age()) * 0.25F)
                    : 11 + Mth.cos((ordinal * 1.5F + context.age()) * 0.5F);
        rig.part("part" + ordinal)
            .set(Mth.cos(phase + index) * radius, vertical, Mth.sin(phase + index) * radius);
      }
    }
  }

  public void golem(VanillaRig rig, VanillaPoseContext context) {
    context.look(rig, "head");
    if (context.entity() instanceof IronGolem golem) {
      float walk = Mth.triangleWave(context.swing(), 13);
      rig.part("right_leg").pitch = -1.5F * walk * context.amount();
      rig.part("left_leg").pitch = -rig.part("right_leg").pitch;
      if (golem.getAttackAnimationTick() > 0) {
        rig.part("right_arm").pitch =
            rig.part("left_arm").pitch =
                -2
                    + 1.5F
                        * Mth.triangleWave(
                            golem.getAttackAnimationTick() - context.partialTick(), 10);
      } else if (golem.getOfferFlowerTick() > 0) {
        rig.part("right_arm").pitch =
            -0.8F + 0.025F * Mth.triangleWave(golem.getOfferFlowerTick(), 70);
      } else {
        rig.part("right_arm").pitch = (-0.2F + 1.5F * walk) * context.amount();
        rig.part("left_arm").pitch = (-0.2F - 1.5F * walk) * context.amount();
      }
    } else {
      float yaw = rig.part("head").yaw * 0.25F;
      rig.part("upper_body").yaw = yaw;
      rig.part("left_arm").yaw = yaw;
      rig.part("right_arm").yaw = yaw + Mth.PI;
      rig.part("left_arm").x = Mth.cos(yaw) * 5;
      rig.part("left_arm").z = -Mth.sin(yaw) * 5;
      rig.part("right_arm").x = -Mth.cos(yaw) * 5;
      rig.part("right_arm").z = Mth.sin(yaw) * 5;
    }
  }

  public void wither(VanillaRig rig, VanillaPoseContext context) {
    var wither = (WitherBoss) context.entity();
    context.look(rig, "center_head");
    float wave = Mth.cos(context.age() * 0.1F);
    rig.part("ribcage").pitch = (0.065F + 0.05F * wave) * Mth.PI;
    rig.part("tail")
        .set(
            -2,
            6.9F + Mth.cos(rig.part("ribcage").pitch) * 10,
            -0.5F + Mth.sin(rig.part("ribcage").pitch) * 10);
    rig.part("tail").pitch = (0.265F + 0.1F * wave) * Mth.PI;
    for (int index = 0; index < 2; index++) {
      rig.part(index == 0 ? "right_head" : "left_head")
          .rotate(
              wither.getHeadXRot(index) * Mth.DEG_TO_RAD,
              (wither.getHeadYRot(index) - wither.yBodyRot) * Mth.DEG_TO_RAD,
              0);
    }
  }

  public void shulker(VanillaRig rig, VanillaPoseContext context) {
    var shulker = (Shulker) context.entity();
    float peek = shulker.getClientPeekAmount(context.partialTick());
    float angle = (0.5F + peek) * Mth.PI;
    float opening = -1 + Mth.sin(angle);
    rig.part("lid").y =
        16 + Mth.sin(angle) * 8 + (angle > Mth.PI ? Mth.sin(context.age() * 0.1F) * 0.7F : 0);
    rig.part("lid").yaw = peek > 0.3F ? opening * opening * opening * opening * Mth.PI * 0.125F : 0;
    rig.part("head").pitch = context.input().headPitch * Mth.DEG_TO_RAD;
    rig.part("head").yaw = (shulker.yHeadRot - 180 - shulker.yBodyRot) * Mth.DEG_TO_RAD;
  }

  public void magmaCube(VanillaRig rig, VanillaPoseContext context) {
    var slime = (Slime) context.entity();
    float squish = Math.max(0, Mth.lerp(context.partialTick(), slime.oSquish, slime.squish));
    for (int index = 0; index < 8; index++) {
      rig.part("cube" + index).y = -(4 - index) * squish * 1.7F;
    }
  }

  public void armorStand(VanillaRig rig, VanillaPoseContext context) {
    var stand = (ArmorStand) context.entity();
    this.rotation(rig, "head", stand.getHeadPose());
    this.rotation(rig, "body", stand.getBodyPose());
    this.rotation(rig, "left_leg", stand.getLeftLegPose());
    this.rotation(rig, "right_leg", stand.getRightLegPose());
    if (rig.has("left_arm")) {
      this.rotation(rig, "left_arm", stand.getLeftArmPose());
      this.rotation(rig, "right_arm", stand.getRightArmPose());
    }
    for (String name : new String[] {"left_body_stick", "right_body_stick", "shoulder_stick"}) {
      this.rotation(rig, name, stand.getBodyPose());
    }
    rig.part("base_plate").yaw =
        -Mth.rotLerp(context.partialTick(), stand.yRotO, stand.getYRot()) * Mth.DEG_TO_RAD;
    if (stand.isSmall()) {
      rig.young(0.75, 16, 0, 0.5, 24, "head");
    }
  }

  private void rotation(VanillaRig rig, String name, Rotations rotation) {
    rig.part(name)
        .rotate(
            rotation.getX() * Mth.DEG_TO_RAD,
            rotation.getY() * Mth.DEG_TO_RAD,
            rotation.getZ() * Mth.DEG_TO_RAD);
  }

  public void ravager(VanillaRig rig, VanillaPoseContext context) {
    var ravager = (Ravager) context.entity();
    context.look(rig, "neck/head");
    context.walk(rig, 0.6662F, 0.4F);
    var neck = rig.part("neck");
    var mouth = rig.part("neck/head/mouth");
    int attack = ravager.getAttackTick();
    if (attack > 0) {
      float extension = (1 + Mth.triangleWave(attack - context.partialTick(), 10)) * 0.5F;
      extension = extension * extension * extension * 12;
      neck.z = -6.5F + extension;
      mouth.pitch =
          attack > 5
              ? Mth.sin((attack - 4 - context.partialTick()) / 4) * Mth.PI * 0.4F
              : 0.15707964F * Mth.sin(Mth.PI * (attack - context.partialTick()) / 10);
    } else {
      boolean stunned = ravager.getStunnedTick() > 0;
      neck.pitch = stunned ? 0.21991149F : 0;
      mouth.pitch = Mth.PI * (stunned ? 0.05F : 0.01F);
      if (stunned) {
        neck.x = Mth.sin(ravager.getStunnedTick() * 0.25F) * 3;
      } else if (ravager.getRoarTick() > 0) {
        mouth.pitch =
            Mth.PI
                / 2
                * Mth.sin(
                    (20 - ravager.getRoarTick() - context.partialTick()) / 20 * Mth.PI * 0.25F);
      }
    }
  }
}
