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

package com.detectivestudios.hitboxplus.mixin;

import com.detectivestudios.hitboxplus.runtime.EntityBodyTargeting;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {

  @Unique
  private static final EntityBodyTargeting hitboxplus$bodyTargeting = new EntityBodyTargeting();

  @Inject(method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;" + "Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;" + "Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)" + "Lnet/minecraft/world/phys/EntityHitResult;", at = @At("HEAD"), cancellable = true)
  private static void pickBody(Entity from, Vec3 start, Vec3 end, AABB search, Predicate<Entity> predicate, double distance, CallbackInfoReturnable<EntityHitResult> callback) {
    callback.setReturnValue(
        hitboxplus$bodyTargeting.trace(
            from.level, from, start, end, search, predicate, distance, true, 0.0F));
  }

  @Inject(method = "getEntityHitResult(Lnet/minecraft/world/level/Level;" + "Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;" + "Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;" + "Ljava/util/function/Predicate;F)Lnet/minecraft/world/phys/EntityHitResult;", at = @At("HEAD"), cancellable = true)
  private static void hitBody(Level level, Entity from, Vec3 start, Vec3 end, AABB search, Predicate<Entity> predicate, float inflation, CallbackInfoReturnable<EntityHitResult> callback) {
    callback.setReturnValue(
        hitboxplus$bodyTargeting.trace(
            level, from, start, end, search, predicate, Double.MAX_VALUE, false, inflation));
  }
}
