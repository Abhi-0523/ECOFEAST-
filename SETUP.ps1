# EcoFeast Setup Script for PowerShell
# Run this script with PowerShell to guide through installation

Write-Host "========================================"
Write-Host "EcoFeast Application Setup Guide"
Write-Host "========================================"
Write-Host ""
Write-Host "Java Status:" -ForegroundColor Green
java -version 2>&1 | Select-Object -First 3
Write-Host ""

$Maven = Get-Command mvn -ErrorAction SilentlyContinue
if ($Maven) {
    Write-Host "Maven Status:" -ForegroundColor Green
    mvn --version | Select-Object -First 1
} else {
    Write-Host "Maven Status:" -ForegroundColor Red
    Write-Host "Maven NOT FOUND - Please install Maven"
}

$MySQL = Get-Command mysql -ErrorAction SilentlyContinue
if ($MySQL) {
    Write-Host "MySQL Status:" -ForegroundColor Green
    mysql --version
} else {
    Write-Host "MySQL Status:" -ForegroundColor Red
    Write-Host "MySQL NOT FOUND - Please install MySQL Server"
}

Write-Host ""
Write-Host "========================================"
Write-Host "INSTALLATION INSTRUCTIONS"
Write-Host "========================================"
Write-Host ""

Write-Host "1. DOWNLOAD MYSQL SERVER" -ForegroundColor Cyan
Write-Host "   URL: https://dev.mysql.com/downloads/mysql/"
Write-Host "   Download: MySQL 8.0 Community Server (Windows)"
Write-Host "   After Install: Add MySQL\bin to PATH"
Write-Host ""

Write-Host "2. DOWNLOAD APACHE TOMCAT 9" -ForegroundColor Cyan
Write-Host "   URL: https://tomcat.apache.org/download-90.cgi"
Write-Host "   Download: apache-tomcat-9.x.x-windows-x64.zip"
Write-Host "   Extract to: C:\tomcat"
Write-Host "   Set: CATALINA_HOME = C:\tomcat"
Write-Host ""

Write-Host "3. DOWNLOAD MAVEN (Recommended)" -ForegroundColor Cyan
Write-Host "   URL: https://maven.apache.org/download.cgi"
Write-Host "   Download: apache-maven-3.8.x-bin.zip"
Write-Host "   Extract to: C:\maven"
Write-Host "   Set: M2_HOME = C:\maven"
Write-Host "   Add to PATH: C:\maven\bin"
Write-Host ""

Write-Host "4. ADD ENVIRONMENT VARIABLES" -ForegroundColor Cyan
Write-Host "   Run: [System.Environment]::SetEnvironmentVariable('JAVA_HOME','C:\Program Files\Java\jdk-17.x','User')"
Write-Host "   Run: [System.Environment]::SetEnvironmentVariable('CATALINA_HOME','C:\tomcat','User')"
Write-Host "   Run: [System.Environment]::SetEnvironmentVariable('M2_HOME','C:\maven','User')"
Write-Host ""

Write-Host "5. RESTART POWERSHELL AND RUN THESE COMMANDS:" -ForegroundColor Yellow
Write-Host ""
Write-Host "   # Navigate to project"
Write-Host "   cd 'C:\Users\ACER\OneDrive\Desktop\Advance Programming\EcoFeast'"
Write-Host ""
Write-Host "   # Create database"
Write-Host "   mysql -u root -p < database/schema.sql"
Write-Host ""
Write-Host "   # Build project"
Write-Host "   mvn clean package"
Write-Host ""
Write-Host "   # Copy to Tomcat"
Write-Host "   Copy-Item target/ecofeast.war \$env:CATALINA_HOME/webapps/"
Write-Host ""
Write-Host "   # Start Tomcat"
Write-Host "   & \$env:CATALINA_HOME/bin/startup.bat"
Write-Host ""
Write-Host "   # Open browser"
Write-Host "   Start-Process 'http://localhost:8080/ecofeast/'"
Write-Host ""

Read-Host "Press Enter to continue"
