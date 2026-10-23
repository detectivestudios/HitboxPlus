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

package com.detectivestudios.hitboxplus.client;

import com.detectivestudios.hitboxplus.api.BodyHitbox;
import com.detectivestudios.hitboxplus.api.HitboxPlus;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityHitboxDebugRenderer {

  private final double[] corners = new double[24];

  public boolean render(
      PoseStack stack, VertexConsumer consumer, Entity entity, float partialTick) {
    var pose = HitboxPlus.getPose(entity, partialTick);
    if (pose.isEmpty()) {
      return false;
    }
    this.renderPose(stack, consumer, pose.orElseThrow(), entity.getPosition(partialTick));
    return true;
  }

  public void renderPose(PoseStack stack, VertexConsumer consumer, HitboxPose pose, Vec3 position) {
    var matrix = stack.last();
    for (BodyHitbox hitbox : pose.hitboxes()) {
      var box = hitbox.box();
      var rotation = box.rotation();
      var extent = box.halfExtents();
      double x = rotation.x();
      double y = rotation.y();
      double z = rotation.z();
      double w = rotation.w();
      double xx = 1 - 2 * (y * y + z * z);
      double xy = 2 * (x * y + z * w);
      double xz = 2 * (x * z - y * w);
      double yx = 2 * (x * y - z * w);
      double yy = 1 - 2 * (x * x + z * z);
      double yz = 2 * (y * z + x * w);
      double zx = 2 * (x * z + y * w);
      double zy = 2 * (y * z - x * w);
      double zz = 1 - 2 * (x * x + y * y);
      for (int corner = 0; corner < 8; corner++) {
        double sx = (corner & 1) == 0 ? -extent.x() : extent.x();
        double sy = (corner & 2) == 0 ? -extent.y() : extent.y();
        double sz = (corner & 4) == 0 ? -extent.z() : extent.z();
        int index = corner * 3;
        this.corners[index] = box.center().x() - position.x + xx * sx + yx * sy + zx * sz;
        this.corners[index + 1] = box.center().y() - position.y + xy * sx + yy * sy + zy * sz;
        this.corners[index + 2] = box.center().z() - position.z + xz * sx + yz * sy + zz * sz;
      }
      float red =
          switch (hitbox.part().region()) {
            case HEAD, TORSO -> 1.0F;
            default -> 0.2F;
          };
      float green =
          switch (hitbox.part().region()) {
            case HEAD, LEFT_FOOT, RIGHT_FOOT -> 0.25F;
            default -> 1.0F;
          };
      float blue =
          switch (hitbox.part().region()) {
            case LEFT_LEG, RIGHT_LEG, LEFT_FOOT, RIGHT_FOOT -> 1.0F;
            default -> 0.2F;
          };
      for (int corner = 0; corner < 8; corner++) {
        if ((corner & 1) == 0) {
          this.edge(
              matrix,
              consumer,
              corner == 2 ? 3 : corner,
              corner == 2 ? 2 : corner | 1,
              red,
              green,
              blue,
              (float) (corner == 2 ? -xx : xx),
              (float) (corner == 2 ? -xy : xy),
              (float) (corner == 2 ? -xz : xz));
        }
        if ((corner & 2) == 0) {
          this.edge(
              matrix,
              consumer,
              corner == 4 ? 6 : corner,
              corner == 4 ? 4 : corner | 2,
              red,
              green,
              blue,
              (float) (corner == 4 ? -yx : yx),
              (float) (corner == 4 ? -yy : yy),
              (float) (corner == 4 ? -yz : yz));
        }
        if ((corner & 4) == 0) {
          this.edge(
              matrix,
              consumer,
              corner == 1 ? 5 : corner,
              corner == 1 ? 1 : corner | 4,
              red,
              green,
              blue,
              (float) (corner == 1 ? -zx : zx),
              (float) (corner == 1 ? -zy : zy),
              (float) (corner == 1 ? -zz : zz));
        }
      }
    }
  }

  private void edge(
      PoseStack.Pose matrix,
      VertexConsumer consumer,
      int first,
      int second,
      float red,
      float green,
      float blue,
      float nx,
      float ny,
      float nz) {
    this.vertex(matrix, consumer, first, red, green, blue, nx, ny, nz);
    this.vertex(matrix, consumer, second, red, green, blue, nx, ny, nz);
  }

  private void vertex(
      PoseStack.Pose matrix,
      VertexConsumer consumer,
      int corner,
      float red,
      float green,
      float blue,
      float nx,
      float ny,
      float nz) {
    int index = corner * 3;
    consumer
        .vertex(
            matrix.pose(),
            (float) this.corners[index],
            (float) this.corners[index + 1],
            (float) this.corners[index + 2])
        .color(red, green, blue, 1)
        .normal(matrix.normal(), nx, ny, nz)
        .endVertex();
  }
}
