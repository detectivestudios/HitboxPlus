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

package com.detectivestudios.hitboxplus.pose;

import com.detectivestudios.hitboxplus.pose.HumanoidAnimation.ArmPose;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;

public class HumanoidInputs {

  public HumanoidAnimation.Input sample(
      LivingEntity entity, LivingModelTransform transform, float partialTick) {
    var input = new HumanoidAnimation.Input();
    input.partialTick = partialTick;
    boolean sitting =
        entity.isPassenger() && entity.getVehicle() != null && entity.getVehicle().shouldRiderSit();
    input.limbAmount =
        sitting
            ? 0.0F
            : Math.min(
                1.0F, Mth.lerp(partialTick, entity.animationSpeedOld, entity.animationSpeed));
    input.limbSwing =
        sitting ? 0.0F : entity.animationPosition - entity.animationSpeed * (1.0F - partialTick);
    if (entity.isBaby()) {
      input.limbSwing *= 3.0F;
    }
    input.age = entity.tickCount + partialTick;
    input.headYaw =
        Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot)
            - transform.bodyYaw(partialTick);
    input.headPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
    if (transform.upsideDown()) {
      input.headYaw = -input.headYaw;
      input.headPitch = -input.headPitch;
    }
    input.attack = entity.getAttackAnim(partialTick);
    input.crouching = entity instanceof Player && entity.isCrouching();
    input.riding = sitting;
    input.flying = entity.getFallFlyingTicks() > 4;
    input.flightScale =
        input.flying ? (float) Math.pow(entity.getDeltaMovement().lengthSqr() / 0.2D, 3.0D) : 1.0F;
    input.swim = entity.getSwimAmount(partialTick);
    input.visuallySwimming = entity.isVisuallySwimming();
    input.rightHanded = entity.getMainArm() == HumanoidArm.RIGHT;
    input.attackRight = (entity.swingingArm != InteractionHand.OFF_HAND) == input.rightHanded;
    input.usingItem = entity.isUsingItem();
    input.useRight = (entity.getUsedItemHand() == InteractionHand.MAIN_HAND) == input.rightHanded;
    input.charge =
        Mth.clamp(
            (float) entity.getTicksUsingItem()
                / Math.max(1, CrossbowItem.getChargeDuration(entity.getUseItem())),
            0.0F,
            1.0F);
    if (entity instanceof Player) {
      ArmPose main = this.armPose(entity, InteractionHand.MAIN_HAND);
      ArmPose off = this.armPose(entity, InteractionHand.OFF_HAND);
      if (main.twoHanded) {
        off = entity.getOffhandItem().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
      }
      input.rightArm = input.rightHanded ? main : off;
      input.leftArm = input.rightHanded ? off : main;
    }
    return input;
  }

  private ArmPose armPose(LivingEntity entity, InteractionHand hand) {
    var stack = entity.getItemInHand(hand);
    if (stack.isEmpty()) {
      return ArmPose.EMPTY;
    }
    if (entity.getUsedItemHand() == hand && entity.getUseItemRemainingTicks() > 0) {
      return switch (stack.getUseAnimation()) {
        case BLOCK -> ArmPose.BLOCK;
        case BOW -> ArmPose.BOW_AND_ARROW;
        case SPEAR -> ArmPose.THROW_SPEAR;
        case CROSSBOW -> ArmPose.CROSSBOW_CHARGE;
        case SPYGLASS -> ArmPose.SPYGLASS;
        default -> ArmPose.ITEM;
      };
    }
    return !entity.swinging
            && stack.getItem() instanceof CrossbowItem
            && CrossbowItem.isCharged(stack)
        ? ArmPose.CROSSBOW_HOLD
        : ArmPose.ITEM;
  }
}
