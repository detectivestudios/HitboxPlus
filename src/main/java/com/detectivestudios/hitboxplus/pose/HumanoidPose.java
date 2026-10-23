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

import com.detectivestudios.hitboxplus.profile.HumanoidBone;

/** Model-space pivots in pixels and Euler rotations in radians, matching a Java player model. */
public class HumanoidPose {

  public final Part head = new Part(0.0F, 0.0F, 0.0F);
  public final Part body = new Part(0.0F, 0.0F, 0.0F);
  public final Part rightArm = new Part(-5.0F, 2.0F, 0.0F);
  public final Part leftArm = new Part(5.0F, 2.0F, 0.0F);
  public final Part rightLeg = new Part(-1.9F, 12.0F, 0.1F);
  public final Part leftLeg = new Part(1.9F, 12.0F, 0.1F);
  public boolean crouching;

  public Part part(HumanoidBone bone) {
    return switch (bone) {
      case HEAD -> this.head;
      case BODY -> this.body;
      case RIGHT_ARM -> this.rightArm;
      case LEFT_ARM -> this.leftArm;
      case RIGHT_LEG -> this.rightLeg;
      case LEFT_LEG -> this.leftLeg;
    };
  }

  public static class Part {

    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;

    public Part(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public Part storePose() {
      return offsetAndRotation(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot);
    }

    public static Part offsetAndRotation(
        float x, float y, float z, float xRot, float yRot, float zRot) {
      var part = new Part(x, y, z);
      part.xRot = xRot;
      part.yRot = yRot;
      part.zRot = zRot;
      return part;
    }
  }
}
