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

package com.detectivestudios.hitboxplus.runtime;

import com.detectivestudios.hitboxplus.profile.EntityHitboxProfile;
import com.detectivestudios.hitboxplus.profile.VanillaProfileKey;
import com.detectivestudios.hitboxplus.profile.editor.EditorProfileDocument;
import com.mojang.logging.LogUtils;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;

/** Immutable generations owned separately by the logical server and the connected client. */
public class ProfileOverrides {

  public static final int MAX_PROFILES = 256;
  // Reserve room for the count, length prefixes and message discriminator in the 1 MiB payload.
  public static final int MAX_SYNC_BYTES = 1048576 - 1024;
  private static final Logger logger = LogUtils.getLogger();
  private Map<VanillaProfileKey, EditorProfileDocument> documents = Map.of();
  private Set<String> entityIds = Set.of();
  private final Set<String> warnings = new HashSet<>();
  private int revision;

  public void replace(Collection<EditorProfileDocument> documents) {
    if (documents.size() > MAX_PROFILES) {
      throw new IllegalArgumentException("At most 256 override profiles are supported");
    }
    int bytes = 0;
    var replacement = new LinkedHashMap<VanillaProfileKey, EditorProfileDocument>();
    for (EditorProfileDocument document : documents) {
      bytes += document.json().getBytes(StandardCharsets.UTF_8).length;
      if (replacement.put(document.key(), document) != null) {
        throw new IllegalArgumentException("Duplicate override: " + document.key());
      }
    }
    if (bytes > MAX_SYNC_BYTES) {
      throw new IllegalArgumentException("Combined hitbox profiles exceed 1 MiB");
    }
    this.documents = Map.copyOf(replacement);
    this.entityIds =
        replacement.keySet().stream()
            .map(VanillaProfileKey::entityId)
            .collect(Collectors.toUnmodifiableSet());
    this.warnings.clear();
    this.revision++;
  }

  public boolean hasEntity(String entityId) {
    return this.entityIds.contains(entityId);
  }

  public void warnOnce(String entityId, String reason) {
    if (this.warnings.add(entityId)) {
      logger.warn("Hitbox+ override for {} cannot be applied: {}", entityId, reason);
    }
  }

  public Optional<EntityHitboxProfile<String>> find(String entityId, String variant) {
    var exact = this.documents.get(new VanillaProfileKey(entityId, variant));
    if (exact == null && variant.endsWith("_layers")) {
      exact =
          this.documents.get(
              new VanillaProfileKey(
                  entityId, variant.substring(0, variant.length() - "_layers".length())));
    }
    var document =
        exact == null ? this.documents.get(new VanillaProfileKey(entityId, "base")) : exact;
    return document == null ? Optional.empty() : Optional.of(document.profile());
  }

  public List<EditorProfileDocument> documents() {
    return List.copyOf(this.documents.values());
  }

  public Optional<EditorProfileDocument> baseDocument(String entityId) {
    return Optional.ofNullable(this.documents.get(new VanillaProfileKey(entityId, "base")));
  }

  public int revision() {
    return this.revision;
  }
}
