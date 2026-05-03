@echo off
REM Run EcoFeast with embedded Tomcat 11 (no CATALINA_HOME needed).
REM Prerequisites: JDK 17+, MySQL with database/schema.sql applied.
REM Set MySQL password if root is not empty: set ECOFEAST_DB_PASSWORD=yourpass

setlocal
if not defined JAVA_HOME set "JAVA_HOME=C:\Program Files\Java\jdk-17"
cd /d "%~dp0"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo.
echo Starting EcoFeast (embedded Tomcat on port 8081)...
echo Open: http://localhost:8081/ecofeast/
echo.
call mvnw.cmd -q compile exec:java
endlocal
