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

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

/** Fired on each mod bus during common setup, after the built-in providers are registered. */
public class RegisterHitboxProvidersEvent extends Event implements IModBusEvent {

  private final EntityHitboxProviders providers;

  public RegisterHitboxProvidersEvent(EntityHitboxProviders providers) {
    this.providers = providers;
  }

  public EntityHitboxProviders providers() {
    return this.providers;
  }
}
