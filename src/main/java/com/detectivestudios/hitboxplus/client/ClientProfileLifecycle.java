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

package com.detectivestudios.hitboxplus.client;

import com.detectivestudios.hitboxplus.runtime.ProfileOverrides;
import java.util.List;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientProfileLifecycle {

  private final ProfileOverrides profiles;

  public ClientProfileLifecycle(ProfileOverrides profiles) {
    this.profiles = profiles;
  }

  public void register() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void disconnected(ClientPlayerNetworkEvent.LoggedOutEvent event) {
    this.profiles.replace(List.of());
  }
}
