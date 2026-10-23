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

import com.detectivestudios.hitboxplus.pose.geckolib.GeckoLibAnimationSource;
import com.detectivestudios.hitboxplus.runtime.ProfileOverrides;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

/** Mod-owned registration. Addon overrides are installed during RegisterHitboxProvidersEvent. */
public class EntityHitboxProviders {

  private final Map<EntityType<?>, Function<LivingEntity, EntityHitboxProvider>> factories =
      new HashMap<>();
  private int revision;
  private final Map<EntityType<?>, GeckoLibAnimationSource> animationSources = new HashMap<>();
  private final ProfileOverrides serverProfiles = new ProfileOverrides();
  private final ProfileOverrides clientProfiles = new ProfileOverrides();

  public ProfileOverrides profiles(boolean clientSide) {
    return clientSide ? this.clientProfiles : this.serverProfiles;
  }

  @SuppressWarnings("unchecked")
  public <T extends LivingEntity> void register(
      EntityType<T> type, Function<T, EntityHitboxProvider> factory) {
    this.factories.put(type, entity -> factory.apply((T) entity));
    this.revision++;
  }

  public Optional<EntityHitboxProvider> create(LivingEntity entity) {
    var factory = this.factories.get(entity.getType());
    return factory == null ? Optional.empty() : Optional.of(factory.apply(entity));
  }

  public boolean supports(EntityType<?> type) {
    return this.factories.containsKey(type);
  }

  /** Overrides the automatic clip selection/root for this entity type's exported rig. */
  public void registerGeckoLib(EntityType<?> type, GeckoLibAnimationSource source) {
    this.animationSources.put(type, source);
    this.revision++;
  }

  public Optional<GeckoLibAnimationSource> geckoLibSource(EntityType<?> type) {
    return Optional.ofNullable(this.animationSources.get(type));
  }

  public int revision() {
    return this.revision;
  }
}
