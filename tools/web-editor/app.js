import * as THREE from './vendor/three.module.js';
import {OrbitControls} from './vendor/OrbitControls.js';
import {TransformControls} from './vendor/TransformControls.js';

const {authoring, models, catalog} = window.HitboxPlus;
const byId = id => document.getElementById(id);
const title = value => value.replace(/^.*:/, '').replace(/[_/]/g, ' ')
  .replace(/\b\w/g, c => c.toUpperCase());
const reflect = value => [value[0], -value[1], value[2]];
const rotations = value => [-value[0], value[1], -value[2]].map(v => v * Math.PI / 180);
const colors = {HEAD: '#e4bb80', TORSO: '#91b4c3', LEFT_ARM: '#b89cd1', RIGHT_ARM: '#9dc8b0',
  LEFT_LEG: '#d99eaa', RIGHT_LEG: '#c5c08d', LEFT_FOOT: '#d99eaa', RIGHT_FOOT: '#c5c08d'};
const color = region => colors[region] || '#81cbb2';
const supported = new Set(['player', 'zombie', 'husk', 'giant', 'skeleton', 'stray',
  'wither_skeleton', 'pig', 'cow', 'mooshroom', 'sheep', 'creeper', 'chicken'].map(v => 'minecraft:' + v));
const stateNames = ['idle', 'walk', 'sprint', 'attack', 'death', 'swim'];
const history = [], future = [];
let profile, reference, selected = 0, meshes = [], groups = new Map(), referenceGroups = new Map();
let scene, camera, renderer, orbit, gizmo, rigGroup, referenceGroup, boneLines, grid;
let dirtyTimer, toastTimer, playing = false, seconds = 0, lastFrame = 0, dragStart;

function toast(message, error = false) {
  byId('toast').textContent = message;
  byId('toast').classList.toggle('error', error);
  byId('toast').hidden = false;
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => { byId('toast').hidden = true; }, error ? 12000 : 4500);
}

function attempt(action) {
  try {
    const result = action();
    if (result?.catch) result.catch(error => toast(error.message, true));
  } catch (error) { toast(error.message, true); }
}

function option(value, text) {
  const node = document.createElement('option');
  node.value = value;
  node.textContent = text;
  return node;
}

function persist() {
  clearTimeout(dirtyTimer);
  byId('save-state').textContent = 'Saving…';
  dirtyTimer = setTimeout(() => {
    try {
      localStorage.setItem('hitboxplus-workspace-v1', JSON.stringify({profile, reference}));
      byId('save-state').textContent = 'Saved on this device';
    } catch { byId('save-state').textContent = 'Export a file to save this workspace'; }
  }, 250);
}

function commit(change, previous) {
  const before = previous || models.clone(profile);
  try {
    change();
    profile = authoring.validateDocument(profile);
  } catch (error) {
    profile = before;
    refresh();
    throw error;
  }
  history.push(before);
  if (history.length > 60) history.shift();
  future.length = 0;
  selected = Math.max(0, Math.min(selected, profile.volumes.length - 1));
  refresh();
  persist();
}

function load(input, source = input) {
  profile = authoring.validateDocument(input);
  reference = authoring.validateDocument(source);
  history.length = 0;
  future.length = 0;
  selected = 0;
  seconds = 0;
  playing = false;
  refresh();
  frameModel();
  persist();
}

function refreshLibrary() {
  const query = byId('search').value.trim().toLowerCase();
  const profiles = catalog.profiles.filter(p => (p.entity + ' ' + p.variant).includes(query));
  byId('preset-count').textContent = profiles.length;
  byId('presets').replaceChildren();
  for (const preset of profiles) {
    const button = document.createElement('button');
    button.className = 'preset';
    button.classList.toggle('active', preset.entity === profile.entity && preset.variant === profile.variant);
    const icon = document.createElement('span');
    icon.className = 'preset-icon';
    const glyph = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    glyph.setAttribute('viewBox', '0 0 24 24');
    glyph.setAttribute('aria-hidden', 'true');
    const path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    path.setAttribute('d', preset.entity.endsWith('player')
      ? 'M9 3h6v6H9z M8 11h8v7h-3v4h-2v-4H8z M5 11v7 M19 11v7'
      : 'm12 3 9 5v9l-9 5-9-5V8z M3 8l9 5 9-5 M12 13v9 M7.5 5.5l9 5');
    glyph.append(path);
    icon.append(glyph);
    const label = document.createElement('span');
    label.textContent = title(preset.entity);
    const subtitle = document.createElement('small');
    subtitle.textContent = title(preset.variant) + ' · ' + preset.volumes.length + ' volumes';
    label.append(subtitle);
    button.append(icon, label);
    button.addEventListener('click', () => attempt(() => {
      load(preset);
      toast('Loaded ' + title(preset.entity) + '. Previous edits are saved only in exported files.');
    }));
    byId('presets').append(button);
  }
}

