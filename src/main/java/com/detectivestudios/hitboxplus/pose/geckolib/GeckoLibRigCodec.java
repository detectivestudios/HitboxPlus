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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeckoLibRigCodec {

  private static final Set<String> STATES =
      Set.of("idle", "walk", "sprint", "attack", "death", "swim");

  public GeckoLibRig decode(JsonObject adapter, JsonArray definitions) {
    if (!"geckolib".equals(adapter.get("type").getAsString())) {
      throw new IllegalArgumentException("Unknown pose adapter type");
    }
    var raw = new LinkedHashMap<String, GeckoLibRig.Bone>();
    for (var value : definitions) {
      var bone = value.getAsJsonObject();
      String id = bone.get("id").getAsString();
      String parent =
          bone.has("parent") && !bone.get("parent").isJsonNull()
              ? bone.get("parent").getAsString()
              : null;
      raw.put(
          id,
          new GeckoLibRig.Bone(
              id,
              parent,
              this.vector(bone, "pivot", Vector3.ZERO),
              this.vector(bone, "rotation", Vector3.ZERO)));
    }
    var ordered = new ArrayList<GeckoLibRig.Bone>();
    var visited = new HashSet<String>();
    for (var bone : raw.values()) {
      this.order(bone, raw, visited, ordered);
    }
    var clips = new LinkedHashMap<String, GeckoLibRig.Clip>();
    var animations = adapter.has("clips") ? adapter.getAsJsonObject("clips") : new JsonObject();
    if (animations.size() > 32) {
      throw new IllegalArgumentException("At most 32 animation clips are supported");
    }
    int frames = 0;
    for (var entry : animations.entrySet()) {
      if (entry.getKey().isBlank() || entry.getKey().length() > 200) {
        throw new IllegalArgumentException("Invalid clip name");
      }
      var clip = entry.getValue().getAsJsonObject();
      double length = this.number(clip, "length", 1, 0.001, 3600);
      var channels = new HashMap<String, GeckoLibRig.Channels>();
      for (var channel : clip.getAsJsonObject("bones").entrySet()) {
        if (!raw.containsKey(channel.getKey())) {
          throw new IllegalArgumentException(
              "Animation references unknown bone: " + channel.getKey());
        }
        var tracks = channel.getValue().getAsJsonObject();
        var position = this.track(tracks, "position", length);
        var rotation = this.track(tracks, "rotation", length);
        var scale = this.track(tracks, "scale", length);
        for (var frame : scale) {
          var s = frame.value();
          if (s.x() <= 0 || s.x() > 100 || s.x() != s.y() || s.x() != s.z()) {
            throw new IllegalArgumentException("Only positive uniform bone scale is supported");
          }
        }
        frames += position.size() + rotation.size() + scale.size();
        if (frames > 8192) {
          throw new IllegalArgumentException("At most 8192 animation keyframes are supported");
        }
        channels.put(channel.getKey(), new GeckoLibRig.Channels(position, rotation, scale));
      }
      clips.put(
          entry.getKey(),
          new GeckoLibRig.Clip(
              length, clip.has("loop") && clip.get("loop").getAsBoolean(), channels));
    }
    var states = new LinkedHashMap<String, String>();
    if (adapter.has("states")) {
      for (var entry : adapter.getAsJsonObject("states").entrySet()) {
        String clip = entry.getValue().getAsString();
        if (!STATES.contains(entry.getKey()) || !clips.containsKey(clip)) {
          throw new IllegalArgumentException("Unknown animation state/clip: " + entry.getKey());
        }
        states.put(entry.getKey(), clip);
      }
    }
    return new GeckoLibRig(
        ordered,
        clips,
        states,
        this.number(adapter, "scale", 1, 0.01, 100),
        this.vector(adapter, "offset", Vector3.ZERO),
        this.number(adapter, "blend_ticks", 4, 0, 40));
  }

  private void order(
      GeckoLibRig.Bone bone,
      Map<String, GeckoLibRig.Bone> raw,
      Set<String> visited,
      List<GeckoLibRig.Bone> result) {
    if (visited.add(bone.id())) {
      if (bone.parent() != null) {
        this.order(raw.get(bone.parent()), raw, visited, result);
      }
      result.add(bone);
    }
  }

  private List<GeckoLibRig.Frame> track(JsonObject object, String name, double length) {
    var frames = new ArrayList<GeckoLibRig.Frame>();
    if (object.has(name)) {
      double previous = -1;
      for (var element : object.getAsJsonArray(name)) {
        var frame = element.getAsJsonObject();
        double time = this.number(frame, "time", 0, 0, length);
        if (time <= previous) {
          throw new IllegalArgumentException("Animation times must be strictly increasing");
        }
        String interpolation =
            frame.has("interpolation") ? frame.get("interpolation").getAsString() : "linear";
        if (!interpolation.equals("linear") && !interpolation.equals("step")) {
          throw new IllegalArgumentException(
              "Unsupported animation interpolation: " + interpolation);
        }
        frames.add(
            new GeckoLibRig.Frame(
                time, this.vector(frame, "value", null), interpolation.equals("step")));
        previous = time;
      }
    }
    return frames;
  }

  private double number(JsonObject object, String name, double fallback, double min, double max) {
    double value = object.has(name) ? object.get(name).getAsDouble() : fallback;
    if (!Double.isFinite(value) || value < min || value > max) {
      throw new IllegalArgumentException("Invalid adapter value: " + name);
    }
    return value;
  }

  private Vector3 vector(JsonObject object, String name, Vector3 fallback) {
    if (!object.has(name) && fallback != null) {
      return fallback;
    }
    var values = object.getAsJsonArray(name);
    if (values == null || values.size() != 3) {
      throw new IllegalArgumentException("Expected three numeric components: " + name);
    }
    double[] coordinates = new double[3];
    for (int axis = 0; axis < 3; axis++) {
      if (!values.get(axis).isJsonPrimitive()
          || !values.get(axis).getAsJsonPrimitive().isNumber()) {
        throw new IllegalArgumentException("Expressions require a custom server animation source");
      }
      coordinates[axis] = values.get(axis).getAsDouble();
      if (!Double.isFinite(coordinates[axis]) || Math.abs(coordinates[axis]) > 1000000) {
        throw new IllegalArgumentException("Invalid animation coordinate");
      }
    }
    return new Vector3(coordinates[0], coordinates[1], coordinates[2]);
  }
}
