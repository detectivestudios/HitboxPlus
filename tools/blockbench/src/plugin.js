'use strict';

class HitboxPlusEditor {
  constructor(authoring, catalog) {
    this.authoring = authoring;
    this.catalog = catalog;
    this.actions = [];
    this.properties = [];
    this.materials = new Map();
    this.refresh = () => this.refreshPreview();
  }

  load() {
    const options = {condition: () => Format?.id === 'hitboxplus'};
    this.format = new ModelFormat('hitboxplus', {
      name: 'Hitbox+', description: 'Edit body-part hitboxes and export JSON or Java profiles.',
      icon: 'accessibility_new', category: 'minecraft', target: 'Minecraft Forge',
      box_uv: true, optional_box_uv: true, bone_rig: true, rotate_cubes: true,
      euler_order: 'ZYX', animation_mode: true, model_identifier: true,
      centered_grid: true, integer_size: false, single_texture: false
    });
    this.properties.push(
      new Property(ModelProject, 'object', 'hitboxplus_profile', {default: () => ({})}),
      new Property(Group, 'string', 'hitboxplus_bone', {default: ''}),
      new Property(Cube, 'string', 'hitboxplus_region', {default: 'OTHER'}),
      new Property(Cube, 'string', 'hitboxplus_part', {default: ''}),
      new Property(Cube, 'string', 'hitboxplus_key', {default: ''}),
      new Property(Cube, 'boolean', 'hitboxplus_reference', {default: false})
    );
    const action = (id, name, icon, click, condition) => {
      const value = new Action('hitboxplus_' + id, {name, icon, click: () => this.attempt(click),
        ...(condition ? {condition} : {})});
      this.actions.push(value);
      return value;
    };
    this.builtin = action('builtin', 'Load Built-in Hitbox Profile', 'view_in_ar',
      () => this.chooseBuiltin());
    this.importAction = action('import', 'Import Hitbox+ Profile', 'file_upload',
      () => this.importDocument());
    this.geometryAction = action('geometry', 'Import GeckoLib / Bedrock Geometry', 'view_in_ar',
      () => this.importGeometry());
    this.adopt = action('adopt', 'Use Current Model as Reference', 'accessibility',
      () => this.adoptModel(), () => !!Project);
    this.configure = action('configure', 'Profile Settings', 'settings',
      () => this.profileSettings(), options.condition);
    this.add = action('add', 'Add Hitbox', 'add_box', () => this.addHitbox(), options.condition);
    this.assign = action('assign', 'Assign Bone and Body Region', 'account_tree',
      () => this.assignBoxes(), () => options.condition()
        && Cube.selected.some(cube => !cube.hitboxplus_reference));
    this.reference = action('reference', 'Show / Hide Reference Model', 'visibility',
      () => this.toggleReferences(), options.condition);
    this.java = action('java', 'Export Hitbox+ Java Profile', 'code',
      () => this.exportJava(), options.condition);
    this.document = action('document', 'Export Hitbox+ JSON Profile', 'save_alt',
      () => this.exportDocument(), options.condition);
    MenuBar.addAction({id: 'hitboxplus_menu', name: 'Hitbox+',
      icon: 'accessibility_new', children: this.actions}, 'tools');
    MenuBar.addAction(this.importAction, 'file.import');
    MenuBar.addAction(this.geometryAction, 'file.import');
    MenuBar.addAction(this.java, 'file.export');
    MenuBar.addAction(this.document, 'file.export');
    Blockbench.on('update_view', this.refresh);
    Blockbench.on('select_project', this.refresh);
    this.format.on('create', () => this.profileSettings());
  }

  attempt(callback) {
    try { return callback(); }
    catch (error) {
      console.error('[Hitbox+]', error);
      Blockbench.showMessageBox({title: 'Hitbox+', icon: 'error', message: error.message});
    }
  }

  chooseBuiltin() {
    const options = {};
    this.catalog.profiles.forEach((profile, index) => {
      options[index] = profile.entity + ' / ' + profile.variant;
    });
    new Dialog({id: 'hitboxplus_builtin', title: 'Load Built-in Hitbox Profile',
      form: {profile: {label: 'Profile', type: 'select', options},
        reference: {label: 'Include reference model', type: 'checkbox', value: true}},
      onConfirm: result => this.attempt(() => this.loadDocument(
        this.catalog.profiles[Number(result.profile)], result.reference))
    }).show();
  }

  importDocument() {
    Blockbench.import({type: 'Hitbox+ Profile', extensions: ['json', 'java'],
      readtype: 'text'}, files => {
      if (files[0]) this.attempt(() => this.loadDocument(
        this.authoring.parseDocument(files[0].content), true));
    });
  }

