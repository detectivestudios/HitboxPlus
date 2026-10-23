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

package com.detectivestudios.hitboxplus.api;

import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.trace.HitboxTracer;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/** Shared combat geometry entry point for gun mods, targeting and debug rendering. */
public class HitboxPlus {

  public static final Capability<EntityHitboxProvider> CAPABILITY =
      CapabilityManager.get(new CapabilityToken<>() {});
  private static final HitboxTracer TRACER = new HitboxTracer();

  public static Optional<HitboxPose> getPose(Entity entity, float partialTick) {
    return entity
        .getCapability(CAPABILITY)
        .resolve()
        .flatMap(provider -> provider.getPose(partialTick));
  }

  public static Optional<BodyHit> trace(Entity entity, Vec3 start, Vec3 end, float partialTick) {
    return getPose(entity, partialTick)
        .flatMap(
            pose ->
                TRACER.trace(
                    pose,
                    new Vector3(start.x, start.y, start.z),
                    new Vector3(end.x, end.y, end.z),
                    0));
  }
}
