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

package com.detectivestudios.hitboxplus.mixin;

import com.detectivestudios.hitboxplus.client.EntityHitboxDebugRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

  @Unique
  private static final EntityHitboxDebugRenderer hitboxplus$debug = new EntityHitboxDebugRenderer();

  @Inject(method = "renderHitbox", at = @At("HEAD"), cancellable = true)
  private static void renderBody(PoseStack stack, VertexConsumer consumer, Entity entity, float partialTick, CallbackInfo callback) {
    if (hitboxplus$debug.render(stack, consumer, entity, partialTick)) {
      callback.cancel();
    }
  }
}
