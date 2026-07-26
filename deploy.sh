#!/bin/bash

# 1. 최신 코드 불러오기
echo ">>> Git Pull 실행"
git pull origin main

# 2. 프로젝트 빌드
echo ">>> 프로젝트 빌드 시작"
./gradlew build -x test

# 3. 기존 실행 중인 프로세스 종료
echo ">>> 기존 애플리케이션 PID 확인"
CURRENT_PID=$(pgrep -f roomescape-0.0.1-SNAPSHOT.jar)

if [ -z "$CURRENT_PID" ]; then
    echo ">>> 실행 중인 애플리케이션이 없습니다."
else
    echo ">>> 기존 애플리케이션 종료 (PID: $CURRENT_PID)"
    kill -15 $CURRENT_PID
    sleep 5
fi

# 4. 새 애플리케이션 실행
echo ">>> 새 애플리케이션 배포 및 실행"
JAR_NAME=$(ls -tr build/libs/*.jar | tail -n 1)

nohup java -jar $JAR_NAME > app.log 2>&1 &

echo ">>> 배포 완료!"
