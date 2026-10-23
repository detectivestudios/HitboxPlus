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

import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class VanillaRigs {

  private final Map<String, Map<String, float[]>> rigs = this.load();

  public VanillaRig pose(String kind, String variant) {
    var definition = this.rigs.get("minecraft:" + kind + "/" + variant);
    if (definition == null) {
      throw new IllegalArgumentException("Missing vanilla rig: " + kind + "/" + variant);
    }
    return new VanillaRig(definition);
  }

  private Map<String, Map<String, float[]>> load() {
    try (var stream = VanillaRigs.class.getResourceAsStream("/data/hitboxplus/vanilla_rigs.json")) {
      if (stream == null) {
        throw new IllegalStateException("Missing vanilla rig definitions");
      }
      var json = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
      var result = new LinkedHashMap<String, Map<String, float[]>>();
      for (var rig : json.getAsJsonObject().entrySet()) {
        var bones = new LinkedHashMap<String, float[]>();
        for (var bone : rig.getValue().getAsJsonObject().entrySet()) {
          var values = bone.getValue().getAsJsonArray();
          float[] pose = new float[6];
          for (int index = 0; index < pose.length; index++) {
            pose[index] = values.get(index).getAsFloat();
          }
          bones.put(bone.getKey(), pose);
        }
        result.put(rig.getKey(), bones);
      }
      return result;
    } catch (IOException exception) {
      throw new IllegalStateException("Cannot read vanilla rigs", exception);
    }
  }
}
