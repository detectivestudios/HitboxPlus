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

import com.detectivestudios.hitboxplus.geometry.OrientedBox;
import java.util.Objects;

public record BodyHitbox(HitboxPart part, OrientedBox box) {

  public BodyHitbox {
    Objects.requireNonNull(part, "part");
    Objects.requireNonNull(box, "box");
  }
}
