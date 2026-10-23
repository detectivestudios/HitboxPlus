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

import com.detectivestudios.hitboxplus.api.HumanoidPoseAnimation;

public class HumanoidAnimation implements HumanoidPoseAnimation {

  @Override
  public HumanoidPose evaluate(Input input) {
    var pose = new HumanoidPose();
    pose.crouching = input.crouching;
    pose.head.yRot = input.headYaw * PoseMath.DEG_TO_RAD;
    pose.head.xRot = input.flying ? -PoseMath.PI / 4.0F : input.headPitch * PoseMath.DEG_TO_RAD;
    if (!input.flying && input.swim > 0.0F && input.visuallySwimming) {
      pose.head.xRot = this.angle(input.swim, pose.head.xRot, -PoseMath.PI / 4.0F);
    }
    float phase = input.limbSwing * 0.6662F;
    float amount = input.limbAmount / Math.max(1.0F, input.flightScale);
    pose.rightArm.xRot = PoseMath.cos(phase + PoseMath.PI) * amount;
    pose.leftArm.xRot = PoseMath.cos(phase) * amount;
    pose.rightLeg.xRot = PoseMath.cos(phase) * 1.4F * amount;
    pose.leftLeg.xRot = PoseMath.cos(phase + PoseMath.PI) * 1.4F * amount;
    if (input.riding) {
      pose.rightArm.xRot -= PoseMath.PI / 5.0F;
      pose.leftArm.xRot -= PoseMath.PI / 5.0F;
      pose.rightLeg.xRot = -1.4137167F;
      pose.leftLeg.xRot = -1.4137167F;
      pose.rightLeg.yRot = PoseMath.PI / 10.0F;
      pose.leftLeg.yRot = -PoseMath.PI / 10.0F;
      pose.rightLeg.zRot = 0.07853982F;
      pose.leftLeg.zRot = -0.07853982F;
    }
    if (input.usingItem) {
      this.arm(pose, input, input.useRight);
    } else {
      boolean offhandTwoHanded = (input.rightHanded ? input.leftArm : input.rightArm).twoHanded;
      boolean leftFirst = input.rightHanded != offhandTwoHanded;
      this.arm(pose, input, !leftFirst);
      this.arm(pose, input, leftFirst);
    }
    this.attack(pose, input);
    if (input.crouching) {
      pose.body.xRot = 0.5F;
      pose.body.y = 3.2F;
      pose.head.y = 4.2F;
      pose.rightArm.xRot += 0.4F;
      pose.leftArm.xRot += 0.4F;
      pose.rightArm.y = 5.2F;
      pose.leftArm.y = 5.2F;
      pose.rightLeg.y = 12.2F;
      pose.leftLeg.y = 12.2F;
      pose.rightLeg.z = 4.0F;
      pose.leftLeg.z = 4.0F;
    }
    if (input.rightArm != ArmPose.SPYGLASS) {
      this.bob(pose.rightArm, input.age, 1.0F);
    }
    if (input.leftArm != ArmPose.SPYGLASS) {
      this.bob(pose.leftArm, input.age, -1.0F);
    }
    if (input.swim > 0.0F) {
      this.swim(pose, input);
    }
    return pose;
  }

