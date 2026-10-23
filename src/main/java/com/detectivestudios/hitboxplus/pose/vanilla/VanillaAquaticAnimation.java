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

import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Guardian;

public class VanillaAquaticAnimation {

  public void fish(VanillaRig rig, VanillaPoseContext context) {
    if (context.kind().equals("pufferfish")) {
      String right = rig.has("right_fin") ? "right_fin" : "right_blue_fin";
      String left = rig.has("left_fin") ? "left_fin" : "left_blue_fin";
      rig.part(right).roll = -0.2F + 0.4F * Mth.sin(context.age() * 0.2F);
      rig.part(left).roll = -rig.part(right).roll;
    } else if (context.kind().equals("salmon")) {
      boolean water = context.entity().isInWater();
      rig.part("body_back").yaw =
          -(water ? 1 : 1.3F) * 0.25F * Mth.sin((water ? 1 : 1.7F) * 0.6F * context.age());
    } else {
      rig.part(context.kind().equals("cod") ? "tail_fin" : "tail").yaw =
          -(context.entity().isInWater() ? 1 : 1.5F) * 0.45F * Mth.sin(context.age() * 0.6F);
    }
  }

  public void squid(VanillaRig rig, VanillaPoseContext context) {
    var squid = (Squid) context.entity();
    float tentacle = Mth.lerp(context.partialTick(), squid.oldTentacleAngle, squid.tentacleAngle);
    for (int index = 0; index < 8; index++) {
      rig.part("tentacle" + index).pitch = tentacle;
    }
  }

  public void dolphin(VanillaRig rig, VanillaPoseContext context) {
    context.look(rig, "body");
    if (context.entity().getDeltaMovement().horizontalDistanceSqr() > 1e-7) {
      float wave = Mth.cos(context.age() * 0.3F);
      rig.part("body").pitch += -0.05F - 0.05F * wave;
      rig.part("body/tail").pitch = -0.1F * wave;
      rig.part("body/tail/tail_fin").pitch = -0.2F * wave;
    }
    if (context.entity().isBaby()) {
      rig.parent("body", 0.5, 12, 0);
    }
  }

  public void guardian(VanillaRig rig, VanillaPoseContext context) {
    var guardian = (Guardian) context.entity();
    context.look(rig, "head");
    // Vanilla's random cosmetic counters only advance on the client; combat uses shared state.
    float spikes = guardian.isMoving() ? 0.55F : 0;
    float[] x = {0, 0, 8, -8, -8, 8, 8, -8, 0, 0, 8, -8};
    float[] y = {-8, -8, -8, -8, 0, 0, 0, 0, 8, 8, 8, 8};
    float[] z = {8, -8, 0, 0, -8, -8, 8, 8, 8, -8, 0, 0};
    for (int index = 0; index < 12; index++) {
      float extension = 1 + Mth.cos(context.age() * 1.5F + index) * 0.01F - spikes;
      rig.part("head/spike" + index)
          .set(x[index] * extension, 16 + y[index] * extension, z[index] * extension);
    }
    float tail = context.age() * (!guardian.isInWater() ? 2 : guardian.isMoving() ? 0.5F : 0.125F);
    rig.part("head/tail0").yaw = Mth.sin(tail) * Mth.PI * 0.05F;
    rig.part("head/tail0/tail1").yaw = Mth.sin(tail) * Mth.PI * 0.1F;
    rig.part("head/tail0/tail1/tail2").yaw = Mth.sin(tail) * Mth.PI * 0.15F;
    var target = guardian.getActiveAttackTarget();
    if (target != null) {
      var eye = target.getEyePosition(0);
      var own = guardian.getEyePosition(0);
      rig.part("head/eye").y = eye.y > own.y ? 0 : 1;
      var view = guardian.getViewVector(0);
      var direction =
          new net.minecraft.world.phys.Vec3(own.x - eye.x, 0, own.z - eye.z)
              .normalize()
              .yRot(Mth.PI / 2);
      double dot = view.x * direction.x + view.z * direction.z;
      rig.part("head/eye").x = Mth.sqrt((float) Math.abs(dot)) * 2 * (float) Math.signum(dot);
    }
  }

  public void turtle(VanillaRig rig, VanillaPoseContext context) {
    var turtle = (Turtle) context.entity();
    context.look(rig, "head");
    float phase = context.swing() * 0.6662F * 0.6F;
    rig.part("right_hind_leg").pitch = Mth.cos(phase) * 0.5F * context.amount();
    rig.part("left_hind_leg").pitch = Mth.cos(phase + Mth.PI) * 0.5F * context.amount();
    rig.part("right_front_leg").roll = Mth.cos(phase + Mth.PI) * 0.5F * context.amount();
    rig.part("left_front_leg").roll = Mth.cos(phase) * 0.5F * context.amount();
    if (!turtle.isInWater() && turtle.isOnGround()) {
      float frequency = turtle.isLayingEgg() ? 4 : 1;
      float amplitude = turtle.isLayingEgg() ? 2 : 1;
      rig.part("right_front_leg").yaw =
          Mth.cos(frequency * context.swing() * 5 + Mth.PI) * 8 * context.amount() * amplitude;
      rig.part("left_front_leg").yaw =
          Mth.cos(frequency * context.swing() * 5) * 8 * context.amount() * amplitude;
      rig.part("right_front_leg").roll = rig.part("left_front_leg").roll = 0;
      rig.part("right_hind_leg").yaw = Mth.cos(context.swing() * 5 + Mth.PI) * 3 * context.amount();
      rig.part("left_hind_leg").yaw = Mth.cos(context.swing() * 5) * 3 * context.amount();
      rig.part("right_hind_leg").pitch = rig.part("left_hind_leg").pitch = 0;
    }
    if (turtle.isBaby()) {
      rig.young(1.5 / 9, 120, 0, 1.0 / 6, 120, "head");
    }
  }

