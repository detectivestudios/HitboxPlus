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

package com.detectivestudios.hitboxplus.profile.vanilla.v118;

import com.detectivestudios.hitboxplus.profile.EntityHitboxProfile;
import com.detectivestudios.hitboxplus.profile.VanillaProfileKey;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class VanillaProfiles {

  public Map<VanillaProfileKey, EntityHitboxProfile<String>> create() {
    var profiles =
        new TreeMap<VanillaProfileKey, EntityHitboxProfile<String>>(
            Comparator.comparing(VanillaProfileKey::entityId)
                .thenComparing(VanillaProfileKey::variant));
    new AquaticMobProfiles().addTo(profiles);
    new ArmorStandProfiles().addTo(profiles);
    new FarmAnimalProfiles().addTo(profiles);
    new FlyingMobProfiles().addTo(profiles);
    new HumanoidProfiles().addTo(profiles);
    new MonsterProfiles().addTo(profiles);
    new VillagerProfiles().addTo(profiles);
    new WildAnimalProfiles().addTo(profiles);
    return Collections.unmodifiableMap(new LinkedHashMap<>(profiles));
  }
}
