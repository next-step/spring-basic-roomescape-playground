#!/bin/bash

REPO=~/app/deploy
PROJECT_NAME=spring-basic-roomescape-playground
PID_FILE=~/app/pid

cd $REPO/$PROJECT_NAME

git pull || { echo "git pull 실패"; exit 1; }

./gradlew build

CURRENT_PID=$(pgrep -f "${PROJECT_NAME}.*.jar")

cd $REPO

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없습니다."

else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 10
fi

JAR_NAME=$(ls -tr $REPO/$PROJECT_NAME/build/libs/*.jar | tail -n 1)

nohup java -jar $JAR_NAME >> $REPO/$PROJECT_NAME/app.log 2>&1 &
NEW_PID=$!
echo $NEW_PID > $PID_FILE
