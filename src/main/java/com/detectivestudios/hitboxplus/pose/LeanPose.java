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
import java.util.Objects;
import java.util.function.Function;

public class LeanPose implements HumanoidPoseAnimation {

  private final HumanoidPoseAnimation baseAnimation;
  private final Function<HumanoidAnimation.Input, State> stateSource;

  public LeanPose() {
    this(new HumanoidAnimation(), input -> State.NONE);
  }

  public LeanPose(
      HumanoidPoseAnimation baseAnimation, Function<HumanoidAnimation.Input, State> stateSource) {
    this.baseAnimation = Objects.requireNonNull(baseAnimation, "Base animation is required");
    this.stateSource = Objects.requireNonNull(stateSource, "Lean state source is required");
  }

  @Override
  public HumanoidPose evaluate(HumanoidAnimation.Input input) {
    var state = Objects.requireNonNull(this.stateSource.apply(input), "Lean state is required");
    var pose =
        Objects.requireNonNull(
            this.baseAnimation.evaluate(input), "Base animation must return a pose");
    this.apply(pose, state.angleDegrees(), state.proneProgress());
    return pose;
  }

  /** The angle is in degrees; prone progress is a finite fraction in [0, 1]. */
  public record State(float angleDegrees, float proneProgress) {

    public static final State NONE = new State(0, 0);

    public State {
      if (!Float.isFinite(angleDegrees)
          || !Float.isFinite(proneProgress)
          || proneProgress < 0
          || proneProgress > 1) {
        throw new IllegalArgumentException(
            "Lean angle must be finite and prone progress must be in [0, 1]");
      }
    }
  }

  public void apply(HumanoidPose model, float angle, float proneProgress) {
    float rad = -angle * PoseMath.DEG_TO_RAD;
    if (rad == 0.0F) {
      return;
    }
    float prone = proneProgress * proneProgress * (3.0F - 2.0F * proneProgress);
    float crouch = model.crouching ? 1.0F - prone : 0.0F;
    float roll = rad * (1.0F + 0.4F * prone) * PoseMath.lerp(crouch, 1.0F, 0.9F);
    float hipHeight = (model.rightLeg.y + model.leftLeg.y) * 0.5F;
    float pivot = PoseMath.lerp(prone, PoseMath.lerp(crouch, 12.0F, hipHeight), model.body.y);
    float shift = PoseMath.sin(rad) * 5.5F * prone;
    float overlap =
        (4.0F * Math.abs(PoseMath.sin(roll)) + Math.abs(rad) * 0.35F)
            * (1.0F - prone)
            * (1.0F - crouch);
    // Slide along the tilted torso so its lower edge still covers the flat hip seam.
    float upperShift = shift - overlap * PoseMath.sin(roll) / PoseMath.cos(roll);
    this.rotate(model.body, roll, pivot, upperShift, overlap);
    this.rotate(model.head, roll, pivot, upperShift, overlap);
    this.rotate(model.rightArm, roll, pivot, upperShift, overlap);
    this.rotate(model.leftArm, roll, pivot, upperShift, overlap);
    model.head.zRot -= roll * crouch * 0.15F;
    if (prone > 0.0F) {
      this.rotate(model.rightLeg, roll * prone, pivot, shift, 0.0F);
      this.rotate(model.leftLeg, roll * prone, pivot, shift, 0.0F);
      float turn = rad * 0.3F * prone;
      model.body.yRot += turn;
      model.head.yRot += turn;
      model.rightLeg.yRot += turn;
      model.leftLeg.yRot += turn;
    }
  }

  private void rotate(HumanoidPose.Part part, float roll, float pivot, float shift, float drop) {
    float sin = PoseMath.sin(roll);
    float cos = PoseMath.cos(roll);
    float x = part.x;
    part.x = x * cos - (part.y - pivot) * sin + shift;
    part.y = x * sin + (part.y - pivot) * cos + pivot + drop;
    part.zRot += roll;
  }
}
