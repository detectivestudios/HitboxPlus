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

import com.detectivestudios.hitboxplus.api.EntityHitboxProviders;
import com.detectivestudios.hitboxplus.pose.PlayerHitboxProvider;
import com.detectivestudios.hitboxplus.pose.VanillaMobProvider;
import com.detectivestudios.hitboxplus.pose.vanilla.DragonModelDefinition;
import com.detectivestudios.hitboxplus.pose.vanilla.EnderDragonProvider;
import com.detectivestudios.hitboxplus.pose.vanilla.VanillaEntityProvider;
import com.detectivestudios.hitboxplus.pose.vanilla.VanillaRigs;
import com.detectivestudios.hitboxplus.profile.VanillaMobProfiles;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class BuiltinProviders {

  private final VanillaMobProfiles profiles = new VanillaMobProfiles();
  private final VanillaRigs rigs = new VanillaRigs();
  private final DragonModelDefinition dragon = new DragonModelDefinition();

  public void register(EntityHitboxProviders providers) {
    providers.register(EntityType.PLAYER, PlayerHitboxProvider::new);
    providers.register(
        EntityType.ENDER_DRAGON, entity -> new EnderDragonProvider(entity, this.dragon));
    this.livingEntity(providers, EntityType.ARMOR_STAND, "armor_stand");
    this.livingEntity(providers, EntityType.AXOLOTL, "axolotl");
    this.livingEntity(providers, EntityType.BAT, "bat");
    this.livingEntity(providers, EntityType.BEE, "bee");
    this.livingEntity(providers, EntityType.BLAZE, "blaze");
    this.livingEntity(providers, EntityType.CAT, "cat");
    this.livingEntity(providers, EntityType.CAVE_SPIDER, "cave_spider");
    this.livingEntity(providers, EntityType.CHICKEN, "chicken");
    this.livingEntity(providers, EntityType.COD, "cod");
    this.livingEntity(providers, EntityType.COW, "cow");
    this.livingEntity(providers, EntityType.CREEPER, "creeper");
    this.livingEntity(providers, EntityType.DOLPHIN, "dolphin");
    this.livingEntity(providers, EntityType.DONKEY, "donkey");
    this.livingEntity(providers, EntityType.DROWNED, "drowned");
    this.livingEntity(providers, EntityType.ELDER_GUARDIAN, "elder_guardian");
    this.livingEntity(providers, EntityType.ENDERMAN, "enderman");
    this.livingEntity(providers, EntityType.ENDERMITE, "endermite");
    this.livingEntity(providers, EntityType.EVOKER, "evoker");
    this.livingEntity(providers, EntityType.FOX, "fox");
    this.livingEntity(providers, EntityType.GHAST, "ghast");
    this.livingEntity(providers, EntityType.GIANT, "giant");
    this.livingEntity(providers, EntityType.GLOW_SQUID, "glow_squid");
    this.livingEntity(providers, EntityType.GOAT, "goat");
    this.livingEntity(providers, EntityType.GUARDIAN, "guardian");
    this.livingEntity(providers, EntityType.HOGLIN, "hoglin");
    this.livingEntity(providers, EntityType.HORSE, "horse");
    this.livingEntity(providers, EntityType.HUSK, "husk");
    this.livingEntity(providers, EntityType.ILLUSIONER, "illusioner");
    this.livingEntity(providers, EntityType.IRON_GOLEM, "iron_golem");
    this.livingEntity(providers, EntityType.LLAMA, "llama");
    this.livingEntity(providers, EntityType.MAGMA_CUBE, "magma_cube");
    this.livingEntity(providers, EntityType.MOOSHROOM, "mooshroom");
    this.livingEntity(providers, EntityType.MULE, "mule");
    this.livingEntity(providers, EntityType.OCELOT, "ocelot");
    this.livingEntity(providers, EntityType.PANDA, "panda");
    this.livingEntity(providers, EntityType.PARROT, "parrot");
    this.livingEntity(providers, EntityType.PHANTOM, "phantom");
    this.livingEntity(providers, EntityType.PIG, "pig");
    this.livingEntity(providers, EntityType.PIGLIN, "piglin");
    this.livingEntity(providers, EntityType.PIGLIN_BRUTE, "piglin_brute");
    this.livingEntity(providers, EntityType.PILLAGER, "pillager");
    this.livingEntity(providers, EntityType.POLAR_BEAR, "polar_bear");
    this.livingEntity(providers, EntityType.PUFFERFISH, "pufferfish");
    this.livingEntity(providers, EntityType.RABBIT, "rabbit");
    this.livingEntity(providers, EntityType.RAVAGER, "ravager");
    this.livingEntity(providers, EntityType.SALMON, "salmon");
    this.livingEntity(providers, EntityType.SHEEP, "sheep");
    this.livingEntity(providers, EntityType.SHULKER, "shulker");
    this.livingEntity(providers, EntityType.SILVERFISH, "silverfish");
    this.livingEntity(providers, EntityType.SKELETON, "skeleton");
    this.livingEntity(providers, EntityType.SKELETON_HORSE, "skeleton_horse");
    this.livingEntity(providers, EntityType.SLIME, "slime");
    this.livingEntity(providers, EntityType.SNOW_GOLEM, "snow_golem");
    this.livingEntity(providers, EntityType.SPIDER, "spider");
    this.livingEntity(providers, EntityType.SQUID, "squid");
    this.livingEntity(providers, EntityType.STRAY, "stray");
    this.livingEntity(providers, EntityType.STRIDER, "strider");
    this.livingEntity(providers, EntityType.TRADER_LLAMA, "trader_llama");
    this.livingEntity(providers, EntityType.TROPICAL_FISH, "tropical_fish");
    this.livingEntity(providers, EntityType.TURTLE, "turtle");
    this.livingEntity(providers, EntityType.VEX, "vex");
    this.livingEntity(providers, EntityType.VILLAGER, "villager");
    this.livingEntity(providers, EntityType.VINDICATOR, "vindicator");
    this.livingEntity(providers, EntityType.WANDERING_TRADER, "wandering_trader");
    this.livingEntity(providers, EntityType.WITCH, "witch");
    this.livingEntity(providers, EntityType.WITHER, "wither");
    this.livingEntity(providers, EntityType.WITHER_SKELETON, "wither_skeleton");
    this.livingEntity(providers, EntityType.WOLF, "wolf");
    this.livingEntity(providers, EntityType.ZOGLIN, "zoglin");
    this.livingEntity(providers, EntityType.ZOMBIE, "zombie");
    this.livingEntity(providers, EntityType.ZOMBIE_HORSE, "zombie_horse");
    this.livingEntity(providers, EntityType.ZOMBIE_VILLAGER, "zombie_villager");
    this.livingEntity(providers, EntityType.ZOMBIFIED_PIGLIN, "zombified_piglin");
  }

  private <T extends LivingEntity> void livingEntity(
      EntityHitboxProviders providers, EntityType<T> type, String name) {
    switch (name) {
      case "zombie",
          "husk",
          "giant",
          "skeleton",
          "stray",
          "wither_skeleton",
          "pig",
          "cow",
          "mooshroom",
          "sheep",
          "creeper",
          "chicken" ->
          providers.register(type, entity -> new VanillaMobProvider(entity, name, this.profiles));
      default ->
          providers.register(
              type, entity -> new VanillaEntityProvider(entity, name, this.profiles, this.rigs));
    }
  }
}
