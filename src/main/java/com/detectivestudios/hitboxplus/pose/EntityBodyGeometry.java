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

import com.detectivestudios.hitboxplus.api.BodyHitbox;
import com.detectivestudios.hitboxplus.api.HitboxPart;
import com.detectivestudios.hitboxplus.api.HitboxPose;
import com.detectivestudios.hitboxplus.geometry.OrientedBox;
import com.detectivestudios.hitboxplus.math.BoneTransform;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.profile.EntityHitboxProfile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class EntityBodyGeometry {

  private volatile Prepared<?> prepared;

  public <B> HitboxPose create(
      EntityHitboxProfile<B> profile, Function<B, BoneTransform> bones, BoneTransform root) {
    var geometry = this.prepare(profile);
    var resolved = new BoneTransform[geometry.bones.size()];
    var boxes = new ArrayList<BodyHitbox>(geometry.volumes.size());
    for (var volume : geometry.volumes) {
      var transform = resolved[volume.bone];
      if (transform == null) {
        B key = geometry.bones.get(volume.bone);
        transform =
            root.compose(
                Objects.requireNonNull(bones.apply(key), () -> "Missing bone transform: " + key));
        resolved[volume.bone] = transform;
      }
      boxes.add(
          new BodyHitbox(
              volume.part,
              new OrientedBox(
                  transform.transform(volume.center),
                  volume.extents.scale(transform.scale()),
                  transform.rotation().multiply(volume.rotation))));
    }
    return new HitboxPose(boxes);
  }

  @SuppressWarnings("unchecked")
  private <B> Prepared<B> prepare(EntityHitboxProfile<B> profile) {
    var current = this.prepared;
    if (current != null && current.profile == profile) {
      return (Prepared<B>) current;
    }
    var result = new Prepared<>(profile);
    this.prepared = result;
    return result;
  }

  private static class Prepared<B> {

    private final EntityHitboxProfile<B> profile;
    private final List<B> bones;
    private final List<Volume> volumes;

    public Prepared(EntityHitboxProfile<B> profile) {
      this.profile = profile;
      var indices = new HashMap<B, Integer>();
      var keys = new ArrayList<B>();
      var volumes = new ArrayList<Volume>(profile.volumes().size());
      for (var volume : profile.volumes()) {
        int index =
            indices.computeIfAbsent(
                volume.bone(),
                key -> {
                  keys.add(key);
                  return keys.size() - 1;
                });
        var local = volume.localTransform();
        var center = volume.bounds().min().add(volume.bounds().max()).scale(0.5D);
        var extents = volume.bounds().max().subtract(volume.bounds().min()).scale(0.5D);
        volumes.add(
            new Volume(
                volume.part(),
                index,
                local.transform(center),
                extents.scale(local.scale()),
                local.rotation()));
      }
      this.bones = List.copyOf(keys);
      this.volumes = List.copyOf(volumes);
    }
  }

  private record Volume(
      HitboxPart part, int bone, Vector3 center, Vector3 extents, Rotation rotation) {}
}
