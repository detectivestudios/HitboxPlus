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
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;

public class VanillaAnimalAnimation {

  public void quadruped(VanillaRig rig, VanillaPoseContext context) {
    context.look(rig, "head");
    context.walk(rig, 0.6662F, 1.4F);
  }

  public void goat(VanillaRig rig, VanillaPoseContext context) {
    this.quadruped(rig, context);
    var goat = (Goat) context.entity();
    if (goat.getRammingXHeadRot() != 0) {
      rig.part("head").pitch = goat.getRammingXHeadRot();
    }
    if (goat.isBaby()) {
      rig.young(0.6, 19, 1, 0.5, 24, "head");
    }
  }

  public void hoglin(VanillaRig rig, VanillaPoseContext context) {
    var hoglin = (HoglinBase) context.entity();
    context.look(rig, "head");
    context.walk(rig, 1, -1.2F);
    float attack = 1 - Math.abs(10 - 2 * hoglin.getAttackAnimationRemainingTicks()) / 10.0F;
    rig.part("head").pitch = Mth.lerp(attack, 0.87266463F, -0.34906584F);
    float ear = context.amount() * Mth.sin(context.swing());
    rig.part("head/right_ear").roll = -0.6981317F - ear;
    rig.part("head/left_ear").roll = 0.6981317F + ear;
    rig.part("body/mane").z = context.entity().isBaby() ? -3 : -7;
    if (context.entity().isBaby()) {
      rig.part("head").y = Mth.lerp(attack, 2, 5);
      rig.young(1.5 / 1.9, 8, 6, 0.5, 24, "head");
    }
  }

  public void bear(VanillaRig rig, VanillaPoseContext context) {
    this.quadruped(rig, context);
    var bear = (PolarBear) context.entity();
    float standing = bear.getStandingAnimationScale(context.partialTick());
    standing *= standing;
    rig.part("body").pitch = Mth.PI / 2 - standing * Mth.PI * 0.35F;
    rig.part("body").y = 9 + 2 * standing;
    for (String name : new String[] {"left_front_leg", "right_front_leg"}) {
      rig.part(name).y = 14 - 20 * standing;
      rig.part(name).z = -8 + 4 * standing;
      rig.part(name).pitch -= standing * Mth.PI * 0.45F;
    }
    rig.part("head").y = 10 - (bear.isBaby() ? 19 : 24) * standing;
    rig.part("head").z = -16 + (bear.isBaby() ? 9 : 13) * standing;
    rig.part("head").pitch += standing * Mth.PI * 0.15F;
    if (bear.isBaby()) {
      rig.young(1.5 / 2.25, 16, 4, 0.5, 24, "head");
    }
  }

  public void rabbit(VanillaRig rig, VanillaPoseContext context) {
    var rabbit = (Rabbit) context.entity();
    for (String name : new String[] {"head", "nose", "left_ear", "right_ear"}) {
      context.look(rig, name);
    }
    rig.part("left_ear").yaw += 0.2617994F;
    rig.part("right_ear").yaw -= 0.2617994F;
    float jump = Mth.sin(rabbit.getJumpCompletion(context.partialTick()) * Mth.PI);
    for (String side : new String[] {"left", "right"}) {
      rig.part(side + "_haunch").pitch = (jump * 50 - 21) * Mth.DEG_TO_RAD;
      rig.part(side + "_hind_foot").pitch = jump * 50 * Mth.DEG_TO_RAD;
      rig.part(side + "_front_leg").pitch = (jump * -40 - 11) * Mth.DEG_TO_RAD;
    }
    if (rabbit.isBaby()) {
      rig.young(0.56666666, 22, 2, 0.4, 36, "head", "nose", "left_ear", "right_ear");
    } else {
      rig.young(0.6, 16, 0, 0.6, 16);
    }
  }

  public void wolf(VanillaRig rig, VanillaPoseContext context) {
    this.quadruped(rig, context);
    var wolf = (Wolf) context.entity();
    rig.part("tail").pitch = wolf.getTailAngle();
    rig.part("tail").yaw =
        wolf.isAngry() ? 0 : Mth.cos(context.swing() * 0.6662F) * 1.4F * context.amount();
    if (wolf.isInSittingPose()) {
      rig.part("upper_body").set(-1, 16, -3);
      rig.part("upper_body").pitch = 1.2566371F;
      rig.part("body").set(0, 18, 0);
      rig.part("body").pitch = Mth.PI / 4;
      rig.part("tail").set(-1, 21, 6);
      rig.part("right_hind_leg").set(-2.5F, 22.7F, 2);
      rig.part("left_hind_leg").set(0.5F, 22.7F, 2);
      rig.part("right_hind_leg").pitch = rig.part("left_hind_leg").pitch = Mth.PI * 1.5F;
      rig.part("right_front_leg").set(-2.49F, 17, -4);
      rig.part("left_front_leg").set(0.51F, 17, -4);
      rig.part("right_front_leg").pitch = rig.part("left_front_leg").pitch = 5.811947F;
    }
    rig.part("head/real_head").roll =
        wolf.getHeadRollAngle(context.partialTick())
            + wolf.getBodyRollAngle(context.partialTick(), 0);
    rig.part("upper_body").roll = wolf.getBodyRollAngle(context.partialTick(), -0.08F);
    rig.part("body").roll = wolf.getBodyRollAngle(context.partialTick(), -0.16F);
    rig.part("tail/real_tail").roll = wolf.getBodyRollAngle(context.partialTick(), -0.2F);
    if (wolf.isBaby()) {
      rig.young(1, 5, 2, 0.5, 24, "head");
    }
  }

