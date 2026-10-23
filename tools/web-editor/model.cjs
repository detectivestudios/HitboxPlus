'use strict';

const authoring = require('../blockbench/src/authoring.cjs');
const clone = value => JSON.parse(JSON.stringify(value));
const numeric = (value, label) => {
  if (!Array.isArray(value) || value.length !== 3 || value.some(v => typeof v !== 'number'
      || !Number.isFinite(v))) throw new Error(label + ': numeric vectors required; expressions need a custom server source.');
  return value.slice();
};
const subtract = (a, b) => a.map((v, i) => v - b[i]);
const zero = [0, 0, 0];

function importGeometry(input, index = 0, entity = 'example:entity') {
  const source = input['minecraft:geometry']?.[index] || input;
  if (!Array.isArray(source.bones) || !source.bones.length) throw new Error('No Bedrock/GeckoLib bones found.');
  const definitions = source.bones;
  const pivot = bone => { const p = numeric(bone.pivot || zero, 'Bone pivot'); return [p[0], 24 - p[1], p[2]]; };
  const lookup = new Map(definitions.map(b => [b.name, b]));
  const bones = [], volumes = [];
  for (const bone of definitions) {
    if (bone.parent && !lookup.has(bone.parent)) throw new Error('Unknown parent: ' + bone.parent);
    const absolute = pivot(bone);
    bones.push({id: bone.name, parent: bone.parent || null,
      pivot: bone.parent ? subtract(absolute, pivot(lookup.get(bone.parent))) : absolute,
      rotation: numeric(bone.rotation || zero, 'Bone rotation')});
    for (const [index, cube] of (bone.cubes || []).entries()) {
      if (bone.neverRender || cube.visibility === false) continue;
      const origin = numeric(cube.origin, 'Cube origin'), size = numeric(cube.size, 'Cube size');
      if (size.some(v => v < 0)) throw new Error('Cube sizes cannot be negative.');
      const inflate = cube.inflate ?? bone.inflate ?? 0;
      if (typeof inflate !== 'number' || !Number.isFinite(inflate)) throw new Error('Invalid cube inflation.');
      const from = [origin[0] - inflate, 24 - origin[1] - size[1] - inflate, origin[2] - inflate];
      const to = [origin[0] + size[0] + inflate, 24 - origin[1] + inflate, origin[2] + size[2] + inflate];
      const p = numeric(cube.pivot || zero, 'Cube pivot');
      volumes.push({key: bone.name + '_' + index, bone: bone.name,
        region: authoring.inferRegion(bone.name), from: subtract(from, absolute),
        to: subtract(to, absolute), origin: subtract([p[0], 24 - p[1], p[2]], absolute),
        rotation: numeric(cube.rotation || zero, 'Cube rotation')});
    }
  }
  return authoring.validateDocument({format: 'hitboxplus', version: 1, entity, variant: 'base',
    bones, volumes, adapter: {type: 'geckolib', clips: {}, states: {}}});
}

