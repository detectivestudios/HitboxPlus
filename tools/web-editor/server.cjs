'use strict';

const http = require('node:http');
const fs = require('node:fs');
const path = require('node:path');
const {spawn, spawnSync} = require('node:child_process');
const root = path.join(__dirname, 'dist');
if (!fs.existsSync(path.join(root, 'index.html'))) {
  const build = spawnSync(process.execPath, [path.join(__dirname, 'build.cjs')], {stdio: 'inherit', windowsHide: true});
  if (build.status !== 0) process.exit(build.status || 1);
}
const portArgument = process.argv.indexOf('--port');
const port = Number(portArgument < 0 ? 8787 : process.argv[portArgument + 1]);
if (!Number.isInteger(port) || port < 1 || port > 65535) throw new Error('Use --port followed by a port between 1 and 65535.');
const url = 'http://127.0.0.1:' + port;
const types = {'.html': 'text/html', '.js': 'text/javascript', '.css': 'text/css', '.json': 'application/json', '.txt': 'text/plain'};
const server = http.createServer((request, response) => {
  if (!['GET', 'HEAD'].includes(request.method)) { response.writeHead(405); response.end(); return; }
  try {
    const pathname = decodeURIComponent(new URL(request.url, 'http://127.0.0.1').pathname);
    const file = path.resolve(root, '.' + (pathname === '/' ? '/index.html' : pathname));
    if (!file.startsWith(root + path.sep) || !fs.existsSync(file) || !fs.statSync(file).isFile()) {
      response.writeHead(404); response.end('Not found'); return;
    }
    response.writeHead(200, {'Content-Type': (types[path.extname(file)] || 'application/octet-stream') + '; charset=utf-8',
      'Cache-Control': 'no-cache', 'X-Content-Type-Options': 'nosniff', 'Referrer-Policy': 'no-referrer'});
    if (request.method === 'HEAD') response.end();
    else fs.createReadStream(file).pipe(response);
  } catch { response.writeHead(400); response.end('Invalid request'); }
});
server.on('error', async error => {
  if (error.code === 'EADDRINUSE') {
    try {
      const response = await fetch(url + '/index.html', {signal: AbortSignal.timeout(2000)});
      if (response.status === 200 && await response.text() === fs.readFileSync(path.join(root, 'index.html'), 'utf8')) {
        console.log('Hitbox+ Editor is already running: ' + url);
        openBrowser();
        return;
      }
    } catch {}
  }
  console.error(error.code === 'EADDRINUSE' ? 'Port ' + port + ' is already in use. Try --port 8788.' : error.message);
  process.exitCode = 1;
});
server.listen(port, '127.0.0.1', () => {
  console.log('Hitbox+ Editor: ' + url + '\nFiles stay in your browser. Press Ctrl+C to stop.');
  openBrowser();
});

function openBrowser() {
  if (process.argv.includes('--open')) {
    const command = process.platform === 'win32' ? 'rundll32.exe' : process.platform === 'darwin' ? 'open' : 'xdg-open';
    const args = process.platform === 'win32' ? ['url.dll,FileProtocolHandler', url] : [url];
    const child = spawn(command, args, {detached: true, stdio: 'ignore', windowsHide: true});
    child.on('error', () => console.log('Open ' + url + ' in your browser.'));
    child.unref();
  }
}
