# Public API

Integrations use `com.detectivestudios.hitboxplus.api`:

```java
var pose = HitboxPlus.getPose(entity, partialTick);
var hit = HitboxPlus.trace(entity, start, end, partialTick);
```

An empty pose means custom geometry is unavailable. An empty hit for an available pose is a real
miss, including gaps between body parts. Custom projectile engines must call these queries from
their collision code. The caller handles block occlusion, nearest-target selection and damage
on the server. `BodyHit` identifies the contacted part and region.

Native `ProjectileUtil` queries return `BodyEntityHitResult` for Hitbox+ contacts.
Its `getBodyHit()` retains the part identity, surface normal and segment fraction; ordinary
unsupported-entity hits retain vanilla `EntityHitResult`. Consuming mods can inspect the
confirmed body region without tracing the same segment again.

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
[adapter guide](../tools/web-editor/README.md#geckolib-animation-adapter) for supported features.

## Composable humanoid animations

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

## Query lifecycle and threading

Built-in providers reuse poses while the sampled entity state is unchanged, and refresh them
when animation, synchronized data, or profiles change. Query live entities on their game thread;
the resulting immutable poses can be traced on other threads. Custom provider callbacks remain
live unless the provider implements its own cache.

See [development](development.md) for dependency setup and [documentation](README.md) for other guides.
