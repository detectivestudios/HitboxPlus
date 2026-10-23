# Contributing to Hitbox+

Bug reports, feature suggestions, questions, and pull requests are welcome in the
[official repository](https://github.com/detectivestudios/HitboxPlus).
Please follow the [community conduct guidelines](CODE_OF_CONDUCT.md) and keep discussions focused
on the project.

## Report a problem or suggest a feature

Search [existing issues](https://github.com/detectivestudios/HitboxPlus/issues) first. If there
is no matching report, [open an issue](https://github.com/detectivestudios/HitboxPlus/issues/new/choose)
and choose the bug report, feature request, or question form.

For general questions, early ideas, and sharing your work, use
[Discussions](https://github.com/detectivestudios/HitboxPlus/discussions).

For bugs, include the exact Hitbox+ version or commit, relevant environment details, steps to
reproduce, and expected versus actual behavior. Hitbox problems are easier to investigate with
the entity type, model variant, animation/pose, and a screenshot or minimal shareable profile.
For crashes, attach the relevant log or crash report after removing private information.

## Submit a pull request

1. For substantial changes, open an issue first so the approach can be discussed.
2. Fork the official repository and create a focused branch from `master`.
3. Make the change, update relevant documentation, and run the checks below.
4. Open a pull request against `detectivestudios/HitboxPlus:master`. Explain the problem,
   resulting behavior, and validation. Link the related issue when there is one.

Small fixes can go directly to a pull request. Draft pull requests are welcome for work in
progress. Maintainers may request revisions before merging.

## Build and validate

Hitbox+ targets Forge 1.18.2 and Java 17. Run Gradle and the formatter with JDK 21; the mod
compiles and runs with Java 17. Gradle manages the Node.js runtime used by the authoring tools.
The first build needs internet access to populate the dependency and toolchain caches.

From the project root on Windows:

```powershell
.\gradlew.bat spotlessApply build --console=plain
```

On other platforms, use `./gradlew spotlessApply build --console=plain`. Once dependencies
are cached, add `--offline`. Java uses two-space indentation, explicit imports, UTF-8, and LF
line endings. Preserve existing license headers; the formatter manages Java headers.

The source checkout can build without the optional local test sources. Such a build does not
run the absent suites; state which checks actually ran in the pull request. If you need the
full test sources, ask a maintainer for the bundle matching your revision. Follow
[docs/testing.md](docs/testing.md) for strict bundle compatibility and local test ownership.
Never change an imported bundle to local mode just to bypass a compatibility error.

With matching test sources available, run the full suite:

```powershell
.\runTests --offline
```

This includes Java and Node tests, dedicated-server checks, a connected Minecraft client, and
isolated browser checks. Editor changes require the authoring/import suites and relevant
isolated browser checks. Networking or profile changes must retain the connected-client
lifecycle suite. Geometry, animation, or scale changes must retain the horse renderer
comparisons and dedicated-server flank-hit regression. Use isolated game processes, worlds,
and browser profiles for automation.

Keep optional test sources, fixtures, manifests, generated ZIPs, screenshots, worlds, build
outputs, and credentials out of commits. Describe test changes in the pull request and
coordinate a matching test-source bundle with the maintainer when needed.

## Architecture and compatibility

- Keep all production Java under `com.detectivestudios.hitboxplus`.
- Keep client rendering dependencies isolated from dedicated-server class loading.
- Preserve server-authoritative hit queries, nearest-contact selection, block occlusion,
  and stable body-part identity/order.
- Keep the mod independent of unrelated mods at build time and runtime. Integrations belong
  behind the public API.
- Preserve the offline Editor, local assets, and project `runEditor` launcher. Match the
  existing JavaScript style and avoid runtime npm dependencies.

## License and contribution rights

Read the [DetectiveStudios Source-Available Contribution License](LICENSE.txt) before
submitting. It permits contribution forks and pull requests to the official repository;
it does not grant general redistribution or reuse rights. Contributors retain ownership
of their original work and grant the contribution permissions described in the license.
Submit only material you are entitled to contribute and identify any third-party licenses.
