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

package com.detectivestudios.hitboxplus.network.message.play;

import com.detectivestudios.hitboxplus.profile.editor.EditorProfileCodec;
import com.detectivestudios.hitboxplus.profile.editor.EditorProfileDocument;
import com.detectivestudios.hitboxplus.runtime.ProfileOverrides;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SyncProfilesMessage(List<EditorProfileDocument> profiles) {

  public SyncProfilesMessage {
    profiles = List.copyOf(profiles);
  }

  public void encode(FriendlyByteBuf buffer) {
    buffer.writeVarInt(this.profiles.size());
    for (var profile : this.profiles) {
      buffer.writeUtf(profile.json(), EditorProfileCodec.MAX_DOCUMENT_BYTES);
    }
  }

  public static SyncProfilesMessage decode(FriendlyByteBuf buffer) {
    int count = buffer.readVarInt();
    if (count < 0 || count > ProfileOverrides.MAX_PROFILES) {
      throw new IllegalArgumentException("Invalid hitbox profile count");
    }
    var profiles = new ArrayList<EditorProfileDocument>();
    var codec = new EditorProfileCodec();
    int bytes = 0;
    for (int index = 0; index < count; index++) {
      String json = buffer.readUtf(EditorProfileCodec.MAX_DOCUMENT_BYTES);
      bytes += json.getBytes(StandardCharsets.UTF_8).length;
      if (bytes > ProfileOverrides.MAX_SYNC_BYTES) {
        throw new IllegalArgumentException("Hitbox profile packet exceeds 1 MiB");
      }
      profiles.add(codec.decode(json));
    }
    var validation = new ProfileOverrides();
    validation.replace(profiles);
    return new SyncProfilesMessage(profiles);
  }

  public void handle(
      Supplier<NetworkEvent.Context> contextSupplier, ProfileOverrides clientProfiles) {
    var context = contextSupplier.get();
    context.enqueueWork(() -> clientProfiles.replace(this.profiles));
    context.setPacketHandled(true);
  }
}
