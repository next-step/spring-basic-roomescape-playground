#!/bin/bash

REPOSITORY="$(cd "$(dirname "$0")" && pwd)"
PID_FILE="$REPOSITORY/app.pid"
LOG_FILE="$REPOSITORY/nohup.log"

echo "[INFO] 최신 코드 pull"
cd "$REPOSITORY" || exit 1
git pull

echo "[INFO] 프로젝트 빌드"
./gradlew build
if [ $? -ne 0 ]; then
  echo "[ERROR] 빌드에 실패했습니다. 배포를 중단합니다."
  exit 1
fi

echo "[INFO] 기존에 실행 중인 애플리케이션 종료"
CURRENT_PID=$(jps -l | grep roomescape | awk '{print $1}')

if [ -z "$CURRENT_PID" ] && [ -f "$PID_FILE" ]; then
  CURRENT_PID=$(cat "$PID_FILE")
  kill -0 "$CURRENT_PID" 2>/dev/null || CURRENT_PID=""
fi

if [ -z "$CURRENT_PID" ]; then
  echo "[INFO] 종료할 애플리케이션이 없습니다."
else
  echo "[INFO] kill -15 $CURRENT_PID"
  kill -15 "$CURRENT_PID" 2>/dev/null || taskkill //PID "$CURRENT_PID" //F
  sleep 5
fi
rm -f "$PID_FILE"

echo "[INFO] 새 애플리케이션 실행"
JAR_NAME=$(ls "$REPOSITORY"/build/libs/*.jar | grep -v plain)
JAR_WIN=$(cygpath -w "$JAR_NAME")

nohup java -jar "$JAR_WIN" > "$LOG_FILE" 2>&1 &
echo $! > "$PID_FILE"

sleep 3
echo "[INFO] 애플리케이션이 실행되었습니다. PID: $(cat "$PID_FILE")"
echo "[INFO] 로그 확인: tail -f $LOG_FILE"