function importBbmodel(input, entity = 'example:entity') {
  if (!Array.isArray(input.elements) || !Array.isArray(input.outliner)) throw new Error('Expected a Blockbench model.');
  const elements = new Map(input.elements.map(e => [e.uuid, e]));
  const groups = new Map((input.groups || []).map(g => [g.uuid, g]));
  const bones = [], volumes = [], used = new Set();
  const id = wanted => { let name = wanted, index = 1; while (used.has(name)) name = wanted + '_' + index++; used.add(name); return name; };
  const root = {id: '$root', parent: null, pivot: zero, rotation: zero};
  bones.push(root);
  const visit = (items, parent, parentPivot) => {
    for (let item of items) {
      if (typeof item === 'string') item = elements.get(item) || groups.get(item);
      else if (item?.uuid && groups.has(item.uuid)) item = {...groups.get(item.uuid), ...item};
      if (!item || item.export === false || item.visibility === false) continue;
      if (item.children) {
        const pivot = authoring.nativePoint(item.origin || zero);
        const bone = {id: id(item.hitboxplus_bone || item.name || 'bone'), parent,
          pivot: subtract(pivot, parentPivot), rotation: authoring.reflectedRotation(item.rotation || zero)};
        bones.push(bone);
        visit(item.children, bone.id, pivot);
      } else if (item.from && item.to && !item.hitboxplus_reference) {
        const box = authoring.nativeBox({...item, origin: item.origin || parentPivot,
          rotation: item.rotation || zero}, parentPivot);
        volumes.push({key: item.hitboxplus_key || id('box'), bone: parent,
          region: item.hitboxplus_region || authoring.inferRegion(parent),
          part: item.hitboxplus_part || null, ...box});
      } else if (item.type && item.type !== 'cube') {
        throw new Error('Mesh elements need cube hitbox proxies in Blockbench before import.');
      }
    }
  };
  visit(input.outliner, '$root', zero);
  const metadata = input.hitboxplus_profile || {};
  const document = {format: 'hitboxplus', version: 1, entity: metadata.entity || entity,
    variant: metadata.variant || 'base', bones, volumes};
  if (metadata.adapter) document.adapter = metadata.adapter;
  else if (/gecko|bedrock/i.test(input.meta?.model_format || '')) {
    for (const bone of bones) {
      bone.pivot[0] = -bone.pivot[0] || 0;
      bone.rotation[1] = -bone.rotation[1] || 0;
      bone.rotation[2] = -bone.rotation[2] || 0;
    }
    for (const volume of volumes) {
      const left = volume.from[0];
      volume.from[0] = -volume.to[0] || 0;
      volume.to[0] = -left || 0;
      volume.origin[0] = -volume.origin[0] || 0;
      volume.rotation[1] = -volume.rotation[1] || 0;
      volume.rotation[2] = -volume.rotation[2] || 0;
    }
    document.adapter = {type: 'geckolib', clips: {}, states: {}};
  }
  return authoring.validateDocument(document);
}

function importAnimations(document, input) {
  const result = clone(document);
  if (!result.adapter) throw new Error('Animation import requires a GeckoLib rig. Import a .geo.json first.');
  if (!input.animations || typeof input.animations !== 'object') throw new Error('No animation clips found.');
  const clips = Object.assign(Object.create(null), result.adapter.clips);
  for (const [name, animation] of Object.entries(input.animations)) {
    if (animation.anim_time_update || animation.override_previous_animation) {
      throw new Error(name + ': custom timing/layers require a server animation source.');
    }
    const channels = Object.create(null);
    let last = 0;
    for (const [bone, source] of Object.entries(animation.bones || {})) {
      const target = {};
      for (const kind of ['position', 'rotation', 'scale']) {
        if (source[kind] == null) continue;
        let raw = source[kind];
        if (typeof raw === 'number' && kind === 'scale') raw = [raw, raw, raw];
        const frames = Array.isArray(raw) ? [['0', raw]] : Object.entries(raw);
        target[kind] = frames.map(([time, value]) => {
          if (value && !Array.isArray(value) && typeof value === 'object') {
            if (value.pre && value.post && JSON.stringify(value.pre) !== JSON.stringify(value.post)) {
              throw new Error(name + ': discontinuous pre/post keyframes need a custom source.');
            }
            const interpolation = value.lerp_mode || value.easing || 'linear';
            if (!['linear', 'step'].includes(interpolation)) throw new Error(name + ': unsupported easing ' + interpolation);
            return frame(time, value.post || value.pre || value.vector, interpolation);
          }
          if (typeof value === 'number' && kind === 'scale') value = [value, value, value];
          return frame(time, value, 'linear');
        }).sort((a, b) => a.time - b.time);
        function frame(time, value, interpolation) {
          time = Number(time);
          if (!Number.isFinite(time) || time < 0) throw new Error('Invalid keyframe time.');
          last = Math.max(last, time);
          value = numeric(value, name + '/' + bone + '/' + kind);
          if (kind === 'position') value[1] = -value[1] || 0;
          return {time, value, interpolation};
        }
      }
      channels[bone] = target;
    }
    clips[name] = {length: animation.animation_length || Math.max(last, 1),
      loop: animation.loop === true, bones: channels};
  }
  result.adapter.clips = clips;
  for (const state of ['idle', 'walk', 'sprint', 'attack', 'death', 'swim']) {
    if (!result.adapter.states[state]) {
      const name = Object.keys(clips).find(n => n.toLowerCase().includes(state));
      if (name) result.adapter.states[state] = name;
    }
  }
  return authoring.validateDocument(result);
}

