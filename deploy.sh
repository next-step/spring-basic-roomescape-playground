#!/bin/bash
REPOSITORY=~/app/deploy
PROJECT=spring-basic-roomescape-playground

cd $REPOSITORY/$PROJECT

git pull

./gradlew build

CURRENT_PID=$(pgrep -f ${PROJECT}.*.jar)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

JAR_NAME=$(ls -tr $REPOSITORY/$PROJECT/build/libs/*.jar | tail -n 1)
echo "JAR Name: $JAR_NAME"

nohup java -jar $JAR_NAME >> $REPOSITORY/$PROJECT/app.log 2>&1 &
