'use strict';

const REGIONS = Object.freeze([
  'HEAD', 'TORSO', 'LEFT_ARM', 'RIGHT_ARM', 'LEFT_LEG', 'RIGHT_LEG', 'LEFT_FOOT',
  'RIGHT_FOOT', 'NECK', 'FRONT_LEFT_LEG', 'FRONT_RIGHT_LEG', 'HIND_LEFT_LEG',
  'HIND_RIGHT_LEG', 'TAIL', 'LEFT_WING', 'RIGHT_WING', 'LEFT_FIN', 'RIGHT_FIN',
  'FIN', 'TENTACLE', 'OTHER'
]);
const RESOURCE_ID = /^[a-z0-9_.-]+:[a-z0-9/._-]+$/;
const JAVA_IDENTIFIER = /^[A-Za-z_$][A-Za-z0-9_$]*$/;
const JAVA_RESERVED = new Set(('abstract assert boolean break byte case catch char class const '
  + 'continue default do double else enum extends final finally float for goto if implements '
  + 'import instanceof int interface long native new package private protected public return '
  + 'short static strictfp super switch synchronized this throw throws transient try void '
  + 'volatile while true false null record var yield sealed permits _').split(' '));

function vector(value, label) {
  if (!Array.isArray(value) || value.length !== 3
      || value.some(component => typeof component !== 'number' || !Number.isFinite(component)
        || Math.abs(component) > 1000000)) {
    throw new Error(label + ' must contain three finite coordinates within ±1,000,000.');
  }
  return value.slice();
}

function text(value, label, maximum = 200) {
  if (typeof value !== 'string' || !value.trim() || value.length > maximum
      || /[\x00-\x1f]/.test(value)) {
    throw new Error(label + ' must be a nonempty, single-line string.');
  }
  return value;
}

function validateDocument(input) {
  if (!input || input.format !== 'hitboxplus' || input.version !== 1) {
    throw new Error('Expected a Hitbox+ editor document, version 1.');
  }
  const entity = text(input.entity, 'Entity ID');
  if (!RESOURCE_ID.test(entity)) throw new Error('Entity ID must use namespace:path.');
  const variant = text(input.variant, 'Variant');
  if (!/^[a-z0-9_.-]+$/.test(variant)) throw new Error('Variant must be a lowercase identifier.');
  if (!Array.isArray(input.bones) || !input.bones.length || input.bones.length > 1024) {
    throw new Error('A profile needs between 1 and 1024 bones.');
  }
  const bones = input.bones.map(bone => ({
    id: text(bone.id, 'Bone ID'),
    parent: bone.parent == null ? null : text(bone.parent, 'Parent bone'),
    pivot: vector(bone.pivot, 'Bone pivot'),
    rotation: vector(bone.rotation || [0, 0, 0], 'Bone rotation')
  }));
  const lookup = new Map(bones.map(bone => [bone.id, bone]));
  if (lookup.size !== bones.length) throw new Error('Bone IDs must be unique.');
  const ordered = [], visiting = new Set(), visited = new Set();
  function visit(bone) {
    if (visited.has(bone.id)) return;
    if (visiting.has(bone.id)) throw new Error('Bone hierarchy contains a cycle.');
    visiting.add(bone.id);
    if (bone.parent != null) {
      if (!lookup.has(bone.parent)) throw new Error('Unknown parent bone: ' + bone.parent);
      visit(lookup.get(bone.parent));
    }
    visiting.delete(bone.id);
    visited.add(bone.id);
    ordered.push(bone);
  }
  bones.forEach(visit);
  if (!Array.isArray(input.volumes) || !input.volumes.length || input.volumes.length > 2048) {
    throw new Error('A profile needs between 1 and 2048 hitbox volumes.');
  }
  const keys = new Set();
  const volumes = input.volumes.map((volume, index) => {
    const key = text(volume.key || 'box_' + index, 'Volume key');
    if (keys.has(key)) throw new Error('Volume keys must be unique: ' + key);
    keys.add(key);
    const bone = text(volume.bone, 'Volume bone');
    if (!lookup.has(bone)) throw new Error('Unknown hitbox bone: ' + bone);
    if (!REGIONS.includes(volume.region)) throw new Error('Unknown body region: ' + volume.region);
    const part = volume.part || null;
    if (part != null && (typeof part !== 'string' || !RESOURCE_ID.test(part))) {
      throw new Error('Custom part ID must use lowercase namespace:path.');
    }
    const from = vector(volume.from, 'Volume minimum');
    const to = vector(volume.to, 'Volume maximum');
    if (from.some((value, axis) => value > to[axis])) {
      throw new Error('Volume minimum must not exceed its maximum.');
    }
    return {key, bone, region: volume.region, part, from, to,
      origin: vector(volume.origin || [0, 0, 0], 'Volume pivot'),
      rotation: vector(volume.rotation || [0, 0, 0], 'Volume rotation')};
  });
  const document = {format: 'hitboxplus', version: 1, entity, variant, bones: ordered, volumes};
  if (input.adapter != null) {
    if (variant !== 'base') throw new Error('Automatic GeckoLib rigs use the base variant.');
    document.adapter = validateAdapter(input.adapter, lookup);
  }
  return document;
}

