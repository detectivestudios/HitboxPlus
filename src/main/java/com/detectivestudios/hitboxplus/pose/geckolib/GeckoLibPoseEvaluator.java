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

import com.detectivestudios.hitboxplus.math.BoneTransform;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GeckoLibPoseEvaluator {

  private final GeckoLibRig rig;

  public GeckoLibPoseEvaluator(GeckoLibRig rig) {
    this.rig = rig;
  }

  public Map<String, BoneTransform> evaluate(String animation, double seconds) {
    return this.evaluate(animation, seconds, "", seconds, 1);
  }

  public Map<String, BoneTransform> evaluate(
      String animation, double seconds, String previous, double previousSeconds, double blend) {
    if (!Double.isFinite(seconds)
        || !Double.isFinite(previousSeconds)
        || !Double.isFinite(blend)
        || blend < 0
        || blend > 1) {
      throw new IllegalArgumentException("Invalid animation sample time/blend");
    }
    var result = new LinkedHashMap<String, BoneTransform>();
    result.put("$root", BoneTransform.IDENTITY);
    for (var bone : this.rig.bones()) {
      var next = this.local(bone, animation, seconds);
      if (blend < 1) {
        var before = this.local(bone, previous, previousSeconds);
        next =
            new BoneTransform(
                before.translation().lerp(next.translation(), blend),
                before.rotation().interpolate(next.rotation(), blend),
                before.scale() + (next.scale() - before.scale()) * blend);
      }
      result.put(bone.id(), bone.parent() == null ? next : result.get(bone.parent()).compose(next));
    }
    return Map.copyOf(result);
  }

  private BoneTransform local(GeckoLibRig.Bone bone, String animation, double seconds) {
    var clip = this.rig.clips().get(animation);
    var channels = clip == null ? null : clip.bones().get(bone.id());
    double time =
        clip == null
            ? 0
            : clip.loop()
                ? ((seconds % clip.length()) + clip.length()) % clip.length()
                : Math.max(0, Math.min(clip.length(), seconds));
    var position =
        channels == null ? Vector3.ZERO : this.sample(channels.position(), time, Vector3.ZERO);
    var rotation =
        bone.rotation()
            .add(
                channels == null
                    ? Vector3.ZERO
                    : this.sample(channels.rotation(), time, Vector3.ZERO));
    double scale =
        channels == null ? 1 : this.sample(channels.scale(), time, new Vector3(1, 1, 1)).x();
    return new BoneTransform(
        bone.pivot().add(position),
        Rotation.euler(
            Math.toRadians(rotation.x()),
            Math.toRadians(rotation.y()),
            Math.toRadians(rotation.z())),
        scale);
  }

  private Vector3 sample(List<GeckoLibRig.Frame> frames, double time, Vector3 fallback) {
    if (frames.isEmpty()) {
      return fallback;
    }
    var before = frames.get(0);
    for (int index = 1; index < frames.size(); index++) {
      var after = frames.get(index);
      if (time < after.time()) {
        return before.step()
            ? before.value()
            : before
                .value()
                .lerp(
                    after.value(),
                    Math.max(0, (time - before.time()) / (after.time() - before.time())));
      }
      before = after;
    }
    return before.value();
  }
}
