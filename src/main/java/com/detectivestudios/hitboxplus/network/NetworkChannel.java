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

package com.detectivestudios.hitboxplus.network;

import com.detectivestudios.hitboxplus.HitboxPlusMod;
import com.detectivestudios.hitboxplus.network.message.play.SyncProfilesMessage;
import com.detectivestudios.hitboxplus.runtime.ProfileOverrides;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.simple.SimpleChannel;

public enum NetworkChannel {

  PLAY(new ResourceLocation(HitboxPlusMod.ID, "profiles")) {

    @Override
    protected void registerMessages(SimpleChannel simpleChannel, ProfileOverrides clientProfiles) {
      simpleChannel
          .messageBuilder(SyncProfilesMessage.class, 0x00, NetworkDirection.PLAY_TO_CLIENT)
          .encoder(SyncProfilesMessage::encode)
          .decoder(SyncProfilesMessage::decode)
          .consumer(
              (message, context) -> {
                message.handle(context, clientProfiles);
              })
          .add();
    }
  };

  private static final String NETWORK_VERSION = "2";
  private static boolean loaded;
  private final SimpleChannel simpleChannel;

  private NetworkChannel(ResourceLocation channelName) {
    this.simpleChannel =
        ChannelBuilder.named(channelName)
            .clientAcceptedVersions(NETWORK_VERSION::equals)
            .serverAcceptedVersions(NETWORK_VERSION::equals)
            .networkProtocolVersion(() -> NETWORK_VERSION)
            .simpleChannel();
  }

  protected abstract void registerMessages(
      SimpleChannel simpleChannel, ProfileOverrides clientProfiles);

  public SimpleChannel getSimpleChannel() {
    return this.simpleChannel;
  }

  public static void loadChannels(ProfileOverrides clientProfiles) {
    if (!loaded) {
      for (var channel : values()) {
        channel.registerMessages(channel.simpleChannel, clientProfiles);
      }
      loaded = true;
    }
  }
}
