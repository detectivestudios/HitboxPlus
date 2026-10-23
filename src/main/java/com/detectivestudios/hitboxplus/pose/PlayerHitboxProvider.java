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

package com.detectivestudios.hitboxplus.pose;

import com.detectivestudios.hitboxplus.api.EntityHitboxProvider;
import com.detectivestudios.hitboxplus.api.EntityModelPose;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.api.HumanoidPoseAnimation;
import com.detectivestudios.hitboxplus.math.BoneTransform;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;

public class PlayerHitboxProvider implements EntityHitboxProvider {

  private static final double MODEL_SCALE = 0.9375D;
  private final Player player;
  private final LivingModelTransform transform;
  private final PlayerAppearance appearance;
  private final HumanoidInputs inputs = new HumanoidInputs();
  private final HumanoidPoseAnimation animation;
  private final PlayerBodyGeometry geometry = new PlayerBodyGeometry();

  public PlayerHitboxProvider(Player player) {
    this(player, new HumanoidAnimation());
  }

  public PlayerHitboxProvider(Player player, HumanoidPoseAnimation animation) {
    this.player = player;
    this.animation = Objects.requireNonNull(animation, "Player animation is required");
    this.transform = new LivingModelTransform(player, MODEL_SCALE);
    this.appearance = new PlayerAppearance(player);
  }

  public boolean usesVanillaAnimation() {
    return this.animation.getClass() == HumanoidAnimation.class;
  }

  @Override
  public Optional<HitboxPose> getPose(float partialTick) {
    if (this.player.isSpectator()) {
      return Optional.empty();
    }
    var pose = this.evaluate(partialTick);
    var root = this.transform.root(partialTick, this.transform.bodyYaw(partialTick));
    return Optional.of(
        this.geometry.create(
            pose,
            root.origin(),
            root.rotation(),
            MODEL_SCALE / 16.0D,
            this.appearance.slim(),
            this.appearance.layers()));
  }

  @Override
  public Optional<EntityModelPose> getModelPose(float partialTick) {
    if (this.player.isSpectator()) {
      return Optional.empty();
    }
    var pose = this.evaluate(partialTick);
    var root = this.transform.root(partialTick, this.transform.bodyYaw(partialTick));
    return Optional.of(
        EntityModelPose.humanoid(
            pose,
            new BoneTransform(root.origin(), root.rotation(), MODEL_SCALE / 16.0D),
            EntityModelPose.playerVariant(this.appearance.slim(), this.appearance.layers())));
  }

  private HumanoidPose evaluate(float partialTick) {
    return Objects.requireNonNull(
        this.animation.evaluate(this.inputs.sample(this.player, this.transform, partialTick)),
        "Player animation must return a pose");
  }
}
