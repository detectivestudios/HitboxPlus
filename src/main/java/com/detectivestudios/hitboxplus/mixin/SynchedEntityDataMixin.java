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

import com.detectivestudios.hitboxplus.runtime.SyncedDataRevision;
import java.util.List;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SynchedEntityData.class)
public class SynchedEntityDataMixin implements SyncedDataRevision {

  @Unique private long hitboxplus$revision;

  @Override
  public long hitboxplus$revision() {
    return this.hitboxplus$revision;
  }

  @Inject(method = "set", at = {@At("HEAD"), @At("TAIL")})
  private <T> void changed(EntityDataAccessor<T> accessor, T value, CallbackInfo callback) {
    this.hitboxplus$revision++;
  }

  @Inject(method = "assignValues", at = {@At("HEAD"), @At("TAIL")})
  private void received(List<SynchedEntityData.DataItem<?>> values, CallbackInfo callback) {
    this.hitboxplus$revision++;
  }
}
