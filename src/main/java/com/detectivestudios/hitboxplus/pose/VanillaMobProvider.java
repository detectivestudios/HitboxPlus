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

import com.detectivestudios.hitboxplus.api.EntityHitboxProvider;
import com.detectivestudios.hitboxplus.api.EntityModelPose;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.math.BoneTransform;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.profile.VanillaMobProfiles;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Items;

public class VanillaMobProvider implements EntityHitboxProvider {

  private final LivingEntity entity;
  private final String kind;
  private final VanillaMobProfiles profiles;
  private final LivingModelTransform transform;
  private final double scale;
  private final HumanoidInputs inputs = new HumanoidInputs();
  private final HumanoidAnimation humanoid = new HumanoidAnimation();
  private final EntityBodyGeometry geometry = new EntityBodyGeometry();

  public VanillaMobProvider(LivingEntity entity, String kind, VanillaMobProfiles profiles) {
    this.entity = entity;
    this.kind = kind;
    this.profiles = profiles;
    this.scale =
        switch (kind) {
          case "giant" -> 6.0D;
          case "husk" -> 1.0625D;
          case "wither_skeleton" -> 1.2D;
          default -> 1.0D;
        };
    this.transform = new LivingModelTransform(entity, this.scale);
  }

  @Override
  public Optional<HitboxPose> getPose(float partialTick) {
    return this.getModelPose(partialTick)
        .map(
            pose ->
                this.geometry.create(
                    this.profiles.find("minecraft:" + this.kind, pose.variant()).orElseThrow(),
                    pose.bones()::get,
                    pose.root()));
  }

  @Override
  public Optional<EntityModelPose> getModelPose(float partialTick) {
    // Fuse swelling is nonuniform and cannot be represented by the rigid bone API.
    if (this.entity instanceof Creeper creeper && creeper.getSwelling(partialTick) > 0.0F) {
      return Optional.empty();
    }
    var input = this.inputs.sample(this.entity, this.transform, partialTick);
    Map<String, BoneTransform> bones;
    if (this.entity instanceof Zombie
        || this.entity instanceof AbstractSkeleton
        || this.kind.equals("giant")) {
      bones = this.humanoid(input);
    } else if (this.entity instanceof Chicken chicken) {
      float flap = Mth.lerp(partialTick, chicken.oFlap, chicken.flap);
      float speed = Mth.lerp(partialTick, chicken.oFlapSpeed, chicken.flapSpeed);
      bones = this.chicken(input, (Mth.sin(flap) + 1.0F) * speed);
    } else {
      bones = this.quadruped(input, partialTick);
    }
    String variant = this.entity instanceof Sheep sheep && !sheep.isSheared() ? "wool" : "base";
    bones.put("$root", BoneTransform.IDENTITY);
    var root = this.transform.root(partialTick, this.transform.bodyYaw(partialTick));
    return Optional.of(
        new EntityModelPose(
            bones, new BoneTransform(root.origin(), root.rotation(), this.scale / 16.0D), variant));
  }

  private Map<String, BoneTransform> humanoid(HumanoidAnimation.Input input) {
    boolean skeleton = this.entity instanceof AbstractSkeleton;
    boolean aggressive = this.entity instanceof Mob mob && mob.isAggressive();
    boolean bow = skeleton && aggressive && this.entity.getMainHandItem().is(Items.BOW);
    if (bow) {
      if (input.rightHanded) {
        input.rightArm = HumanoidAnimation.ArmPose.BOW_AND_ARROW;
      } else {
        input.leftArm = HumanoidAnimation.ArmPose.BOW_AND_ARROW;
      }
    }
    HumanoidPose pose = this.humanoid.evaluate(input);
    if (skeleton) {
      pose.rightLeg.x = -2.0F;
      pose.leftLeg.x = 2.0F;
      pose.rightLeg.z = 0.0F;
      pose.leftLeg.z = 0.0F;
    }
    if (this.entity instanceof Zombie || skeleton && aggressive && !bow) {
      float swing = Mth.sin(input.attack * (float) Math.PI);
      float reach =
          Mth.sin((1.0F - (1.0F - input.attack) * (1.0F - input.attack)) * (float) Math.PI);
      float pitch = -(float) Math.PI / (skeleton ? 2.0F : aggressive ? 1.5F : 2.25F);
      pitch += (skeleton ? -1.0F : 1.0F) * (swing * 1.2F - reach * 0.4F);
      pose.rightArm.yRot = -(0.1F - swing * 0.6F);
      pose.leftArm.yRot = 0.1F - swing * 0.6F;
      pose.rightArm.zRot = Mth.cos(input.age * 0.09F) * 0.05F + 0.05F;
      pose.leftArm.zRot = -pose.rightArm.zRot;
      float bob = Mth.sin(input.age * 0.067F) * 0.05F;
      pose.rightArm.xRot = pitch + bob;
      pose.leftArm.xRot = pitch - bob;
    }
    var bones = new HashMap<String, BoneTransform>();
    bones.put("head", this.part(pose.head));
    bones.put("hat", this.part(pose.head));
    bones.put("body", this.part(pose.body));
    bones.put("right_arm", this.part(pose.rightArm));
    bones.put("left_arm", this.part(pose.leftArm));
    bones.put("right_leg", this.part(pose.rightLeg));
    bones.put("left_leg", this.part(pose.leftLeg));
    if (this.entity.isBaby()) {
      bones.replaceAll(
          (name, bone) ->
              this.young(bone, name.equals("head") || name.equals("hat"), 16.0F, 0.0F, 0.75D));
    }
    return bones;
  }

