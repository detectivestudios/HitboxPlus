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

import java.util.Objects;

/** Extensible identity for additional anatomy, equipment, machines, or other model regions. */
public record NamedHitboxPart(String id, BodyPart region) implements HitboxPart {

  public NamedHitboxPart {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(region, "region");
    if (!id.matches("[a-z0-9_.-]+:[a-z0-9/._-]+")) {
      throw new IllegalArgumentException("Part ID must have a lowercase namespace:path");
    }
  }
}