  public void cat(VanillaRig rig, VanillaPoseContext context) {
    context.look(rig, "head");
    boolean sprint = context.entity().isSprinting();
    float phase = context.swing() * 0.6662F;
    float amount = context.amount();
    rig.part("left_hind_leg").pitch = Mth.cos(phase) * amount;
    rig.part("right_hind_leg").pitch = Mth.cos(phase + (sprint ? 0.3F : Mth.PI)) * amount;
    rig.part("left_front_leg").pitch = Mth.cos(phase + Mth.PI + (sprint ? 0.3F : 0)) * amount;
    rig.part("right_front_leg").pitch = Mth.cos(phase + (sprint ? Mth.PI : 0)) * amount;
    boolean crouching = context.entity().isCrouching();
    if (crouching) {
      rig.part("body").y++;
      rig.part("head").y += 2;
      rig.part("tail1").y++;
      rig.part("tail2").y -= 4;
      rig.part("tail2").z += 2;
      rig.part("tail1").pitch = Mth.PI / 2;
    } else if (sprint) {
      rig.part("tail2").y = rig.part("tail1").y;
      rig.part("tail2").z += 2;
      rig.part("tail1").pitch = Mth.PI / 2;
    }
    rig.part("tail2").pitch =
        1.7278761F
            + (sprint ? Mth.PI / 10 : crouching ? 0.47123894F : Mth.PI / 4)
                * Mth.cos(context.swing())
                * amount;
    if (context.entity() instanceof Cat cat) {
      this.domesticCat(rig, context, cat);
    }
    if (context.entity().isBaby()) {
      rig.young(0.75, 10, 4, 0.5, 24, "head");
    }
  }

  private void domesticCat(VanillaRig rig, VanillaPoseContext context, Cat cat) {
    if (cat.isInSittingPose()) {
      rig.part("body").pitch = Mth.PI / 4;
      rig.part("body").y -= 4;
      rig.part("body").z += 5;
      rig.part("head").y -= 3.3F;
      rig.part("head").z++;
      rig.part("tail1").y += 8;
      rig.part("tail1").z -= 2;
      rig.part("tail1").pitch = 1.7278761F;
      rig.part("tail2").y += 2;
      rig.part("tail2").z -= 0.8F;
      rig.part("tail2").pitch = 2.670354F;
      for (String side : new String[] {"left", "right"}) {
        rig.part(side + "_front_leg").y = 16.1F;
        rig.part(side + "_front_leg").z = -7;
        rig.part(side + "_front_leg").pitch = -0.15707964F;
        rig.part(side + "_hind_leg").y = 21;
        rig.part(side + "_hind_leg").z = 1;
        rig.part(side + "_hind_leg").pitch = -Mth.PI / 2;
      }
    }
    float lie = cat.getLieDownAmount(context.partialTick());
    if (lie > 0) {
      rig.part("head").roll = this.angle(lie, rig.part("head").roll, -1.2707963F);
      rig.part("head").yaw = this.angle(lie, rig.part("head").yaw, 1.2707963F);
      rig.part("left_front_leg").pitch = -1.2707963F;
      rig.part("right_front_leg").pitch = -0.47079635F;
      rig.part("right_front_leg").roll = -0.2F;
      rig.part("right_front_leg").x = -0.2F;
      rig.part("left_hind_leg").pitch = -0.4F;
      rig.part("right_hind_leg").pitch = 0.5F;
      rig.part("right_hind_leg").roll = -0.5F;
      rig.part("right_hind_leg").x = -0.3F;
      rig.part("right_hind_leg").y = 20;
      float tail = cat.getLieDownAmountTail(context.partialTick());
      rig.part("tail1").pitch = this.angle(tail, rig.part("tail1").pitch, 0.8F);
      rig.part("tail2").pitch = this.angle(tail, rig.part("tail2").pitch, -0.4F);
    }
    float relax = cat.getRelaxStateOneAmount(context.partialTick());
    rig.part("head").pitch = this.angle(relax, rig.part("head").pitch, -0.58177644F);
  }

