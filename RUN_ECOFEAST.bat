@echo off
REM EcoFeast Startup Script
REM This script sets up paths and runs the complete application

setlocal enabledelayedexpansion

echo.
echo ============================================
echo      EcoFeast Application Startup
echo ============================================
echo.

REM Set Java paths
set JAVA_HOME=C:\Program Files\Java\jdk-17
set MAVEN_HOME=C:\maven
set CATALINA_HOME=C:\tomcat
set MYSQL_HOME=C:\Program Files\MySQL\MySQL Server 8.0

REM Extend PATH
set PATH=!MAVEN_HOME!\bin;!MYSQL_HOME!\bin;!JAVA_HOME!\bin;!PATH!

REM Change to EcoFeast directory
cd /d "C:\Users\ACER\OneDrive\Desktop\Advance Programming\EcoFeast"

echo.
echo [STEP 1] Verifying installations...
echo.

REM Check Java
java -version 2>&1 | findstr /I "version" && (
    echo ✓ Java 17 found
) || (
    echo ✗ Java 17 NOT found
    pause
    exit /b 1
)

REM Check Maven
mvn --version 2>&1 | findstr /I "Apache Maven" && (
    echo ✓ Maven found
) || (
    echo ✗ Maven NOT found - trying manual path...
    "!MAVEN_HOME!\bin\mvn.cmd" --version >nul 2>&1 && (
        echo ✓ Maven found with full path
    ) || (
        echo ✗ Maven NOT found even with full path
        pause
        exit /b 1
    )
)

REM Check MySQL
mysql --version 2>&1 | findstr /I "mysql" && (
    echo ✓ MySQL found
) || (
    echo ✗ MySQL NOT found - trying manual path...
    "!MYSQL_HOME!\bin\mysql.exe" --version >nul 2>&1 && (
        echo ✓ MySQL found with full path
    ) || (
        echo ✗ MySQL NOT found
        pause
        exit /b 1
    )
)

REM Check Tomcat
if exist "!CATALINA_HOME!\bin\startup.bat" (
    echo ✓ Tomcat found
) else (
    echo ✗ Tomcat NOT found
    pause
    exit /b 1
)

echo.
echo [STEP 2] Setting up database...
echo Please enter your MySQL root password when prompted.
echo.

mysql -u root -p < database\schema.sql

if errorlevel 1 (
    echo.
    echo ✗ Database setup failed!
    pause
    exit /b 1
)

echo ✓ Database setup complete
echo.

echo [STEP 3] Building application with Maven...
echo This may take 1-2 minutes...
echo.

mvn clean package

if errorlevel 1 (
    echo.
    echo ✗ Build failed!
    pause
    exit /b 1
)

echo ✓ Build successful
echo.

echo [STEP 4] Deploying to Tomcat...
echo.

REM Copy WAR file
copy target\ecofeast.war "!CATALINA_HOME!\webapps\" /Y
if errorlevel 1 (
    echo ✗ Deployment failed!
    pause
    exit /b 1
)

echo ✓ WAR file deployed
echo.

echo [STEP 5] Starting Tomcat...
echo.

REM Start Tomcat
call "!CATALINA_HOME!\bin\startup.bat"

echo.
echo Waiting for Tomcat to start (5 seconds)...
timeout /t 5 /nobreak

echo.
echo ============================================
echo   Application is READY!
echo ============================================
echo.
echo Access the application at:
echo   http://localhost:8080/ecofeast/
echo.
echo Test Credentials:
echo   Admin:  admin@ecofeast.com / admin123
echo   User:   john@example.com / user123
echo.
echo Press any key to open browser...
pause

REM Open browser
start http://localhost:8080/ecofeast/

echo.
echo Application started successfully!
echo To stop Tomcat, run: !CATALINA_HOME!\bin\shutdown.bat
echo.
pause
