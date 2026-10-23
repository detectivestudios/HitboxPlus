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

package com.detectivestudios.hitboxplus.runtime;

import com.detectivestudios.hitboxplus.HitboxPlusMod;
import com.detectivestudios.hitboxplus.api.EntityHitboxProviders;
import com.detectivestudios.hitboxplus.profile.VanillaProfileKey;
import com.detectivestudios.hitboxplus.profile.editor.EditorProfileCodec;
import com.detectivestudios.hitboxplus.profile.editor.EditorProfileDocument;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class ProfileReloadListener extends SimpleJsonResourceReloadListener {

  private static final Logger logger = LogUtils.getLogger();
  private final ProfileOverrides profiles;
  private final EntityHitboxProviders providers;
  private final EditorProfileCodec codec = new EditorProfileCodec();

  public ProfileReloadListener(EntityHitboxProviders providers) {
    super(new Gson(), HitboxPlusMod.ID + "/profiles");
    this.providers = providers;
    this.profiles = providers.profiles(false);
  }

  @Override
  protected void apply(
      @NotNull Map<ResourceLocation, JsonElement> resources,
      @NotNull ResourceManager manager,
      @NotNull ProfilerFiller profiler) {
    var loaded = new LinkedHashMap<VanillaProfileKey, EditorProfileDocument>();
    resources.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(
            entry -> {
              try {
                var document = this.codec.decode(entry.getValue());
                var entityId = new ResourceLocation(document.key().entityId());
                var type = ForgeRegistries.ENTITIES.getValue(entityId);
                if (!ForgeRegistries.ENTITIES.containsKey(entityId)) {
                  logger.warn("Hitbox+ profile targets an unknown entity: {}", entityId);
                } else if (!this.providers.supports(type) && document.rig().isEmpty()) {
                  logger.warn(
                      "Hitbox+ profile {} needs an entity pose provider before it can"
                          + " activate",
                      document.key());
                }
                if (loaded.putIfAbsent(document.key(), document) != null) {
                  logger.error(
                      "Duplicate Hitbox+ profile {} in {}", document.key(), entry.getKey());
                }
              } catch (RuntimeException exception) {
                logger.error(
                    "Invalid Hitbox+ profile {}: {}", entry.getKey(), exception.getMessage());
              }
            });
    try {
      this.profiles.replace(loaded.values());
      logger.info("Loaded {} Hitbox+ geometry overrides", loaded.size());
    } catch (IllegalArgumentException exception) {
      logger.error("Keeping previous Hitbox+ profiles: {}", exception.getMessage());
    }
  }
}