function refresh() {
  const volume = profile.volumes[selected];
  byId('entity').value = profile.entity;
  byId('variant').value = profile.variant;
  byId('pose-source').value = profile.adapter ? 'geckolib' : 'provider';
  byId('model-title').textContent = title(profile.entity);
  byId('box-count').textContent = profile.volumes.length;
  byId('summary').textContent = profile.volumes.length + ' volumes · ' + profile.bones.length + ' bones';
  byId('undo').disabled = history.length === 0;
  byId('redo').disabled = future.length === 0;
  byId('delete').disabled = profile.volumes.length === 1;
  byId('coverage').classList.toggle('warning', !profile.adapter && !supported.has(profile.entity)
    || !!profile.adapter && !Object.keys(profile.adapter.clips).length);
  byId('coverage').textContent = profile.adapter
    ? Object.keys(profile.adapter.clips).length
      ? 'GeckoLib rig: exported bones and bound clips activate automatically. Custom controller logic can use the server API.'
      : 'Reference rig only. Import animations and bind states to make the hitboxes follow movement.'
    : supported.has(profile.entity)
      ? 'Live entity provider available. Your boxes keep the entity’s existing animations.'
      : 'This shape needs a pose adapter. Import a GeckoLib rig or register an entity provider in the mod.';
  byId('rig-settings').hidden = !profile.adapter;
  if (profile.adapter) {
    byId('model-scale').value = profile.adapter.scale;
    byId('blend-ticks').value = profile.adapter.blend_ticks;
  }
  byId('state-bindings').replaceChildren();
  for (const state of stateNames) {
    const label = document.createElement('label');
    label.className = 'field';
    label.textContent = title(state) + ' clip';
    const select = document.createElement('select');
    select.dataset.state = state;
    select.append(option('', 'Use fallback / reference pose'));
    for (const name of Object.keys(profile.adapter?.clips || {})) select.append(option(name, name));
    select.value = profile.adapter?.states[state] || '';
    select.addEventListener('change', () => attempt(() => commit(() => {
      if (select.value) profile.adapter.states[state] = select.value;
      else delete profile.adapter.states[state];
    })));
    label.append(select);
    byId('state-bindings').append(label);
  }
  const clip = byId('clip').value;
  byId('clip').replaceChildren(option('', 'Reference pose'));
  for (const name of Object.keys(profile.adapter?.clips || {})) byId('clip').append(option(name, name));
  byId('clip').value = Object.hasOwn(profile.adapter?.clips || {}, clip) ? clip : '';
  byId('timeline').max = profile.adapter?.clips[byId('clip').value]?.length || 1;
  seconds = Math.min(seconds, Number(byId('timeline').max));
  byId('volumes').replaceChildren();
  profile.volumes.forEach((item, index) => {
    const button = document.createElement('button');
    button.className = 'volume' + (selected === index ? ' active' : '');
    button.setAttribute('aria-pressed', String(selected === index));
    const swatch = document.createElement('span');
    swatch.className = 'swatch';
    swatch.style.background = color(item.region);
    const label = document.createElement('span');
    label.textContent = title(item.part || item.region);
    const bone = document.createElement('small');
    bone.textContent = item.bone;
    button.append(swatch, label, bone);
    button.addEventListener('click', () => { selected = index; refresh(); });
    byId('volumes').append(button);
  });
  byId('selection-name').textContent = title(volume.part || volume.region);
  byId('active-volume').textContent = volume.region.replace(/_/g, ' ');
  byId('bone').replaceChildren(...profile.bones.map(b => option(b.id, b.id)));
  byId('bone').value = volume.bone;
  byId('region').replaceChildren(...authoring.REGIONS.map(r => option(r, title(r))));
  byId('region').value = volume.region;
  byId('part').value = volume.part || '';
  refreshVectors();
  refreshLibrary();
  rebuildScene();
}

