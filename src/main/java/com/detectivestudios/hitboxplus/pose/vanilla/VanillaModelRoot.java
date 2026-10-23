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

package com.detectivestudios.hitboxplus.pose.vanilla;

import com.detectivestudios.hitboxplus.math.BoneTransform;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.pose.LivingModelTransform;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;

public class VanillaModelRoot {

  public BoneTransform sample(VanillaPoseContext context) {
    var entity = context.entity();
    double scale = this.scale(context);
    var transform = new LivingModelTransform(entity, 1);
    var base = transform.root(context.partialTick(), transform.bodyYaw(context.partialTick()));
    var frame =
        new Frame(
            base.origin().subtract(base.rotation().transform(new Vector3(0, -1.501, 0))),
            base.rotation().multiply(Rotation.euler(0, 0, Math.PI)));
    if (entity instanceof Squid squid) {
      var position = squid.getPosition(context.partialTick());
      frame = new Frame(new Vector3(position.x, position.y + 0.5, position.z), Rotation.IDENTITY);
      frame.rotate(0, (180 - transform.bodyYaw(context.partialTick())) * Mth.DEG_TO_RAD, 0);
      frame.rotate(
          Mth.lerp(context.partialTick(), squid.xBodyRotO, squid.xBodyRot) * Mth.DEG_TO_RAD, 0, 0);
      frame.rotate(
          0, Mth.lerp(context.partialTick(), squid.zBodyRotO, squid.zBodyRot) * Mth.DEG_TO_RAD, 0);
      frame.move(0, -1.2, 0);
    } else if (entity instanceof Shulker shulker) {
      frame.rotate(0, Math.PI, 0);
      frame.move(0, 0.5, 0);
      var quaternion = shulker.getAttachFace().getOpposite().getRotation();
      frame.rotation =
          frame.rotation.multiply(
              new Rotation(quaternion.i(), quaternion.j(), quaternion.k(), quaternion.r()));
      frame.move(0, -0.5, 0);
    } else if (entity instanceof Phantom) {
      frame.rotate(entity.getXRot() * Mth.DEG_TO_RAD, 0, 0);
    } else if (entity instanceof Cat cat) {
      float lie = cat.getLieDownAmount(context.partialTick());
      frame.move(0.4 * lie, 0.15 * lie, 0.1 * lie);
      frame.rotate(0, 0, lie * Math.PI / 2);
    } else if (entity instanceof Panda panda) {
      this.panda(frame, context, panda);
    } else if (entity instanceof ArmorStand stand) {
      var position = stand.getPosition(context.partialTick());
      frame =
          new Frame(
              new Vector3(position.x, position.y, position.z),
              Rotation.euler(
                  0,
                  (180 - Mth.rotLerp(context.partialTick(), stand.yRotO, stand.getYRot()))
                      * Mth.DEG_TO_RAD,
                  0));
    }
    switch (context.kind()) {
      case "spider", "cave_spider", "silverfish", "endermite" -> {
        if (entity.deathTime > 0) {
          float roll =
              Mth.clamp(Mth.sqrt((entity.deathTime + context.partialTick() - 1) / 20 * 1.6F), 0, 1);
          frame.rotate(0, 0, roll * Math.PI / 2);
        }
      }
      case "drowned" -> {
        float swim = entity.getSwimAmount(context.partialTick());
        if (swim > 0) {
          frame.rotate(
              Mth.lerp(swim, entity.getXRot(), -10 - entity.getXRot()) * Mth.DEG_TO_RAD, 0, 0);
        }
      }
      case "bat" -> {
        if (entity instanceof Bat bat) {
          frame.origin =
              frame.origin.add(
                  new Vector3(0, bat.isResting() ? -0.1 : Mth.cos(context.age() * 0.3F) * 0.1, 0));
        }
      }
      case "cod", "salmon", "tropical_fish" -> {
        boolean salmon = context.kind().equals("salmon");
        boolean dry = !entity.isInWater();
        float wave =
            (salmon && dry ? 1.3F : 1)
                * 4.3F
                * Mth.sin((salmon && dry ? 1.7F : 1) * 0.6F * context.age());
        frame.rotate(0, wave * Mth.DEG_TO_RAD, 0);
        if (salmon) {
          frame.move(0, 0, -0.4);
        }
        if (dry) {
          frame.move(
              context.kind().equals("cod") ? 0.1 : 0.2,
              0.1,
              context.kind().equals("cod") ? -0.1 : 0);
          frame.rotate(0, 0, Math.PI / 2);
        }
      }
      case "pufferfish" ->
          frame.origin = frame.origin.add(new Vector3(0, Mth.cos(context.age() * 0.05F) * 0.08, 0));
      case "iron_golem" -> {
        if (context.amount() >= 0.01F) {
          float phase = context.swing() + 6;
          float sway = (Math.abs(phase % 13 - 6.5F) - 3.25F) / 3.25F;
          frame.rotate(0, 0, 6.5F * sway * Mth.DEG_TO_RAD);
        }
      }
      default -> {}
    }
    frame.rotate(0, 0, Math.PI);
    if (entity instanceof Phantom) {
      frame.move(0, 1.3125 * scale, 0.1875 * scale);
    }
    if (context.kind().equals("slime")) {
      frame.move(0, 0.000999, 0);
    }
    frame.move(0, -1.501 * scale, 0);
    if (context.kind().equals("turtle")
        && entity instanceof Turtle turtle
        && !turtle.isBaby()
        && turtle.hasEgg()) {
      frame.move(0, -0.08 * scale, 0);
    }
    return new BoneTransform(frame.origin, frame.rotation, scale / 16);
  }