function sampleTrack(frames, time, fallback) {
  if (!frames?.length) return fallback;
  let before = frames[0];
  for (const after of frames.slice(1)) {
    if (time < after.time) {
      const t = Math.max(0, (time - before.time) / (after.time - before.time));
      return before.interpolation === 'step' ? before.value : before.value.map((v, i) => v + (after.value[i] - v) * t);
    }
    before = after;
  }
  return before.value;
}

function poseBones(document, clipName, seconds) {
  const clip = document.adapter?.clips[clipName];
  const time = !clip ? 0 : clip.loop ? ((seconds % clip.length) + clip.length) % clip.length
    : Math.max(0, Math.min(clip.length, seconds));
  return document.bones.map(bone => {
    const channels = clip?.bones[bone.id];
    const position = sampleTrack(channels?.position, time, zero);
    const rotation = sampleTrack(channels?.rotation, time, zero);
    return {...bone, pivot: bone.pivot.map((v, i) => v + position[i]),
      rotation: bone.rotation.map((v, i) => v + rotation[i]),
      scale: sampleTrack(channels?.scale, time, [1, 1, 1])[0]};
  });
}

function crc32(bytes) {
  let crc = -1;
  for (const byte of bytes) {
    crc ^= byte;
    for (let bit = 0; bit < 8; bit++) crc = (crc >>> 1) ^ (0xedb88320 & -(crc & 1));
  }
  return (crc ^ -1) >>> 0;
}

function datapack(document) {
  document = authoring.validateDocument(document);
  const encoded = new TextEncoder();
  const namespace = 'hitboxplus_custom';
  const filename = document.entity.replace(/[^a-z0-9_.-]/g, '_') + '_' + document.variant + '.json';
  const entries = [
    ['pack.mcmeta', JSON.stringify({pack: {pack_format: 9, description: 'Hitbox+ custom profiles'}})],
    ['data/' + namespace + '/hitboxplus/profiles/' + filename, JSON.stringify(document, null, 2)]
  ];
  const local = [], central = [];
  let offset = 0, directorySize = 0;
  const header = (size, values) => {
    const bytes = new Uint8Array(size), view = new DataView(bytes.buffer);
    for (const [position, value, width] of values) view[width === 2 ? 'setUint16' : 'setUint32'](position, value, true);
    return bytes;
  };
  for (const [path, text] of entries) {
    const name = encoded.encode(path), bytes = encoded.encode(text), crc = crc32(bytes);
    const start = header(30, [[0, 0x04034b50, 4], [4, 20, 2], [6, 0x800, 2],
      [14, crc, 4], [18, bytes.length, 4], [22, bytes.length, 4], [26, name.length, 2]]);
    local.push(start, name, bytes);
    const end = header(46, [[0, 0x02014b50, 4], [4, 20, 2], [6, 20, 2], [8, 0x800, 2],
      [16, crc, 4], [20, bytes.length, 4], [24, bytes.length, 4], [28, name.length, 2], [42, offset, 4]]);
    central.push(end, name);
    offset += start.length + name.length + bytes.length;
    directorySize += end.length + name.length;
  }
  const finish = header(22, [[0, 0x06054b50, 4], [8, entries.length, 2], [10, entries.length, 2],
    [12, directorySize, 4], [16, offset, 4]]);
  const parts = [...local, ...central, finish], output = new Uint8Array(offset + directorySize + 22);
  let cursor = 0;
  for (const part of parts) { output.set(part, cursor); cursor += part.length; }
  return output;
}

module.exports = {importGeometry, importBbmodel, importAnimations, poseBones, datapack, clone};
