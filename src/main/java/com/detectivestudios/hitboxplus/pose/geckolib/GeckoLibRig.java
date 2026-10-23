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

import com.detectivestudios.hitboxplus.math.Vector3;
import java.util.List;
import java.util.Map;

/** Renderer-independent GeckoLib/Bedrock rig, converted into Minecraft model coordinates. */
public record GeckoLibRig(
    List<Bone> bones,
    Map<String, Clip> clips,
    Map<String, String> states,
    double scale,
    Vector3 offset,
    double blendTicks) {

  public GeckoLibRig {
    bones = List.copyOf(bones);
    clips = Map.copyOf(clips);
    states = Map.copyOf(states);
  }

  public record Bone(String id, String parent, Vector3 pivot, Vector3 rotation) {}

  public record Clip(double length, boolean loop, Map<String, Channels> bones) {

    public Clip {
      bones = Map.copyOf(bones);
    }
  }

  public record Channels(List<Frame> position, List<Frame> rotation, List<Frame> scale) {

    public Channels {
      position = List.copyOf(position);
      rotation = List.copyOf(rotation);
      scale = List.copyOf(scale);
    }
  }

  public record Frame(double time, Vector3 value, boolean step) {}
}
