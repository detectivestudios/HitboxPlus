# Local test environment

From the project root:

```powershell
.\runTests
```

In IntelliJ, run the root project's Gradle task **verification > runTests**.
On other platforms use `./gradlew runTests --console=plain`.
Use JDK 21 for Gradle and JDK 17 for Minecraft. Gradle manages Node.js automatically.
Chrome, Edge or Chromium must be installed. The runner detects the browser and creates its
own headless profile, local server and available ports, then closes them after testing.
It never attaches to an existing browser or editor workspace. No npm installation is needed.

The command checks test sources and browser availability, builds the mod and editor, runs
the Java/build-logic and Node tests, runs the dedicated-server GameTests and connected-client
network lifecycle tests, then tests the
editor's presets, imports, animations, editing, exports, persistence and mobile layout.
The game servers, Minecraft client and browser stages run sequentially to avoid competing for resources.
Failures stop the command with a nonzero exit code. Gradle can reuse unchanged Java test results;
add `--rerun-tasks` to force all tasks to execute again.

The first setup needs internet access for Gradle dependencies, Java toolchains and Node.
After setup, `.\runTests --offline` uses the local caches.
To select a browser explicitly, set its executable path for the current PowerShell session:

```powershell
$env:HITBOXPLUS_TEST_BROWSER = 'C:\Program Files\Google\Chrome\Application\chrome.exe'
.\runTests --offline
```

Reports and artifacts:

- Java: `build/reports/tests/test/index.html`
- Build logic: `buildSrc/build/reports/tests/test/index.html`
- Browser results, screenshots and exports: `build/reports/editor-browser/`
- Dedicated-server output and test world: `run/gametest/`
- Connected-client checks and client/server logs: `build/reports/network/`

## Connected-client network tests

Run just the network lifecycle scenario with:

```powershell
.\gradlew.bat testNetworkLifecycle --offline --console=plain
```

This launches separate Forge client and dedicated-server JVMs with a test-only mod under
`src/test/network/`. Production artifacts contain neither that mod nor its fixtures.
The server binds only to `127.0.0.1` on an available port, uses offline test authentication,
and runs in a fresh world under `build/tmp/network-tests/`. Its generated development-server
configuration includes the EULA acceptance file. No Minecraft account or normal game profile
is used. The client has an isolated game directory; its small window is hidden after startup.
A working desktop/OpenGL environment is required, even though the window is hidden.

The test checks real login synchronization, live datapack reload and client generation changes,
clearing profiles on disconnect, reloading while no clients are connected, and receiving only
the new generation on reconnect. It also checks that client disconnects leave authoritative
server state unchanged and that server shutdown clears server profiles. Datapacks are changed
on disk and reloaded through Minecraft; packet decoding and handling use the production channel.
Failures retain the isolated session directory for inspection. Successful sessions are removed.
Timeouts and cancellation stop only the processes created by the runner.

Before the first reload, the same isolated client compares horse hitbox corners with vertices
from Minecraft's real renderers. The 150 poses cover adults and babies of horses, donkeys, mules,
skeleton horses and zombie horses, including walking, grazing, rearing and transitions at three
partial ticks. The client report includes `horseRenderedGeometry`; the dedicated-server suite
also checks hits through the normal horse's visible flank.

For debugger work, set `HITBOXPLUS_NETWORK_TEST_DEBUG_PORT=5007` in the current shell before
running the network task, then attach an IntelliJ Remote JVM Debug configuration to
`127.0.0.1:5007`. Use logpoints first. Remove the variable after debugging. Never attach to
the user's normal client or server. Test run configuration names must sort after their Forge
`client` and `server` parents, because ForgeGradle 6 resolves inherited runs in name order.

## Optional test sources

The four local test folders are ignored by Git. To share them with another checkout, run:

```powershell
.\gradlew.bat testSourcesZip --offline --console=plain
```

The filename includes the build version: `build/distributions/hitboxplus-test-sources-<version>.zip`.
Extract the matching ZIP into the other project's root,
then run `.\runTests`. This restores the Java and Node tests together with their fixtures.
Full testing reports missing sources as an error; normal `build` still works without them.
The test sources are not added to the mod JAR or portable editor ZIP.

Every bundle includes `test-sources-manifest.json` with the project revision, a SHA-256 fingerprint
of production/build/tooling inputs, and SHA-256 checksums of all test sources and fixtures.
Imported bundles are checked before test compilation/execution, including build-logic tests.
A different project revision, changed test file, missing file or extra file rejects the bundle.
Text line endings are normalized so Windows and Unix checkouts can share the same bundle.
Import into clean test folders when upgrading, so removed files from an older bundle do not linger.
Do not edit manifest hashes or select local mode merely to get past a compatibility error.

## Maintaining local test sources

If you own and maintain the source tests in this checkout, explicitly enable local mode once:

```powershell
.\gradlew.bat useLocalTestSources --offline --console=plain
.\runTests --offline
```

Local mode allows tests to evolve with production changes and still runs every included suite.
The ignored local manifest records that choice. `testSourcesZip` always exports a strict bundle
manifest, regardless of local mode; importing that ZIP enables strict verification in the recipient.
Switch an imported bundle to local mode only when intentionally taking ownership of its tests.
After changing production code, tests or test tooling, rerun the full suite and regenerate the ZIP
before sharing it. The existing ZIP remains tied to the revision from which it was generated.
