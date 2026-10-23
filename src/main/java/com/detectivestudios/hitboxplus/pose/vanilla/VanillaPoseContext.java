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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public record VanillaPoseContext(
    LivingEntity entity, String kind, HumanoidAnimation.Input input, float partialTick) {

  public float age() {
    return this.input.age;
  }

  public float swing() {
    return this.input.limbSwing;
  }

  public float amount() {
    return this.input.limbAmount;
  }

  public void look(VanillaRig rig, String name) {
    rig.part(name).pitch = this.input.headPitch * Mth.DEG_TO_RAD;
    rig.part(name).yaw = this.input.headYaw * Mth.DEG_TO_RAD;
  }

  public void walk(VanillaRig rig, float frequency, float amplitude) {
    float step = Mth.cos(this.swing() * frequency) * amplitude * this.amount();
    float opposite = Mth.cos(this.swing() * frequency + Mth.PI) * amplitude * this.amount();
    rig.part("right_hind_leg").pitch = step;
    rig.part("left_hind_leg").pitch = opposite;
    rig.part("right_front_leg").pitch = opposite;
    rig.part("left_front_leg").pitch = step;
  }
}
