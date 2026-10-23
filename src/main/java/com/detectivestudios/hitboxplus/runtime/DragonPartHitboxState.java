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

import com.detectivestudios.hitboxplus.api.EntityHitboxProvider;
import com.detectivestudios.hitboxplus.api.HitboxPlus;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.pose.vanilla.EnderDragonProvider;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DragonPartHitboxState implements ICapabilityProvider {

  private final EnderDragonPart part;
  private final LazyOptional<EntityHitboxProvider> capability = LazyOptional.of(() -> this::pose);
  private HitboxPose source;
  private Optional<HitboxPose> cached = Optional.empty();

  public DragonPartHitboxState(EnderDragonPart part) {
    this.part = part;
  }

  private Optional<HitboxPose> pose(float partialTick) {
    var pose = HitboxPlus.getPose(this.part.parentMob, partialTick).orElse(null);
    if (this.source != pose) {
      this.source = pose;
      this.cached =
          pose == null
              ? Optional.empty()
              : Optional.of(EnderDragonProvider.partPose(this.part, pose));
    }
    return this.cached;
  }

  public void invalidate() {
    this.source = null;
    this.cached = Optional.empty();
    this.capability.invalidate();
  }

  @Override
  public <T> @NotNull LazyOptional<T> getCapability(
      @NotNull Capability<T> capability, @Nullable Direction side) {
    return capability == HitboxPlus.CAPABILITY ? this.capability.cast() : LazyOptional.empty();
  }
}