function validateAdapter(input, bones) {
  if (input.type !== 'geckolib') throw new Error('Unknown pose adapter type.');
  const number = (value, fallback, min, max) => {
    value = value ?? fallback;
    if (typeof value !== 'number' || !Number.isFinite(value) || value < min || value > max) {
      throw new Error('Invalid adapter number.');
    }
    return value;
  };
  const clips = Object.create(null), entries = Object.entries(input.clips || {});
  if (entries.length > 32) throw new Error('A rig supports at most 32 clips.');
  let count = 0;
  for (const [name, clip] of entries) {
    text(name, 'Clip name');
    const length = number(clip.length, 1, 0.001, 3600), channels = Object.create(null);
    for (const [bone, source] of Object.entries(clip.bones || {})) {
      if (!bones.has(bone)) throw new Error('Animation references unknown bone: ' + bone);
      const tracks = {};
      for (const kind of ['position', 'rotation', 'scale']) {
        let previous = -1;
        tracks[kind] = (source[kind] || []).map(frame => {
          const time = number(frame.time, 0, 0, length);
          if (time <= previous) throw new Error('Animation times must increase.');
          previous = time;
          const value = vector(frame.value, 'Animation value');
          if (kind === 'scale' && (value[0] <= 0 || value[0] > 100
              || value[0] !== value[1] || value[0] !== value[2])) {
            throw new Error('Only positive uniform bone scale is supported.');
          }
          const interpolation = frame.interpolation || 'linear';
          if (!['linear', 'step'].includes(interpolation)) throw new Error('Unsupported interpolation.');
          if (++count > 8192) throw new Error('A rig supports at most 8192 keyframes.');
          return {time, value, interpolation};
        });
      }
      channels[bone] = tracks;
    }
    clips[name] = {length, loop: !!clip.loop, bones: channels};
  }
  const states = {};
  for (const [state, clip] of Object.entries(input.states || {})) {
    if (!['idle', 'walk', 'sprint', 'attack', 'death', 'swim'].includes(state)
        || typeof clip !== 'string'
        || !Object.hasOwn(clips, clip)) throw new Error('Unknown animation state/clip: ' + state);
    states[state] = clip;
  }
  return {type: 'geckolib', scale: number(input.scale, 1, 0.01, 100),
    offset: vector(input.offset || [0, 0, 0], 'Model offset'),
    blend_ticks: number(input.blend_ticks, 4, 0, 40), clips, states};
}

function add(a, b) { return a.map((value, axis) => value + b[axis]); }
function subtract(a, b) { return a.map((value, axis) => value - b[axis]); }
function editorPoint(value) { return [value[0], 24 - value[1], value[2]]; }
function nativePoint(value) { return editorPoint(value); }
function reflectedRotation(value) { return [-value[0] || 0, value[1] || 0, -value[2] || 0]; }

function bindPivots(document) {
  const result = new Map();
  for (const bone of document.bones) {
    result.set(bone.id, add(bone.parent == null ? [0, 0, 0] : result.get(bone.parent), bone.pivot));
  }
  return result;
}

function editorBox(volume, pivot) {
  const a = editorPoint(add(pivot, volume.from));
  const b = editorPoint(add(pivot, volume.to));
  return {from: a.map((value, axis) => Math.min(value, b[axis])),
    to: a.map((value, axis) => Math.max(value, b[axis])),
    origin: editorPoint(add(pivot, volume.origin)),
    rotation: reflectedRotation(volume.rotation)};
}

function nativeBox(cube, pivot) {
  const a = subtract(nativePoint(cube.from), pivot);
  const b = subtract(nativePoint(cube.to), pivot);
  const inflate = cube.inflate || 0;
  if (!Number.isFinite(inflate)) throw new Error('Cube inflation must be finite.');
  return {from: a.map((value, axis) => Math.min(value, b[axis]) - inflate),
    to: a.map((value, axis) => Math.max(value, b[axis]) + inflate),
    origin: subtract(nativePoint(cube.origin), pivot),
    rotation: reflectedRotation(cube.rotation)};
}

function parseDocument(content) {
  const marker = '/* HITBOXPLUS_DOCUMENT\n';
  const start = content.indexOf(marker);
  if (start >= 0) {
    const end = content.indexOf('\n*/', start);
    if (end < 0) throw new Error('Incomplete Hitbox+ document comment.');
    content = content.substring(start + marker.length, end);
  }
  let parsed;
  try { parsed = JSON.parse(content.replace(/^\uFEFF/, '')); }
  catch (error) { throw new Error('Import a .hitboxplus.json document or Java exported by this plugin.'); }
  return validateDocument(parsed);
}

function javaIdentifier(value, label) {
  if (!JAVA_IDENTIFIER.test(value) || JAVA_RESERVED.has(value)) {
    throw new Error(label + ' must be a valid Java identifier.');
  }
  return value;
}

