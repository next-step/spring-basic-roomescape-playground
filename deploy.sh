#!/bin/bash

REPOSITORY_DIR=~/app/deploy
PROJECT_NAME=spring-basic-roomescape-playground
PID_FILE=~/app/pid

cd $REPOSITORY_DIR/$PROJECT_NAME

# 저장소에서 최신 코드 pull
echo "> Git Pull"
git pull

# 프로젝트 빌드
echo "> 프로젝트 Build 시작"
./gradlew build

echo "> deploy 디렉토리로 이동"
cd $REPOSITORY_DIR

echo "> Build 파일 복사"
cp $REPOSITORY_DIR/$PROJECT_NAME/build/libs/*.jar $REPOSITORY_DIR

# 기존에 실행 중인 애플리이션의 PID를 찾아 종료
echo "> 현재 구동중인 애플리케이션 pid 확인"
if [ -f "$PID_FILE" ]; then
    PID=$(cat $PID_FILE)
    if ps -p $PID > /dev/null; then
        echo "애플리케이션을 종료 중입니다. (PID: $PID)"
        kill -15 $PID
        sleep 5
    else
        echo "애플리케이션이 실행 중이지 않습니다."
    fi
else
    echo "PID 파일을 찾을 수 없습니다."
fi

# 빌드된 애플리케이션 샐행
echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY_DIR/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"
nohup java -jar $JAR_NAME 2>&1 &
