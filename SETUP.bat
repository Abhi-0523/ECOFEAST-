@echo off
REM EcoFeast Setup Script for Windows
REM This script will help you download and install required dependencies

echo ========================================
echo EcoFeast Application Setup
echo ========================================
echo.
echo This script will guide you through setting up the EcoFeast application.
echo Required components:
echo   1. Java Development Kit (JDK) - Already installed
echo   2. MySQL Server - NEEDS INSTALLATION
echo   3. Apache Tomcat 9+ - NEEDS INSTALLATION
echo   4. Maven (Optional) - NEEDS INSTALLATION
echo.
echo ========================================
echo STEP 1: Download MySQL Server
echo ========================================
echo Visit: https://dev.mysql.com/downloads/mysql/
echo Download MySQL 8.0 Community Server for Windows
echo.
echo After installation:
echo   - Add MySQL bin folder to PATH
echo   - Default username: root
echo   - Note your password
echo.
pause

echo.
echo ========================================
echo STEP 2: Download Apache Tomcat 9
echo ========================================
echo Visit: https://tomcat.apache.org/download-90.cgi
echo Download Tomcat 9.x.x (Windows zip version recommended)
echo.
echo After extraction:
echo   - Extract to a location (e.g., C:\tomcat)
echo   - Set CATALINA_HOME environment variable to Tomcat folder
echo.
pause

echo.
echo ========================================
echo STEP 3: Download Maven (Optional but Recommended)
echo ========================================
echo Visit: https://maven.apache.org/download.cgi
echo Download Maven 3.8+
echo.
echo After extraction:
echo   - Extract to a location (e.g., C:\maven)
echo   - Add M2_HOME to environment variables
echo   - Add %%M2_HOME%%\bin to PATH
echo.
pause

echo.
echo ========================================
echo STEP 4: Setup Environment Variables
echo ========================================
echo Open "Environment Variables" and add:
echo   - JAVA_HOME = path to your JDK (e.g., C:\Program Files\Java\jdk-17.x)
echo   - CATALINA_HOME = path to Tomcat (e.g., C:\tomcat)
echo   - M2_HOME = path to Maven (e.g., C:\maven)
echo   - Add %%JAVA_HOME%%\bin to PATH
echo   - Add %%CATALINA_HOME%%\bin to PATH
echo   - Add %%M2_HOME%%\bin to PATH
echo.
echo Then restart PowerShell/Command Prompt for changes to take effect.
pause

echo.
echo ========================================
echo Setup Complete!
echo ========================================
echo.
echo Next steps:
echo 1. Create MySQL database:
echo    mysql -u root -p < database\schema.sql
echo.
echo 2. Build the project:
echo    mvn clean package
echo.
echo 3. Copy WAR file to Tomcat:
echo    copy target\ecofeast.war %%CATALINA_HOME%%\webapps\
echo.
echo 4. Start Tomcat:
echo    %%CATALINA_HOME%%\bin\startup.bat
echo.
echo 5. Access application:
echo    http://localhost:8080/ecofeast/
echo.
pause