function javaNumber(value) {
  if (!Number.isFinite(value)) throw new Error('Cannot export a nonfinite Java coordinate.');
  let result = String(Object.is(value, -0) ? 0 : value);
  if (!/[.eE]/.test(result)) result += '.0';
  return result + 'D';
}

function javaString(value) {
  return JSON.stringify(value).replace(/[\u2028\u2029]/g,
    character => '\\u' + character.charCodeAt(0).toString(16));
}

function javaVector(value) { return 'new Vector3(' + value.map(javaNumber).join(', ') + ')'; }

function inferRegion(bone) {
  const name = bone.toLowerCase();
  if (/head|skull|ear|horn|nose|snout|beak/.test(name)) return 'HEAD';
  if (/tail/.test(name)) return 'TAIL';
  if (/neck/.test(name)) return 'NECK';
  const side = /left|^l_|_l$/.test(name) ? 'LEFT' : /right|^r_|_r$/.test(name) ? 'RIGHT' : null;
  if (side && /wing/.test(name)) return side + '_WING';
  if (side && /arm|hand/.test(name)) return side + '_ARM';
  if (side && /foot/.test(name)) return side + '_FOOT';
  if (side && /leg/.test(name)) {
    if (/front|fore/.test(name)) return 'FRONT_' + side + '_LEG';
    if (/hind|rear|back/.test(name)) return 'HIND_' + side + '_LEG';
    return side + '_LEG';
  }
  return /body|torso|chest|abdomen/.test(name) ? 'TORSO' : 'OTHER';
}

function exportJava(input, packageName, className) {
  const document = validateDocument(input);
  text(packageName, 'Java package');
  packageName.split('.').forEach(value => javaIdentifier(value, 'Package segment'));
  javaIdentifier(className, 'Class name');
  const imports = [
    'com.detectivestudios.hitboxplus.api.BodyPart',
    'com.detectivestudios.hitboxplus.geometry.Bounds',
    'com.detectivestudios.hitboxplus.math.BoneTransform',
    'com.detectivestudios.hitboxplus.math.Rotation',
    'com.detectivestudios.hitboxplus.math.Vector3',
    'com.detectivestudios.hitboxplus.profile.EntityHitboxProfile'
  ];
  if (document.volumes.some(volume => volume.part)) {
    imports.splice(1, 0, 'com.detectivestudios.hitboxplus.api.NamedHitboxPart');
  }
  imports.push('java.util.LinkedHashMap', 'java.util.List', 'java.util.Map');
  const lines = ['package ' + packageName + ';', '', ...imports.map(value => 'import ' + value + ';'),
    '', '/** Geometry for ' + document.entity + ' (' + document.variant + '). */',
    'public class ' + className + ' {', '', '  public EntityHitboxProfile<String> create() {',
    '    return new EntityHitboxProfile<>(List.of('];
  document.volumes.forEach((volume, index) => {
    const part = volume.part
      ? 'new NamedHitboxPart(' + javaString(volume.part) + ', BodyPart.' + volume.region + ')'
      : 'BodyPart.' + volume.region;
    lines.push('        new EntityHitboxProfile.Volume<>(' + part + ',',
      '            ' + javaString(volume.bone) + ', new Bounds(',
      '                ' + javaVector(subtract(volume.from, volume.origin)) + ',',
      '                ' + javaVector(subtract(volume.to, volume.origin)) + '),',
      '            new BoneTransform(' + javaVector(volume.origin) + ',',
      '                Rotation.euler(',
      ...volume.rotation.map((degrees, axis) => '                    '
        + javaNumber(degrees * Math.PI / 180) + (axis < 2 ? ',' : '),')),
      '                1.0D))' + (index === document.volumes.length - 1 ? '));' : ','));
  });
  lines.push('  }', '', '  /** Reference pose only; live providers supply current animation transforms. */',
    '  public Map<String, BoneTransform> bindPose() {', '    var bones = new LinkedHashMap<String, BoneTransform>();');
  for (const bone of document.bones) {
    lines.push('    bones.put(' + javaString(bone.id) + ',',
      ...(bone.parent == null ? [] : ['        bones.get(' + javaString(bone.parent) + ').compose(']),
      '        new BoneTransform(' + javaVector(bone.pivot) + ',',
      '            Rotation.euler(',
      ...bone.rotation.map((degrees, axis) => '                '
        + javaNumber(degrees * Math.PI / 180) + (axis < 2 ? ',' : '),')),
      '            1.0D)' + (bone.parent == null ? ');' : '));'));
  }
  lines.push('    return Map.copyOf(bones);', '  }', '}', '',
    '/* HITBOXPLUS_DOCUMENT',
    JSON.stringify(document, null, 2).replace(/\*\//g, '*\\/'), '*/', '');
  return lines.join('\n');
}

module.exports = {REGIONS, validateDocument, parseDocument, exportJava, bindPivots,
  editorPoint, nativePoint, reflectedRotation, editorBox, nativeBox, add, subtract, inferRegion};
