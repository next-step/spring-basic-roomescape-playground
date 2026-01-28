#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd -P)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd -P)"
PROJECT_NAME="spring-basic-roomescape-playground"

cd "$PROJECT_DIR"

BRANCH="${1:-${DEPLOY_BRANCH:-$(git rev-parse --abbrev-ref HEAD)}}"
echo "> Git Pull (branch: $BRANCH)"
git fetch origin "$BRANCH"
if ! git rev-parse --verify "$BRANCH" >/dev/null 2>&1; then
  echo "> Local branch '$BRANCH' not found. Creating from origin/$BRANCH"
  git checkout -q -b "$BRANCH" "origin/$BRANCH"
else
  git checkout -q "$BRANCH"
fi
git pull --ff-only origin "$BRANCH"

echo "> 프로젝트 Build 시작"
./gradlew clean build -x test

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID="$(pgrep -f "${PROJECT_NAME}.*.jar" || true)"
echo "현재 구동 중인 애플리케이션 pid: ${CURRENT_PID:-none}"

if [ -z "${CURRENT_PID}" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME="$(ls -tr "$PROJECT_DIR/build/libs/"*.jar | grep -v plain | tail -n 1)"
echo "> JAR Name: $JAR_NAME"

nohup java -jar "$JAR_NAME" > "$PROJECT_DIR/application.log" 2>&1 &


