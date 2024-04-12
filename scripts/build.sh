#!/bin/bash
export JAVA_HOME="$JAVA17_HOME"

cd ..
./gradlew build
mv build/libs/
