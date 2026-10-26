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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/** Native projectile contact retaining the body part, normal and segment fraction. */
public class BodyEntityHitResult extends EntityHitResult {

  private final BodyHit bodyHit;

  public BodyEntityHitResult(Entity entity, BodyHit bodyHit) {
    super(entity, new Vec3(bodyHit.position().x(), bodyHit.position().y(), bodyHit.position().z()));
    this.bodyHit = Objects.requireNonNull(bodyHit, "bodyHit");
  }

  public BodyHit getBodyHit() {
    return this.bodyHit;
  }
}