  importGeometry() {
    Blockbench.import({type: 'GeckoLib / Bedrock Geometry', extensions: ['json'], readtype: 'text'},
      files => {
        if (!files[0]) return;
        this.attempt(() => {
          const model = JSON.parse(files[0].content);
          const geometries = model['minecraft:geometry'];
          if (!Array.isArray(geometries) || !geometries.length) {
            throw new Error('Expected a GeckoLib/Bedrock .geo.json with minecraft:geometry.');
          }
          const load = index => {
            const selected = {...model, 'minecraft:geometry': [geometries[index]]};
            Codecs.bedrock.load(selected, files[0]);
            if (Format.id === 'bedrock') this.adoptModel();
          };
          if (geometries.length === 1) load(0);
          else new Dialog({id: 'hitboxplus_geometry', title: 'Choose Model Geometry',
            form: {geometry: {label: 'Geometry', type: 'select', options: Object.fromEntries(
              geometries.map((geometry, index) => [index, geometry.description?.identifier || String(index)]))}},
            onConfirm: result => this.attempt(() => load(Number(result.geometry)))
          }).show();
        });
      });
  }

  loadDocument(input, reference = true) {
    const document = this.authoring.validateDocument(input);
    if (!newProject(this.format)) return false;
    Project.name = document.entity.split(':')[1] + '_' + document.variant;
    Project.hitboxplus_profile = {entity: document.entity, variant: document.variant,
      javaPackage: 'com.example.hitboxes', javaClass: this.defaultClass(document.entity),
      ...(document.adapter ? {adapter: document.adapter} : {})};
    const pivots = this.authoring.bindPivots(document);
    const groups = new Map();
    for (const bone of document.bones) {
      const group = new Group({name: bone.id.split('/').pop(),
        origin: this.authoring.editorPoint(pivots.get(bone.id)),
        rotation: this.authoring.reflectedRotation(bone.rotation),
        hitboxplus_bone: bone.id});
      if (bone.parent != null) group.addTo(groups.get(bone.parent));
      group.init();
      groups.set(bone.id, group);
    }
    for (const volume of document.volumes) {
      const box = this.authoring.editorBox(volume, pivots.get(volume.bone));
      if (reference) {
        new Cube({...box, name: 'Reference · ' + volume.bone, color: 7, locked: true,
          export: false, hitboxplus_reference: true}).addTo(groups.get(volume.bone)).init();
      }
      new Cube({...box, name: volume.region, color: this.colorIndex(volume.region),
        hitboxplus_region: volume.region, hitboxplus_part: volume.part || '',
        hitboxplus_key: volume.key}).addTo(groups.get(volume.bone)).init();
    }
    Canvas.updateAll();
    this.refreshPreview();
    Project.saved = false;
    return true;
  }

  defaultClass(entity) {
    return entity.split(':')[1].split(/[\/_.-]/).map(word =>
      word.charAt(0).toUpperCase() + word.slice(1)).join('') + 'HitboxProfile';
  }

  profileSettings() {
    const current = Project.hitboxplus_profile || {};
    new Dialog({id: 'hitboxplus_settings', title: 'Hitbox+ Profile Settings',
      form: {
        entity: {label: 'Entity ID', value: current.entity || 'minecraft:player'},
        variant: {label: 'Profile variant', value: current.variant || 'base'},
        javaPackage: {label: 'Java package', value: current.javaPackage || 'com.example.hitboxes'},
        javaClass: {label: 'Java class', value: current.javaClass || 'CustomHitboxProfile'}
      },
      onConfirm: result => {
        Project.hitboxplus_profile = {...current, ...result};
        Project.saved = false;
      }
    }).show();
  }

