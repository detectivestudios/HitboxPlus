# Development

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
connected-client and browser checks. See [testing](testing.md) for local development mode,
reports and revision-checked test source ZIPs.

The mod JAR is written to `build/libs/`; the portable editor ZIP is in `build/distributions/`.
Gradle prints the generated mod version. Consumers add `mavenLocal()` and use that version:

```groovy
implementation fg.deobf('com.detectivestudios.hitboxplus:hitboxplus:<version>')
```

The mod ID, Maven artifact, profile format and data directory use `hitboxplus`. Integrations use
`com.detectivestudios.hitboxplus.api.HitboxPlus`; update existing dependencies and profile paths
to these names.

See the [public API](api.md) for integration examples and the
[contribution guide](../.github/CONTRIBUTING.md) for submitting changes.
