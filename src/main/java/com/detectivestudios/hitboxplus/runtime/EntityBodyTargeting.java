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

package com.detectivestudios.hitboxplus.runtime;

import com.detectivestudios.hitboxplus.api.BodyEntityHitResult;
import com.detectivestudios.hitboxplus.api.BodyHit;
import com.detectivestudios.hitboxplus.api.HitboxPlus;
import com.detectivestudios.hitboxplus.client.HitboxClient;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.trace.HitboxTracer;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

public class EntityBodyTargeting {

  private final HitboxTracer tracer = new HitboxTracer();

  @Nullable
  public EntityHitResult trace(
      Level level,
      @Nullable Entity from,
      Vec3 start,
      Vec3 end,
      AABB search,
      Predicate<Entity> predicate,
      double maximumDistanceSqr,
      boolean checkVehicle,
      float nonPlayerInflation) {
    var candidates = level.getEntities(from, search.inflate(4.0D), predicate);
    EntityHitResult nearest = null;
    double distance = maximumDistanceSqr;
    float partialTick =
        level.isClientSide()
            ? DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> HitboxClient::partialTick)
            : 1.0F;
    var rayStart = new Vector3(start.x, start.y, start.z);
    var rayEnd = new Vector3(end.x, end.y, end.z);
    for (Entity candidate : candidates) {
      Optional<Vec3> contact;
      BodyHit bodyHit = null;
      var pose = HitboxPlus.getPose(candidate, partialTick);
      if (pose.isPresent()) {
        var hit = this.tracer.trace(pose.orElseThrow(), rayStart, rayEnd, 0.0D);
        bodyHit = hit.orElse(null);
        contact =
            hit.map(
                result ->
                    new Vec3(result.position().x(), result.position().y(), result.position().z()));
      } else {
        double padding = checkVehicle ? candidate.getPickRadius() : nonPlayerInflation;
        AABB box = candidate.getBoundingBox().inflate(padding);
        contact = box.contains(start) ? Optional.of(start) : box.clip(start, end);
      }
      if (contact.isEmpty()) {
        continue;
      }
      Vec3 point = contact.orElseThrow();
      double squared = start.distanceToSqr(point);
      if (squared > 0.0D
          && checkVehicle
          && from != null
          && candidate.getRootVehicle() == from.getRootVehicle()
          && !candidate.canRiderInteract()) {
        continue;
      }
      if (squared < distance || (squared == 0.0D && distance >= 0.0D)) {
        nearest =
            bodyHit == null
                ? new EntityHitResult(candidate, point)
                : new BodyEntityHitResult(candidate, bodyHit);
        distance = squared;
      }
    }
    return nearest;
  }
}
