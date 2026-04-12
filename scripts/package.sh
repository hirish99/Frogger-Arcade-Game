#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD_DIR="$ROOT_DIR/.build/package"
DIST_DIR="$ROOT_DIR/dist"
APP_DIR_NAME="frogger"
JAVAFX_VERSION="${JAVAFX_VERSION:-17.0.17}"

detect_platform() {
  if [[ -n "${DIST_PLATFORM:-}" && -n "${JAVAFX_PLATFORM:-}" ]]; then
    return
  fi

  case "$(uname -s):$(uname -m)" in
    Darwin:x86_64)
      DIST_PLATFORM="${DIST_PLATFORM:-mac-x64}"
      JAVAFX_PLATFORM="${JAVAFX_PLATFORM:-mac}"
      ;;
    Darwin:arm64)
      DIST_PLATFORM="${DIST_PLATFORM:-mac-aarch64}"
      JAVAFX_PLATFORM="${JAVAFX_PLATFORM:-mac-aarch64}"
      ;;
    Linux:x86_64)
      DIST_PLATFORM="${DIST_PLATFORM:-linux-x64}"
      JAVAFX_PLATFORM="${JAVAFX_PLATFORM:-linux}"
      ;;
    Linux:aarch64)
      DIST_PLATFORM="${DIST_PLATFORM:-linux-aarch64}"
      JAVAFX_PLATFORM="${JAVAFX_PLATFORM:-linux-aarch64}"
      ;;
    MINGW*:x86_64|MSYS*:x86_64|CYGWIN*:x86_64)
      DIST_PLATFORM="${DIST_PLATFORM:-win-x64}"
      JAVAFX_PLATFORM="${JAVAFX_PLATFORM:-win}"
      ;;
    *)
      echo "Unsupported platform for packaging."
      exit 1
      ;;
  esac
}

create_launchers() {
  local package_root="$1"

  cat > "$package_root/run-frogger.sh" <<'EOF'
#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

java \
  --module-path "$ROOT_DIR/lib" \
  --add-modules javafx.controls,javafx.media \
  -cp "$ROOT_DIR/frogger.jar" \
  com.hirish.frogger.FroggerApplication
EOF
  chmod +x "$package_root/run-frogger.sh"

  cat > "$package_root/run-frogger.bat" <<'EOF'
@echo off
set ROOT_DIR=%~dp0
java --module-path "%ROOT_DIR%lib" --add-modules javafx.controls,javafx.media -cp "%ROOT_DIR%frogger.jar" com.hirish.frogger.FroggerApplication
EOF

  cat > "$package_root/README.txt" <<EOF
Frogger Arcade Game

This bundle contains the compiled game and the JavaFX runtime libraries for $DIST_PLATFORM.

Requirements:
- Java 11 or newer must already be installed.

To run:
- macOS/Linux: ./run-frogger.sh
- Windows: run-frogger.bat
EOF
}

create_archive() {
  local package_root="$1"
  local archive_path="$2"

  rm -f "$archive_path"
  jar --create --file "$archive_path" -C "$BUILD_DIR" "$(basename "$package_root")"
}

stage_javafx_runtime() {
  local cached_javafx_dir
  cached_javafx_dir="${JAVAFX_LIB:-$ROOT_DIR/.javafx/lib}"

  if [[ -d "$cached_javafx_dir" ]] \
    && [[ -f "$cached_javafx_dir/javafx-controls-$JAVAFX_VERSION-$JAVAFX_PLATFORM.jar" ]]; then
    cp "$cached_javafx_dir"/javafx-{base,graphics,controls,media}-$JAVAFX_VERSION-$JAVAFX_PLATFORM.jar "$JAVA_FX_DIR"/
    return
  fi

  export JAVAFX_TARGET_DIR="$JAVA_FX_DIR"
  export JAVAFX_PLATFORM
  "$ROOT_DIR/scripts/setup-javafx.sh"
}

detect_platform
export DIST_PLATFORM
export JAVAFX_PLATFORM

JAVA_FX_DIR="$BUILD_DIR/javafx/lib"
PACKAGE_ROOT="$BUILD_DIR/$APP_DIR_NAME-$DIST_PLATFORM"
ARCHIVE_PATH="$DIST_DIR/$APP_DIR_NAME-$DIST_PLATFORM.zip"

rm -rf "$BUILD_DIR"
mkdir -p "$JAVA_FX_DIR" "$PACKAGE_ROOT/lib" "$DIST_DIR"

stage_javafx_runtime

export JAVAFX_LIB="$JAVA_FX_DIR"
"$ROOT_DIR/scripts/compile.sh"

jar --create \
  --file "$PACKAGE_ROOT/frogger.jar" \
  -C "$ROOT_DIR/.build/main" . \
  -C "$ROOT_DIR/src/main/resources" .

cp "$JAVA_FX_DIR"/*.jar "$PACKAGE_ROOT/lib/"
create_launchers "$PACKAGE_ROOT"
create_archive "$PACKAGE_ROOT" "$ARCHIVE_PATH"

echo "Created downloadable build: $ARCHIVE_PATH"
