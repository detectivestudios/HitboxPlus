@echo off
setlocal DisableDelayedExpansion
set "editorPage=%~dp0dist\hitboxplus-editor.html"
if exist "%~dp0hitboxplus-editor.html" set "editorPage=%~dp0hitboxplus-editor.html"
if exist "%editorPage%" goto openEditor

if not exist "%~dp0build.cjs" (
  echo Editor files are missing. Extract the complete Hitbox+ Editor ZIP again.
  pause
  exit /b 1
)
where node >nul 2>nul
if errorlevel 1 (
  goto buildFromProject
)
node "%~dp0build.cjs"
if errorlevel 1 (
  pause
  exit /b 1
)

:openEditor
start "" "%editorPage%"
exit /b %errorlevel%

:buildFromProject
if not exist "%~dp0..\..\gradlew.bat" (
  echo Editor files are missing. Extract the complete Hitbox+ Editor ZIP again.
  pause
  exit /b 1
)
call "%~dp0..\..\gradlew.bat" -p "%~dp0..\.." bundleWebEditor --console=plain
if errorlevel 1 exit /b %errorlevel%
goto openEditor
