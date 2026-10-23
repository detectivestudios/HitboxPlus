'use strict';

const fs = require('node:fs');
const path = require('node:path');
const output = path.join(__dirname, 'dist');
fs.mkdirSync(path.join(output, 'vendor'), {recursive: true});
fs.mkdirSync(path.join(output, 'assets'), {recursive: true});
const projectLicense = fs.readFileSync(path.join(__dirname, '../../LICENSE.txt'), 'utf8');
fs.writeFileSync(path.join(output, 'LICENSE.txt'), projectLicense);
for (const name of fs.readdirSync(path.join(__dirname, 'assets'))) {
  fs.copyFileSync(path.join(__dirname, 'assets', name), path.join(output, 'assets', name));
}
for (const name of ['index.html', 'style.css', 'app.js']) {
  fs.copyFileSync(path.join(__dirname, name), path.join(output, name));
}
for (const name of fs.readdirSync(path.join(__dirname, 'vendor'))) {
  fs.copyFileSync(path.join(__dirname, 'vendor', name), path.join(output, 'vendor', name));
}
const authoring = fs.readFileSync(path.join(__dirname, '../blockbench/src/authoring.cjs'), 'utf8');
const models = fs.readFileSync(path.join(__dirname, 'model.cjs'), 'utf8')
  .replace("const authoring = require('../blockbench/src/authoring.cjs');", 'const authoring = window.HitboxPlus.authoring;');
const catalog = require('../blockbench/catalog/vanilla-1.18.2.json');
const source = 'window.HitboxPlus = {};\nwindow.HitboxPlus.authoring = (() => { const module = {exports: {}};\n'
  + authoring + '\nreturn module.exports; })();\n'
  + 'window.HitboxPlus.models = (() => { const module = {exports: {}};\n'
  + models + '\nreturn module.exports; })();\n'
  + 'window.HitboxPlus.catalog = ' + JSON.stringify(catalog) + ';\n';
fs.writeFileSync(path.join(output, 'shared.js'), source);
const dataModule = filename => 'data:text/javascript;base64,'
  + fs.readFileSync(path.join(__dirname, 'vendor', filename)).toString('base64');
const app = fs.readFileSync(path.join(__dirname, 'app.js'), 'utf8')
  .replace('./vendor/three.module.js', dataModule('three.module.js'))
  .replace('./vendor/OrbitControls.js', dataModule('OrbitControls.js'))
  .replace('./vendor/TransformControls.js', dataModule('TransformControls.js'));
const inline = value => value.replace(/<\/script/gi, '<\\/script');
const singleFile = fs.readFileSync(path.join(__dirname, 'index.html'), 'utf8')
  .replaceAll('assets/detectivestudios.png', 'data:image/png;base64,'
    + fs.readFileSync(path.join(__dirname, 'assets/detectivestudios.png')).toString('base64'))
  .replace('<link rel="stylesheet" href="style.css">', '<style>\n' + fs.readFileSync(path.join(__dirname, 'style.css'), 'utf8') + '\n</style>')
  .replace('{"imports":{"three":"./vendor/three.module.js"}}', JSON.stringify({imports: {three: dataModule('three.module.js')}}))
  .replace('<script defer src="shared.js"></script>', '<script>\n' + inline(source) + '\n</script>')
  .replace('<script type="module" src="app.js"></script>', '<script type="module">\n' + inline(app) + '\n</script>')
  .replace('<!doctype html>', '<!doctype html>\n<!--\n' + projectLicense + '\n\nThree.js license:\n'
    + fs.readFileSync(path.join(__dirname, 'vendor/LICENSE-three.txt'), 'utf8') + '\n-->');
fs.writeFileSync(path.join(output, 'hitboxplus-editor.html'), singleFile);
console.log('Built Hitbox+ Editor: ' + catalog.profiles.length + ' presets, all assets local.');
