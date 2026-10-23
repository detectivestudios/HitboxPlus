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

import com.detectivestudios.hitboxplus.pose.HumanoidAnimation;
import com.detectivestudios.hitboxplus.pose.HumanoidPose;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.Items;

public class VanillaHumanoidAnimation {

  private final HumanoidAnimation animation = new HumanoidAnimation();

  public void animate(VanillaRig rig, VanillaPoseContext context) {
    var input = context.input();
    var entity = context.entity();
    if (entity instanceof AbstractPiglin piglin) {
      switch (piglin.getArmPose()) {
        case CROSSBOW_HOLD -> input.rightArm = HumanoidAnimation.ArmPose.CROSSBOW_HOLD;
        case CROSSBOW_CHARGE -> input.rightArm = HumanoidAnimation.ArmPose.CROSSBOW_CHARGE;
        default -> {}
      }
      if (!input.rightHanded) {
        input.leftArm = input.rightArm;
        input.rightArm = HumanoidAnimation.ArmPose.EMPTY;
      }
    }
    var pose = this.animation.evaluate(input);
    if (entity instanceof Zombie zombie) {
      this.zombieArms(pose, context, zombie.isAggressive());
      if (context.kind().equals("zombie_villager")) {
        pose.rightLeg.x = -2;
        pose.leftLeg.x = 2;
      }
      if (context.kind().equals("drowned")) {
        this.drowned(pose, context, zombie.isAggressive());
      }
    } else if (entity instanceof EnderMan enderman) {
      pose.body.y = -14;
      pose.body.z = 0;
      pose.body.xRot = 0;
      pose.head.y = enderman.isCreepy() ? -18 : -13;
      pose.head.z = 0;
      for (var part :
          new HumanoidPose.Part[] {pose.rightArm, pose.leftArm, pose.rightLeg, pose.leftLeg}) {
        part.xRot = Mth.clamp(part.xRot * 0.5F, -0.4F, 0.4F);
      }
      pose.rightArm.x = -5;
      pose.leftArm.x = 5;
      pose.rightArm.y = pose.leftArm.y = -12;
      pose.rightLeg.y = pose.leftLeg.y = -5;
      pose.rightLeg.x = -2;
      pose.leftLeg.x = 2;
      pose.rightLeg.z = pose.leftLeg.z = 0;
      if (enderman.getCarriedBlock() != null) {
        pose.rightArm.xRot = pose.leftArm.xRot = -0.5F;
        pose.rightArm.zRot = 0.05F;
        pose.leftArm.zRot = -0.05F;
      }
    } else if (entity instanceof Vex vex) {
      if (vex.isCharging()) {
        if (vex.getMainHandItem().isEmpty()) {
          pose.rightArm.xRot = pose.leftArm.xRot = Mth.PI * 1.5F;
        } else if (input.rightHanded) {
          pose.rightArm.xRot = 3.7699115F;
        } else {
          pose.leftArm.xRot = 3.7699115F;
        }
      }
      pose.rightLeg.xRot += Mth.PI / 5;
      float wing = 0.47123894F + Mth.cos(context.age() * 0.8F) * Mth.PI * 0.05F;
      rig.part("right_wing").set(0, 1, 2);
      rig.part("left_wing").set(0, 1, 2);
      rig.part("right_wing").rotate(0.47123894F, wing, 0.47123894F);
      rig.part("left_wing").rotate(0.47123894F, -wing, -0.47123894F);
    }
    if (entity instanceof AbstractPiglin piglin) {
      this.piglin(pose, rig, context, piglin);
    }
    this.copy(rig, "head", pose.head);
    this.copy(rig, "body", pose.body);
    this.copy(rig, "right_arm", pose.rightArm);
    this.copy(rig, "left_arm", pose.leftArm);
    this.copy(rig, "right_leg", pose.rightLeg);
    this.copy(rig, "left_leg", pose.leftLeg);
    for (String[] pair :
        new String[][] {
          {"jacket", "body"},
          {"left_pants", "left_leg"},
          {"right_pants", "right_leg"},
          {"left_sleeve", "left_arm"},
          {"right_sleeve", "right_arm"}
        }) {
      if (rig.has(pair[0])) {
        rig.part(pair[0]).copy(rig.part(pair[1]));
      }
    }
    if (entity.isBaby()) {
      rig.young(0.75, 16, 0, 0.5, 24, "head");
    }
  }

