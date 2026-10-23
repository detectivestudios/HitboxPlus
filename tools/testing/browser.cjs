'use strict';

const fs = require('node:fs');
const path = require('node:path');
const net = require('node:net');
const {spawn, spawnSync} = require('node:child_process');
const {once} = require('node:events');
const {setTimeout: delay} = require('node:timers/promises');

const project = path.resolve(__dirname, '../..');
const output = path.join(project, 'build/reports/editor-browser');
let server, browser, smoke, profile, debugUrl, cleanupPromise;

function findBrowser() {
  if (process.env.HITBOXPLUS_TEST_BROWSER) {
    const configured = path.resolve(process.env.HITBOXPLUS_TEST_BROWSER);
    if (!fs.existsSync(configured)) throw new Error('Test browser does not exist: ' + configured);
    return configured;
  }
  const candidates = [];
  if (process.platform === 'win32') {
    for (const directory of [process.env.PROGRAMFILES, process.env['PROGRAMFILES(X86)'], process.env.LOCALAPPDATA].filter(Boolean)) {
      candidates.push(path.join(directory, 'Google/Chrome/Application/chrome.exe'),
        path.join(directory, 'Microsoft/Edge/Application/msedge.exe'));
    }
  } else if (process.platform === 'darwin') {
    candidates.push('/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
      '/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge',
      '/Applications/Chromium.app/Contents/MacOS/Chromium');
  }
  const names = process.platform === 'win32' ? ['chrome.exe', 'msedge.exe', 'chromium.exe']
    : ['google-chrome', 'google-chrome-stable', 'chromium', 'chromium-browser', 'microsoft-edge'];
  for (const directory of (process.env.PATH || '').split(path.delimiter).filter(Boolean)) {
    for (const name of names) candidates.push(path.join(directory, name));
  }
  const available = candidates.find(candidate => fs.existsSync(candidate));
  if (!available) throw new Error('Install Chrome, Edge or Chromium, or set HITBOXPLUS_TEST_BROWSER to its executable path.');
  return available;
}

function start(command, args, stdio = 'pipe') {
  const child = spawn(command, args, {cwd: project, stdio, windowsHide: true});
  child.launchError = null;
  child.once('error', error => { child.launchError = error; });
  child.diagnostics = '';
  if (child.stderr) child.stderr.on('data', chunk => { child.diagnostics = (child.diagnostics + chunk).slice(-8000); });
  if (child.stdout) child.stdout.resume();
  return child;
}

async function waitFor(description, child, probe) {
  const deadline = Date.now() + 30000;
  while (Date.now() < deadline) {
    if (child.launchError) throw child.launchError;
    if (child.exitCode !== null || child.signalCode !== null) throw new Error(description + ' exited: ' + child.diagnostics);
    try {
      const result = await probe();
      if (result) return result;
    } catch {}
    await delay(100);
  }
  throw new Error('Timed out waiting for ' + description + '. ' + child.diagnostics);
}

async function freePort() {
  const reservation = net.createServer();
  reservation.listen(0, '127.0.0.1');
  await once(reservation, 'listening');
  const port = reservation.address().port;
  await new Promise(resolve => reservation.close(resolve));
  return port;
}

async function stop(child) {
  if (!child || child.exitCode !== null || !child.pid) return;
  const closed = once(child, 'close').catch(() => {});
  if (process.platform === 'win32') {
    spawnSync('taskkill.exe', ['/PID', String(child.pid), '/T', '/F'], {stdio: 'ignore', windowsHide: true});
  } else {
    child.kill();
  }
  await Promise.race([closed, delay(3000)]);
}

function cleanup() {
  if (cleanupPromise) return cleanupPromise;
  cleanupPromise = (async () => {
    await stop(smoke);
    if (debugUrl && browser && browser.exitCode === null) {
      await new Promise(resolve => {
        const socket = new WebSocket(debugUrl);
        const timer = setTimeout(() => { socket.close(); resolve(); }, 2000);
        socket.onopen = () => socket.send(JSON.stringify({id: 1, method: 'Browser.close'}));
        socket.onclose = socket.onerror = () => { clearTimeout(timer); resolve(); };
      });
    }
    await stop(browser);
    await stop(server);
    const temporaryRoot = path.join(project, 'build/tmp/editor-browser') + path.sep;
    if (profile && path.resolve(profile).startsWith(temporaryRoot)) {
      fs.rmSync(profile, {recursive: true, force: true, maxRetries: 5, retryDelay: 200});
    }
  })();
  return cleanupPromise;
}

async function main() {
  const executable = findBrowser();
  console.log('Test browser: ' + executable);
  if (process.argv.includes('--check-browser')) return;
  fs.mkdirSync(output, {recursive: true});
  const temporaryRoot = path.join(project, 'build/tmp/editor-browser');
  fs.mkdirSync(temporaryRoot, {recursive: true});
  profile = fs.mkdtempSync(path.join(temporaryRoot, 'profile-'));
  const port = await freePort();
  const url = 'http://127.0.0.1:' + port;
  server = start(process.execPath, [path.join(project, 'tools/web-editor/server.cjs'), '--port', String(port)]);
  await waitFor('isolated editor server', server, async () => {
    const response = await fetch(url, {signal: AbortSignal.timeout(1000)});
    return response.status === 200;
  });
  browser = start(executable, ['--headless=new', '--remote-debugging-port=0', '--user-data-dir=' + profile,
    '--no-first-run', '--no-default-browser-check', '--disable-background-networking',
    '--use-angle=swiftshader', '--enable-unsafe-swiftshader', url]);
  const debugPort = await waitFor('isolated browser', browser, async () => {
    const connection = fs.readFileSync(path.join(profile, 'DevToolsActivePort'), 'utf8').trim().split(/\r?\n/);
    const port = Number(connection[0]);
    if (!Number.isInteger(port) || port <= 0 || !connection[1]) return false;
    const targets = await (await fetch('http://127.0.0.1:' + port + '/json', {signal: AbortSignal.timeout(1000)})).json();
    if (!targets.some(target => target.type === 'page' && target.url.startsWith(url))) return false;
    debugUrl = 'ws://127.0.0.1:' + port + connection[1];
    return port;
  });
  console.log('Testing editor with an isolated browser profile at ' + url);
  smoke = start(process.execPath, [path.join(project, 'tools/web-editor/test/browser-smoke.cjs'),
    '--debug-port', String(debugPort), '--url', url, '--output-dir', output], 'inherit');
  const timeout = setTimeout(() => { void stop(smoke); }, 120000);
  let code;
  try { [code] = await once(smoke, 'close'); }
  finally { clearTimeout(timeout); }
  if (code !== 0) throw new Error('Editor browser tests failed. Reports: ' + output);
  console.log('Browser checks passed. Reports: ' + output);
}

process.once('SIGINT', () => { void cleanup().finally(() => process.exit(130)); });
process.once('SIGTERM', () => { void cleanup().finally(() => process.exit(143)); });
main().finally(() => cleanup())
  .catch(error => { console.error(error.message); process.exitCode = 1; });
