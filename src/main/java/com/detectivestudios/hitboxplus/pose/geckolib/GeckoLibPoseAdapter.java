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

package com.detectivestudios.hitboxplus.pose.geckolib;

import com.detectivestudios.hitboxplus.api.EntityHitboxProvider;
import com.detectivestudios.hitboxplus.api.EntityModelPose;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.math.BoneTransform;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.pose.EntityBodyGeometry;
import com.detectivestudios.hitboxplus.profile.editor.EditorProfileDocument;
import java.util.Map;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;

public class GeckoLibPoseAdapter implements EntityHitboxProvider {

  private final LivingEntity entity;
  private final EditorProfileDocument document;
  private final GeckoLibRig rig;
  private final GeckoLibPoseEvaluator evaluator;
  private final GeckoLibAnimationSource source;
  private final EntityBodyGeometry geometry = new EntityBodyGeometry();
  private String current = "";
  private String previous = "";
  private double changedAt = Double.NEGATIVE_INFINITY;
  private boolean initialized;
  private double clipStartedAt;
  private double lastSeconds;
  private double previousSeconds;
  private AnimationKey cachedKey;
  private Map<String, BoneTransform> cachedBones;
  private EntityModelPose cachedModel;
  private HitboxPose cachedPose;

  public GeckoLibPoseAdapter(LivingEntity entity, EditorProfileDocument document) {
    this(entity, document, null);
  }

  public GeckoLibPoseAdapter(
      LivingEntity entity, EditorProfileDocument document, GeckoLibAnimationSource source) {
    this.entity = entity;
    this.document = document;
    this.rig = document.rig().orElseThrow();
    this.evaluator = new GeckoLibPoseEvaluator(this.rig);
    this.source = source;
  }

  @Override
  public Optional<HitboxPose> getPose(float partialTick) {
    return this.getModelPose(partialTick)
        .map(
            model -> {
              if (!model.equals(this.cachedModel)) {
                this.cachedPose =
                    this.geometry.create(this.document.profile(), model.bones()::get, model.root());
                this.cachedModel = model;
              }
              return this.cachedPose;
            });
  }

  @Override
  public Optional<EntityModelPose> getModelPose(float partialTick) {
    var sample =
        this.source == null
            ? this.defaultSample(partialTick)
            : this.source.sample(this.entity, this.rig, partialTick);
    if (!sample.clip().isEmpty() && !this.rig.clips().containsKey(sample.clip())) {
      return Optional.empty();
    }
    double clock = this.entity.level.getGameTime() + partialTick;
    if (!this.initialized || !this.current.equals(sample.clip())) {
      this.previous = this.initialized ? this.current : sample.clip();
      this.previousSeconds = this.initialized ? this.lastSeconds : sample.seconds();
      this.current = sample.clip();
      this.clipStartedAt = clock;
      this.changedAt = this.initialized ? clock : Double.NEGATIVE_INFINITY;
      this.initialized = true;
    }
    double blend =
        this.rig.blendTicks() == 0
            ? 1
            : Math.max(0, Math.min(1, (clock - this.changedAt) / this.rig.blendTicks()));
    blend = blend * blend * (3 - 2 * blend);
    var key = new AnimationKey(sample.clip(), sample.seconds(), clock, blend);
    if (!key.equals(this.cachedKey)) {
      this.cachedBones =
          this.evaluator.evaluate(
              sample.clip(), sample.seconds(), this.previous, this.previousSeconds, blend);
      this.cachedKey = key;
    }
    var bones =
        this.source == null
            ? this.cachedBones
            : this.source.adjustBones(this.entity, this.cachedBones, partialTick);
    this.lastSeconds = sample.seconds();
    return Optional.of(
        new EntityModelPose(
            bones,
            sample.worldRoot().orElseGet(() -> this.worldRoot(partialTick)),
            this.document.key().variant()));
  }

