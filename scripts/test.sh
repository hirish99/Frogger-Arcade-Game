#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD_DIR="$ROOT_DIR/.build/test"
TEST_SOURCES=$(find "$ROOT_DIR/src/test/java" -name '*.java' -print)

rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

javac \
  -d "$BUILD_DIR" \
  "$ROOT_DIR/src/main/java/com/hirish/frogger/config/GameConfig.java" \
  "$ROOT_DIR/src/main/java/com/hirish/frogger/logic/GameState.java" \
  "$ROOT_DIR/src/main/java/com/hirish/frogger/logic/HorizontalLoop.java" \
  $TEST_SOURCES

java -cp "$BUILD_DIR" com.hirish.frogger.testing.LogicTestRunner
