#!/usr/bin/env bash
set -euo pipefail

# Required: REPO_URL
# Optional: BRANCH (default: main), REPO_DIR (default: /opt/roomescape)

if [[ -z "${REPO_URL:-}" ]]; then
  echo "REPO_URL is required. Example:"
  echo "REPO_URL=https://github.com/your-org/spring-basic-roomescape-playground.git BRANCH=main REPO_DIR=/opt/roomescape bash scripts/init.sh"
  exit 1
fi

BRANCH="${BRANCH:-main}"
REPO_DIR="${REPO_DIR:-/opt/roomescape}"

if [[ ! -d "$REPO_DIR" ]]; then
  mkdir -p "$REPO_DIR"
fi

if [[ ! -d "$REPO_DIR/.git" ]]; then
  git clone -b "$BRANCH" "$REPO_URL" "$REPO_DIR"
fi

mkdir -p "$REPO_DIR/run" "$REPO_DIR/logs"
chmod +x "$REPO_DIR"/scripts/*.sh 2>/dev/null || true
touch "$REPO_DIR/run/app.pid"

echo "Initialized repository at $REPO_DIR"


