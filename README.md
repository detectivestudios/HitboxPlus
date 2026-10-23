# Hitbox+ by DetectiveStudios

Animated body-part hitboxes for Minecraft Forge 1.18.2 / Java 17, with a local Editor and
Blockbench authoring tools. Install matching mod versions on clients and the server.

## Get started

| I want to... | Start here |
| --- | --- |
| Create hitboxes for an entity | [Hitbox+ Editor](tools/web-editor/README.md) |
| Work in Blockbench | [Blockbench plugin](tools/blockbench/README.md) |
| Use Hitbox+ in my mod | [Public API guide](docs/api.md) |
| Build the project | [Development guide](docs/development.md) |
| Report a problem or suggest a feature | [Issue forms](https://github.com/detectivestudios/HitboxPlus/issues/new/choose) |
| Ask a general question or share work | [Discussions](https://github.com/detectivestudios/HitboxPlus/discussions) |

Browse the [documentation index](docs/README.md) for all guides and the repository layout.

## How it works

Profiles define volumes attached to bones. Providers evaluate animation from synchronized
entity state and transform those volumes into world space. The server owns combat queries
and geometry overrides; clients receive synchronized profiles for matching geometry and
F3+B debug rendering.

Built-in providers support players, all 73 vanilla living-model catalog types, and the Ender
Dragon. Providers handle animation, model variants, and entity size on the server and client.
Unsupported modded entities keep ordinary collision. See [vanilla coverage](docs/vanilla-hitbox-profiles.md).

Hitboxes affect combat and targeting. Minecraft's movement collision stays unchanged, and the
consuming mod decides how a confirmed body-part hit affects damage.

## Author hitboxes

Run the Editor from the project root:

```powershell
.\runEditor
```

Import models and animations, preview hitbox volumes, and export profiles or installable
Minecraft datapacks. The [Editor guide](tools/web-editor/README.md) covers setup, supported
formats, editing, and offline use.

Bundle profiles under `src/main/resources/data/<namespace>/hitboxplus/profiles/`, or install an
exported datapack in the server world's `datapacks` folder. Use `/reload` for datapack changes.
JSON overrides replace geometry while providers supply live animation.

## Public API

```java
var pose = HitboxPlus.getPose(entity, partialTick);
var hit = HitboxPlus.trace(entity, start, end, partialTick);
```

The [API guide](docs/api.md) covers body-part hits, custom providers, model transforms,
GeckoLib adapters, and server-authoritative integration.

### Composable humanoid animations

Combine vanilla motion, prone, lean, and custom animation through `HumanoidPoseAnimation`.
See [animation composition](docs/api.md#composable-humanoid-animations) for the full example,
units, and synchronization requirements.

## Build and integrate locally

Use JDK 21 for Gradle and JDK 17 for Minecraft. The first build needs internet access; later
builds can use cached dependencies with `--offline`.

```powershell
.\gradlew.bat build --offline --console=plain
```

See [development](docs/development.md) for local Maven publishing and consuming-mod setup,
and [testing](docs/testing.md) for the full suite and revision-matched test-source bundles.

## Contribute

[Report a bug, suggest a feature, or ask a question](https://github.com/detectivestudios/HitboxPlus/issues/new/choose).
Search [existing issues](https://github.com/detectivestudios/HitboxPlus/issues) first.
For code or documentation changes, read the [contribution guide](.github/CONTRIBUTING.md)
and open a pull request against `master`.

Follow the [community conduct guidelines](.github/CODE_OF_CONDUCT.md). Contributions and reuse
are governed by the [DetectiveStudios Source-Available Contribution License](LICENSE.txt).
