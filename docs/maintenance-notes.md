# Maintenance notes

## Horse hitbox correction (2026-10-23)

- Normal horses were missing the vanilla renderer's 1.1 scale in `VanillaModelRoot`. Debugger
  inspection confirmed a rendered scale of 1.1 with an unscaled torso half-width of 0.315625
  blocks; the isolated renderer comparison reproduced a 0.1549295744-block corner mismatch.
  The shared root now applies the renderer scale on both client and server.
- Baby skeleton and zombie horses were using adult leg meshes. They now have explicit `young`
  profiles, runtime rigs and editor presets generated from the vanilla baby meshes.
- Keep `HorseHitboxChecks` in the isolated client suite. It compares actual rendered vertices
  with hitbox corners in both directions for 150 poses across all five horse types and both ages.
  `BuiltinProviderGameTests.horseRendererScaleKeepsVisibleFlanksHittableOnTheServer` checks
  that shots through the previously missed visible flank hit on a dedicated server.
- Use `testNetworkLifecycle` for the real-renderer regression and `runTests` for full validation.
  Do not replace these comparisons with packet encoding tests or bind-pose mesh checks alone.

## Test workflow

Use [testing.md](testing.md) for the maintained commands, reports and bundle compatibility rules.
Keep the real client/server network lifecycle test in the full suite. Mock packet/codec tests
alone do not cover login, live profile reload, disconnect cleanup or reconnect behavior.
The isolated fixture mod is under `src/test/network/`; the launcher is `tools/testing/network.cjs`.
Use `testNetworkLifecycle` for focused checks and `runTests` before publishing.
Versioned test bundles include a strict compatibility manifest. Local test authors explicitly
choose `useLocalTestSources`; imported bundles must never be refreshed or converted automatically.