  private void arm(HumanoidPose pose, Input input, boolean right) {
    var arm = right ? pose.rightArm : pose.leftArm;
    var other = right ? pose.leftArm : pose.rightArm;
    float side = right ? 1.0F : -1.0F;
    switch (right ? input.rightArm : input.leftArm) {
      case EMPTY -> arm.yRot = 0.0F;
      case ITEM -> arm.xRot = arm.xRot * 0.5F - PoseMath.PI / 10.0F;
      case BLOCK -> {
        arm.xRot = arm.xRot * 0.5F - 0.9424779F;
        arm.yRot = -side * PoseMath.PI / 6.0F;
      }
      case THROW_SPEAR -> arm.xRot = arm.xRot * 0.5F - PoseMath.PI;
      case BOW_AND_ARROW -> {
        pose.rightArm.yRot = pose.head.yRot - 0.1F - (right ? 0.0F : 0.4F);
        pose.leftArm.yRot = pose.head.yRot + 0.1F + (right ? 0.4F : 0.0F);
        pose.rightArm.xRot = -PoseMath.HALF_PI + pose.head.xRot;
        pose.leftArm.xRot = -PoseMath.HALF_PI + pose.head.xRot;
      }
      case CROSSBOW_HOLD -> {
        arm.yRot = -side * 0.3F + pose.head.yRot;
        other.yRot = side * 0.6F + pose.head.yRot;
        arm.xRot = -PoseMath.HALF_PI + pose.head.xRot + 0.1F;
        other.xRot = -1.5F + pose.head.xRot;
      }
      case CROSSBOW_CHARGE -> {
        arm.yRot = -side * 0.8F;
        arm.xRot = -0.97079635F;
        other.yRot = side * PoseMath.lerp(input.charge, 0.4F, 0.85F);
        other.xRot = PoseMath.lerp(input.charge, arm.xRot, -PoseMath.HALF_PI);
      }
      case SPYGLASS -> {
        arm.xRot =
            PoseMath.clamp(
                pose.head.xRot - 1.9198622F - (input.crouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
        arm.yRot = pose.head.yRot - side * 0.2617994F;
      }
    }
  }

  private void attack(HumanoidPose pose, Input input) {
    if (input.attack <= 0.0F) {
      return;
    }
    var arm = input.attackRight ? pose.rightArm : pose.leftArm;
    pose.body.yRot =
        PoseMath.sin((float) Math.sqrt(input.attack) * PoseMath.PI * 2.0F)
            * 0.2F
            * (input.attackRight ? 1.0F : -1.0F);
    pose.rightArm.z = PoseMath.sin(pose.body.yRot) * 5.0F;
    pose.rightArm.x = -PoseMath.cos(pose.body.yRot) * 5.0F;
    pose.leftArm.z = -PoseMath.sin(pose.body.yRot) * 5.0F;
    pose.leftArm.x = PoseMath.cos(pose.body.yRot) * 5.0F;
    pose.rightArm.yRot += pose.body.yRot;
    pose.leftArm.yRot += pose.body.yRot;
    // Vanilla HumanoidModel also adds body yaw to the left arm's pitch during attacks.
    //noinspection SuspiciousNameCombination
    pose.leftArm.xRot += pose.body.yRot;
    float inverse = 1.0F - input.attack;
    float reach = PoseMath.sin((1.0F - inverse * inverse * inverse * inverse) * PoseMath.PI);
    float swing = PoseMath.sin(input.attack * PoseMath.PI);
    arm.xRot -= reach * 1.2F + swing * -(pose.head.xRot - 0.7F) * 0.75F;
    arm.yRot += pose.body.yRot * 2.0F;
    arm.zRot -= swing * 0.4F;
  }

  private void bob(HumanoidPose.Part arm, float age, float side) {
    arm.zRot += side * (PoseMath.cos(age * 0.09F) * 0.05F + 0.05F);
    arm.xRot += side * PoseMath.sin(age * 0.067F) * 0.05F;
  }

  private void swim(HumanoidPose pose, Input input) {
    float phase = input.limbSwing % 26.0F;
    if (!input.usingItem) {
      float pitch;
      float leftRoll;
      float rightRoll;
      if (phase < 14.0F) {
        float weight = (-65.0F * phase + phase * phase) / (-65.0F * 14.0F + 196.0F);
        pitch = 0.0F;
        leftRoll = PoseMath.PI + 1.8707964F * weight;
        rightRoll = PoseMath.PI - 1.8707964F * weight;
      } else if (phase < 22.0F) {
        float weight = (phase - 14.0F) / 8.0F;
        pitch = PoseMath.HALF_PI * weight;
        leftRoll = 5.012389F - 1.8707964F * weight;
        rightRoll = 1.2707963F + 1.8707964F * weight;
      } else {
        pitch = PoseMath.HALF_PI * (1.0F - (phase - 22.0F) / 4.0F);
        leftRoll = PoseMath.PI;
        rightRoll = PoseMath.PI;
      }
      float left = input.attack > 0.0F && !input.attackRight ? 0.0F : input.swim;
      float right = input.attack > 0.0F && input.attackRight ? 0.0F : input.swim;
      pose.leftArm.xRot = this.angle(left, pose.leftArm.xRot, pitch);
      pose.leftArm.yRot = this.angle(left, pose.leftArm.yRot, PoseMath.PI);
      pose.leftArm.zRot = this.angle(left, pose.leftArm.zRot, leftRoll);
      pose.rightArm.xRot = PoseMath.lerp(right, pose.rightArm.xRot, pitch);
      pose.rightArm.yRot = PoseMath.lerp(right, pose.rightArm.yRot, PoseMath.PI);
      pose.rightArm.zRot = PoseMath.lerp(right, pose.rightArm.zRot, rightRoll);
    }
    pose.leftLeg.xRot =
        PoseMath.lerp(
            input.swim,
            pose.leftLeg.xRot,
            0.3F * PoseMath.cos(input.limbSwing / 3.0F + PoseMath.PI));
    pose.rightLeg.xRot =
        PoseMath.lerp(input.swim, pose.rightLeg.xRot, 0.3F * PoseMath.cos(input.limbSwing / 3.0F));
  }

  private float angle(float fraction, float start, float end) {
    float delta = (end - start) % (PoseMath.PI * 2.0F);
    if (delta < -PoseMath.PI) {
      delta += PoseMath.PI * 2.0F;
    }
    if (delta >= PoseMath.PI) {
      delta -= PoseMath.PI * 2.0F;
    }
    return start + fraction * delta;
  }

  public enum ArmPose {

    EMPTY(false),
    ITEM(false),
    BLOCK(false),
    THROW_SPEAR(false),
    BOW_AND_ARROW(true),
    CROSSBOW_CHARGE(true),
    CROSSBOW_HOLD(true),
    SPYGLASS(false);

    public final boolean twoHanded;

    private ArmPose(boolean twoHanded) {
      this.twoHanded = twoHanded;
    }
  }

  public static class Input {

    public float partialTick;
    public float limbSwing;
    public float limbAmount;
    public float age;
    public float headYaw;
    public float headPitch;
    public float attack;
    public float swim;
    public float charge;
    public float flightScale = 1.0F;
    public boolean crouching;
    public boolean riding;
    public boolean flying;
    public boolean visuallySwimming;
    public boolean rightHanded = true;
    public boolean attackRight = true;
    public boolean usingItem;
    public boolean useRight = true;
    public ArmPose rightArm = ArmPose.EMPTY;
    public ArmPose leftArm = ArmPose.EMPTY;
  }
}
