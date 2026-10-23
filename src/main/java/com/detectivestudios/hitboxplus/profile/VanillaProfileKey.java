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

import java.util.Objects;

public record VanillaProfileKey(String entityId, String variant) {

  public VanillaProfileKey {
    Objects.requireNonNull(entityId, "entityId");
    Objects.requireNonNull(variant, "variant");
    if (!entityId.matches("[a-z0-9_.-]+:[a-z0-9/._-]+") || !variant.matches("[a-z0-9_.-]+")) {
      throw new IllegalArgumentException("Invalid entity ID or profile variant");
    }
  }
}
