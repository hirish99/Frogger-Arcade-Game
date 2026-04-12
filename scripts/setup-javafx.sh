#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TARGET_DIR="$ROOT_DIR/.javafx/lib"
JAVAFX_VERSION="${JAVAFX_VERSION:-17.0.17}"

case "$(uname -s):$(uname -m)" in
  Darwin:x86_64)
    JAVAFX_PLATFORM="mac"
    ;;
  Darwin:arm64)
    JAVAFX_PLATFORM="mac-aarch64"
    ;;
  Linux:x86_64)
    JAVAFX_PLATFORM="linux"
    ;;
  Linux:aarch64)
    JAVAFX_PLATFORM="linux-aarch64"
    ;;
  *)
    echo "Unsupported platform for automatic JavaFX download."
    exit 1
    ;;
esac

mkdir -p "$TARGET_DIR"

for module in base graphics controls media; do
  artifact="javafx-$module-$JAVAFX_VERSION-$JAVAFX_PLATFORM.jar"
  url="https://repo1.maven.org/maven2/org/openjfx/javafx-$module/$JAVAFX_VERSION/$artifact"
  curl -fsSL "$url" -o "$TARGET_DIR/$artifact"
done

echo "Downloaded JavaFX $JAVAFX_VERSION for $JAVAFX_PLATFORM into $TARGET_DIR."
