const fs = require('node:fs');
const path = require('node:path');

const root = path.resolve(__dirname, '../..');
const catalog = JSON.parse(fs.readFileSync(path.join(__dirname, 'catalog/vanilla-1.18.2.json')));
const rigs = {};
for (const profile of catalog.profiles) {
  if (profile.entity === 'minecraft:player') {
    continue;
  }
  rigs[`${profile.entity}/${profile.variant}`] = Object.fromEntries(profile.bones.map(bone => [
    bone.id,
    [...bone.pivot, ...bone.rotation.map(value => value * Math.PI / 180)]
  ]));
}
const destination = path.join(root, 'src/main/resources/data/hitboxplus/vanilla_rigs.json');
fs.mkdirSync(path.dirname(destination), { recursive: true });
const json = JSON.stringify(rigs, null, 2).replace(/\[\n[\s\S]*?]/g, value =>
  '[' + value.slice(1, -1).split(',').map(number => number.trim()).join(', ') + ']');
fs.writeFileSync(destination, json + '\n');
console.log(`Exported ${Object.keys(rigs).length} runtime rigs`);
