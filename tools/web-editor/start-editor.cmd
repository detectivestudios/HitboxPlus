@echo off
setlocal DisableDelayedExpansion
set "studioPage=%~dp0dist\hitboxplus-studio.html"
if exist "%~dp0hitboxplus-studio.html" set "studioPage=%~dp0hitboxplus-studio.html"
if exist "%studioPage%" goto openStudio

if not exist "%~dp0build.cjs" (
  echo Editor files are missing. Extract the complete Hitbox+ Studio ZIP again.
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

:openStudio
start "" "%studioPage%"
exit /b %errorlevel%

:buildFromProject
if not exist "%~dp0..\..\gradlew.bat" (
  echo Editor files are missing. Extract the complete Hitbox+ Studio ZIP again.
  pause
  exit /b 1
)
call "%~dp0..\..\gradlew.bat" -p "%~dp0..\.." bundleWebEditor --console=plain
if errorlevel 1 exit /b %errorlevel%
goto openStudio
