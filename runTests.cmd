@echo off
setlocal DisableDelayedExpansion
call "%~dp0gradlew.bat" -p "%~dp0." runTests --console=plain %*
exit /b %errorlevel%
