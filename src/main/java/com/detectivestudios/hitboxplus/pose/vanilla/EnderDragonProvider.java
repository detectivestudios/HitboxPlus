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

import com.detectivestudios.hitboxplus.api.BodyPart;
import com.detectivestudios.hitboxplus.api.EntityHitboxProvider;
import com.detectivestudios.hitboxplus.api.EntityModelPose;
import com.detectivestudios.hitboxplus.api.HitboxPart;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.api.NamedHitboxPart;
import com.detectivestudios.hitboxplus.math.BoneTransform;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.pose.EntityBodyGeometry;
import java.util.Optional;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public class EnderDragonProvider implements EntityHitboxProvider {

  private final EnderDragon entity;
  private final DragonModelDefinition model;
  private final EntityBodyGeometry geometry = new EntityBodyGeometry();
  private final VanillaRig rig;

  public EnderDragonProvider(EnderDragon entity, DragonModelDefinition model) {
    this.entity = entity;
    this.model = model;
    this.rig = model.rig();
  }

  @Override
  public Optional<HitboxPose> getPose(float partialTick) {
    return this.getModelPose(partialTick)
        .map(pose -> this.geometry.create(this.model.profile(), pose.bones()::get, pose.root()));
  }

  @Override
  public Optional<EntityModelPose> getModelPose(float partialTick) {
    var rig = this.rig;
    rig.reset();
    float cycle = Mth.lerp(partialTick, this.entity.oFlapTime, this.entity.flapTime) * Mth.TWO_PI;
    float bob = (float) (Math.sin(cycle - 1) + 1);
    bob = (bob * bob + bob * 2) * 0.05F;
    rig.part("head/jaw").pitch = (float) (Math.sin(cycle) + 1) * 0.2F;
    double[] reference = this.entity.getLatencyPos(6, partialTick);
    float turn =
        Mth.wrapDegrees(
            (float)
                (this.entity.getLatencyPos(5, partialTick)[0]
                    - this.entity.getLatencyPos(10, partialTick)[0]));
    float heading =
        Mth.wrapDegrees((float) this.entity.getLatencyPos(5, partialTick)[0] + turn / 2);
    float x = 0;
    float y = 20;
    float z = -12;
    for (int index = 0; index < 5; index++) {
      double[] previous = this.entity.getLatencyPos(5 - index, partialTick);
      var neck = rig.part("neck" + index);
      neck.set(x, y, z);
      neck.yaw = Mth.wrapDegrees((float) (previous[0] - reference[0])) * Mth.DEG_TO_RAD * 1.5F;
      neck.pitch =
          (float) Math.cos(index * 0.45F + cycle) * 0.15F
              + this.entity.getHeadPartYOffset(index, reference, previous) * Mth.DEG_TO_RAD * 7.5F;
      neck.roll = -Mth.wrapDegrees((float) previous[0] - heading) * Mth.DEG_TO_RAD * 1.5F;
      x -= Mth.sin(neck.yaw) * Mth.cos(neck.pitch) * 10;
      y += Mth.sin(neck.pitch) * 10;
      z -= Mth.cos(neck.yaw) * Mth.cos(neck.pitch) * 10;
    }
    double[] head = this.entity.getLatencyPos(0, partialTick);
    rig.part("head").set(x, y, z);
    rig.part("head")
        .rotate(
            Mth.wrapDegrees(this.entity.getHeadPartYOffset(6, reference, head))
                * Mth.DEG_TO_RAD
                * 7.5F,
            Mth.wrapDegrees((float) (head[0] - reference[0])) * Mth.DEG_TO_RAD,
            -Mth.wrapDegrees((float) head[0] - heading) * Mth.DEG_TO_RAD);
    var sway =
        new BoneTransform(
                new Vector3(0, 16, 0), Rotation.euler(0, 0, -turn * 1.5F * Mth.DEG_TO_RAD), 1)
            .compose(new BoneTransform(new Vector3(0, -16, 0), Rotation.IDENTITY, 1));
    rig.parent("body", sway);
    for (String side : new String[] {"left", "right"}) {
      float sign = side.equals("left") ? 1 : -1;
      String wing = side + "_wing";
      rig.parent(wing, sway);
      rig.part(wing)
          .rotate(
              0.125F - (float) Math.cos(cycle) * 0.2F,
              -0.25F * sign,
              -(float) (Math.sin(cycle) + 0.125) * 0.8F * sign);
      rig.part(wing + "/" + side + "_wing_tip").roll =
          (float) (Math.sin(cycle + 2) + 0.5) * 0.75F * sign;
      String front = side + "_front_leg";
      String hind = side + "_hind_leg";
      rig.parent(front, sway);
      rig.parent(hind, sway);
      rig.part(front).pitch = 1.3F + bob * 0.1F;
      rig.part(front + "/" + front + "_tip").pitch = -0.5F - bob * 0.1F;
      rig.part(front + "/" + front + "_tip/" + side + "_front_foot").pitch = 0.75F + bob * 0.1F;
      rig.part(hind).pitch = 1 + bob * 0.1F;
      rig.part(hind + "/" + hind + "_tip").pitch = 0.5F + bob * 0.1F;
      rig.part(hind + "/" + hind + "_tip/" + side + "_hind_foot").pitch = 0.75F + bob * 0.1F;
    }
    reference = this.entity.getLatencyPos(11, partialTick);
    x = 0;
    y = 10;
    z = 60;
    float wave = 0;
    for (int index = 0; index < 12; index++) {
      double[] previous = this.entity.getLatencyPos(12 + index, partialTick);
      wave += Mth.sin(index * 0.45F + cycle) * 0.05F;
      var tail = rig.part("tail" + index);
      tail.set(x, y, z);
      tail.rotate(
          wave + (float) (previous[1] - reference[1]) * Mth.DEG_TO_RAD * 7.5F,
          (Mth.wrapDegrees((float) (previous[0] - reference[0])) * 1.5F + 180) * Mth.DEG_TO_RAD,
          Mth.wrapDegrees((float) previous[0] - heading) * Mth.DEG_TO_RAD * 1.5F);
      x -= Mth.sin(tail.yaw) * Mth.cos(tail.pitch) * 10;
      y += Mth.sin(tail.pitch) * 10;
      z -= Mth.cos(tail.yaw) * Mth.cos(tail.pitch) * 10;
    }
    return Optional.of(new EntityModelPose(rig.compose(), this.root(partialTick, bob), "base"));
  }

  private BoneTransform root(float partialTick, float bob) {
    var position = this.entity.getPosition(partialTick);
    var rotation =
        Rotation.euler(0, -this.entity.getLatencyPos(7, partialTick)[0] * Mth.DEG_TO_RAD, 0)
            .multiply(
                Rotation.euler(
                    (this.entity.getLatencyPos(5, partialTick)[1]
                            - this.entity.getLatencyPos(10, partialTick)[1])
                        * 10
                        * Mth.DEG_TO_RAD,
                    0,
                    0));
    var origin =
        new Vector3(position.x, position.y, position.z)
            .add(rotation.transform(new Vector3(0, 0, 1)));
    rotation = rotation.multiply(Rotation.euler(0, 0, Math.PI));
    origin = origin.add(rotation.transform(new Vector3(0, -1.501 + bob - 2, -3)));
    rotation = rotation.multiply(Rotation.euler(bob * 2 * Mth.DEG_TO_RAD, 0, 0));
    return new BoneTransform(origin, rotation, 1.0 / 16);
  }

  public static EnderDragonPart damagePart(EnderDragon dragon, HitboxPart part) {
    var parts = dragon.getSubEntities();
    if (part.region() == BodyPart.TAIL
        && part instanceof NamedHitboxPart named
        && named.id().startsWith("minecraft:ender_dragon/tail")) {
      String segment = named.id().substring("minecraft:ender_dragon/tail".length()).split("/")[0];
      if (segment.matches("[0-9]{1,2}")) {
        return parts[3 + Math.min(2, Integer.parseInt(segment) / 4)];
      }
    }
    return switch (part.region()) {
      case HEAD -> dragon.head;
      case NECK -> parts[1];
      case LEFT_WING -> parts[6];
      case RIGHT_WING -> parts[7];
      case TAIL -> parts[5];
      default -> parts[2];
    };
  }

  public static HitboxPose partPose(EnderDragonPart part, HitboxPose pose) {
    return new HitboxPose(
        pose.hitboxes().stream()
            .filter(hitbox -> damagePart(part.parentMob, hitbox.part()) == part)
            .toList());
  }
}