  private void zombieArms(HumanoidPose pose, VanillaPoseContext context, boolean aggressive) {
    float attack = context.input().attack;
    float swing = Mth.sin(attack * Mth.PI);
    float reach = Mth.sin((1 - (1 - attack) * (1 - attack)) * Mth.PI);
    float pitch = -Mth.PI / (aggressive ? 1.5F : 2.25F) + swing * 1.2F - reach * 0.4F;
    pose.rightArm.yRot = -(0.1F - swing * 0.6F);
    pose.leftArm.yRot = 0.1F - swing * 0.6F;
    pose.rightArm.zRot = Mth.cos(context.age() * 0.09F) * 0.05F + 0.05F;
    pose.leftArm.zRot = -pose.rightArm.zRot;
    float bob = Mth.sin(context.age() * 0.067F) * 0.05F;
    pose.rightArm.xRot = pitch + bob;
    pose.leftArm.xRot = pitch - bob;
  }

  private void drowned(HumanoidPose pose, VanillaPoseContext context, boolean aggressive) {
    if (aggressive && context.entity().getMainHandItem().is(Items.TRIDENT)) {
      var arm = context.input().rightHanded ? pose.rightArm : pose.leftArm;
      arm.xRot = arm.xRot * 0.5F - Mth.PI;
      arm.yRot = 0;
    }
    float swim = context.input().swim;
    if (swim > 0) {
      float wave = Mth.sin(context.age() * 0.1F) * swim;
      pose.rightArm.xRot = this.lerpAngle(swim, pose.rightArm.xRot, -2.5132742F) + wave * 0.35F;
      pose.leftArm.xRot = this.lerpAngle(swim, pose.leftArm.xRot, -2.5132742F) - wave * 0.35F;
      pose.rightArm.zRot = this.lerpAngle(swim, pose.rightArm.zRot, -0.15F);
      pose.leftArm.zRot = this.lerpAngle(swim, pose.leftArm.zRot, 0.15F);
      pose.rightLeg.xRot += wave * 0.55F;
      pose.leftLeg.xRot -= wave * 0.55F;
      pose.head.xRot = 0;
    }
  }

  private void piglin(
      HumanoidPose pose, VanillaRig rig, VanillaPoseContext context, AbstractPiglin piglin) {
    float phase = context.age() * 0.1F + context.swing() * 0.5F;
    float amplitude = 0.08F + context.amount() * 0.4F;
    rig.part("head/left_ear").roll = -Mth.PI / 6 - Mth.cos(phase * 1.2F) * amplitude;
    rig.part("head/right_ear").roll = Mth.PI / 6 + Mth.cos(phase) * amplitude;
    switch (piglin.getArmPose()) {
      case ATTACKING_WITH_MELEE_WEAPON -> {
        var arm = context.input().rightHanded ? pose.rightArm : pose.leftArm;
        if (context.input().attack == 0) {
          arm.xRot = -1.8F;
        }
      }
      case ADMIRING_ITEM -> {
        pose.head.xRot = 0.5F;
        pose.head.yRot = 0;
        var arm = context.input().rightHanded ? pose.leftArm : pose.rightArm;
        arm.yRot = context.input().rightHanded ? 0.5F : -0.5F;
        arm.xRot = -0.9F;
      }
      case DANCING -> {
        float age = context.age() / 60;
        rig.part("head/right_ear").roll = Mth.PI / 6 + Mth.sin(age * 30) * 10 * Mth.DEG_TO_RAD;
        rig.part("head/left_ear").roll = -Mth.PI / 6 - Mth.cos(age * 30) * 10 * Mth.DEG_TO_RAD;
        pose.head.x = Mth.sin(age * 10);
        pose.head.y = Mth.sin(age * 40) + 0.4F;
        pose.rightArm.zRot = (70 + Mth.cos(age * 40) * 10) * Mth.DEG_TO_RAD;
        pose.leftArm.zRot = -pose.rightArm.zRot;
        pose.rightArm.y = pose.leftArm.y = Mth.sin(age * 40) * 0.5F + 1.5F;
        pose.body.y = Mth.sin(age * 40) * 0.35F;
      }
      default -> {}
    }
  }

  private float lerpAngle(float amount, float from, float to) {
    return from + amount * Mth.wrapDegrees((to - from) * Mth.RAD_TO_DEG) * Mth.DEG_TO_RAD;
  }

  private void copy(VanillaRig rig, String name, HumanoidPose.Part part) {
    rig.part(name).set(part.x, part.y, part.z);
    rig.part(name).rotate(part.xRot, part.yRot, part.zRot);
  }
}