  public void fox(VanillaRig rig, VanillaPoseContext context) {
    this.quadruped(rig, context);
    var fox = (Fox) context.entity();
    rig.part("head").roll = fox.getHeadRollAngle(context.partialTick());
    if (fox.isCrouching()) {
      rig.part("body").pitch = 1.6755161F;
      rig.part("body").y += fox.getCrouchAmount(context.partialTick());
      rig.part("head").y += fox.getCrouchAmount(context.partialTick());
      rig.part("head").pitch = rig.part("head").yaw = 0;
      float sway = Mth.cos(context.age()) * 0.01F;
      rig.part("body").yaw = sway;
      rig.part("right_hind_leg").roll = rig.part("left_hind_leg").roll = sway;
      rig.part("right_front_leg").roll = rig.part("left_front_leg").roll = sway * 0.5F;
    } else if (fox.isSleeping()) {
      rig.part("body").roll = -Mth.PI / 2;
      rig.part("body").set(0, 21, fox.isBaby() ? -2 : -6);
      rig.part("body/tail").pitch = fox.isBaby() ? -2.1816616F : -2.6179938F;
      rig.part("head").set(1, 19.49F, -3);
      rig.part("head").rotate(0, -2.0943952F, Mth.cos(context.age() * 0.027F) / 22);
    } else if (fox.isSitting()) {
      rig.part("body").pitch = Mth.PI / 6;
      rig.part("body").set(0, 9, -3);
      rig.part("body/tail").pitch = Mth.PI / 4;
      rig.part("body/tail").set(-4, 15, -2);
      rig.part("head").set(-1, fox.isBaby() ? 13 : 10, fox.isBaby() ? -3.75F : -0.25F);
      rig.part("right_hind_leg").set(-5, 21.5F, 6.75F);
      rig.part("left_hind_leg").set(-1, 21.5F, 6.75F);
      rig.part("right_hind_leg").pitch = rig.part("left_hind_leg").pitch = -1.3089969F;
      rig.part("right_front_leg").pitch = rig.part("left_front_leg").pitch = -0.2617994F;
    } else if (fox.isFaceplanted()) {
      context.walk(rig, 0.4662F, 0.1F);
    }
    if (fox.isBaby()) {
      rig.young(0.75, 8, 3.35, 0.5, 24, "head");
    }
  }

  public void horse(VanillaRig rig, VanillaPoseContext context) {
    var horse = (AbstractHorse) context.entity();
    float yaw = Mth.clamp(context.input().headYaw, -20, 20) * Mth.DEG_TO_RAD;
    float pitch = context.input().headPitch * Mth.DEG_TO_RAD;
    if (context.amount() > 0.2F) {
      pitch += Mth.cos(context.swing() * 0.4F) * 0.15F * context.amount();
    }
    float eat = horse.getEatAnim(context.partialTick());
    float stand = horse.getStandAnim(context.partialTick());
    float normal = 1 - Math.max(eat, stand);
    var head = rig.part("head_parts");
    head.pitch =
        stand * (0.2617994F + pitch)
            + eat * (2.1816616F + Mth.sin(context.age()) * 0.05F)
            + normal
                * (Mth.PI / 6
                    + pitch
                    + horse.getMouthAnim(context.partialTick()) * Mth.sin(context.age()) * 0.05F);
    head.yaw = (stand + normal) * yaw;
    head.y = stand * -4 + eat * 11 + normal * 4;
    head.z = stand * -4 - (eat + normal) * 12;
    rig.part("body").pitch = -stand * Mth.PI / 4;
    float gait = Mth.cos((horse.isInWater() ? 0.2F : 1) * context.swing() * 0.6662F + Mth.PI);
    float step = gait * 0.8F * context.amount();
    float rear = Mth.cos(context.age() * 0.6F + Mth.PI);
    String suffix = horse.isBaby() && rig.has("left_front_baby_leg") ? "baby_leg" : "leg";
    for (String side : new String[] {"left", "right"}) {
      boolean left = side.equals("left");
      var front = rig.part(side + "_front_" + suffix);
      front.y = 2 * stand + 14 * (1 - stand);
      front.z = -6 * stand - 10 * (1 - stand);
      front.pitch =
          (-Mth.PI / 3 + (left ? rear : -rear)) * stand + (left ? step : -step) * (1 - stand);
      rig.part(side + "_hind_" + suffix).pitch =
          0.2617994F * stand + (left ? -1 : 1) * gait * 0.5F * context.amount() * (1 - stand);
    }
    var tail = rig.part("body/tail");
    tail.pitch = Mth.PI / 6 + context.amount() * 0.75F;
    tail.y = -5 + context.amount();
    tail.z = 2 + context.amount() * 2;
    tail.yaw = horse.tailCounter != 0 ? Mth.cos(context.age() * 0.7F) : 0;
    if (horse.isBaby()) {
      rig.young(1.5 / 2.7272, 16.2, 1.36, 0.5, 20, "head_parts");
    }
  }

