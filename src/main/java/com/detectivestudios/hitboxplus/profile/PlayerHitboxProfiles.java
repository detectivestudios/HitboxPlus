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

package com.detectivestudios.hitboxplus.profile;

import com.detectivestudios.hitboxplus.api.BodyPart;
import com.detectivestudios.hitboxplus.geometry.Bounds;
import com.detectivestudios.hitboxplus.math.Vector3;
import java.util.ArrayList;
import java.util.List;

public class PlayerHitboxProfiles {

  private final HumanoidHitboxProfile standard = this.create(false);
  private final HumanoidHitboxProfile slim = this.create(true);

  public HumanoidHitboxProfile select(boolean slim) {
    return slim ? this.slim : this.standard;
  }

  private HumanoidHitboxProfile create(boolean slim) {
    var volumes = new ArrayList<BodyVolume>(8);
    volumes.add(
        this.volume(
            BodyPart.HEAD,
            HumanoidBone.HEAD,
            new Bounds(new Vector3(-4.0D, -8.0D, -4.0D), new Vector3(4.0D, 0.0D, 4.0D)),
            SkinLayer.NONE,
            0.0D));
    volumes.add(
        this.volume(
            BodyPart.TORSO,
            HumanoidBone.BODY,
            new Bounds(new Vector3(-4.0D, 0.0D, -2.0D), new Vector3(4.0D, 12.0D, 2.0D)),
            SkinLayer.JACKET,
            0.25D));
    volumes.add(
        this.volume(
            BodyPart.RIGHT_ARM,
            HumanoidBone.RIGHT_ARM,
            new Bounds(
                new Vector3(slim ? -2.0D : -3.0D, -2.0D, -2.0D), new Vector3(1.0D, 10.0D, 2.0D)),
            SkinLayer.RIGHT_SLEEVE,
            0.25D));
    volumes.add(
        this.volume(
            BodyPart.LEFT_ARM,
            HumanoidBone.LEFT_ARM,
            new Bounds(
                new Vector3(-1.0D, -2.0D, -2.0D), new Vector3(slim ? 2.0D : 3.0D, 10.0D, 2.0D)),
            SkinLayer.LEFT_SLEEVE,
            0.25D));
    this.leg(
        volumes,
        BodyPart.RIGHT_LEG,
        BodyPart.RIGHT_FOOT,
        HumanoidBone.RIGHT_LEG,
        SkinLayer.RIGHT_PANTS);
    this.leg(
        volumes,
        BodyPart.LEFT_LEG,
        BodyPart.LEFT_FOOT,
        HumanoidBone.LEFT_LEG,
        SkinLayer.LEFT_PANTS);
    return new HumanoidHitboxProfile(volumes);
  }

  private BodyVolume volume(
      BodyPart part, HumanoidBone bone, Bounds bounds, SkinLayer layer, double padding) {
    return new BodyVolume(part, bone, bounds, bounds.inflate(padding), layer);
  }

  private void leg(
      List<BodyVolume> volumes, BodyPart leg, BodyPart foot, HumanoidBone bone, SkinLayer layer) {
    // Expand only outer mesh faces; an inflated ankle seam would overlap damage regions.
    volumes.add(
        new BodyVolume(
            leg,
            bone,
            new Bounds(new Vector3(-2.0D, 0.0D, -2.0D), new Vector3(2.0D, 10.0D, 2.0D)),
            new Bounds(new Vector3(-2.25D, -0.25D, -2.25D), new Vector3(2.25D, 10.0D, 2.25D)),
            layer));
    volumes.add(
        new BodyVolume(
            foot,
            bone,
            new Bounds(new Vector3(-2.0D, 10.0D, -2.0D), new Vector3(2.0D, 12.0D, 2.0D)),
            new Bounds(new Vector3(-2.25D, 10.0D, -2.25D), new Vector3(2.25D, 12.25D, 2.25D)),
            layer));
  }
}
