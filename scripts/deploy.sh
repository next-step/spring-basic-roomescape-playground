#!/bin/bash

set -e

REPO_DIR="/home/ec2-user/roomescape"
PID_FILE="$REPO_DIR/application.pid"

cd "$REPO_DIR"

# 저장소에서 최신 코드를 pull 받음
git pull origin main

# 프로젝트 빌드
./gradlew clean bootJar -x test

# 기존에 실행 중인 애플리케이션의 PID를 찾아 종료
if [ -f "$PID_FILE" ]; then
    kill "$(cat "$PID_FILE")" 2>/dev/null
fi

# 빌드된 애플리케이션 실행
JAR=$(ls build/libs/*.jar | grep -v plain | head -n 1)
nohup java -jar "$JAR" --spring.profiles.active=prod > application.log 2>&1 &
echo $! > "$PID_FILE"