function refreshVectors() {
  const volume = profile.volumes[selected];
  byId('vectors').replaceChildren();
  for (const [kind, label] of [['from', 'Position · minimum'], ['size', 'Size'],
    ['rotation', 'Rotation · degrees'], ['origin', 'Rotation pivot']]) {
    const field = document.createElement('div');
    field.className = 'vector-field';
    const caption = document.createElement('label');
    caption.textContent = label;
    const row = document.createElement('div');
    row.className = 'vector-row';
    const values = kind === 'size' ? volume.to.map((v, i) => v - volume.from[i]) : volume[kind];
    values.forEach((value, axis) => {
      const wrapper = document.createElement('label');
      wrapper.className = 'axis-field';
      const badge = document.createElement('span');
      badge.textContent = 'XYZ'[axis];
      const input = document.createElement('input');
      input.type = 'number';
      input.step = '0.25';
      input.value = String(Math.round(value * 10000) / 10000);
      input.dataset.vector = kind;
      input.dataset.axis = String(axis);
      input.setAttribute('aria-label', label + ' ' + 'XYZ'[axis]);
      if (kind === 'size') input.min = '0';
      input.addEventListener('change', () => attempt(() => commit(() => {
        if (!input.value.trim() || !Number.isFinite(input.valueAsNumber)) throw new Error('Enter a finite number.');
        const value = input.valueAsNumber;
        if (kind === 'size') volume.to[axis] = volume.from[axis] + value;
        else if (kind === 'from') {
          volume.to[axis] += value - volume.from[axis];
          volume.from[axis] = value;
        } else volume[kind][axis] = value;
      })));
      wrapper.append(badge, input);
      row.append(wrapper);
    });
    field.append(caption, row);
    byId('vectors').append(field);
  }
}

function initScene() {
  try {
    renderer = new THREE.WebGLRenderer({canvas: byId('viewport'), antialias: true, alpha: true});
  } catch {
    byId('webgl-error').hidden = false;
    return;
  }
  renderer.setPixelRatio(Math.min(devicePixelRatio, 2));
  scene = new THREE.Scene();
  camera = new THREE.PerspectiveCamera(35, 1, 0.01, 100000);
  camera.position.set(45, 29, 65);
  orbit = new OrbitControls(camera, renderer.domElement);
  orbit.target.set(0, 12, 0);
  orbit.enableDamping = true;
  grid = new THREE.GridHelper(128, 16, '#64616b', '#34363c');
  grid.material.transparent = true;
  grid.material.opacity = 0.55;
  scene.add(grid, new THREE.HemisphereLight('#ffffff', '#77777f', 2));
  const light = new THREE.DirectionalLight('#ffffff', 2);
  light.position.set(15, 30, 35);
  scene.add(light);
  gizmo = new TransformControls(camera, renderer.domElement);
  gizmo.setSpace('local');
  gizmo.setSize(0.8);
  scene.add(gizmo);
  gizmo.addEventListener('dragging-changed', event => { orbit.enabled = !event.value; });
  gizmo.addEventListener('mouseDown', () => {
    playing = false;
    dragStart = models.clone(profile);
  });
  gizmo.addEventListener('objectChange', () => {
    if (!dragStart || !gizmo.object) return;
    const before = dragStart.volumes[selected], volume = profile.volumes[selected];
    const object = gizmo.object, position = reflect(object.position.toArray());
    const delta = position.map((v, i) => v - before.origin[i]);
    volume.origin = position;
    volume.from = before.from.map((v, i) => before.origin[i] + (v - before.origin[i])
      * Math.max(0.0001, object.scale.getComponent(i)) + delta[i]);
    volume.to = before.to.map((v, i) => before.origin[i] + (v - before.origin[i])
      * Math.max(0.0001, object.scale.getComponent(i)) + delta[i]);
    volume.rotation = [-object.rotation.x, object.rotation.y, -object.rotation.z]
      .map(v => v * 180 / Math.PI);
    refreshVectors();
  });
  gizmo.addEventListener('mouseUp', () => {
    if (!dragStart) return;
    const before = dragStart;
    dragStart = undefined;
    attempt(() => commit(() => {}, before));
  });
  let pointer;
  renderer.domElement.addEventListener('pointerdown', event => { pointer = [event.clientX, event.clientY]; });
  renderer.domElement.addEventListener('pointerup', event => {
    if (!pointer || Math.hypot(event.clientX - pointer[0], event.clientY - pointer[1]) > 3
        || gizmo.axis || gizmo.dragging || !byId('show-boxes').checked) return;
    const rect = renderer.domElement.getBoundingClientRect();
    const mouse = new THREE.Vector2((event.clientX - rect.left) / rect.width * 2 - 1,
      -(event.clientY - rect.top) / rect.height * 2 + 1);
    const ray = new THREE.Raycaster();
    ray.setFromCamera(mouse, camera);
    const hit = ray.intersectObjects(meshes.map(m => m.mesh), false)[0];
    if (hit) { selected = hit.object.userData.index; refresh(); }
  });
  new ResizeObserver(() => {
    const rect = byId('viewport').getBoundingClientRect();
    renderer.setSize(rect.width, rect.height, false);
    camera.aspect = rect.width / rect.height;
    camera.updateProjectionMatrix();
  }).observe(byId('viewport'));
  requestAnimationFrame(animate);
}

