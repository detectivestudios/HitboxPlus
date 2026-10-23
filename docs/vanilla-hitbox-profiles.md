# Vanilla entity hitbox profiles

The Java 17 kernel now bundles **90 profiles for 73 vanilla Minecraft 1.18.2 entity types**,
containing **861 bone-local volumes**. It retains separate cubes rather than filling their
enclosing box. Profiles include cube deformation and zero-thickness fins/feathers.

The [Forge runtime](../README.md) automatically enables every catalog type, players and the Ender
Dragon. Common pose evaluators supply animation, young scaling, visibility and world transforms
without loading client models on dedicated servers. The vanilla picking/projectile hooks and public
API consume those poses. Pufferfish inflation, tropical fish shapes, illager arms, goat horns,
horse baby legs, turtle egg bellies and armor stand arms select their corresponding variants.
Slime and magma cube providers apply size and squash/stretch to their collision geometry.
Baby llamas use uniform part scales; vanilla's nonuniform baby render proportions remain approximate.
Creeper fuse swelling retains ordinary collision because rotated legs under nonuniform scale
cannot be represented exactly by the rigid bone API.

Resource-pack geometry, modded models and custom third-party projectile tracers still require
explicit integrations. Purely cosmetic random offsets and camera-dependent effects are not
authoritative animation inputs; guardian tail/spike motion uses synchronized state and entity age.

## Coverage

All IDs below use the `minecraft:` namespace and have a `base` profile.

| Family | Entity IDs |
| --- | --- |
| Humanoid monsters | zombie, husk, drowned, zombie_villager, skeleton, stray, wither_skeleton, piglin, piglin_brute, zombified_piglin, giant, enderman, vex |
| Villagers and raids | villager, wandering_trader, witch, evoker, illusioner, pillager, vindicator, ravager |
| Farm animals and mounts | pig, cow, mooshroom, sheep, goat, horse, donkey, mule, llama, trader_llama, skeleton_horse, zombie_horse |
| Wild/tamed animals and Nether mounts | wolf, cat, ocelot, fox, rabbit, polar_bear, panda, hoglin, zoglin, strider |
| Birds, bats and insects | chicken, parrot, bat, bee, phantom |
| Aquatic animals | axolotl, cod, salmon, tropical_fish, pufferfish, dolphin, squid, glow_squid, guardian, elder_guardian, turtle |
| Other monsters and golems | creeper, spider, cave_spider, silverfish, endermite, blaze, ghast, slime, magma_cube, shulker, iron_golem, snow_golem, wither |
| Other | armor_stand |

The Ender Dragon has a separate multipart provider with 65 volumes, including five neck and twelve
tail segments. Queries against its vanilla damage parts use the corresponding mesh sections, so
projectile hits retain vanilla head/body damage routing. Its repeated segments are not part of the
static editor catalog. Players use the standard/slim player profiles. Vehicles, projectiles,
block-rendered decorations, modded models and resource-pack geometry are outside this living-model
catalog.

## Variants

| Entity | Additional variant | Shape/visibility |
| --- | --- | --- |
| pufferfish | `medium`, `full` | Inflation meshes; `base` is small |
| tropical_fish | `large` | Large body template; `base` is small |
| sheep | `wool` | Base anatomy plus fur, including the exposed snout; `base` is sheared |
| horse, donkey, mule, skeleton_horse, zombie_horse | `young` | Baby leg meshes; `base` uses adult legs |
| goat | `young` | Horns omitted |
| evoker, illusioner, pillager, vindicator | `separate_arms` | Individual arms; `base` uses folded arms |
| turtle | `gravid` | Additional egg belly |
| slime | `inner` | Inner mesh; `base` is the outer body |
| armor_stand | `arms` | Visible arms; `base` omits arms |

Variants describe model topology/visibility, not a complete age or animation transform.
Young head/body scales still come from the adapter. Saddles, chests, humanoid armor/outer hats,
unused piglin cape geometry, and block/item decorations are excluded from the relevant base
profiles. Existing damage/equipment logic owns armor and equipment behavior.

Combined folded-arm meshes and paired bee-leg planes retain named identities with
`BodyPart.OTHER` where a cube cannot accurately be assigned to one side. Fins and tentacles
have common regions; other extra anatomy retains namespaced identities and semantic regions.
Pig snouts and cow horns/udders keep their existing named identities.

## API

```java
var catalog = new VanillaMobProfiles();
var wolf = catalog.find("minecraft:wolf").orElseThrow();
var inflated = catalog.find("minecraft:pufferfish", "full").orElseThrow();
var wool = catalog.find("minecraft:sheep", "wool").orElseThrow();
var pose = new EntityBodyGeometry().create(wolf, authoritativeBones::get, worldRoot);
```