  public void axolotl(VanillaRig rig, VanillaPoseContext context) {
    var axolotl = (Axolotl) context.entity();
    context.look(rig, "body");
    var body = rig.part("body");
    var head = rig.part("body/head");
    var leftHind = rig.part("body/left_hind_leg");
    var leftFront = rig.part("body/left_front_leg");
    var rightHind = rig.part("body/right_hind_leg");
    var rightFront = rig.part("body/right_front_leg");
    var top = rig.part("body/head/top_gills");
    var leftGills = rig.part("body/head/left_gills");
    var rightGills = rig.part("body/head/right_gills");
    var tail = rig.part("body/tail");
    boolean moving =
        axolotl.getDeltaMovement().horizontalDistanceSqr() > 1e-7
            || axolotl.getXRot() != axolotl.xRotO
            || axolotl.getYRot() != axolotl.yRotO
            || axolotl.xOld != axolotl.getX()
            || axolotl.zOld != axolotl.getZ();
    if (axolotl.isPlayingDead()) {
      leftHind.rotate(1.4137167F, 1.0995574F, Mth.PI / 4);
      leftFront.rotate(Mth.PI / 4, 2.042035F, 0);
      body.pitch = -0.15F;
      body.roll = 0.35F;
    } else if (axolotl.isInWaterOrBubble()) {
      if (moving) {
        float phase = context.age() * 0.33F;
        float wave = Mth.sin(phase);
        body.pitch += 0.13F * wave;
        body.y -= 0.45F * Mth.cos(phase);
        head.pitch = -0.234F * wave;
        top.pitch = -0.5F * wave - 0.8F;
        leftGills.yaw = 0.3F * wave + 0.9F;
        tail.yaw = 0.3F * Mth.cos(phase * 0.9F);
        leftHind.rotate(1.8849558F, -0.4F * wave, Mth.PI / 2);
        leftFront.rotate(1.8849558F, -0.2F * Mth.cos(phase) - 0.1F, Mth.PI / 2);
      } else {
        float phase = context.age() * 0.075F;
        float wave = Mth.cos(phase);
        body.pitch = -0.15F + 0.075F * wave;
        body.y -= Mth.sin(phase) * 0.15F;
        head.pitch = -body.pitch;
        top.pitch = 0.2F * wave;
        leftGills.yaw = -0.3F * wave - 0.19F;
        leftHind.rotate(2.3561945F - wave * 0.11F, 0.47123894F, 1.7278761F);
        leftFront.rotate(Mth.PI / 4 - wave * 0.2F, 2.042035F, 0);
        tail.yaw = 0.5F * wave;
      }
    } else {
      body.pitch = 0;
      float phase = context.age() * (moving ? 0.11F : 0.09F);
      float cosine = Mth.cos(phase);
      float sine = Mth.sin(phase);
      if (moving) {
        float offset = (cosine * cosine - 2 * cosine) / 5;
        head.yaw = 0.09F * cosine;
        tail.yaw = head.yaw;
        top.pitch = 0.6F - 0.08F * (cosine * cosine + 2 * sine);
        leftHind.rotate(0.9424779F, 1.5F - offset, -0.1F);
        leftFront.rotate(1.0995574F, Mth.PI / 2 - 0.7F * cosine, 0);
        rightHind.rotate(leftHind.pitch, -1 - offset, 0);
        rightFront.rotate(leftFront.pitch, -Mth.PI / 2 - 0.7F * cosine, 0);
      } else {
        float wave = sine * sine - 2 * sine;
        head.pitch = -0.09F * wave;
        head.roll = -0.2F;
        tail.yaw = -0.1F + 0.1F * wave;
        top.pitch = 0.6F + 0.05F * (cosine * cosine - 3 * sine);
        leftHind.rotate(1.1F, 1, 0);
        leftFront.rotate(0.8F, 2.3F, -0.5F);
      }
      leftGills.yaw = -top.pitch;
    }
    if (axolotl.isPlayingDead() || axolotl.isInWaterOrBubble() || !moving) {
      rightHind.rotate(leftHind.pitch, -leftHind.yaw, -leftHind.roll);
      rightFront.rotate(leftFront.pitch, -leftFront.yaw, -leftFront.roll);
    }
    rightGills.yaw = -leftGills.yaw;
    if (axolotl.isBaby()) {
      rig.parent("body", 0.5, 12, 0);
    }
  }
}
