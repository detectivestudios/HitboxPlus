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
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.monster.Phantom;

public class VanillaFlyingAnimation {

  public void bat(VanillaRig rig, VanillaPoseContext context) {
    var bat = (Bat) context.entity();
    context.look(rig, "head");
    if (bat.isResting()) {
      rig.part("head").yaw = Mth.PI - rig.part("head").yaw;
      rig.part("head").roll = Mth.PI;
      rig.part("head").y = -2;
      rig.part("body").pitch = Mth.PI;
      rig.part("body/right_wing").set(-3, 0, 3);
      rig.part("body/left_wing").set(3, 0, 3);
      rig.part("body/right_wing").rotate(-0.15707964F, -1.2566371F, 0);
      rig.part("body/left_wing").rotate(-0.15707964F, 1.2566371F, 0);
      rig.part("body/right_wing/right_wing_tip").yaw = -1.7278761F;
      rig.part("body/left_wing/left_wing_tip").yaw = 1.7278761F;
    } else {
      rig.part("body").pitch = Mth.PI / 4 + Mth.cos(context.age() * 0.1F) * 0.15F;
      float wing = Mth.cos(context.age() * 1.3F) * Mth.PI * 0.25F;
      rig.part("body/right_wing").yaw = wing;
      rig.part("body/left_wing").yaw = -wing;
      rig.part("body/right_wing/right_wing_tip").yaw = wing * 0.5F;
      rig.part("body/left_wing/left_wing_tip").yaw = -wing * 0.5F;
    }
  }

  public void bee(VanillaRig rig, VanillaPoseContext context) {
    var bee = (Bee) context.entity();
    boolean resting = bee.isOnGround() && bee.getDeltaMovement().lengthSqr() < 1e-7;
    var right = rig.part("bone/right_wing");
    var left = rig.part("bone/left_wing");
    right.yaw = resting ? -0.2618F : 0;
    left.yaw = -right.yaw;
    right.roll = resting ? 0 : Mth.cos(context.age() * 2.1F) * Mth.PI * 0.15F;
    left.roll = -right.roll;
    for (String name : new String[] {"front_legs", "middle_legs", "back_legs"}) {
      rig.part("bone/" + name).pitch = resting ? 0 : Mth.PI / 4;
    }
    if (!bee.isAngry() && !resting) {
      float bob = Mth.cos(context.age() * 0.18F);
      rig.part("bone").pitch = 0.1F + bob * Mth.PI * 0.025F;
      rig.part("bone").y = 19 - bob * 0.9F;
      rig.part("bone/body/left_antenna").pitch = bob * Mth.PI * 0.03F;
      rig.part("bone/body/right_antenna").pitch = bob * Mth.PI * 0.03F;
      rig.part("bone/front_legs").pitch = -bob * Mth.PI * 0.1F + Mth.PI / 8;
      rig.part("bone/back_legs").pitch = -bob * Mth.PI * 0.05F + Mth.PI / 4;
    }
    float roll = bee.getRollAmount(context.partialTick());
    rig.part("bone").pitch +=
        roll
            * Mth.wrapDegrees((3.0915928F - rig.part("bone").pitch) * Mth.RAD_TO_DEG)
            * Mth.DEG_TO_RAD;
    if (bee.isBaby()) {
      rig.parent("bone", 0.5, 12, 0);
    }
  }

  public void phantom(VanillaRig rig, VanillaPoseContext context) {
    var phantom = (Phantom) context.entity();
    float phase = (phantom.getUniqueFlapTickOffset() + context.age()) * 7.448451F * Mth.DEG_TO_RAD;
    float wing = Mth.cos(phase) * 16 * Mth.DEG_TO_RAD;
    for (String name : new String[] {"body/left_wing_base", "body/left_wing_base/left_wing_tip"}) {
      rig.part(name).roll = wing;
    }
    for (String name :
        new String[] {"body/right_wing_base", "body/right_wing_base/right_wing_tip"}) {
      rig.part(name).roll = -wing;
    }
    float tail = -(5 + Mth.cos(phase * 2) * 5) * Mth.DEG_TO_RAD;
    rig.part("body/tail_base").pitch = tail;
    rig.part("body/tail_base/tail_tip").pitch = tail;
  }

  public void ghast(VanillaRig rig, VanillaPoseContext context) {
    for (int index = 0; index < 9; index++) {
      rig.part("tentacle" + index).pitch = 0.2F * Mth.sin(context.age() * 0.3F + index) + 0.4F;
    }
  }

  public void parrot(VanillaRig rig, VanillaPoseContext context) {
    var parrot = (Parrot) context.entity();
    context.look(rig, "head");
    rig.part("body").pitch = 0.4937F;
    rig.part("left_wing").pitch = rig.part("right_wing").pitch = -0.6981F;
    rig.part("left_wing").yaw = -Mth.PI;
    rig.part("right_wing").yaw = -Mth.PI;
    rig.part("left_leg").pitch = rig.part("right_leg").pitch = -0.0299F;
    rig.part("head/feather").pitch = -0.2214F;
    float flap = Mth.lerp(context.partialTick(), parrot.oFlap, parrot.flap);
    float speed = Mth.lerp(context.partialTick(), parrot.oFlapSpeed, parrot.flapSpeed);
    float wing = (Mth.sin(flap) + 1) * speed;
    if (parrot.isInSittingPose()) {
      rig.part("head").y = 17.59F;
      rig.part("tail").pitch = 1.5388988F;
      rig.part("tail").y = 22.97F;
      rig.part("body").y = 18.4F;
      rig.part("left_wing").roll = -0.0873F;
      rig.part("right_wing").roll = 0.0873F;
      rig.part("left_wing").y = rig.part("right_wing").y = 18.84F;
      rig.part("left_leg").y = rig.part("right_leg").y = 23;
      rig.part("left_leg").pitch = rig.part("right_leg").pitch = 0.9701F;
    } else {
      float bob = wing * 0.3F;
      rig.part("head").y += bob;
      rig.part("body").y += bob;
      rig.part("tail").y += bob;
      rig.part("tail").pitch =
          1.015F + Mth.cos(context.swing() * 0.6662F) * 0.3F * context.amount();
      rig.part("left_wing").y += bob;
      rig.part("right_wing").y += bob;
      rig.part("left_wing").roll = -0.0873F - wing;
      rig.part("right_wing").roll = 0.0873F + wing;
      rig.part("left_leg").y += bob;
      rig.part("right_leg").y += bob;
      if (parrot.isFlying()) {
        rig.part("left_leg").pitch += 0.6981317F;
        rig.part("right_leg").pitch += 0.6981317F;
      } else {
        rig.part("left_leg").pitch += Mth.cos(context.swing() * 0.6662F) * 1.4F * context.amount();
        rig.part("right_leg").pitch +=
            Mth.cos(context.swing() * 0.6662F + Mth.PI) * 1.4F * context.amount();
      }
    }
    if (parrot.isPartyParrot()) {
      float x = Mth.cos(parrot.tickCount);
      float y = Mth.sin(parrot.tickCount);
      rig.part("head").rotate(0, 0, y * 0.4F);
      for (String name : new String[] {"head", "body", "tail", "left_wing", "right_wing"}) {
        rig.part(name).x += x;
      }
      rig.part("head").y = 15.69F + y;
      rig.part("body").y = 16.5F + y;
      rig.part("tail").y = 21.07F + y;
      rig.part("left_wing").y = rig.part("right_wing").y = 16.94F + y;
      rig.part("left_leg").roll = -0.34906584F;
      rig.part("right_leg").roll = 0.34906584F;
    }
  }
}
