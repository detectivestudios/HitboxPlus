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
import com.detectivestudios.hitboxplus.math.Vector3;
import java.util.Objects;
import java.util.function.Function;

public class PronePose implements HumanoidPoseAnimation {

  private final HumanoidPoseAnimation baseAnimation;
  private final Function<HumanoidAnimation.Input, State> stateSource;

  public PronePose() {
    this(new HumanoidAnimation(), input -> State.NONE);
  }

  public PronePose(
      HumanoidPoseAnimation baseAnimation, Function<HumanoidAnimation.Input, State> stateSource) {
    this.baseAnimation = Objects.requireNonNull(baseAnimation, "Base animation is required");
    this.stateSource = Objects.requireNonNull(stateSource, "Prone state source is required");
  }

  @Override
  public HumanoidPose evaluate(HumanoidAnimation.Input input) {
    var state = Objects.requireNonNull(this.stateSource.apply(input), "Prone state is required");
    var pose =
        Objects.requireNonNull(
            this.baseAnimation.evaluate(input), "Base animation must return a pose");
    if (state.progress() > 0.0F) {
      this.apply(
          pose,
          state.holdsGun(),
          state.handcuffed(),
          input.rightHanded,
          input.limbSwing,
          input.limbAmount,
          input.age,
          input.headYaw,
          input.headPitch,
          state.progress(),
          state.aim());
    }
    return pose;
  }

  /** Prone progress and aim are finite fractions in [0, 1]. */
  public record State(float progress, float aim, boolean holdsGun, boolean handcuffed) {

    public static final State NONE = new State(0, 0, false, false);

    public State {
      if (!Float.isFinite(progress)
          || progress < 0
          || progress > 1
          || !Float.isFinite(aim)
          || aim < 0
          || aim > 1) {
        throw new IllegalArgumentException(
            "Prone progress and aim must be finite fractions in [0, 1]");
      }
    }
  }

  public void apply(
      HumanoidPose model,
      boolean holdsGun,
      boolean handcuffed,
      boolean rightHanded,
      float limbSwing,
      float limbSwingAmount,
      float ageInTicks,
      float netHeadYaw,
      float headPitch,
      float progress,
      float aim) {
    float weight = this.smooth(progress);
    float left = PoseMath.cos(limbSwing * 0.6662F);
    float right = PoseMath.cos(limbSwing * 0.6662F + PoseMath.PI);
    float crawlSway = PoseMath.sin(limbSwing * 0.6662F) * limbSwingAmount;
    float floor = 22.0F - Math.abs(left) * limbSwingAmount * 0.4F;
    float breath = PoseMath.sin(ageInTicks * 0.08F);
    float sway = PoseMath.cos(ageInTicks * 0.05F);
    float legShift = PoseMath.sin(ageInTicks * 0.04F);
    float pitch = headPitch * PoseMath.DEG_TO_RAD;
    float yaw = netHeadYaw * PoseMath.DEG_TO_RAD;
    float aimWeight = holdsGun ? this.smooth(aim) : 0.0F;
    var standingBody = model.body.storePose();
    var proneBody =
        HumanoidPose.Part.offsetAndRotation(
            crawlSway * 0.6F,
            floor + breath * 0.12F,
            -6.0F,
            PoseMath.HALF_PI + breath * 0.015F,
            left * 0.08F * limbSwingAmount + sway * 0.012F,
            crawlSway * 0.1F);
    this.blend(
        model.body,
        weight,
        proneBody.x,
        proneBody.y,
        proneBody.z,
        proneBody.xRot,
        proneBody.yRot,
        proneBody.zRot);
    this.blend(
        model.head,
        weight,
        crawlSway * 0.4F - 0.2F * aimWeight,
        floor - 2.0F + 0.35F * aimWeight + breath * 0.15F,
        -6.0F,
        pitch + 0.06F * aimWeight + breath * 0.02F,
        yaw - 0.03F * aimWeight + sway * 0.015F,
        -crawlSway * 0.05F - 0.1F * aimWeight + breath * 0.01F);
    this.blend(
        model.rightLeg,
        weight,
        -2.0F - Math.max(0.0F, right) * 0.6F * limbSwingAmount,
        floor,
        6.0F + right * 6.0F * limbSwingAmount,
        PoseMath.HALF_PI + right * 0.15F * limbSwingAmount + breath * 0.015F,
        -0.05F + right * 0.15F * limbSwingAmount + legShift * 0.008F,
        -0.05F + right * 0.1F * limbSwingAmount);
    this.blend(
        model.leftLeg,
        weight,
        2.0F + Math.max(0.0F, left) * 0.6F * limbSwingAmount,
        floor,
        6.0F + left * 6.0F * limbSwingAmount,
        PoseMath.HALF_PI + left * 0.15F * limbSwingAmount - breath * 0.015F,
        0.05F + left * 0.15F * limbSwingAmount - legShift * 0.008F,
        0.05F + left * 0.1F * limbSwingAmount);
    if (holdsGun) {
      float crawl = this.smooth(PoseMath.clamp(limbSwingAmount * 2.5F, 0.0F, 1.0F));
      float direction = rightHanded ? 1.0F : -1.0F;
      var gunArm = rightHanded ? model.rightArm : model.leftArm;
      var supportArm = rightHanded ? model.leftArm : model.rightArm;
      float gunPhase = rightHanded ? right : left;
      float supportPhase = rightHanded ? left : right;
      this.blend(
          gunArm,
          weight,
          -direction * PoseMath.lerp(crawl, PoseMath.lerp(aimWeight, 5.5F, 4.5F), 5.0F),
          floor + (1.0F - crawl) * breath * 0.08F,
          PoseMath.lerp(
              crawl,
              -5.0F + (1.0F - aimWeight) * gunPhase * 0.8F * limbSwingAmount,
              -4.5F + gunPhase * 5.5F * limbSwingAmount),
          -PoseMath.HALF_PI + pitch,
          yaw - direction * 0.1F,
          0.0F);
      this.blend(
          supportArm,
          weight,
          direction * PoseMath.lerp(aimWeight, 5.3F, 4.3F),
          floor + (1.0F - crawl) * (0.5F * aimWeight + breath * 0.08F),
          PoseMath.lerp(
              crawl,
              PoseMath.lerp(aimWeight, -5.0F, -5.2F),
              -5.0F + supportPhase * 2.5F * limbSwingAmount),
          -PoseMath.HALF_PI + pitch,
          yaw + direction * 0.5F,
          0.0F);
    } else if (handcuffed) {
      this.blend(
          model.rightArm,
          weight,
          -5.0F,
          floor,
          -5.0F,
          model.rightArm.xRot,
          model.rightArm.yRot,
          model.rightArm.zRot);
      this.blend(
          model.leftArm,
          weight,
          5.0F,
          floor,
          -5.0F,
          model.leftArm.xRot,
          model.leftArm.yRot,
          model.leftArm.zRot);
    } else {
      this.blend(
          model.rightArm,
          weight,
          -5.0F,
          floor + breath * 0.12F,
          -5.0F + left * 3.0F * limbSwingAmount,
          -PoseMath.HALF_PI + left * 0.15F * limbSwingAmount + breath * 0.03F,
          left * 0.1F * limbSwingAmount + sway * 0.02F,
          0.0F);
      this.blend(
          model.leftArm,
          weight,
          5.0F,
          floor + breath * 0.12F,
          -5.0F + right * 3.0F * limbSwingAmount,
          -PoseMath.HALF_PI + right * 0.15F * limbSwingAmount + breath * 0.03F,
          right * 0.1F * limbSwingAmount + sway * 0.02F,
          0.0F);
    }
    if (weight > 0.0F && weight < 1.0F) {
      var blendedBody = model.body.storePose();
      this.correctJoint(model.rightLeg, 0.0F, 12.0F, standingBody, proneBody, blendedBody, weight);
      this.correctJoint(model.leftLeg, 0.0F, 12.0F, standingBody, proneBody, blendedBody, weight);
      this.correctJoint(model.rightArm, -5.0F, 2.0F, standingBody, proneBody, blendedBody, weight);
      this.correctJoint(model.leftArm, 5.0F, 2.0F, standingBody, proneBody, blendedBody, weight);
    }
  }

