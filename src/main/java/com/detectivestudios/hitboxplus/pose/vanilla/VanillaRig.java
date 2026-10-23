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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class VanillaRig {

  private final Map<String, Part> parts = new LinkedHashMap<>();
  private final Map<String, BoneTransform> parents = new HashMap<>();
  private long generation;

  public VanillaRig(Map<String, float[]> definition) {
    definition.forEach((name, values) -> this.parts.put(name, new Part(values)));
    this.parts.forEach(
        (name, part) -> {
          part.name = name;
          if (!name.equals("$root")) {
            int separator = name.lastIndexOf('/');
            String parent = separator < 0 ? "$root" : name.substring(0, separator);
            part.parent = this.part(parent);
          }
        });
  }

  public Part part(String name) {
    var part = this.parts.get(name);
    if (part == null) {
      throw new IllegalArgumentException("Missing vanilla bone: " + name);
    }
    return part;
  }

  public boolean has(String name) {
    return this.parts.containsKey(name);
  }

  public void reset() {
    for (var part : this.parts.values()) {
      part.reset();
    }
    this.parents.clear();
  }

  public void parent(String name, double scale, double y, double z) {
    this.parents.put(name, new BoneTransform(new Vector3(0, y, z), Rotation.IDENTITY, scale));
  }

  public void parent(String name, BoneTransform transform) {
    this.parents.put(name, transform);
  }

  public void young(
      double headScale,
      double headY,
      double headZ,
      double bodyScale,
      double bodyY,
      String... heads) {
    var headParts = java.util.Set.of(heads);
    this.parts.keySet().stream()
        .filter(name -> !name.contains("/") && !name.equals("$root"))
        .forEach(
            name ->
                this.parent(
                    name,
                    headParts.contains(name) ? headScale : bodyScale,
                    headParts.contains(name) ? headY * headScale : bodyY * bodyScale,
                    headParts.contains(name) ? headZ * headScale : 0));
  }

  public Map<String, BoneTransform> compose() {
    this.generation++;
    var result = new LinkedHashMap<String, BoneTransform>();
    for (var part : this.parts.values()) {
      this.compose(part, result);
    }
    return result;
  }

  private BoneTransform compose(Part part, Map<String, BoneTransform> result) {
    if (part.generation == this.generation) {
      return part.composed;
    }
    BoneTransform parent =
        part.parent == null ? BoneTransform.IDENTITY : this.compose(part.parent, result);
    var override = this.parents.get(part.name);
    if (override != null) {
      parent = parent.compose(override);
    }
    var local = part.transform();
    if (part.resolvedParent != parent || part.resolvedLocal != local) {
      part.composed = parent.compose(local);
      part.resolvedParent = parent;
      part.resolvedLocal = local;
    }
    part.generation = this.generation;
    result.put(part.name, part.composed);
    return part.composed;
  }

  public static class Part {

    private final float bindX;
    private final float bindY;
    private final float bindZ;
    private final float bindPitch;
    private final float bindYaw;
    private final float bindRoll;
    private String name;
    private Part parent;
    private long generation = Long.MIN_VALUE;
    private BoneTransform composed;
    private BoneTransform resolvedParent;
    private BoneTransform resolvedLocal;
    private BoneTransform local;
    private float observedX;
    private float observedY;
    private float observedZ;
    private float observedPitch;
    private float observedYaw;
    private float observedRoll;

    public float x;
    public float y;
    public float z;
    public float pitch;
    public float yaw;
    public float roll;

    public Part(float[] values) {
      this.bindX = values[0];
      this.bindY = values[1];
      this.bindZ = values[2];
      this.bindPitch = values[3];
      this.bindYaw = values[4];
      this.bindRoll = values[5];
      this.reset();
    }

    public void reset() {
      this.set(this.bindX, this.bindY, this.bindZ);
      this.rotate(this.bindPitch, this.bindYaw, this.bindRoll);
    }

    public void set(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public void rotate(float pitch, float yaw, float roll) {
      this.pitch = pitch;
      this.yaw = yaw;
      this.roll = roll;
    }

    public void copy(Part other) {
      this.set(other.x, other.y, other.z);
      this.rotate(other.pitch, other.yaw, other.roll);
    }

    public BoneTransform transform() {
      if (this.local == null
          || this.x != this.observedX
          || this.y != this.observedY
          || this.z != this.observedZ
          || this.pitch != this.observedPitch
          || this.yaw != this.observedYaw
          || this.roll != this.observedRoll) {
        this.local =
            this.x == 0
                    && this.y == 0
                    && this.z == 0
                    && this.pitch == 0
                    && this.yaw == 0
                    && this.roll == 0
                ? BoneTransform.IDENTITY
                : new BoneTransform(
                    new Vector3(this.x, this.y, this.z),
                    Rotation.euler(this.pitch, this.yaw, this.roll),
                    1);
        this.observedX = this.x;
        this.observedY = this.y;
        this.observedZ = this.z;
        this.observedPitch = this.pitch;
        this.observedYaw = this.yaw;
        this.observedRoll = this.roll;
      }
      return this.local;
    }
  }
}
