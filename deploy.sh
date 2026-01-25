#!/bin/bash

# 1. 저장소에서 최신 코드를 pull 받음
echo "> Git Pull"
git pull origin main

# 2. 프로젝트 빌드
echo "> Project Build"
./gradlew build

# 3. 기존에 실행 중인 애플리케이션의 PID를 찾아 종료
echo "> 현재 실행 중인 애플리케이션 PID 확인"
CURRENT_PID=$(pgrep -f "spring-basic-roomescape-playground")

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 실행 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

# 4. 빌드된 애플리케이션 실행
echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr build/libs/*.jar | grep -v "plain" | tail -n 1)

echo "> JAR Name: $JAR_NAME"

nohup java -jar $JAR_NAME > nohup.out 2>&1 &