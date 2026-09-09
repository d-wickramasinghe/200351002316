@echo off
setlocal
set "MAVEN_VERSION=3.9.11"
set "MAVEN_HOME=%USERPROFILE%\.m2\wrapper\apache-maven-%MAVEN_VERSION%"

where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    call mvn %*
    exit /b %ERRORLEVEL%
)

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Maven was not found. Downloading Apache Maven %MAVEN_VERSION%...
    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $root='%USERPROFILE%\.m2\wrapper'; $zip=Join-Path $env:TEMP 'apache-maven-%MAVEN_VERSION%.zip'; New-Item -ItemType Directory -Force -Path $root | Out-Null; Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile $zip; Expand-Archive -Path $zip -DestinationPath $root -Force; Remove-Item $zip -Force"
    if errorlevel 1 (
        echo Maven download failed. Check your internet connection and try again.
        exit /b 1
    )
)

call "%MAVEN_HOME%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%
