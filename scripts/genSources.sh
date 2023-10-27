#!/bin/bash
export JAVA_HOME="$JAVA17_HOME"
cd ..
./gradlew genSources --stacktrace
