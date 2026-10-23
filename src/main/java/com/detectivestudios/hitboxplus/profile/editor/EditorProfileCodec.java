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

package com.detectivestudios.hitboxplus.profile.editor;

import com.detectivestudios.hitboxplus.api.BodyPart;
import com.detectivestudios.hitboxplus.api.HitboxPart;
import com.detectivestudios.hitboxplus.api.NamedHitboxPart;
import com.detectivestudios.hitboxplus.geometry.Bounds;
import com.detectivestudios.hitboxplus.math.BoneTransform;
import com.detectivestudios.hitboxplus.math.Rotation;
import com.detectivestudios.hitboxplus.math.Vector3;
import com.detectivestudios.hitboxplus.pose.geckolib.GeckoLibRig;
import com.detectivestudios.hitboxplus.pose.geckolib.GeckoLibRigCodec;
import com.detectivestudios.hitboxplus.profile.EntityHitboxProfile;
import com.detectivestudios.hitboxplus.profile.VanillaProfileKey;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

public class EditorProfileCodec {

  public static final int MAX_DOCUMENT_BYTES = 262144;
  public static final int MAX_VOLUMES = 2048;

  public EditorProfileDocument decode(String json) {
    if (json.getBytes(StandardCharsets.UTF_8).length > MAX_DOCUMENT_BYTES) {
      throw new IllegalArgumentException("Hitbox profile exceeds 256 KiB");
    }
    return this.decode(JsonParser.parseString(json));
  }

  public EditorProfileDocument decode(JsonElement element) {
    JsonObject document = element.getAsJsonObject();
    if (!"hitboxplus".equals(this.text(document, "format"))
        || document.get("version").getAsInt() != 1) {
      throw new IllegalArgumentException("Expected Hitbox+ editor format version 1");
    }
    String json = document.toString();
    if (json.getBytes(StandardCharsets.UTF_8).length > MAX_DOCUMENT_BYTES) {
      throw new IllegalArgumentException("Hitbox profile exceeds 256 KiB");
    }
    var key = new VanillaProfileKey(this.text(document, "entity"), this.text(document, "variant"));
    var boneList = document.getAsJsonArray("bones");
    if (boneList == null || boneList.isEmpty() || boneList.size() > 1024) {
      throw new IllegalArgumentException("Expected 1–1024 bones");
    }
    var parents = new HashMap<String, String>();
    for (JsonElement value : boneList) {
      var bone = value.getAsJsonObject();
      String id = this.text(bone, "id");
      if (parents.containsKey(id)) {
        throw new IllegalArgumentException("Duplicate bone: " + id);
      }
      parents.put(
          id,
          bone.has("parent") && !bone.get("parent").isJsonNull()
              ? this.text(bone, "parent")
              : null);
      this.vector(bone, "pivot");
      this.optionalVector(bone, "rotation");
    }
    for (String bone : parents.keySet()) {
      this.checkHierarchy(bone, parents);
    }
    var volumeList = document.getAsJsonArray("volumes");
    if (volumeList == null || volumeList.isEmpty() || volumeList.size() > MAX_VOLUMES) {
      throw new IllegalArgumentException("Expected 1–2048 hitbox volumes");
    }
    var keys = new HashSet<String>();
    var volumes = new ArrayList<EntityHitboxProfile.Volume<String>>();
    for (JsonElement value : volumeList) {
      var volume = value.getAsJsonObject();
      if (!keys.add(this.text(volume, "key"))) {
        throw new IllegalArgumentException("Duplicate volume key");
      }
      String bone = this.text(volume, "bone");
      if (!parents.containsKey(bone)) {
        throw new IllegalArgumentException("Volume references unknown bone: " + bone);
      }
      BodyPart region = BodyPart.valueOf(this.text(volume, "region"));
      HitboxPart part =
          volume.has("part")
                  && !volume.get("part").isJsonNull()
                  && !volume.get("part").getAsString().isEmpty()
              ? new NamedHitboxPart(this.text(volume, "part"), region)
              : region;
      Vector3 origin = this.optionalVector(volume, "origin");
      Vector3 rotation = this.optionalVector(volume, "rotation");
      var bounds =
          new Bounds(
              this.vector(volume, "from").subtract(origin),
              this.vector(volume, "to").subtract(origin));
      volumes.add(
          new EntityHitboxProfile.Volume<>(
              part,
              bone,
              bounds,
              new BoneTransform(
                  origin,
                  Rotation.euler(
                      Math.toRadians(rotation.x()),
                      Math.toRadians(rotation.y()),
                      Math.toRadians(rotation.z())),
                  1)));
    }
    var rig =
        document.has("adapter")
            ? Optional.of(
                new GeckoLibRigCodec().decode(document.getAsJsonObject("adapter"), boneList))
            : Optional.<GeckoLibRig>empty();
    if (rig.isPresent() && !key.variant().equals("base")) {
      throw new IllegalArgumentException("Automatic GeckoLib rigs must use the base variant");
    }
    return new EditorProfileDocument(key, new EntityHitboxProfile<>(volumes), json, rig);
  }

  private void checkHierarchy(String bone, Map<String, String> parents) {
    var visited = new HashSet<String>();
    while (bone != null) {
      if (!visited.add(bone)) {
        throw new IllegalArgumentException("Bone hierarchy contains a cycle");
      }
      if (!parents.containsKey(bone)) {
        throw new IllegalArgumentException("Unknown parent bone: " + bone);
      }
      bone = parents.get(bone);
    }
  }

  private String text(JsonObject object, String key) {
    String value = object.get(key).getAsString();
    if (value.isBlank()
        || value.length() > 200
        || value.chars().anyMatch(Character::isISOControl)) {
      throw new IllegalArgumentException("Invalid text field: " + key);
    }
    return value;
  }

  private Vector3 optionalVector(JsonObject object, String key) {
    return object.has(key) ? this.vector(object, key) : Vector3.ZERO;
  }

  private Vector3 vector(JsonObject object, String key) {
    var array = object.getAsJsonArray(key);
    if (array == null || array.size() != 3) {
      throw new IllegalArgumentException("Expected a three-component vector: " + key);
    }
    double x = array.get(0).getAsDouble();
    double y = array.get(1).getAsDouble();
    double z = array.get(2).getAsDouble();
    if (!Double.isFinite(x)
        || !Double.isFinite(y)
        || !Double.isFinite(z)
        || Math.abs(x) > 1000000
        || Math.abs(y) > 1000000
        || Math.abs(z) > 1000000) {
      throw new IllegalArgumentException("Invalid vector coordinate: " + key);
    }
    return new Vector3(x, y, z);
  }
}
