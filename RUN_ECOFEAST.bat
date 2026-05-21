@echo off
setlocal
cd /d "%~dp0"

echo.
echo ============================================
echo      EcoFeast Application Startup
echo ============================================
echo.
echo Starting embedded Tomcat on port 8081...
echo Open: http://localhost:8081/ecofeast/
echo.

if not defined JAVA_HOME set "JAVA_HOME=C:\Program Files\Java\jdk-17"

"C:\Windows\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -ExecutionPolicy Bypass -File "%~dp0RUN_EMBEDDED.ps1" %*

endlocal
