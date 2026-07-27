#!/bin/bash

REPOSITORY=/home/ubuntu/app
REPO_URL="https://github.com/htdufhc-bit/spring-basic-roomescape-playground"
PID_FILE="$REPOSITORY/app.pid"

echo "[INFO] 최신 코드 pull"
cd $REPOSITORY
git pull $REPO_URL

echo "[INFO] 프로젝트 빌드"
cd $REPOSITORY
./gradlew build -x test

echo "[INFO] 기존에 실행 중인 애플리케이션 PID 종료"
CURRENT_PID=$(pgrep -f "$REPOSITORY/build/libs/*.jar")

if [ -z "$CURRENT_PID" ]; then
  echo "[INFO] 종료할 애플리케이션이 없습니다."
else
  echo "[INFO] kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "[INFO] 새 애플리케이션 실행"
JAR_NAME=$(ls $REPOSITORY/build/libs/*.jar | grep -v plain)

nohup java -jar $JAR_NAME > $REPOSITORY/nohup.log 2>&1 &

echo $! > $PID_FILE
echo "[INFO] 애플리케이션이 실행되었습니다. PID: $(cat $PID_FILE)"