  private BoneTransform part(HumanoidPose.Part part) {
    return this.bone(part.x, part.y, part.z, part.xRot, part.yRot, part.zRot);
  }

  private Map<String, BoneTransform> quadruped(HumanoidAnimation.Input input, float partialTick) {
    var bones = new HashMap<String, BoneTransform>();
    float pitch = input.headPitch * Mth.DEG_TO_RAD;
    float yaw = input.headYaw * Mth.DEG_TO_RAD;
    float phase = input.limbSwing * 0.6662F;
    float step = Mth.cos(phase) * 1.4F * input.limbAmount;
    float opposite = Mth.cos(phase + (float) Math.PI) * 1.4F * input.limbAmount;
    float x;
    float y;
    float hind;
    float front;
    float headOffset;
    switch (this.kind) {
      case "creeper" -> {
        bones.put("head", this.bone(0, 6, 0, pitch, yaw, 0));
        bones.put("body", this.bone(0, 6, 0, 0, 0, 0));
        // CreeperModel assigns the right and left fields to the opposite named mesh parts.
        this.legs(bones, 2, 18, 4, -4, opposite, step);
        return bones;
      }
      case "pig" -> {
        bones.put("head", this.bone(0, 12, -6, pitch, yaw, 0));
        bones.put("body", this.bone(0, 11, 2, (float) Math.PI / 2.0F, 0, 0));
        x = 3;
        y = 18;
        hind = 7;
        front = -5;
        headOffset = 4;
      }
      case "sheep" -> {
        Sheep sheep = (Sheep) this.entity;
        bones.put(
            "head",
            this.bone(
                0,
                6 + sheep.getHeadEatPositionScale(partialTick) * 9,
                -8,
                sheep.getHeadEatAngleScale(partialTick),
                yaw,
                0));
        bones.put("body", this.bone(0, 5, 2, (float) Math.PI / 2.0F, 0, 0));
        x = 3;
        y = 12;
        hind = 7;
        front = -5;
        headOffset = 8;
      }
      default -> {
        bones.put("head", this.bone(0, 4, -8, pitch, yaw, 0));
        bones.put("body", this.bone(0, 5, 2, (float) Math.PI / 2.0F, 0, 0));
        x = 4;
        y = 12;
        hind = 7;
        front = -6;
        headOffset = 10;
      }
    }
    this.legs(bones, x, y, hind, front, step, opposite);
    if (this.entity.isBaby()) {
      bones.replaceAll((name, bone) -> this.young(bone, name.equals("head"), headOffset, 4, 1.0D));
    }
    return bones;
  }

  private void legs(
      Map<String, BoneTransform> bones,
      float x,
      float y,
      float hind,
      float front,
      float step,
      float opposite) {
    bones.put("right_hind_leg", this.bone(-x, y, hind, step, 0, 0));
    bones.put("left_hind_leg", this.bone(x, y, hind, opposite, 0, 0));
    bones.put("right_front_leg", this.bone(-x, y, front, opposite, 0, 0));
    bones.put("left_front_leg", this.bone(x, y, front, step, 0, 0));
  }

  private Map<String, BoneTransform> chicken(HumanoidAnimation.Input input, float flap) {
    var bones = new HashMap<String, BoneTransform>();
    var head =
        this.bone(0, 15, -4, input.headPitch * Mth.DEG_TO_RAD, input.headYaw * Mth.DEG_TO_RAD, 0);
    bones.put("head", head);
    bones.put("beak", head);
    bones.put("red_thing", head);
    bones.put("body", this.bone(0, 16, 0, (float) Math.PI / 2.0F, 0, 0));
    float phase = input.limbSwing * 0.6662F;
    bones.put("right_leg", this.bone(-2, 19, 1, Mth.cos(phase) * 1.4F * input.limbAmount, 0, 0));
    bones.put(
        "left_leg",
        this.bone(1, 19, 1, Mth.cos(phase + (float) Math.PI) * 1.4F * input.limbAmount, 0, 0));
    bones.put("right_wing", this.bone(-4, 13, 0, 0, 0, flap));
    bones.put("left_wing", this.bone(4, 13, 0, 0, 0, -flap));
    if (this.entity.isBaby()) {
      bones.replaceAll(
          (name, bone) ->
              this.young(
                  bone,
                  name.equals("head") || name.equals("beak") || name.equals("red_thing"),
                  5,
                  2,
                  1.0D));
    }
    return bones;
  }

  private BoneTransform young(
      BoneTransform bone, boolean head, float y, float z, double headScale) {
    var parent =
        head
            ? new BoneTransform(
                new Vector3(0, y * headScale, z * headScale), Rotation.IDENTITY, headScale)
            : new BoneTransform(new Vector3(0, 12, 0), Rotation.IDENTITY, 0.5D);
    return parent.compose(bone);
  }

  private BoneTransform bone(float x, float y, float z, float pitch, float yaw, float roll) {
    return new BoneTransform(new Vector3(x, y, z), Rotation.euler(pitch, yaw, roll), 1.0D);
  }
}
