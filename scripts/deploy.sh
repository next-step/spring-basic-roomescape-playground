#!/bin/bash

PROJECT_DIR=$(dirname "$(readlink -f "$0")")
PROJECT_NAME=spring-basic-roomescape-playground

cd $PROJECT_DIR

echo "> Git Pull"
git pull origin ke-62

echo "> 프로젝트 Build 시작"
./gradlew clean build -x test

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)
echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $PROJECT_DIR/build/libs/*.jar | grep -v plain | tail -n 1)
echo "> JAR Name: $JAR_NAME"

nohup java -jar $JAR_NAME > $PROJECT_DIR/application.log 2>&1 &


