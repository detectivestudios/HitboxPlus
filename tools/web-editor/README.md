# Hitbox+ Studio

A local browser editor for Minecraft entity hitboxes. Choose a vanilla preset, import a model,
fit the colored volumes, preview animations, then download a profile or an installable datapack.
All editing and file processing happen in your browser. Fonts, scripts and 3D libraries are local;
no account, external CDN, upload endpoint or npm installation is needed at runtime.

The header reads `DetectiveStudios | Hitbox+ - Editor` beside the supplied logo.
The interface uses a charcoal/amber theme and a
`© <current year> DetectiveStudios` footer on desktop and mobile. Branding is bundled into the
standalone HTML as well as the static website, so it remains available offline.

## Start

From the repository's main folder, run:

```powershell
.\runEditor
```

You can also double-click `runEditor.cmd` in that folder. The project launcher uses Gradle to
download the pinned Node.js runtime into `.gradle/nodejs/` on first use, rebuilds the editor,
and opens **http://127.0.0.1:8787** in your default browser. Use the project's Gradle JDK 21;
the first setup requires internet access. No system Node installation or PATH changes are needed.
IntelliJ's Gradle `runEditor` task uses the same managed runtime, as do builds and authoring tests.
Keep its terminal open while editing; press Ctrl+C to stop the local server. If the browser
does not open automatically, use the URL printed in the terminal. For a different port, run
`.\runEditor --port 8788`.
Launching again reopens the existing editor when the same project is already serving that port.

For the easiest start, double-click **`hitboxplus-studio.html`** in the extracted editor ZIP.
It includes the entire editor and works offline in a modern browser, without installing Node or
running a web server. In a source checkout, the generated file is under `dist/`.

The included `start-editor.cmd` also opens the standalone HTML directly. For development or a
local HTTP server outside the repository, install a supported Node.js LTS release and run:

```text
node server.cjs --open
```

Open **http://127.0.0.1:8787**. Use `--port 8788` if the default port is occupied. The server binds
only to loopback and serves static editor files; it does not write to your Minecraft project.
Press Ctrl+C in its terminal to stop it. Running from source builds the local assets automatically
if `dist` is missing. In the repository, rebuild with `.\gradlew.bat bundleWebEditor` and
run both authoring test suites with `.\gradlew.bat testBlockbenchPlugin testWebEditor`.
Direct `node build.cjs` also works if you already have Node installed.

The repository also builds a portable `build/distributions/hitboxplus-studio.zip`.
Extract it and open its HTML file, or run its launcher/server command. The included `dist` folder can be hosted on
any ordinary static web server to share the editor. This task creates the local website and ZIP;
it does not deploy a public instance.

## Edit a model

1. Choose one of the 92 vanilla/player presets, or import `.geo.json`, cube-based `.bbmodel`, or
   Hitbox+ JSON. Dragging a file onto the editor works too. For geometry files containing
   multiple models, choose the model in the dialog. Java files exported by the Blockbench plugin
   can be reopened without executing Java.
2. Set **Target entity** to the actual registry ID, for example `my_mod:mutant`. Presets retain their
   exact variant; automatic GeckoLib rigs use `base`.
3. Select a volume in the viewport or list. Use the **Move**, **Rotate** and **Resize** gizmos, or
   enter exact position, size, rotation and pivot values. Add/duplicate/delete boxes as needed.
   Assign a bone and a damage region; optional namespaced part IDs identify extra anatomy.
4. Toggle the reference geometry, grid or bone lines to inspect the fit. **Frame** fits the camera.
   Keyboard shortcuts are W/E/R for tools, F for framing, Ctrl+Z/Ctrl+Y for undo/redo and Space
   for playback. Arrow keys move the selected box by 1 model pixel: Left/Right on X, Up/Down on Y
   (Up decreases Y), and Shift+Up/Down on Z (Up decreases Z). Holding a key repeats the movement.
   Movement includes the rotation pivot and supports undo/redo. Form inputs keep their normal
   keyboard behavior.
5. The current workspace is restored from local browser storage. Export files to retain multiple
   models or to move work between devices. Loading another model replaces the current workspace.

The viewport uses model pixels; geometry is exported in the shared Minecraft coordinate system.
The reference is an independent copy of the imported geometry and is excluded from profile exports.
Textures and arbitrary mesh geometry are not required for editing cube hitboxes. Models containing
meshes need cube hitbox proxies authored in Blockbench first.

## GeckoLib animation adapter

Importing `.geo.json` creates a profile with an automatic **GeckoLib rig** adapter. Import a matching
`.animation.json`, then open **Animation adapter** to bind clips to idle, walk, sprint, attack,
death and swim. The timeline previews individual clips. Model scale and transition blend ticks
are editable. A rig without clips is explicitly a reference pose, shown with a warning.

The server evaluates the exported hierarchy/keyframes and selects bound clips from trusted entity
state. No hand-written entity provider is needed for this supported workflow. The default root
matches GeckoLib 3's standard entity renderer, including its coordinate conversion and 0.01-block
render offset. Existing vanilla providers remain the default for vanilla presets.

Supported animation data is finite numeric position/rotation keyframes, linear/step interpolation,
looping/clamped playback and positive uniform bone scale. Unsupported Molang, custom animation
timing, nonuniform scale, spline/easing modes and discontinuous pre/post keyframes fail explicitly
during import. They are never evaluated as JavaScript or silently baked into a wrong pose.
Custom renderer transforms, procedural head tracking and arbitrary mod controller logic use
[`GeckoLibAnimationSource`](../../src/main/java/com/detectivestudios/hitboxplus/pose/geckolib/GeckoLibAnimationSource.java)
on the server. See the [adapter API](../../README.md#geckolib-adapter).

GeckoLib 4 is not a dependency. The adapter consumes the compatible geometry/numeric animation
format and does not load GeckoLib's client controllers on a dedicated server. A mod's visual
controller must play the same bound clips/timing, or supply its authoritative selection through
the source API, for exact visual parity.

## Export to Minecraft

**Export profile JSON** produces a ready-to-load Hitbox+ document. Put it under:

```text
src/main/resources/data/your_mod/hitboxplus/profiles/your_entity.json
```

**Download ready-to-use datapack** produces a ZIP with `pack.mcmeta` and the correct data directory.
Put the ZIP directly in `world/datapacks` and run `/reload`. Removing it and reloading restores the
default profile. This format targets Minecraft 1.18.2. The target ID in the profile determines the
entity; the data namespace is `hitboxplus_custom`.

Install matching Hitbox+ versions on server and clients. The server synchronizes accepted
profiles and rigs; clients cannot submit their own authoritative geometry. F3+B shows active boxes.
The profile modifies combat/targeting geometry; physical movement continues to use vanilla AABBs.

## Development and validation

Run from the repository root using the managed runtime:

```powershell
.\gradlew.bat testWebEditor bundleWebEditor editorZip --console=plain
```

Gradle's `testWebEditor`, `bundleWebEditor` and `editorZip` tasks perform these checks/builds.
The full build includes the editor ZIP, but the mod JAR contains no browser assets or test models.
Three.js 0.160.1 is vendored with its MIT license; see [THIRD-PARTY.txt](THIRD-PARTY.txt).

`test/browser-smoke.cjs` exercises a real, explicitly isolated Chrome instance via DevTools on
port 9241, with this editor served on port 8787. It covers all presets, edits, assignments, undo/redo,
animation import/playback, JSON round trips, datapack output, persistence and mobile overflow.
It replaces its test workspace; do not attach it to a browser profile containing user work.
