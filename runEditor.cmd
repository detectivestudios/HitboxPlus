@echo off
setlocal DisableDelayedExpansion
set "editorPort=8787"
if "%~1"=="" goto runStudio
if not "%~1"=="--port" goto usage
if "%~2"=="" goto usage
if not "%~3"=="" goto usage
set "editorPort=%~2"

:runStudio
call "%~dp0gradlew.bat" -p "%~dp0." runEditor "-PeditorPort=%editorPort%" --console=plain
exit /b %errorlevel%

:usage
echo Usage: runEditor [--port 8788]
exit /b 1
