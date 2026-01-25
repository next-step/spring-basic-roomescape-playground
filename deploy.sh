#!/bin/bash

APP_NAME="roomescape"
APP_DIR="/home/ubuntu/roomescape"
JAR_NAME="roomescape-0.0.1-SNAPSHOT.jar"
PROD_BRANCH="deploy"
KILL_TIMEOUT=10000

# 1. Git 저장소 확인
if [ ! -d "$APP_DIR/.git" ]; then
  git clone https://github.com/chemistryx/spring-basic-roomescape-playground -b $PROD_BRANCH $APP_DIR
fi

cd $APP_DIR || exit 1

# 2. 최신 저장소 pull
git pull origin $PROD_BRANCH

# 3. 빌드
./gradlew clean build -x test

# 4. 기존 프로세스 종료
pm2 stop $APP_NAME --kill-timeout $KILL_TIMEOUT
pm2 delete $APP_NAME || true

# 5. 실행
pm2 start java \
  --name $APP_NAME \
  --kill-timeout $KILL_TIMEOUT \
  -- -jar build/libs/$JAR_NAME \
  --spring.profiles.active=prod

# 6. PM2 저장
pm2 save
