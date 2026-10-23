# Hitbox+ by DetectiveStudios

A Forge 1.18.2 / Java 17 mod by DetectiveStudios for animated, body-part hitboxes.
Install matching versions on clients and the server. Other mods integrate through the public API.

## How it works

Profiles define volumes attached to bones. Providers evaluate animation from synchronized entity
state and transform those volumes into world space. The server owns combat queries and geometry
overrides; clients receive synchronized profiles for matching geometry and F3+B debug rendering.

Built-in providers support players, all 73 vanilla living-model catalog types and the Ender Dragon.
Providers handle animation, model variants and entity size on the server and client. Unsupported
modded entities keep ordinary collision. See [vanilla coverage](docs/vanilla-hitbox-profiles.md).

Hitboxes affect combat and targeting. Minecraft's movement collision stays unchanged, and the
consuming mod decides how a confirmed body-part hit affects damage.

## Public API

Integrations use `com.detectivestudios.hitboxplus.api`:

```java
var pose = HitboxPlus.getPose(entity, partialTick);
var hit = HitboxPlus.trace(entity, start, end, partialTick);
```

An empty pose means custom geometry is unavailable. An empty hit for an available pose is a real
miss, including gaps between body parts. Custom projectile engines must call these queries from
their collision code. The caller handles block occlusion, nearest-target selection and damage
on the server. `BodyHit` identifies the contacted part and region.

Register custom providers on your **mod event bus** during `RegisterHitboxProvidersEvent`:

```java
@SubscribeEvent
public static void registerHitboxes(RegisterHitboxProvidersEvent event) {
  event.providers().register(MyEntities.CREATURE.get(), CreatureHitboxProvider::new);
}
```

Implement `EntityHitboxProvider.getPose(float)` to return current world-space hitboxes. To support
JSON geometry overrides, also implement `getModelPose(float)`: bone transforms use model pixels
and must include their parent transforms; the root maps pixels into world blocks. Keep volume
identities and order stable between observations used for moving hit queries.

`BodyPart` provides common damage regions; `NamedHitboxPart` supports namespaced custom anatomy.
For lower-level tracing and transforms, use `HitboxTracer`, `MovingHitboxTracer`, `BoneTransform`
and `EntityBodyGeometry`.

