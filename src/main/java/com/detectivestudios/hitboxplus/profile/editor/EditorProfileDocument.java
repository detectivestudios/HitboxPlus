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

package com.detectivestudios.hitboxplus.profile.editor;

import com.detectivestudios.hitboxplus.pose.geckolib.GeckoLibRig;
import com.detectivestudios.hitboxplus.profile.EntityHitboxProfile;
import com.detectivestudios.hitboxplus.profile.VanillaProfileKey;
import java.util.Optional;

public record EditorProfileDocument(
    VanillaProfileKey key,
    EntityHitboxProfile<String> profile,
    String json,
    Optional<GeckoLibRig> rig) {

  public EditorProfileDocument(
      VanillaProfileKey key, EntityHitboxProfile<String> profile, String json) {
    this(key, profile, json, Optional.empty());
  }
}
