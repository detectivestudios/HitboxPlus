'use strict';

const fs = require('node:fs');
const path = require('node:path');
const net = require('node:net');
const {spawn, spawnSync} = require('node:child_process');
const {once} = require('node:events');
const {setTimeout: delay} = require('node:timers/promises');

const project = path.resolve(__dirname, '../..');
const output = path.join(project, 'build/reports/network');
const temporaryRoot = path.join(project, 'build/tmp/network-tests');
let server, client, session, cleanupPromise;

async function freePort() {
  const reservation = net.createServer();
  reservation.listen(0, '127.0.0.1');
  await once(reservation, 'listening');
  const port = reservation.address().port;
  await new Promise(resolve => reservation.close(resolve));
  return port;
}

function readReport(side) {
  const filename = path.join(session, side + '.json');
  return fs.existsSync(filename) ? JSON.parse(fs.readFileSync(filename, 'utf8')) : null;
}

function javaArgument(value) {
  let argument = String(value);
  if (argument.startsWith('"') && argument.endsWith('"')) argument = argument.slice(1, -1);
  return '"' + argument.replaceAll('\\', '\\\\').replaceAll('"', '\\"').replaceAll('\n', '\\n').replaceAll('\r', '\\r') + '"';
}

function start(side, specification, port) {
  const directory = path.join(session, side);
  const args = [...specification.args];
  const gameDirectory = args.indexOf('--gameDir');
  if (gameDirectory >= 0) args[gameDirectory + 1] = directory;
  if (side === 'client') args.push('--server', '127.0.0.1', '--port', String(port));
  const argumentsFile = path.join(session, side + '.args');
  const debug = side === 'client' && process.env.HITBOXPLUS_NETWORK_TEST_DEBUG_PORT
    ? ['-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=127.0.0.1:' + process.env.HITBOXPLUS_NETWORK_TEST_DEBUG_PORT] : [];
  fs.writeFileSync(argumentsFile, [...specification.jvmArgs, ...debug, '-cp', specification.classpath,
    specification.mainClass, ...args].map(javaArgument).join('\n'));
  const log = fs.openSync(path.join(output, side + '.log'), 'w');
  let child;
  try {
    child = spawn(specification.executable, ['@' + argumentsFile], {cwd: directory, windowsHide: true,
      env: {...process.env, ...specification.environment, HITBOXPLUS_NETWORK_TEST_DIR: session,
        HITBOXPLUS_NETWORK_TEST_PORT: String(port)}, stdio: ['ignore', log, log]});
  } finally { fs.closeSync(log); }
  child.launchError = null;
  child.once('error', error => { child.launchError = error; });
  return child;
}

function checkProcess(side, child) {
  if (!child) return;
  if (child.launchError) throw child.launchError;
  const report = readReport(side);
  if (report && !report.passed) throw new Error(side + ': ' + report.error);
  if (child.exitCode !== null || child.signalCode !== null) {
    if (child.exitCode !== 0 || !report?.passed) {
      const tail = fs.readFileSync(path.join(output, side + '.log'), 'utf8').slice(-6000);
      throw new Error(side + ' exited before completing the network scenario.\n' + tail);
    }
  }
}

async function waitFor(description, probe, timeout) {
  const deadline = Date.now() + timeout;
  while (Date.now() < deadline) {
    checkProcess('server', server);
    checkProcess('client', client);
    if (probe()) return;
    await delay(200);
  }
  throw new Error('Timed out waiting for ' + description + '. Logs: ' + output);
}

async function stop(child) {
  if (!child?.pid || child.exitCode !== null || child.signalCode !== null) return;
  const closed = once(child, 'close').catch(() => {});
  if (process.platform === 'win32') {
    spawnSync('taskkill.exe', ['/PID', String(child.pid), '/T', '/F'], {stdio: 'ignore', windowsHide: true});
  } else { child.kill(); }
  await Promise.race([closed, delay(3000)]);
}

