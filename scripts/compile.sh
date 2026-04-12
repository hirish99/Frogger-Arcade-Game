#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD_DIR="$ROOT_DIR/.build/main"
JAVAFX_LIB="${JAVAFX_LIB:-}"
MAIN_SOURCES=$(find "$ROOT_DIR/src/main/java" -name '*.java' -print)

if [[ -z "$JAVAFX_LIB" ]]; then
  if [[ -d "$ROOT_DIR/.javafx/lib" ]]; then
    JAVAFX_LIB="$ROOT_DIR/.javafx/lib"
  fi
fi

if [[ -z "$JAVAFX_LIB" ]]; then
  for candidate in /usr/local/opt/openjfx/libexec/lib /opt/homebrew/opt/openjfx/libexec/lib; do
    if [[ -d "$candidate" ]]; then
      JAVAFX_LIB="$candidate"
      break
    fi
  done
fi

if [[ -z "$JAVAFX_LIB" || ! -d "$JAVAFX_LIB" ]]; then
  echo "Unable to locate OpenJFX. Run scripts/setup-javafx.sh or set JAVAFX_LIB to your OpenJFX lib directory."
  exit 1
fi

rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

javac \
  --module-path "$JAVAFX_LIB" \
  --add-modules javafx.controls,javafx.media \
  -d "$BUILD_DIR" \
  $MAIN_SOURCES

echo "Main sources compiled successfully."
