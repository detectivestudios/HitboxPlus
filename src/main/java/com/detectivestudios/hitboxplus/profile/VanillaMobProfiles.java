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

package com.detectivestudios.hitboxplus.profile;

import com.detectivestudios.hitboxplus.profile.vanilla.v118.VanillaProfiles;
import java.util.Map;
import java.util.Optional;

/** Geometry definitions only; providers supply animation and world transforms. */
public class VanillaMobProfiles {

  private final Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles =
      new VanillaProfiles().create();

  public Map<VanillaProfileKey, EntityHitboxProfile<String>> profiles() {
    return this.profiles;
  }

  public Optional<EntityHitboxProfile<String>> find(String entityId) {
    return this.find(entityId, "base");
  }

  public Optional<EntityHitboxProfile<String>> find(String entityId, String variant) {
    return Optional.ofNullable(this.profiles.get(new VanillaProfileKey(entityId, variant)));
  }

  public EntityHitboxProfile<String> zombie() {
    return this.find("minecraft:zombie").orElseThrow();
  }

  public EntityHitboxProfile<String> pig() {
    return this.find("minecraft:pig").orElseThrow();
  }

  public EntityHitboxProfile<String> cow() {
    return this.find("minecraft:cow").orElseThrow();
  }
}