  private void correctJoint(
      HumanoidPose.Part part,
      float x,
      float y,
      HumanoidPose.Part standing,
      HumanoidPose.Part prone,
      HumanoidPose.Part blended,
      float weight) {
    var start = this.transformJoint(standing, x, y);
    var end = this.transformJoint(prone, x, y);
    var joint = this.transformJoint(blended, x, y);
    // Joint anchors follow the rotation arc instead of a straight line between the poses.
    var correction = joint.subtract(start.lerp(end, weight));
    part.x += (float) correction.x();
    part.y += (float) correction.y();
    part.z += (float) correction.z();
  }

  private Vector3 transformJoint(HumanoidPose.Part pose, float x, float y) {
    float rotatedY = y * PoseMath.cos(pose.xRot);
    float rotatedZ = y * PoseMath.sin(pose.xRot);
    float rotatedX = x * PoseMath.cos(pose.yRot) + rotatedZ * PoseMath.sin(pose.yRot);
    rotatedZ = rotatedZ * PoseMath.cos(pose.yRot) - x * PoseMath.sin(pose.yRot);
    return new Vector3(
        pose.x + rotatedX * PoseMath.cos(pose.zRot) - rotatedY * PoseMath.sin(pose.zRot),
        pose.y + rotatedX * PoseMath.sin(pose.zRot) + rotatedY * PoseMath.cos(pose.zRot),
        pose.z + rotatedZ);
  }

  public void applyAttack(
      HumanoidPose.Part arm, boolean rightArm, float attackTime, float progress) {
    float swing = PoseMath.sin(attackTime * PoseMath.PI);
    float reach = PoseMath.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * PoseMath.PI);
    float direction = rightArm ? 1.0F : -1.0F;
    arm.xRot -= reach * 1.2F * progress;
    arm.yRot += direction * swing * 0.6F * progress;
    arm.zRot += direction * swing * 0.3F * progress;
    arm.z -= swing * 3.5F * progress;
    arm.y -= swing * 1.5F * progress;
  }

  private void blend(
      HumanoidPose.Part part,
      float weight,
      float x,
      float y,
      float z,
      float xRot,
      float yRot,
      float zRot) {
    part.x = PoseMath.lerp(weight, part.x, x);
    part.y = PoseMath.lerp(weight, part.y, y);
    part.z = PoseMath.lerp(weight, part.z, z);
    part.xRot = PoseMath.lerp(weight, part.xRot, xRot);
    part.yRot = PoseMath.lerp(weight, part.yRot, yRot);
    part.zRot = PoseMath.lerp(weight, part.zRot, zRot);
  }

  private float smooth(float progress) {
    return progress * progress * (3.0F - 2.0F * progress);
  }
}
