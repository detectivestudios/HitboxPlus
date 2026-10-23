'use strict';

const fs = require('node:fs');
const path = require('node:path');
const root = __dirname;
const authoring = require('./src/authoring.cjs');
const catalog = JSON.parse(fs.readFileSync(path.join(root, 'catalog/vanilla-1.18.2.json'), 'utf8'));
catalog.profiles = catalog.profiles.map(profile => authoring.validateDocument(profile));
const license = fs.readFileSync(path.join(root, '../../HEADER.txt'), 'utf8')
  .replaceAll('${year}', String(new Date().getFullYear()));
const source = '/*\n' + license + '\n*/\n(function () {\n'
  + 'const HitboxPlusAuthoring = (() => { const module = {exports: {}};\n'
  + fs.readFileSync(path.join(root, 'src/authoring.cjs'), 'utf8')
  + '\nreturn module.exports; })();\n'
  + 'const HITBOX_PLUS_CATALOG = ' + JSON.stringify(catalog) + ';\n'
  + fs.readFileSync(path.join(root, 'src/plugin.js'), 'utf8') + '\n})();\n';
fs.mkdirSync(path.join(root, 'dist'), {recursive: true});
fs.copyFileSync(path.join(root, '../../LICENSE.txt'), path.join(root, 'dist/LICENSE.txt'));
fs.writeFileSync(path.join(root, 'dist/hitboxplus.js'), source);
console.log('Built Hitbox+ Blockbench plugin with ' + catalog.profiles.length + ' profiles');
