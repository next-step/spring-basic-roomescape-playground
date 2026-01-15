#!/bin/bash

echo "==============================="
echo "애플리케이션 배포 시작"
echo "==============================="

BRANCH="jpa2-taewoo"
JAR_PATH="build/libs/spring-basic-roomescape-playground-0.0.1-SNAPSHOT.jar"
PROFILE="prod"
LOG_FILE="app.log"

echo ">>> 최신 코드 가져오기"
git pull origin $BRANCH

echo ">>> 프로젝트 빌드"
./gradlew clean build -x test

echo ">>> 기존 애플리케이션 종료 확인"
CURRENT_PID=$(pgrep -f "spring-basic-roomescape-playground-0.0.1-SNAPSHOT.jar")

if [ -z "$CURRENT_PID" ]; then
  echo ">>> 실행 중인 애플리케이션이 없습니다."
else
  echo ">>> 실행 중인 애플리케이션 종료 (PID: $CURRENT_PID)"
  kill -15 $CURRENT_PID
  sleep 3
fi

echo ">>> 애플리케이션 실행"
nohup java -jar $JAR_PATH --spring.profiles.active=$PROFILE > $LOG_FILE 2>&1 &

sleep 2
NEW_PID=$(pgrep -f "spring-basic-roomescape-playground-0.0.1-SNAPSHOT.jar")
echo ">>> 실행된 애플리케이션 PID: $NEW_PID"
echo ">>> 로그 파일: $LOG_FILE"

echo "==============================="
echo "배포 완료"
echo "==============================="