  adoptModel() {
    const selected = Cube.selected.length ? Cube.selected.slice()
      : Cube.all.filter(cube => !cube.hitboxplus_reference);
    this.format.convertTo();
    Project.hitboxplus_profile = {entity: 'example:custom_entity', variant: 'base',
      javaPackage: 'com.example.hitboxes', javaClass: 'CustomHitboxProfile'};
    if (!Group.all.length) new Group({name: 'body', origin: [0, 24, 0]}).init();
    const used = new Set();
    for (const group of Group.all) {
      let path = group.name, parent = group.parent;
      while (parent instanceof Group) { path = parent.name + '/' + path; parent = parent.parent; }
      let unique = path, suffix = 2;
      while (used.has(unique)) unique = path + '_' + suffix++;
      group.hitboxplus_bone = unique;
      used.add(unique);
    }
    for (const cube of Cube.all.slice()) {
      if (!(cube.parent instanceof Group)) cube.addTo(Group.all[0]);
      cube.hitboxplus_reference = true;
      cube.locked = true;
      cube.export = false;
    }
    for (const source of selected) {
      const data = source.getSaveCopy();
      data.hitboxplus_reference = false;
      data.hitboxplus_key = guid();
      data.hitboxplus_region = this.authoring.inferRegion(source.parent.name);
      data.name = data.hitboxplus_region;
      data.locked = false;
      data.export = true;
      new Cube(data).addTo(source.parent).init();
    }
    Canvas.updateAll();
    this.refreshPreview();
    this.profileSettings();
  }

  addHitbox() {
    const group = Group.first_selected || Group.all[0];
    if (!group) throw new Error('Load a profile or create a bone group first.');
    Undo.initEdit({elements: [], outliner: true});
    const cube = new Cube({name: 'OTHER', from: group.origin.map(value => value - 2),
      to: group.origin.map(value => value + 2), origin: group.origin.slice(),
      hitboxplus_key: guid(), hitboxplus_region: 'OTHER'}).addTo(group).init();
    cube.select();
    Undo.finishEdit('Add Hitbox+ volume', {elements: [cube], outliner: true});
    Canvas.updateAll();
    this.assignBoxes();
  }

  assignBoxes() {
    const cubes = Cube.selected.filter(cube => !cube.hitboxplus_reference);
    if (!cubes.length) throw new Error('Select a hitbox cube first.');
    const options = {};
    Group.all.forEach(group => { options[group.uuid] = group.hitboxplus_bone || group.name; });
    new Dialog({id: 'hitboxplus_assign', title: 'Assign Bone and Body Region',
      form: {
        bone: {label: 'Bone', type: 'select', options, value: cubes[0].parent.uuid},
        region: {label: 'Body region', type: 'select',
          options: Object.fromEntries(this.authoring.REGIONS.map(region => [region, region])),
          value: cubes[0].hitboxplus_region},
        part: {label: 'Custom part ID (optional)', value: cubes[0].hitboxplus_part || ''}
      },
      onConfirm: result => this.attempt(() => {
        const group = Group.all.find(value => value.uuid === result.bone);
        if (!group) throw new Error('The selected bone no longer exists.');
        if (result.part && !/^[a-z0-9_.-]+:[a-z0-9/._-]+$/.test(result.part)) {
          throw new Error('Custom part ID must use lowercase namespace:path.');
        }
        Undo.initEdit({elements: cubes, outliner: true});
        for (const cube of cubes) {
          cube.hitboxplus_region = result.region;
          cube.hitboxplus_part = result.part;
          cube.name = result.part || result.region;
          cube.color = this.colorIndex(result.region);
          cube.addTo(group);
        }
        Undo.finishEdit('Assign Hitbox+ volumes');
        Canvas.updateAll();
      })
    }).show();
  }

  toggleReferences() {
    const references = Cube.all.filter(cube => cube.hitboxplus_reference);
    const visible = references.some(cube => !cube.visibility);
    references.forEach(cube => { cube.visibility = visible; });
    Canvas.updateAll();
  }

  readDocument() {
    if (Format.id !== 'hitboxplus') throw new Error('Open a Hitbox+ project first.');
    const metadata = Project.hitboxplus_profile || {};
    const bones = Group.all.map(group => {
      const parent = group.parent instanceof Group ? group.parent : null;
      const pivot = this.authoring.nativePoint(group.origin);
      return {id: group.hitboxplus_bone || group.name,
        parent: parent ? parent.hitboxplus_bone || parent.name : null,
        pivot: parent ? this.authoring.subtract(pivot, this.authoring.nativePoint(parent.origin)) : pivot,
        rotation: this.authoring.reflectedRotation(group.rotation)};
    });
    const keys = new Set();
    const volumes = Cube.all.filter(cube => !cube.hitboxplus_reference && cube.export !== false)
      .map((cube, index) => {
        if (!(cube.parent instanceof Group)) throw new Error('Each hitbox must belong to a bone group.');
        let key = cube.hitboxplus_key || 'box_' + index;
        if (keys.has(key)) {
          key = guid();
          cube.hitboxplus_key = key;
          Project.saved = false;
        }
        keys.add(key);
        return {key,
          bone: cube.parent.hitboxplus_bone || cube.parent.name,
          region: cube.hitboxplus_region || 'OTHER', part: cube.hitboxplus_part || null,
          ...this.authoring.nativeBox(cube, this.authoring.nativePoint(cube.parent.origin))};
      });
    return this.authoring.validateDocument({format: 'hitboxplus', version: 1,
      entity: metadata.entity || 'example:custom_entity', variant: metadata.variant || 'base',
      bones, volumes, ...(metadata.adapter ? {adapter: metadata.adapter} : {})});
  }

