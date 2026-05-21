param(
    [int]$Port = 8081,
    [switch]$CompileOnly
)

$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $repoRoot

$javaHome = $env:JAVA_HOME
if (-not $javaHome -or -not (Test-Path (Join-Path $javaHome "bin\java.exe"))) {
    $defaultJavaHome = "C:\Program Files\Java\jdk-17"
    if (Test-Path (Join-Path $defaultJavaHome "bin\java.exe")) {
        $javaHome = $defaultJavaHome
    } else {
        throw "JDK 17 was not found. Set JAVA_HOME to a JDK 17 installation."
    }
}

$javac = Join-Path $javaHome "bin\javac.exe"
$java = Join-Path $javaHome "bin\java.exe"
$m2 = Join-Path $env:USERPROFILE ".m2\repository"

$dependencyPaths = @(
    "jakarta\servlet\jakarta.servlet-api\6.1.0\jakarta.servlet-api-6.1.0.jar",
    "jakarta\servlet\jsp\jakarta.servlet.jsp-api\4.0.0\jakarta.servlet.jsp-api-4.0.0.jar",
    "jakarta\servlet\jsp\jstl\jakarta.servlet.jsp.jstl-api\3.0.0\jakarta.servlet.jsp.jstl-api-3.0.0.jar",
    "org\glassfish\web\jakarta.servlet.jsp.jstl\3.0.1\jakarta.servlet.jsp.jstl-3.0.1.jar",
    "com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar",
    "org\apache\tomcat\embed\tomcat-embed-core\11.0.0-M18\tomcat-embed-core-11.0.0-M18.jar",
    "org\apache\tomcat\embed\tomcat-embed-jasper\11.0.0-M18\tomcat-embed-jasper-11.0.0-M18.jar",
    "org\apache\tomcat\embed\tomcat-embed-el\11.0.0-M18\tomcat-embed-el-11.0.0-M18.jar",
    "org\apache\tomcat\tomcat-annotations-api\11.0.0-M18\tomcat-annotations-api-11.0.0-M18.jar",
    "org\eclipse\jdt\ecj\3.37.0\ecj-3.37.0.jar"
)

$dependencies = $dependencyPaths | ForEach-Object { Join-Path $m2 $_ }
$missing = $dependencies | Where-Object { -not (Test-Path $_) }
if ($missing) {
    throw "Missing dependency jar(s):`n$($missing -join "`n")`nInstall Maven and run 'mvn compile' once to populate the local cache."
}

$classesDir = Join-Path $repoRoot "target\classes"
New-Item -ItemType Directory -Force -Path $classesDir | Out-Null

$sources = Get-ChildItem -Path (Join-Path $repoRoot "src\main\java") -Recurse -Filter "*.java"
if (-not $sources) {
    throw "No Java sources found under src\main\java."
}

$compileClasspath = $dependencies -join ";"
Write-Host "Compiling EcoFeast..."
& $javac -encoding UTF-8 -cp $compileClasspath -d $classesDir $sources.FullName
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

if ($CompileOnly) {
    Write-Host "Compile complete."
    exit 0
}

$runClasspath = (@($classesDir) + $dependencies) -join ";"
Write-Host "Starting EcoFeast on http://localhost:$Port/ecofeast/"
& $java -cp $runClasspath "-Dport=$Port" com.ecofeast.EcoFeastApplication
exit $LASTEXITCODE