function dispose(object) {
  if (!object) return;
  scene.remove(object);
  object.traverse(node => {
    node.geometry?.dispose();
    const materials = Array.isArray(node.material) ? node.material : [node.material];
    materials.forEach(material => material?.dispose());
  });
}

function makeRig(document, isReference) {
  const root = new THREE.Group(), lookup = new Map(), items = [];
  root.position.y = 24;
  for (const bone of document.bones) {
    const group = new THREE.Group();
    lookup.set(bone.id, group);
    group.position.fromArray(reflect(bone.pivot));
    group.rotation.set(...rotations(bone.rotation), 'ZYX');
  }
  for (const bone of document.bones) (lookup.get(bone.parent) || root).add(lookup.get(bone.id));
  document.volumes.forEach((volume, index) => {
    const pivot = new THREE.Group();
    pivot.position.fromArray(reflect(volume.origin));
    pivot.rotation.set(...rotations(volume.rotation), 'ZYX');
    const size = volume.to.map((v, i) => Math.max(0.002, v - volume.from[i]));
    const geometry = new THREE.BoxGeometry(...size);
    const material = new THREE.MeshStandardMaterial({color: isReference ? '#aaa9ae' : color(volume.region),
      transparent: true, opacity: isReference ? 0.12 : index === selected ? 0.46 : 0.2,
      roughness: 0.75, metalness: 0, depthWrite: false, side: THREE.DoubleSide});
    const mesh = new THREE.Mesh(geometry, material);
    mesh.position.fromArray(reflect(volume.from.map((v, i) => (v + volume.to[i]) / 2 - volume.origin[i])));
    mesh.userData.index = index;
    pivot.add(mesh);
    if (!isReference) {
      const outline = new THREE.LineSegments(new THREE.EdgesGeometry(geometry),
        new THREE.LineBasicMaterial({color: index === selected ? '#fff0d5' : color(volume.region),
          transparent: true, opacity: index === selected ? 1 : 0.7}));
      outline.position.copy(mesh.position);
      pivot.add(outline);
    }
    (lookup.get(volume.bone) || root).add(pivot);
    items.push({pivot, mesh});
  });
  scene.add(root);
  return {root, lookup, items};
}