  exportDocument() {
    const document = this.readDocument();
    const content = JSON.stringify(document, null, 2) + '\n';
    if (new TextEncoder().encode(JSON.stringify(document)).length > 262144) {
      throw new Error('Runtime profiles must not exceed 256 KiB.');
    }
    Blockbench.export({type: 'Hitbox+ Profile', extensions: ['json'],
      name: Project.name, savetype: 'text', content});
  }

  exportJava() {
    const metadata = Project.hitboxplus_profile || {};
    const className = metadata.javaClass || this.defaultClass(metadata.entity || 'example:custom');
    const source = this.authoring.exportJava(this.readDocument(),
      metadata.javaPackage || 'com.example.hitboxes', className);
    Blockbench.export({type: 'Hitbox+ Java Profile', extensions: ['java'],
      name: className, savetype: 'text', content: source});
  }

  colorIndex(region) {
    if (region === 'HEAD') return 1;
    if (region === 'TORSO') return 2;
    if (region.includes('ARM') || region.includes('WING')) return 3;
    if (region.includes('LEG') || region.includes('FOOT')) return 4;
    return 5;
  }

  color(region) {
    if (region === 'HEAD') return 0xff6b6b;
    if (region === 'TORSO') return 0x55c9f3;
    if (region.includes('LEFT')) return 0x8cdb88;
    if (region.includes('RIGHT')) return 0xf6c767;
    return 0xb39cf5;
  }

  refreshPreview() {
    if (!Project || Format.id !== 'hitboxplus') return;
    for (const cube of Cube.all) {
      if (!cube.mesh) continue;
      const reference = cube.hitboxplus_reference;
      const key = reference ? 'reference' : cube.hitboxplus_region;
      let materials = this.materials.get(key);
      if (!materials) {
        const color = reference ? 0x8795a6 : this.color(key);
        materials = {surface: new THREE.MeshBasicMaterial({color, transparent: true,
          opacity: reference ? 0.55 : 0.18, depthWrite: false, side: THREE.DoubleSide}),
          edge: new THREE.LineBasicMaterial({color, transparent: true,
            opacity: reference ? 0.25 : 0.95, depthTest: reference})};
        this.materials.set(key, materials);
      }
      cube.mesh.material = materials.surface;
      const previous = cube.mesh.getObjectByName('hitboxplus_edges');
      if (previous) { cube.mesh.remove(previous); previous.geometry.dispose(); }
      const edges = new THREE.LineSegments(new THREE.EdgesGeometry(cube.mesh.geometry), materials.edge);
      edges.name = 'hitboxplus_edges';
      edges.renderOrder = reference ? 0 : 5;
      cube.mesh.add(edges);
    }
  }

  unload() {
    Blockbench.removeListener('update_view', this.refresh);
    Blockbench.removeListener('select_project', this.refresh);
    MenuBar.removeAction('tools.hitboxplus_menu');
    this.actions.forEach(action => action.delete());
    if (Project && Format.id === 'hitboxplus') {
      Cube.all.forEach(cube => {
        const edges = cube.mesh?.getObjectByName('hitboxplus_edges');
        if (edges) { cube.mesh.remove(edges); edges.geometry.dispose(); }
      });
      Canvas.updateAll();
    }
    this.materials.forEach(materials => { materials.surface.dispose(); materials.edge.dispose(); });
    this.properties.forEach(property => property.delete());
    this.format.delete();
  }
}

let hitboxPlusEditor;
Plugin.register('hitboxplus', {
  title: 'Hitbox+ by DetectiveStudios', author: 'detectivestudios', icon: 'accessibility_new',
  description: 'Visually edit animated body-part hitboxes and export Java profiles.',
  version: '0.1.0', min_version: '4.12.0', variant: 'both',
  tags: ['Minecraft: Java Edition', 'Animation', 'Format'],
  onload() {
    hitboxPlusEditor = new HitboxPlusEditor(HitboxPlusAuthoring, HITBOX_PLUS_CATALOG);
    hitboxPlusEditor.load();
  },
  onunload() {
    hitboxPlusEditor?.unload();
    hitboxPlusEditor = undefined;
  }
});