  public void panda(VanillaRig rig, VanillaPoseContext context) {
    this.quadruped(rig, context);
    var panda = (Panda) context.entity();
    var head = rig.part("head");
    var right = rig.part("right_front_leg");
    var left = rig.part("left_front_leg");
    if (panda.getUnhappyCounter() > 0) {
      head.yaw = head.roll = 0.35F * Mth.sin(context.age() * 0.6F);
      right.pitch = -0.75F * Mth.sin(context.age() * 0.3F);
      left.pitch = -right.pitch;
    }
    if (panda.isSneezing()) {
      int sneeze = panda.getSneezeCounter();
      if (sneeze < 15) {
        head.pitch = -Mth.PI / 4 * sneeze / 14;
      } else if (sneeze < 20) {
        // Vanilla 1.18.2 truncates the recovery fraction to zero throughout ticks 15–19.
        head.pitch = -Mth.PI / 4;
      }
    }
    float sit = panda.getSitAmount(context.partialTick());
    if (sit > 0) {
      rig.part("body").pitch = this.angle(sit, rig.part("body").pitch, 1.7407963F);
      head.pitch = this.angle(sit, head.pitch, Mth.PI / 2);
      right.roll = -0.27079642F;
      left.roll = 0.27079642F;
      rig.part("right_hind_leg").roll = 0.5707964F;
      rig.part("left_hind_leg").roll = -0.5707964F;
      if (panda.isEating()) {
        head.pitch = Mth.PI / 2 + 0.2F * Mth.sin(context.age() * 0.6F);
        right.pitch = left.pitch = -0.4F - 0.2F * Mth.sin(context.age() * 0.6F);
      }
      if (panda.isScared()) {
        head.pitch = 2.1707964F;
        right.pitch = left.pitch = -0.9F;
      }
    }
    float lie = panda.getLieOnBackAmount(context.partialTick());
    float roll = panda.isBaby() ? 0 : panda.getRollAmount(context.partialTick());
    if (lie > 0 || roll > 0) {
      float phase = context.age() * (roll > 0 ? 0.5F : 0.15F);
      float leg = (roll > 0 ? 0.5F : 0.6F) * Mth.sin(phase);
      rig.part("right_hind_leg").pitch = -leg;
      rig.part("left_hind_leg").pitch = leg;
      right.pitch = (roll > 0 ? 0.5F * Mth.sin(phase) : 0.3F * Mth.sin(context.age() * 0.25F));
      left.pitch = -right.pitch;
      head.pitch =
          this.angle(roll > 0 ? roll : lie, head.pitch, roll > 0 ? 2.0561945F : Mth.PI / 2);
    }
    if (panda.isBaby()) {
      rig.young(1.5 / 2.7, 23, 4.8, 1.0 / 3, 49, "head");
    }
  }

  public void strider(VanillaRig rig, VanillaPoseContext context) {
    var strider = (Strider) context.entity();
    if (!strider.isVehicle()) {
      context.look(rig, "body");
    }
    float amount = Math.min(0.25F, context.amount());
    float phase = context.swing() * 1.5F;
    rig.part("body").roll = Mth.sin(phase) * 0.4F * amount;
    rig.part("body").y = 2 - Mth.cos(phase) * 4 * amount;
    for (String side : new String[] {"left", "right"}) {
      float offset = side.equals("left") ? 0 : Mth.PI;
      rig.part(side + "_leg").pitch = Mth.sin(phase * 0.5F + offset) * 2 * amount;
      rig.part(side + "_leg").roll = 0.17453292F * Mth.cos(phase * 0.5F + offset) * amount;
      rig.part(side + "_leg").y = 8 + Mth.sin(phase * 0.5F + Mth.PI - offset) * 4 * amount;
      String[] bristles = {"bottom", "middle", "top"};
      float[] weights = {1.3F, 1.2F, 0.6F};
      float[] idle = {-0.4F, 0.2F, 0.4F};
      for (int index = 0; index < bristles.length; index++) {
        rig.part("body/" + side + "_" + bristles[index] + "_bristle").roll +=
            Mth.cos(phase + Mth.PI) * amount * weights[index]
                + (index == 0 ? 0.05F : 0.1F) * Mth.sin(context.age() * idle[index]);
      }
    }
    if (strider.isBaby()) {
      rig.young(0.5, 24, 0, 0.5, 24);
    }
  }

  private float angle(float amount, float from, float to) {
    return from + amount * Mth.wrapDegrees((to - from) * Mth.RAD_TO_DEG) * Mth.DEG_TO_RAD;
  }
}