GeckoLib geometry and numeric animations can use the editor's exported rig adapter without a
GeckoLib runtime dependency. Custom controller selection uses `registerGeckoLib(...)` with a
`GeckoLibAnimationSource`. Keep animation logic driven by synchronized entity state. See the
[adapter guide](tools/web-editor/README.md#geckolib-animation-adapter) for supported features.

### Composable humanoid animations

`HumanoidPoseAnimation` is the shared interface for vanilla humanoid motion, `PronePose`,
`LeanPose` and custom animations. Prone and lean accept the animation to run first and a function
that supplies their current state. For example, register a player provider on your mod bus:

```java
event.providers().register(EntityType.PLAYER, player -> {
  var state = MyPlayerAnimationState.get(player); // Your mod's synchronized, entity-owned state.
  var prone = new PronePose(new HumanoidAnimation(), input -> new PronePose.State(
      state.proneProgress(input.partialTick),
      state.aimProgress(input.partialTick),
      state.holdsGun(),
      state.handcuffed()));
  HumanoidPoseAnimation animation = new LeanPose(prone, input -> new LeanPose.State(
      state.leanAngleDegrees(input.partialTick),
      state.proneProgress(input.partialTick)));
  return new PlayerHitboxProvider(player, animation);
});
```

This evaluates vanilla motion, then prone, then lean. New animations implement
`HumanoidPoseAnimation.evaluate(HumanoidAnimation.Input)` and can wrap an existing animation
the same way. Return a fresh mutable `HumanoidPose` for each evaluation, and treat the input
as read-only. Its `partialTick` is the interpolation fraction passed to the hitbox query;
head angles are in degrees, while resulting pose rotations are in radians and pivots in pixels.

State sources must only sample state. Advance transitions once per game tick in the consuming
mod and synchronize their inputs for client rendering; hit queries must use server-owned state.
Prone progress and aim are fractions in `[0, 1]`; lean angles are in degrees. Prone attack
adjustments remain available through `PronePose.applyAttack(...)` for integrations owning
their attack animation.

The injected animation is used for both live hitboxes and `getModelPose`, including JSON geometry
overrides. Custom animations bypass the vanilla-only pose cache so changes to addon state are
visible within the same tick. The default player constructor retains vanilla animation and
caching, and the existing direct `apply(...)` methods remain available. These animations change
model-space bone poses; integrations requiring a different world-space root supply their own
`EntityHitboxProvider`.

## Author hitboxes

Run the local editor from the project root:

```powershell
.\runEditor
```

[Hitbox+ Editor](tools/web-editor/README.md) imports models and animations, previews volumes,
and exports profiles or ready-to-install datapacks. A [Blockbench plugin](tools/blockbench/README.md)
is also available.

Bundle profiles under `src/main/resources/data/<namespace>/hitboxplus/profiles/`, or install an
exported datapack in the server world's `datapacks` folder. Use `/reload` for datapack changes.
JSON overrides replace geometry while providers supply live animation.

Built-in providers reuse poses while the sampled entity state is unchanged, and refresh them
when animation, synchronized data or profiles change. Query live entities on their game thread;
the resulting immutable poses can be traced on other threads. Custom provider callbacks remain
live unless the provider implements its own cache.

## Build and integrate locally

Use JDK 21 for Gradle and JDK 17 for the mod. Gradle downloads the pinned Node.js runtime
into the project's ignored `.gradle/nodejs/` directory for authoring tasks and `.\runEditor`.
No system Node installation or PATH changes are needed. The first setup needs internet access;
run `.\gradlew.bat nodeSetup --console=plain` once before building offline.

```powershell
.\gradlew.bat build --offline --console=plain
.\gradlew.bat publishToMavenLocal --offline --console=plain
```

Test sources and fixtures are optional local files excluded by `.gitignore`. The build runs
included Java and Node test suites and skips absent suites. They are not needed to build the
mod or editor. Keep each included suite's fixtures with its test sources.

Run `.\runTests` for the full local test environment, including dedicated-server and isolated
connected-client and browser checks. See [testing](docs/testing.md) for local development mode,
reports and revision-checked test source ZIPs.

The mod JAR is written to `build/libs/`; the portable editor ZIP is in `build/distributions/`.
Gradle prints the generated mod version. Consumers add `mavenLocal()` and use that version:

```groovy
implementation fg.deobf('com.detectivestudios.hitboxplus:hitboxplus:<version>')
```

The mod ID, Maven artifact, profile format and data directory use `hitboxplus`. Integrations use
`com.detectivestudios.hitboxplus.api.HitboxPlus`; update existing dependencies and profile paths
to these names.

## Contribute

[Report a bug, suggest a feature, or ask a question](https://github.com/detectivestudios/HitboxPlus/issues/new/choose).
Search [existing issues](https://github.com/detectivestudios/HitboxPlus/issues) before opening a new one.
Use [Discussions](https://github.com/detectivestudios/HitboxPlus/discussions) for general questions,
ideas, and sharing your work.

To contribute code or documentation, fork the repository and open a pull request against `master`.
Read the [contribution guide](CONTRIBUTING.md) for setup, validation, and the review process.
Contributions and reuse are governed by the
[DetectiveStudios Source-Available Contribution License](LICENSE.txt).

Before submitting, run:

```powershell
.\gradlew.bat spotlessApply build --offline --console=plain
```

The build checks Java formatting, runtime tests and authoring/import tests. Editor changes also
need the relevant isolated browser checks described in the editor guides. Keep server authority
intact and client rendering dependencies isolated from dedicated-server code.
