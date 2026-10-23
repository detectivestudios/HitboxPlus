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

package com.detectivestudios.hitboxplus.pose;

import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;

/** Delay skin lookup until the player profile has initialized. */
public class PlayerAppearance {

  private final Player player;
  private Boolean slim;

  public PlayerAppearance(Player player) {
    this.player = player;
  }

  public boolean slim() {
    if (this.slim == null) {
      this.slim = this.resolveSlim();
    }
    return this.slim;
  }

  public PlayerBodyGeometry.Layers layers() {
    var entity = this.player;
    return new PlayerBodyGeometry.Layers(
        entity.isModelPartShown(PlayerModelPart.HAT),
        entity.isModelPartShown(PlayerModelPart.JACKET),
        entity.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE),
        entity.isModelPartShown(PlayerModelPart.LEFT_SLEEVE),
        entity.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG),
        entity.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG));
  }

  private boolean resolveSlim() {
    var profile = this.player.getGameProfile();
    for (var property : profile.getProperties().get("textures")) {
      try {
        String json =
            new String(Base64.getDecoder().decode(property.getValue()), StandardCharsets.UTF_8);
        var textures = JsonParser.parseString(json).getAsJsonObject().getAsJsonObject("textures");
        if (textures != null && textures.has("SKIN")) {
          var skin = textures.getAsJsonObject("SKIN");
          var metadata = skin.getAsJsonObject("metadata");
          return metadata != null
              && metadata.has("model")
              && "slim".equals(metadata.get("model").getAsString());
        }
      } catch (RuntimeException ignored) {
        // Invalid or missing cosmetic profile metadata falls back to the UUID's default skin.
      }
    }
    return (profile.getId().hashCode() & 1) == 1;
  }
}
