#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TARGET_DIR="${JAVAFX_TARGET_DIR:-$ROOT_DIR/.javafx/lib}"
JAVAFX_VERSION="${JAVAFX_VERSION:-17.0.17}"

if [[ -z "${JAVAFX_PLATFORM:-}" ]]; then
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
    MINGW*:x86_64|MSYS*:x86_64|CYGWIN*:x86_64)
      JAVAFX_PLATFORM="win"
      ;;
    *)
      echo "Unsupported platform for automatic JavaFX download."
      exit 1
      ;;
  esac
fi

mkdir -p "$TARGET_DIR"

for module in base graphics controls media; do
  artifact="javafx-$module-$JAVAFX_VERSION-$JAVAFX_PLATFORM.jar"
  url="https://repo1.maven.org/maven2/org/openjfx/javafx-$module/$JAVAFX_VERSION/$artifact"
  if [[ ! -f "$TARGET_DIR/$artifact" ]]; then
    curl -fsSL "$url" -o "$TARGET_DIR/$artifact"
  fi
done

echo "Downloaded JavaFX $JAVAFX_VERSION for $JAVAFX_PLATFORM into $TARGET_DIR."
