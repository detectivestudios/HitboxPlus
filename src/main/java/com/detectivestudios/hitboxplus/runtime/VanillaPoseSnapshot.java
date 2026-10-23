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

import com.detectivestudios.hitboxplus.api.EntityHitboxProvider;
import com.detectivestudios.hitboxplus.pose.PlayerHitboxProvider;
import com.detectivestudios.hitboxplus.pose.VanillaMobProvider;
import com.detectivestudios.hitboxplus.pose.vanilla.EnderDragonProvider;
import com.detectivestudios.hitboxplus.pose.vanilla.VanillaEntityProvider;
import java.lang.ref.WeakReference;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class VanillaPoseSnapshot {

  private final long[] values;
  private int index;
  private int count = -1;
  private boolean same;
  private WeakReference<Entity> vehicle;
  private Item main;
  private Item off;

  public VanillaPoseSnapshot(LivingEntity entity) {
    this.values = new long[entity instanceof EnderDragon ? 256 : 96];
  }

  public static boolean supports(EntityHitboxProvider provider) {
    Class<?> type = provider.getClass();
    return (type == PlayerHitboxProvider.class
            && ((PlayerHitboxProvider) provider).usesVanillaAnimation())
        || type == VanillaMobProvider.class
        || type == VanillaEntityProvider.class
        || type == EnderDragonProvider.class;
  }

  public boolean unchanged(LivingEntity entity, float partialTick) {
    if (!(entity.getEntityData() instanceof SyncedDataRevision revision)) {
      return false;
    }
    this.index = 0;
    this.same = this.count >= 0;
    this.add(revision.hitboxplus$revision());
    this.add(entity.tickCount);
    this.add(partialTick);
    this.add(entity.getX());
    this.add(entity.getY());
    this.add(entity.getZ());
    this.add(entity.xOld);
    this.add(entity.yOld);
    this.add(entity.zOld);
    this.add(entity.xo);
    this.add(entity.yo);
    this.add(entity.zo);
    this.add(entity.getXRot());
    this.add(entity.getYRot());
    this.add(entity.xRotO);
    this.add(entity.yRotO);
    this.add(entity.yBodyRot);
    this.add(entity.yBodyRotO);
    this.add(entity.yHeadRot);
    this.add(entity.yHeadRotO);
    this.add(entity.animationSpeed);
    this.add(entity.animationSpeedOld);
    this.add(entity.animationPosition);
    this.add(entity.getAttackAnim(partialTick));
    this.add(entity.deathTime);
    this.add(entity.getFallFlyingTicks());
    this.add(entity.getSwimAmount(partialTick));
    var velocity = entity.getDeltaMovement();
    this.add(velocity.x);
    this.add(velocity.y);
    this.add(velocity.z);
    this.add(entity.getMainArm().ordinal());
    this.add(entity.swingingArm);
    this.add(entity.swinging);
    this.add(entity.isOnGround());
    this.add(entity.isInWater());
    this.add(entity.isInWaterOrBubble());
    this.add(entity.isAutoSpinAttack());
    this.add(entity.isVisuallySwimming());
    this.add(entity.isVehicle());
    this.add(entity.isUsingItem());
    this.add(entity.isSpectator());
    this.add(entity.getUsedItemHand().ordinal());
    this.add(entity.getUseItemRemainingTicks());
    this.add(entity.getTicksUsingItem());
    this.add(
        entity.getUseItem().isEmpty() ? 25 : CrossbowItem.getChargeDuration(entity.getUseItem()));
    var main = entity.getMainHandItem();
    var off = entity.getOffhandItem();
    this.same &= this.main == main.getItem() && this.off == off.getItem();
    this.main = main.getItem();
    this.off = off.getItem();
    this.item(main);
    this.item(off);
    var vehicle = entity.getVehicle();
    var previousVehicle = this.vehicle == null ? null : this.vehicle.get();
    if (previousVehicle != vehicle) {
      this.same = false;
      this.vehicle = vehicle == null ? null : new WeakReference<>(vehicle);
    }
    if (vehicle != null) {
      this.add(vehicle.shouldRiderSit());
      if (vehicle instanceof LivingEntity living) {
        this.add(living.yBodyRot);
        this.add(living.yBodyRotO);
      }
    }
    if (entity.isSleeping()) {
      var bed = entity.getBedOrientation();
      this.add(bed == null ? -1 : bed.ordinal());
    }
    this.special(entity, partialTick);
    this.same &= this.index == this.count;
    this.count = this.index;
    return this.same;
  }

  private void item(ItemStack stack) {
    this.add(stack.isEmpty());
    this.add(stack.getUseAnimation().ordinal());
    this.add(stack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(stack));
  }

  private void special(LivingEntity entity, float tick) {
    if (entity instanceof AbstractPiglin piglin) {
      this.add(piglin.getArmPose().ordinal());
    } else if (entity instanceof AbstractIllager illager) {
      this.add(illager.getArmPose().ordinal());
    } else if (entity instanceof AbstractVillager villager) {
      this.add(villager.getUnhappyCounter());
    } else if (entity instanceof Sheep sheep) {
      this.add(sheep.getHeadEatPositionScale(tick));
      this.add(sheep.getHeadEatAngleScale(tick));
    } else if (entity instanceof Creeper creeper) {
      this.add(creeper.getSwelling(tick));
    } else if (entity instanceof Chicken chicken) {
      this.add(chicken.flap);
      this.add(chicken.oFlap);
      this.add(chicken.flapSpeed);
      this.add(chicken.oFlapSpeed);
    } else if (entity instanceof Parrot parrot) {
      this.add(parrot.flap);
      this.add(parrot.oFlap);
      this.add(parrot.flapSpeed);
      this.add(parrot.oFlapSpeed);
      this.add(parrot.isPartyParrot());
    } else if (entity instanceof IronGolem golem) {
      this.add(golem.getAttackAnimationTick());
      this.add(golem.getOfferFlowerTick());
    } else if (entity instanceof WitherBoss wither) {
      for (int head = 0; head < 2; head++) {
        this.add(wither.getHeadXRot(head));
        this.add(wither.getHeadYRot(head));
      }
    } else if (entity instanceof Shulker shulker) {
      this.add(shulker.getClientPeekAmount(tick));
    } else if (entity instanceof Slime slime) {
      this.add(slime.squish);
      this.add(slime.oSquish);
    } else if (entity instanceof Ravager ravager) {
      this.add(ravager.getAttackTick());
      this.add(ravager.getRoarTick());
      this.add(ravager.getStunnedTick());
    } else if (entity instanceof Goat goat) {
      this.add(goat.getRammingXHeadRot());
    } else if (entity instanceof HoglinBase hoglin) {
      this.add(hoglin.getAttackAnimationRemainingTicks());
    } else if (entity instanceof PolarBear bear) {
      this.add(bear.getStandingAnimationScale(tick));
    } else if (entity instanceof Rabbit rabbit) {
      this.add(rabbit.getJumpCompletion(tick));
    } else if (entity instanceof Wolf wolf) {
      this.add(wolf.getTailAngle());
      this.add(wolf.isAngry());
      this.add(wolf.getHeadRollAngle(tick));
      this.add(wolf.getBodyRollAngle(tick, 0));
      this.add(wolf.getBodyRollAngle(tick, -0.08F));
      this.add(wolf.getBodyRollAngle(tick, -0.16F));
      this.add(wolf.getBodyRollAngle(tick, -0.2F));
    } else if (entity instanceof Cat cat) {
      this.add(cat.getLieDownAmount(tick));
      this.add(cat.getLieDownAmountTail(tick));
      this.add(cat.getRelaxStateOneAmount(tick));
    } else if (entity instanceof Fox fox) {
      this.add(fox.getCrouchAmount(tick));
      this.add(fox.getHeadRollAngle(tick));
    } else if (entity instanceof AbstractHorse horse) {
      this.add(horse.getEatAnim(tick));
      this.add(horse.getStandAnim(tick));
      this.add(horse.getMouthAnim(tick));
      this.add(horse.tailCounter);
    } else if (entity instanceof Panda panda) {
      this.add(panda.getSitAmount(tick));
      this.add(panda.getLieOnBackAmount(tick));
      this.add(panda.getRollAmount(tick));
      this.add(panda.rollCounter);
      this.add(panda.isScared());
    } else if (entity instanceof Bee bee) {
      this.add(bee.getRollAmount(tick));
      this.add(bee.isAngry());
    } else if (entity instanceof Squid squid) {
      this.add(squid.xBodyRot);
      this.add(squid.xBodyRotO);
      this.add(squid.zBodyRot);
      this.add(squid.zBodyRotO);
      this.add(squid.tentacleAngle);
      this.add(squid.oldTentacleAngle);
    } else if (entity instanceof Guardian guardian) {
      var target = guardian.getActiveAttackTarget();
      this.add(target != null);
      if (target != null) {
        var eye = target.getEyePosition(0);
        this.add(eye.x);
        this.add(eye.y);
        this.add(eye.z);
      }
    } else if (entity instanceof EnderDragon dragon) {
      this.add(dragon.flapTime);
      this.add(dragon.oFlapTime);
      this.add(dragon.posPointer);
      this.add(dragon.getPhaseManager().getCurrentPhase().getPhase().getId());
      this.add(dragon.isDeadOrDying());
      for (var position : dragon.positions) {
        this.add(position[0]);
        this.add(position[1]);
      }
    }
  }

  private void add(@Nullable InteractionHand hand) {
    // Vanilla leaves the hand unset before an entity's first swing.
    this.add(hand == null ? -1L : hand.ordinal());
  }

  private void add(boolean value) {
    this.add(value ? 1L : 0L);
  }

  private void add(double value) {
    this.add(Double.doubleToLongBits(value));
  }

  private void add(long value) {
    this.same &= this.values[this.index] == value;
    this.values[this.index++] = value;
  }
}