Lookup returns `Optional.empty()` for unknown entities/variants and does not silently choose
another shape. `profiles()` returns an immutable map of `VanillaProfileKey` to profile.
`zombie()`, `pig()`, and `cow()` remain available.

Bone keys are vanilla model paths such as `head_parts/head/left_ear` or `body/tail`,
without a leading slash; `$root` denotes a root cube. Bounds use model pixels, relative to
that specific bone. The resolver supplies each bone's fully composed transform in model space.
`worldRoot` converts model pixels to blocks and applies the renderer/world transform.
Shared templates such as giant/zombie or guardian/elder_guardian still need the appropriate
species scale. Size-dependent mobs such as slimes need their entity-specific scale.
Nonuniform renderer squash/stretch requires an adapter or geometry extension; it is not encoded
by the uniform `BoneTransform` scale or by these static mesh definitions.

Maintain volume order and part identity across motion observations. A visibility/profile
change is a topology discontinuity; start a new motion interval instead of interpolating
unmatched volumes. Optional bone visibility beyond these variants can be represented by an
adapter-owned filtered `EntityHitboxProfile`.

## Source and maintenance

The committed catalog consists of typed Java factories under
[`profile.vanilla.v118`](../src/main/java/com/detectivestudios/hitboxplus/profile/vanilla/v118/VanillaProfiles.java).
The public `VanillaMobProfiles` lookup API constructs these definitions directly.
Geometry factories do not require external data files. Common animation providers load the bundled
`data/hitboxplus/vanilla_rigs.json` bind transforms, exported from the reviewed editor catalog.
Optional authored JSON overrides now load from server data resources/datapacks. The
[Blockbench workflow](../tools/blockbench/README.md) exports this format and includes
the full catalog plus standard/slim player presets. JSON replaces geometry, while live providers
continue to supply animation.
Minecraft rendering classes remain outside the kernel.

Each family has its own class: `HumanoidProfiles`, `VillagerProfiles`,
`FarmAnimalProfiles`, `WildAnimalProfiles`, `FlyingMobProfiles`, `AquaticMobProfiles`,
`MonsterProfiles`, and `ArmorStandProfiles`. Each entity/variant has a small factory method.
`ProfileVolumes` constructs typed bounds, and `VanillaProfiles` assembles an immutable catalog
in the same entity/variant order as before.

The development-only
[`VanillaModelFixtures`](../src/test/java/com/detectivestudios/hitboxplus/client/hitbox/VanillaModelFixtures.java)
selects Minecraft model factories and explicit hidden parts. The exporter uses actual compiled
cube vertices, since Minecraft's public cube min/max fields omit deformation.
[`VanillaProfileJavaWriter`](../src/test/java/com/detectivestudios/hitboxplus/client/hitbox/VanillaProfileJavaWriter.java)
emits Java classes with explicit `BodyPart` references, named part constructors and numeric
bounds. Output ordering is deterministic by family, entity, variant, bone path and cube index.

```powershell
.\gradlew.bat exportVanillaHitboxProfiles --offline --console=plain
.\gradlew.bat exportHitboxPlusEditorCatalog --offline --console=plain
node tools/blockbench/export-runtime-rigs.cjs
.\gradlew.bat test --offline --console=plain
```

Export is an explicit authoring action and never runs during a normal build. Format generated
Java with `spotlessApply`, then review geometry,
semantic regions, hidden parts and the generated diff after changing factories/mapping.
Manual definition edits survive ordinary builds; explicit regeneration replaces the generated
classes using the model factories and semantic mapping.
No geometry or code was copied from the Body Part Damage reference.

`VanillaProfileMeshTest` exercises all 90 profiles in their bind pose and with independent
bone rotations, checking actual rendered vertices, profile corners, parent transforms and
world scale/orientation. Catalog tests also check lookup, semantic regions, visibility variants,
deformation and immutability. Existing player and young pig/cow/zombie
mesh checks remain. Runtime rig checks compare all 90 hierarchies with vanilla meshes. Animation
parity tests compare walking and idle motion with vanilla models. Dedicated-server GameTests
exercise every registered catalog type, young models, live variants, slime deformation and dragon
damage-part routing.

The isolated connected client also compares every hitbox corner against vertices emitted by the
actual horse renderers, in both directions. It covers all five horse types as adults and babies,
idle, walking, grazing, rearing and transitioning, at three partial ticks (150 poses). This includes
normal horses' 1.1 renderer scale and the longer baby legs. A dedicated-server test checks shots
through the visible flank outside the old undersized box.

Run `.\gradlew.bat build --offline --console=plain` from the project root to validate the
standalone mod, typed profile definitions and authoring tools. Publish the validated mod with
`publishToMavenLocal` for local integrations through the public API.
