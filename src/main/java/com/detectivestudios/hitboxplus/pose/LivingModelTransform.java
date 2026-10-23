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

import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.phys.Vec3;

public class LivingModelTransform {

  private final double modelScale;

  private final LivingEntity entity;

  public LivingModelTransform(LivingEntity entity, double modelScale) {
    this.entity = entity;
    this.modelScale = modelScale;
  }

  public float bodyYaw(float partialTick) {
    var entity = this.entity;
    float yaw = Mth.rotLerp(partialTick, entity.yBodyRotO, entity.yBodyRot);
    if (entity.isPassenger()
        && entity.getVehicle() instanceof LivingEntity vehicle
        && vehicle.shouldRiderSit()) {
      float head = Mth.rotLerp(partialTick, entity.yHeadRotO, entity.yHeadRot);
      yaw = Mth.rotLerp(partialTick, vehicle.yBodyRotO, vehicle.yBodyRot);
      float difference = Mth.clamp(Mth.wrapDegrees(head - yaw), -85.0F, 85.0F);
      yaw = head - difference + (difference * difference > 2500.0F ? difference * 0.2F : 0.0F);
    }
    return yaw;
  }

  public Root root(float partialTick, float bodyYaw) {
    var entity = this.entity;
    Vec3 position = entity.getPosition(partialTick);
    var origin =
        new Vector3(
            position.x,
            position.y - (entity instanceof Player && entity.isCrouching() ? 0.125D : 0.0D),
            position.z);
    boolean shaking =
        entity instanceof AbstractSkeleton skeleton
            ? skeleton.isShaking()
            : entity.isFullyFrozen()
                || entity instanceof Zombie zombie && zombie.isUnderWaterConverting();
    if (shaking) {
      bodyYaw += (float) (Math.cos(entity.tickCount * 3.25D) * Math.PI * 0.4D);
    }
    Rotation rotation = Rotation.euler(0.0D, Math.toRadians(180.0D - bodyYaw), 0.0D);
    if (entity.deathTime > 0) {
      float roll =
          Mth.clamp(Mth.sqrt((entity.deathTime + partialTick - 1.0F) / 20.0F * 1.6F), 0.0F, 1.0F);
      rotation = rotation.multiply(Rotation.euler(0.0D, 0.0D, roll * Math.PI * 0.5D));
    } else if (entity.isAutoSpinAttack()) {
      rotation =
          rotation
              .multiply(Rotation.euler(Math.toRadians(-90.0D - entity.getXRot()), 0.0D, 0.0D))
              .multiply(
                  Rotation.euler(
                      0.0D, Math.toRadians((entity.tickCount + partialTick) * -75.0D), 0.0D));
    } else if (entity.getPose() == Pose.SLEEPING) {
      Direction bed = entity.getBedOrientation();
      float yaw =
          bed == null
              ? bodyYaw
              : switch (bed) {
                case SOUTH -> 90.0F;
                case WEST -> 0.0F;
                case NORTH -> 270.0F;
                default -> 180.0F;
              };
      if (bed != null) {
        double shift = entity.getEyeHeight(Pose.STANDING) - 0.1D;
        origin = origin.add(new Vector3(-bed.getStepX() * shift, 0.0D, -bed.getStepZ() * shift));
      }
      rotation =
          Rotation.euler(0.0D, Math.toRadians(yaw), 0.0D)
              .multiply(Rotation.euler(0.0D, 0.0D, Math.PI * 0.5D))
              .multiply(Rotation.euler(0.0D, Math.PI * 1.5D, 0.0D));
    } else if (this.upsideDown()) {
      origin = origin.add(new Vector3(0.0D, entity.getBbHeight() + 0.1D, 0.0D));
      rotation = rotation.multiply(Rotation.euler(0.0D, 0.0D, Math.PI));
    }
    if (entity.isFallFlying()) {
      if (!entity.isAutoSpinAttack()) {
        float time = entity.getFallFlyingTicks() + partialTick;
        double weight = Mth.clamp(time * time / 100.0F, 0.0F, 1.0F);
        rotation =
            rotation.multiply(
                Rotation.euler(Math.toRadians(weight * (-90.0D - entity.getXRot())), 0.0D, 0.0D));
      }
      Vec3 view = entity.getViewVector(partialTick);
      Vec3 velocity = entity.getDeltaMovement();
      double length = Math.sqrt(view.horizontalDistanceSqr() * velocity.horizontalDistanceSqr());
      if (length > 0.0D) {
        double dot = Mth.clamp((velocity.x * view.x + velocity.z * view.z) / length, -1.0D, 1.0D);
        double cross = velocity.x * view.z - velocity.z * view.x;
        rotation =
            rotation.multiply(Rotation.euler(0.0D, Math.signum(cross) * Math.acos(dot), 0.0D));
      }
    } else if (entity instanceof Player && entity.getSwimAmount(partialTick) > 0.0F) {
      double pitch = entity.isInWater() ? -90.0D - entity.getXRot() : -90.0D;
      rotation =
          rotation.multiply(
              Rotation.euler(
                  Math.toRadians(entity.getSwimAmount(partialTick) * pitch), 0.0D, 0.0D));
      if (entity.isVisuallySwimming()) {
        origin = origin.add(rotation.transform(new Vector3(0.0D, -1.0D, 0.3D)));
      }
    }
    rotation = rotation.multiply(Rotation.euler(0.0D, 0.0D, Math.PI));
    return new Root(
        origin.add(rotation.transform(new Vector3(0.0D, -1.501D * this.modelScale, 0.0D))),
        rotation);
  }

  public boolean upsideDown() {
    String name = ChatFormatting.stripFormatting(this.entity.getName().getString());
    return ("Dinnerbone".equals(name) || "Grumm".equals(name))
        && (!(this.entity instanceof Player player)
            || player.isModelPartShown(PlayerModelPart.CAPE));
  }

  public record Root(Vector3 origin, Rotation rotation) {}
}
