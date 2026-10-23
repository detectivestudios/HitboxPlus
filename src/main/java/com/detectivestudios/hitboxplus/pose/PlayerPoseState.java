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

public class PlayerPoseState {

  private float previousProne;
  private float prone;
  private float previousAim;
  private float aim;
  private boolean suppressSwimming;
  private int shootingHold;

  public void tick(
      boolean nativePose,
      boolean proneIntent,
      boolean visuallySwimming,
      float swimAmount,
      boolean aiming,
      boolean shooting) {
    this.previousProne = this.prone;
    this.previousAim = this.aim;
    boolean target = !nativePose && (proneIntent || (this.suppressSwimming && visuallySwimming));
    this.suppressSwimming |= target;
    if (nativePose) {
      this.prone = 0.0F;
      this.previousProne = 0.0F;
      this.suppressSwimming = false;
    } else {
      this.prone = this.approach(this.prone, target ? 1.0F : 0.0F);
      if (!target && this.prone == 0.0F && swimAmount == 0.0F) {
        this.suppressSwimming = false;
      }
    }
    this.aim = this.approach(this.aim, aiming ? 1.0F : 0.0F);
    if (shooting) {
      this.shootingHold = 15;
    } else if (this.shootingHold > 0) {
      this.shootingHold--;
    }
  }

  private float approach(float value, float target) {
    float result = PoseMath.lerp(1.0F - (float) Math.exp(-0.05F * 12.0F), value, target);
    return Math.abs(result - target) < 0.003F ? target : result;
  }

  public float proneProgress(float partialTick) {
    return PoseMath.lerp(partialTick, this.previousProne, this.prone);
  }

  public float aimProgress(float partialTick) {
    return PoseMath.lerp(partialTick, this.previousAim, this.aim);
  }

  public boolean suppressSwimming() {
    return this.suppressSwimming;
  }

  public boolean holdingGun() {
    return this.shootingHold > 0;
  }
}