function rebuildScene() {
  if (!renderer) return;
  gizmo.detach();
  dispose(rigGroup);
  dispose(referenceGroup);
  dispose(boneLines);
  const rig = makeRig(profile, false), original = makeRig(reference, true);
  rigGroup = rig.root;
  groups = rig.lookup;
  meshes = rig.items;
  referenceGroup = original.root;
  referenceGroups = original.lookup;
  boneLines = new THREE.Group();
  boneLines.add(new THREE.LineSegments(new THREE.BufferGeometry().setAttribute('position',
    new THREE.Float32BufferAttribute(new Float32Array(profile.bones.length * 6), 3)),
    new THREE.LineBasicMaterial({color: '#f0e6c7', depthTest: false})));
  scene.add(boneLines);
  if (byId('show-boxes').checked) gizmo.attach(meshes[selected].pivot);
  updatePose();
}

function updatePose() {
  if (!renderer || !profile) return;
  for (const [document, lookup] of [[profile, groups], [reference, referenceGroups]]) {
    for (const bone of models.poseBones({...document, adapter: profile.adapter}, byId('clip').value, seconds)) {
      const group = lookup.get(bone.id);
      group.position.fromArray(reflect(bone.pivot));
      group.rotation.set(...rotations(bone.rotation), 'ZYX');
      group.scale.setScalar(bone.scale);
    }
  }
  rigGroup.visible = byId('show-boxes').checked;
  referenceGroup.visible = byId('show-reference').checked;
  grid.visible = byId('show-grid').checked;
  boneLines.visible = byId('show-bones').checked;
  if (boneLines.visible) {
    rigGroup.updateMatrixWorld(true);
    const vertices = boneLines.children[0].geometry.attributes.position;
    const position = new THREE.Vector3(), parent = new THREE.Vector3();
    let index = 0;
    for (const bone of profile.bones) {
      groups.get(bone.id).getWorldPosition(position);
      (groups.get(bone.parent) || rigGroup).getWorldPosition(parent);
      vertices.setXYZ(index++, parent.x, parent.y, parent.z);
      vertices.setXYZ(index++, position.x, position.y, position.z);
    }
    vertices.needsUpdate = true;
    boneLines.children[0].geometry.computeBoundingSphere();
  }
  byId('time').textContent = seconds.toFixed(2) + 's';
  byId('timeline').value = seconds;
  byId('play').textContent = playing ? 'Ⅱ' : '▶';
  byId('play').setAttribute('aria-label', playing ? 'Pause animation preview' : 'Play animation preview');
}

function animate(now) {
  const elapsed = Math.min(0.1, (now - lastFrame) / 1000);
  lastFrame = now;
  if (playing && !gizmo.dragging) {
    const clip = profile.adapter?.clips[byId('clip').value];
    seconds += elapsed;
    const length = clip?.length || 1;
    if (seconds > length) {
      if (clip?.loop) seconds %= length;
      else { seconds = length; playing = false; }
    }
    updatePose();
  }
  orbit.update();
  renderer.render(scene, camera);
  requestAnimationFrame(animate);
}

function frameModel() {
  if (!renderer || !rigGroup) return;
  const bounds = new THREE.Box3().setFromObject(rigGroup);
  const center = bounds.getCenter(new THREE.Vector3());
  const size = bounds.getSize(new THREE.Vector3());
  const distance = Math.max(8, size.length()) / (2 * Math.tan(camera.fov * Math.PI / 360)) * 1.2;
  orbit.target.copy(center);
  camera.position.copy(center).add(new THREE.Vector3(0.65, 0.4, 1).normalize().multiplyScalar(distance));
  orbit.update();
}

async function openFile(file) {
  if (!file) return;
  if (file.size > 8 * 1024 * 1024) throw new Error('Model files must be smaller than 8 MiB.');
  const text = await file.text();
  if (file.name.endsWith('.java')) return load(authoring.parseDocument(text));
  const input = JSON.parse(text.replace(/^\uFEFF/, ''));
  if (input.format === 'hitboxplus') return load(input);
  if (input.animations && !input.elements && !input['minecraft:geometry']) {
    return commit(() => { profile = models.importAnimations(profile, input); });
  }
  if (input.elements) return load(models.importBbmodel(input));
  const geometries = input['minecraft:geometry'];
  if (geometries?.length > 1) {
    byId('geometry-options').replaceChildren(...geometries.map((g, i) => option(i, g.description?.identifier || 'Geometry ' + (i + 1))));
    byId('geometry-confirm').onclick = () => attempt(() => {
      load(models.importGeometry(input, Number(byId('geometry-options').value)));
      byId('geometry-dialog').close();
    });
    byId('geometry-dialog').showModal();
  } else load(models.importGeometry(input));
}