  private double scale(VanillaPoseContext context) {
    var entity = context.entity();
    if (entity instanceof Slime slime) {
      return slime.getSize() * (context.kind().equals("slime") ? 0.999 : 1);
    }
    if (entity instanceof Phantom phantom) {
      return 1 + 0.15 * phantom.getPhantomSize();
    }
    if (entity instanceof WitherBoss wither) {
      return 2
          - (wither.getInvulnerableTicks() > 0
              ? (wither.getInvulnerableTicks() - context.partialTick()) / 220.0 * 0.5
              : 0);
    }
    return switch (context.kind()) {
      case "bat" -> 0.35;
      case "vex" -> 0.4;
      case "ghast" -> 4.5;
      case "cave_spider" -> 0.7;
      case "elder_guardian" -> 2.35;
      case "cat" -> 0.8;
      case "polar_bear" -> 1.2;
      case "horse" -> 1.1;
      case "donkey" -> 0.87;
      case "mule" -> 0.92;
      case "villager" -> entity.isBaby() ? 0.46875 : 0.9375;
      case "wandering_trader", "witch" -> 0.9375;
      default -> 1;
    };
  }

  private void panda(Frame frame, VanillaPoseContext context, Panda panda) {
    if (panda.rollCounter > 0) {
      int phase = panda.rollCounter / 8;
      float progress =
          ((panda.rollCounter % 8) + (panda.rollCounter % 8 == 7 ? 0 : context.partialTick())) / 7;
      float height = panda.isBaby() ? 0.3F : 0.8F;
      double y =
          switch (phase) {
            case 0 -> (height + 0.2F) * progress;
            case 1 -> height + 0.2F + (height - 0.2F) * progress;
            case 2 -> height + height * (1 - progress);
            default -> height * (1 - progress);
          };
      frame.move(0, y, 0);
      frame.rotate(-(phase + progress) * Math.PI / 2, 0, 0);
    }
    float sit = panda.getSitAmount(context.partialTick());
    if (sit > 0) {
      frame.move(0, 0.8 * sit, 0);
      frame.rotate((panda.getXRot() + sit * 90) * Mth.DEG_TO_RAD, 0, 0);
      frame.move(0, -sit, 0);
      if (panda.isScared()) {
        frame.rotate(0, Math.cos(panda.tickCount * 1.25) * Math.PI * 0.05 * Mth.DEG_TO_RAD, 0);
        if (panda.isBaby()) {
          frame.move(0, 0.8, 0.55);
        }
      }
    }
    float lie = panda.getLieOnBackAmount(context.partialTick());
    if (lie > 0) {
      frame.move(0, (panda.isBaby() ? 0.5 : 1.3) * lie, 0);
      frame.rotate((panda.getXRot() + lie * 180) * Mth.DEG_TO_RAD, 0, 0);
    }
  }

  private static class Frame {

    private Vector3 origin;
    private Rotation rotation;

    public Frame(Vector3 origin, Rotation rotation) {
      this.origin = origin;
      this.rotation = rotation;
    }

    public void move(double x, double y, double z) {
      this.origin = this.origin.add(this.rotation.transform(new Vector3(x, y, z)));
    }

    public void rotate(double pitch, double yaw, double roll) {
      this.rotation = this.rotation.multiply(Rotation.euler(pitch, yaw, roll));
    }
  }
}
