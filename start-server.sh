#!/bin/bash
export PROJECT=spring-basic-roomescape-playground

cd ~/app/deploy/${PROJECT}

git pull

./gradlew build -x test

sleep 5

CURRENT_PID=$(ps -ef | grep java | grep ${PROJECT}-0.0.1 | awk '{print $2}')

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 어플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"

    kill -15 $CURRENT_PID

    sleep 10
fi

nohup java -jar ~/app/deploy/${PROJECT}/build/libs/${PROJECT}-0.0.1-SNAPSHOT.jar > ~/app/deploy/${PROJECT}/build/libs/app.log 2>&1 &