function download(bytes, filename, type) {
  const url = URL.createObjectURL(new Blob([bytes], {type}));
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  link.click();
  setTimeout(() => URL.revokeObjectURL(url), 10000);
}

function validatedExport() {
  const document = authoring.validateDocument(profile);
  if (new TextEncoder().encode(JSON.stringify(document)).length > 262144) {
    throw new Error('Profile exceeds the runtime’s 256 KiB limit. Reduce the boxes or animation frames.');
  }
  return document;
}

function setupEvents() {
  byId('search').addEventListener('input', refreshLibrary);
  byId('import-model').onclick = () => byId('model-file').click();
  byId('import-animation').onclick = () => byId('animation-file').click();
  for (const id of ['model-file', 'animation-file']) byId(id).onchange = () => {
    const file = byId(id).files[0];
    attempt(() => openFile(file));
    byId(id).value = '';
  };
  for (const id of ['entity', 'variant', 'part', 'bone', 'region']) byId(id).onchange = () => attempt(() => commit(() => {
    if (['entity', 'variant'].includes(id)) profile[id] = byId(id).value.trim();
    else profile.volumes[selected][id] = byId(id).value.trim() || null;
  }));
  byId('pose-source').onchange = () => attempt(() => commit(() => {
    if (byId('pose-source').value === 'geckolib') {
      profile.variant = 'base';
      profile.adapter = {type: 'geckolib', clips: {}, states: {}, scale: 1, offset: [0, 0, 0], blend_ticks: 4};
    } else delete profile.adapter;
  }));
  for (const [id, key] of [['model-scale', 'scale'], ['blend-ticks', 'blend_ticks']]) {
    byId(id).onchange = () => attempt(() => commit(() => { profile.adapter[key] = byId(id).valueAsNumber; }));
  }
  const uniqueKey = () => {
    const used = new Set(profile.volumes.map(v => v.key));
    let key = 'box_' + profile.volumes.length;
    while (used.has(key)) key += '_copy';
    return key;
  };
  byId('add-box').onclick = () => attempt(() => commit(() => {
    const bone = profile.volumes[selected].bone;
    profile.volumes.push({key: uniqueKey(), bone, region: authoring.inferRegion(bone), part: null,
      from: [-2, -2, -2], to: [2, 2, 2], origin: [0, 0, 0], rotation: [0, 0, 0]});
    selected = profile.volumes.length - 1;
  }));
  byId('duplicate').onclick = () => attempt(() => commit(() => {
    profile.volumes.push({...models.clone(profile.volumes[selected]), key: uniqueKey()});
    selected = profile.volumes.length - 1;
  }));
  byId('delete').onclick = () => attempt(() => commit(() => {
    if (profile.volumes.length <= 1) throw new Error('Keep at least one hitbox volume.');
    profile.volumes.splice(selected, 1);
  }));
  byId('undo').onclick = () => {
    if (!history.length) return;
    future.push(models.clone(profile));
    profile = history.pop();
    selected = Math.min(selected, profile.volumes.length - 1);
    refresh(); persist();
  };
  byId('redo').onclick = () => {
    if (!future.length) return;
    history.push(models.clone(profile));
    profile = future.pop();
    selected = Math.min(selected, profile.volumes.length - 1);
    refresh(); persist();
  };
  byId('frame').onclick = frameModel;
  byId('help').onclick = () => byId('guide').showModal();
  document.querySelectorAll('[data-close]').forEach(button => {
    button.onclick = () => byId(button.dataset.close).close();
  });
  document.querySelectorAll('[data-mode]').forEach(button => {
    button.onclick = () => {
      gizmo?.setMode(button.dataset.mode);
      document.querySelectorAll('[data-mode]').forEach(b => b.classList.toggle('active', b === button));
    };
  });
  for (const id of ['show-reference', 'show-boxes', 'show-grid', 'show-bones']) byId(id).onchange = () => {
    if (id === 'show-boxes' && gizmo) {
      if (byId(id).checked) gizmo.attach(meshes[selected].pivot);
      else gizmo.detach();
    }
    updatePose();
  };
  byId('clip').onchange = () => {
    seconds = 0;
    byId('timeline').max = profile.adapter?.clips[byId('clip').value]?.length || 1;
    updatePose();
  };
  byId('play').onclick = () => { playing = !playing; if (seconds >= Number(byId('timeline').max)) seconds = 0; updatePose(); };
  byId('timeline').oninput = () => { playing = false; seconds = byId('timeline').valueAsNumber; updatePose(); };
  byId('export-json').onclick = () => attempt(() => {
    const document = validatedExport();
    download(JSON.stringify(document, null, 2) + '\n', document.entity.replace(':', '_') + '_' + document.variant + '.json', 'application/json');
    toast('Profile exported. Add it under data/your_mod/hitboxplus/profiles/ in the project.');
  });
  byId('export-pack').onclick = () => attempt(() => {
    const document = validatedExport();
    download(models.datapack(document), 'hitboxplus-' + document.entity.replace(':', '-') + '.zip', 'application/zip');
    toast('Datapack exported. Put the ZIP in world/datapacks, then run /reload.');
  });
  document.addEventListener('keydown', event => {
    if (event.defaultPrevented || event.isComposing || event.target.isContentEditable
      || event.target.closest?.('input, select, textarea') || document.querySelector('dialog[open]')) return;
    const key = event.key.toLowerCase();
    if (['arrowleft', 'arrowright', 'arrowup', 'arrowdown'].includes(key)) {
      if (event.ctrlKey || event.metaKey || event.altKey || dragStart) return;
      event.preventDefault();
      const horizontal = key === 'arrowleft' || key === 'arrowright';
      const axis = horizontal ? 0 : event.shiftKey ? 2 : 1;
      const delta = key === 'arrowleft' || key === 'arrowup' ? -1 : 1;
      attempt(() => commit(() => {
        playing = false;
        const volume = profile.volumes[selected];
        for (const vector of ['from', 'to', 'origin']) volume[vector][axis] += delta;
      }));
    } else if ((event.ctrlKey || event.metaKey) && key === 'z') {
      event.preventDefault(); (event.shiftKey ? byId('redo') : byId('undo')).click();
    } else if ((event.ctrlKey || event.metaKey) && key === 'y') { event.preventDefault(); byId('redo').click(); }
    else if (['w', 'e', 'r'].includes(key)) document.querySelector('[data-mode="' + ({w: 'translate', e: 'rotate', r: 'scale'}[key]) + '"]').click();
    else if (key === 'f') frameModel();
    else if (key === 'delete') byId('delete').click();
    else if (key === ' ') { event.preventDefault(); byId('play').click(); }
  });
  let dragDepth = 0;
  document.addEventListener('dragover', event => event.preventDefault());
  document.addEventListener('dragenter', event => {
    event.preventDefault();
    if (event.dataTransfer.types.includes('Files')) { dragDepth++; byId('drop-zone').classList.add('dragging'); }
  });
  document.addEventListener('dragleave', () => { if (--dragDepth <= 0) byId('drop-zone').classList.remove('dragging'); });
  document.addEventListener('drop', event => {
    event.preventDefault(); dragDepth = 0; byId('drop-zone').classList.remove('dragging');
    attempt(() => openFile(event.dataTransfer.files[0]));
  });
}

initScene();
setupEvents();
byId('copyright-year').textContent = new Date().getFullYear();
let restored;
try { restored = JSON.parse(localStorage.getItem('hitboxplus-workspace-v1')); } catch { /* Unavailable or outdated browser storage starts a fresh workspace. */ }
try {
  const initial = catalog.profiles.find(p => p.entity === 'minecraft:player' && p.variant === 'standard_layers');
  load(restored?.profile || initial, restored?.reference || restored?.profile || initial);
} catch {
  load(catalog.profiles[0]);
  toast('The saved workspace could not be restored. Import an exported profile to continue.', true);
}