  private GeckoLibAnimationSource.Sample defaultSample(float partialTick) {
    String state =
        this.entity.deathTime > 0
            ? "death"
            : this.entity.swinging
                ? "attack"
                : this.entity.isSwimming()
                    ? "swim"
                    : this.entity.isSprinting()
                        ? "sprint"
                        : this.entity.animationSpeed > 0.01F ? "walk" : "idle";
    String clip =
        this.rig
            .states()
            .getOrDefault(
                state,
                this.rig.states().getOrDefault(state.equals("sprint") ? "walk" : "idle", ""));
    double seconds = (this.entity.level.getGameTime() + partialTick) / 20.0;
    var animation = this.rig.clips().get(clip);
    if (animation != null && !animation.loop()) {
      seconds =
          this.initialized && this.current.equals(clip)
              ? Math.max(
                  0, (this.entity.level.getGameTime() + partialTick - this.clipStartedAt) / 20)
              : 0;
    }
    if (state.equals("death")) {
      seconds = (this.entity.deathTime + partialTick) / 20.0;
    } else if (state.equals("attack") && this.rig.clips().containsKey(clip)) {
      seconds = this.entity.getAttackAnim(partialTick) * this.rig.clips().get(clip).length();
    }
    return new GeckoLibAnimationSource.Sample(clip, seconds);
  }

  private BoneTransform worldRoot(float partialTick) {
    float yaw = Mth.rotLerp(partialTick, this.entity.yBodyRotO, this.entity.yBodyRot);
    if (this.entity.isPassenger()
        && this.entity.getVehicle() instanceof LivingEntity vehicle
        && vehicle.shouldRiderSit()) {
      float head = Mth.rotLerp(partialTick, this.entity.yHeadRotO, this.entity.yHeadRot);
      yaw = Mth.rotLerp(partialTick, vehicle.yBodyRotO, vehicle.yBodyRot);
      float difference = Mth.clamp(Mth.wrapDegrees(head - yaw), -85, 85);
      yaw = head - difference + (difference * difference > 2500 ? difference * 0.2F : 0);
    }
    var position = this.entity.getPosition(partialTick);
    var origin = new Vector3(position.x, position.y, position.z);
    boolean sleeping = this.entity.getPose() == Pose.SLEEPING;
    var renderRotation =
        sleeping ? Rotation.IDENTITY : Rotation.euler(0, Math.toRadians(180 - yaw), 0);
    if (sleeping && this.entity.getBedOrientation() != null) {
      var bed = this.entity.getBedOrientation();
      double shift = this.entity.getEyeHeight(Pose.STANDING) - 0.1;
      origin = origin.add(new Vector3(-bed.getStepX() * shift, 0, -bed.getStepZ() * shift));
    }
    if (this.entity.deathTime > 0) {
      double roll =
          Math.min(1, Math.sqrt(Math.max(0, (this.entity.deathTime + partialTick - 1) / 20 * 1.6)));
      renderRotation = renderRotation.multiply(Rotation.euler(0, 0, roll * Math.PI / 2));
    } else if (this.entity.isAutoSpinAttack()) {
      renderRotation =
          renderRotation
              .multiply(Rotation.euler(Math.toRadians(-90 - this.entity.getXRot()), 0, 0))
              .multiply(
                  Rotation.euler(
                      0, Math.toRadians((this.entity.tickCount + partialTick) * -75), 0));
    } else if (sleeping) {
      var bed = this.entity.getBedOrientation();
      double angle =
          bed == null
              ? yaw
              : switch (bed) {
                case SOUTH -> 90;
                case NORTH -> 270;
                case EAST -> 180;
                default -> 0;
              };
      renderRotation =
          Rotation.euler(0, Math.toRadians(angle), 0)
              .multiply(Rotation.euler(0, 0, Math.PI / 2))
              .multiply(Rotation.euler(0, Math.PI * 1.5, 0));
    } else if (this.entity.hasCustomName() || this.entity instanceof Player) {
      String name = ChatFormatting.stripFormatting(this.entity.getName().getString());
      boolean cape =
          !(this.entity instanceof Player player) || player.isModelPartShown(PlayerModelPart.CAPE);
      if (cape && name != null && (name.equals("Dinnerbone") || name.equalsIgnoreCase("Grumm"))) {
        origin =
            origin.add(
                renderRotation.transform(new Vector3(0, this.entity.getBbHeight() + 0.1, 0)));
        renderRotation = renderRotation.multiply(Rotation.euler(0, 0, Math.PI));
      }
    }
    var rotation = renderRotation.multiply(Rotation.euler(0, 0, Math.PI));
    var translation =
        origin
            .add(renderRotation.transform(new Vector3(0, 0.01, 0)))
            .add(rotation.transform(new Vector3(0, -1.5 * this.rig.scale(), 0)))
            .add(rotation.transform(this.rig.offset().scale(this.rig.scale() / 16)));
    return new BoneTransform(translation, rotation, this.rig.scale() / 16);
  }

  private record AnimationKey(String clip, double seconds, double clock, double blend) {}
}