function cleanup() {
  if (!cleanupPromise) cleanupPromise = (async () => { await stop(client); await stop(server); })();
  return cleanupPromise;
}

async function main() {
  const specifications = JSON.parse(fs.readFileSync(path.join(project, 'build/network-tests/launch.json'), 'utf8'));
  fs.mkdirSync(output, {recursive: true});
  fs.mkdirSync(temporaryRoot, {recursive: true});
  session = fs.mkdtempSync(path.join(temporaryRoot, 'session-'));
  const serverDirectory = path.join(session, 'server');
  const clientDirectory = path.join(session, 'client');
  fs.mkdirSync(serverDirectory);
  fs.mkdirSync(clientDirectory);
  const port = await freePort();
  const properties = ['online-mode=false', 'server-ip=127.0.0.1', 'server-port=' + port,
    'level-name=network-world', 'level-type=minecraft:flat', 'generate-structures=false',
    'view-distance=2', 'simulation-distance=2', 'max-players=1', 'spawn-protection=0',
    'max-tick-time=60000', 'sync-chunk-writes=false', 'enable-query=false', 'enable-rcon=false'];
  fs.writeFileSync(path.join(serverDirectory, 'server.properties'), properties.join('\n'));
  fs.writeFileSync(path.join(serverDirectory, 'eula.txt'), 'eula=true\n');
  const datapack = path.join(serverDirectory, 'network-world/datapacks/network-test');
  const profileDirectory = path.join(datapack, 'data/hitboxplus_network_tests/hitboxplus/profiles');
  fs.mkdirSync(profileDirectory, {recursive: true});
  fs.writeFileSync(path.join(datapack, 'pack.mcmeta'), JSON.stringify({pack: {pack_format: 9, description: 'Hitbox+ isolated network test'}}));
  fs.copyFileSync(path.join(project, 'src/test/network/resources/network-profiles/login.json'), path.join(profileDirectory, 'player.json'));
  fs.writeFileSync(path.join(clientDirectory, 'options.txt'), ['renderDistance:2', 'simulationDistance:2',
    'maxFps:30', 'enableVsync:false', 'fullscreen:false', 'pauseOnLostFocus:false',
    'tutorialStep:none', 'soundCategory_master:0.0', 'realmsNotifications:false'].join('\n'));
  console.log('Starting isolated network test server on loopback port ' + port);
  server = start('server', specifications.server, port);
  await waitFor('server startup', () => fs.existsSync(path.join(session, 'server.phase')), 120000);
  console.log('Connecting an isolated Minecraft client; its test window is hidden after startup.');
  client = start('client', specifications.client, port);
  await waitFor('login, reload, disconnect and reconnect', () => readReport('client')?.passed && readReport('server')?.passed, 240000);
  await waitFor('test processes to exit', () => client.exitCode === 0 && server.exitCode === 0, 30000);
  const report = {passed: true, client: readReport('client'), server: readReport('server')};
  fs.writeFileSync(path.join(output, 'network.json'), JSON.stringify(report, null, 2));
  console.log(JSON.stringify(report, null, 2));
  console.log('Connected-client tests passed. Reports: ' + output);
  if (path.resolve(session).startsWith(temporaryRoot + path.sep)) {
    fs.rmSync(session, {recursive: true, force: true, maxRetries: 5, retryDelay: 200});
  }
}

process.once('SIGINT', () => { void cleanup().finally(() => process.exit(130)); });
process.once('SIGTERM', () => { void cleanup().finally(() => process.exit(143)); });
main().catch(error => {
  console.error(error.message);
  if (session) {
    fs.writeFileSync(path.join(output, 'network.json'), JSON.stringify({passed: false,
      error: error.message, session, client: readReport('client'), server: readReport('server')}, null, 2));
  }
  process.exitCode = 1;
}).finally(() => cleanup());
