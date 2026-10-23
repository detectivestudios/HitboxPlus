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

package com.detectivestudios.hitboxplus;

import com.detectivestudios.hitboxplus.api.EntityHitboxProvider;
import com.detectivestudios.hitboxplus.api.EntityHitboxProviders;
import com.detectivestudios.hitboxplus.api.RegisterHitboxProvidersEvent;
import com.detectivestudios.hitboxplus.client.ClientProfileLifecycle;
import com.detectivestudios.hitboxplus.network.NetworkChannel;
import com.detectivestudios.hitboxplus.network.message.play.SyncProfilesMessage;
import com.detectivestudios.hitboxplus.runtime.BuiltinProviders;
import com.detectivestudios.hitboxplus.runtime.DragonPartHitboxState;
import com.detectivestudios.hitboxplus.runtime.EntityHitboxState;
import com.detectivestudios.hitboxplus.runtime.ProfileReloadListener;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;

@Mod(HitboxPlusMod.ID)
public class HitboxPlusMod {

  public static final String ID = "hitboxplus";
  private final EntityHitboxProviders providers = new EntityHitboxProviders();

  public HitboxPlusMod() {
    NetworkChannel.loadChannels(this.providers.profiles(true));
    new BuiltinProviders().register(this.providers);
    var bus = FMLJavaModLoadingContext.get().getModEventBus();
    bus.addListener(this::setup);
    bus.addListener(this::registerCapabilities);
    MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, this::attach);
    MinecraftForge.EVENT_BUS.register(this);
    DistExecutor.unsafeRunWhenOn(
        Dist.CLIENT,
        () -> () -> new ClientProfileLifecycle(this.providers.profiles(true)).register());
  }

  @SubscribeEvent
  public void addReloadListener(AddReloadListenerEvent event) {
    event.addListener(new ProfileReloadListener(this.providers));
  }

  @SubscribeEvent
  public void synchronizeProfiles(OnDatapackSyncEvent event) {
    var player = event.getPlayer();
    var target =
        player == null
            ? PacketDistributor.ALL.noArg()
            : PacketDistributor.PLAYER.with(() -> player);
    NetworkChannel.PLAY
        .getSimpleChannel()
        .send(target, new SyncProfilesMessage(this.providers.profiles(false).documents()));
  }

  @SubscribeEvent
  public void stopped(ServerStoppedEvent event) {
    this.providers.profiles(false).replace(List.of());
  }

  private void setup(FMLCommonSetupEvent event) {
    event.enqueueWork(
        () -> {
          // The gameplay bus remains shut down during mod setup; registration uses the mod buses.
          ModLoader.get().postEvent(new RegisterHitboxProvidersEvent(this.providers));
        });
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.register(EntityHitboxProvider.class);
  }

  private void attach(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof LivingEntity living) {
      var state = new EntityHitboxState(living, this.providers);
      event.addCapability(new ResourceLocation(ID, "body"), state);
      event.addListener(state::invalidate);
    } else if (event.getObject() instanceof EnderDragonPart part) {
      var state = new DragonPartHitboxState(part);
      event.addCapability(new ResourceLocation(ID, "body"), state);
      event.addListener(state::invalidate);
    }
  }
}
