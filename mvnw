#!/usr/bin/env bash
if [ -f "$(dirname "$0")/.mvn/wrapper/maven-wrapper.jar" ]; then
  java -jar "$(dirname "$0")/.mvn/wrapper/maven-wrapper.jar" "$@"
else
  echo "Maven wrapper not found. Trying system 'mvn' on PATH..."
  mvn "$@"
fi

