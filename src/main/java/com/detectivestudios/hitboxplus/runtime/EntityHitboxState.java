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
import com.detectivestudios.hitboxplus.api.EntityHitboxProviders;
import com.detectivestudios.hitboxplus.api.HitboxPlus;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.pose.EntityBodyGeometry;
import com.detectivestudios.hitboxplus.pose.geckolib.GeckoLibPoseAdapter;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityHitboxState implements ICapabilityProvider {

  private final LivingEntity entity;
  private final EntityHitboxProviders providers;
  private final String entityId;
  private final LazyOptional<EntityHitboxProvider> capability = LazyOptional.of(() -> this::pose);
  private int revision = -1;
  private Optional<EntityHitboxProvider> provider = Optional.empty();
  private final EntityBodyGeometry geometry = new EntityBodyGeometry();
  private int profileRevision = -1;
  private Optional<EntityHitboxProvider> rigProvider = Optional.empty();
  private VanillaPoseSnapshot snapshot;
  private Optional<HitboxPose> cachedPose = Optional.empty();

  public EntityHitboxState(LivingEntity entity, EntityHitboxProviders providers) {
    this.entity = entity;
    this.providers = providers;
    this.entityId =
        Objects.requireNonNull(entity.getType().getRegistryName(), "Entity type must be registered")
            .toString();
  }

  private Optional<HitboxPose> pose(float partialTick) {
    var overrides = this.providers.profiles(this.entity.level.isClientSide());
    if (this.profileRevision != overrides.revision()
        || this.revision != this.providers.revision()) {
      this.rigProvider =
          overrides
              .baseDocument(this.entityId)
              .filter(document -> document.rig().isPresent())
              .map(
                  document ->
                      new GeckoLibPoseAdapter(
                          this.entity,
                          document,
                          this.providers.geckoLibSource(this.entity.getType()).orElse(null)));
      this.profileRevision = overrides.revision();
      this.cachedPose = Optional.empty();
    }
    if (this.revision != this.providers.revision()) {
      this.provider = this.providers.create(this.entity);
      this.snapshot =
          this.provider
              .filter(VanillaPoseSnapshot::supports)
              .map(value -> new VanillaPoseSnapshot(this.entity))
              .orElse(null);
      this.cachedPose = Optional.empty();
      this.revision = this.providers.revision();
    }
    if (this.rigProvider.isPresent()) {
      return this.rigProvider.orElseThrow().getPose(partialTick);
    }
    if (this.snapshot != null
        && this.snapshot.unchanged(this.entity, partialTick)
        && this.cachedPose.isPresent()) {
      return this.cachedPose;
    }
    var result =
        this.provider.flatMap(
            value -> {
              var profiles = this.providers.profiles(this.entity.level.isClientSide());
              String id = this.entityId;
              if (profiles.hasEntity(id)) {
                var model = value.getModelPose(partialTick);
                if (model.isPresent()) {
                  var current = model.orElseThrow();
                  var profile = profiles.find(id, current.variant());
                  if (profile.isPresent()
                      && profile.orElseThrow().volumes().stream()
                          .allMatch(volume -> current.bones().containsKey(volume.bone()))) {
                    return Optional.of(
                        this.geometry.create(
                            profile.orElseThrow(), current.bones()::get, current.root()));
                  }
                  if (profile.isPresent()) {
                    profiles.warnOnce(
                        id, "the profile references bones missing from the pose provider");
                  }
                } else {
                  profiles.warnOnce(
                      id, "the current provider/state does not expose an animated skeleton");
                }
              }
              return value.getPose(partialTick);
            });
    if (this.snapshot != null) {
      this.cachedPose = result;
    }
    return result;
  }

  public void invalidate() {
    this.snapshot = null;
    this.cachedPose = Optional.empty();
    this.provider = Optional.empty();
    this.rigProvider = Optional.empty();
    this.capability.invalidate();
  }

  @Override
  public <T> @NotNull LazyOptional<T> getCapability(
      @NotNull Capability<T> capability, @Nullable Direction side) {
    return capability == HitboxPlus.CAPABILITY ? this.capability.cast() : LazyOptional.empty();
  }
}
