# Hitbox+ documentation

Choose a guide based on what you want to do.

| Task | Guide |
| --- | --- |
| Understand the mod and get started | [Project overview](../README.md) |
| Create and edit hitbox profiles | [Hitbox+ Editor](../tools/web-editor/README.md) |
| Author models in Blockbench | [Blockbench plugin](../tools/blockbench/README.md) |
| Integrate hit queries and animations into another mod | [Public API](api.md) |
| Build from source or use a local Maven dependency | [Development](development.md) |
| Run tests and use revision-matched test bundles | [Testing](testing.md) |
| Check supported vanilla entities and variants | [Vanilla profile coverage](vanilla-hitbox-profiles.md) |
| Submit a fix or improvement | [Contributing](../.github/CONTRIBUTING.md) |
| Ask a question or report a problem | [Support](../.github/SUPPORT.md) |

## Repository layout

| Location | Purpose |
| --- | --- |
| `src/main/` | Forge mod source code and resources |
| `tools/web-editor/` | Offline browser Editor |
| `tools/blockbench/` | Blockbench plugin and shared authoring/import code |
| `tools/testing/` | Isolated browser and Minecraft test launchers |
| `docs/` | API, development, testing, and coverage guides |
| `.github/` | Issue forms, pull request template, and community guides |
| `gradle/` | Gradle wrapper, versioning, toolchain, and test-bundle configuration |
| `buildSrc/` | Shared Java formatting build logic |
| `.idea/fileTemplates/` | Shared IntelliJ templates for licensed Java source files |

The root contains the project overview, license and source-header notice, Gradle entry points,
and `runEditor`/`runTests` launchers. Build outputs, test bundles, worlds, and local agent notes
stay in ignored paths.
