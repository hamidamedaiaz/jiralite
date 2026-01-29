@echo off
if exist "%~dp0\.mvn\wrapper\maven-wrapper.jar" (
  java -jar "%~dp0\.mvn\wrapper\maven-wrapper.jar" %*
) else (
  echo Maven wrapper not found. Trying system 'mvn' on PATH...
  mvn %*
)

