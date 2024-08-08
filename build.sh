#!/bin/bash

# 현재 구동 중인 애플리케이션 확인
CURRENT_APP=$(docker-compose ps | grep "app_blue" | grep "Up" || echo "")

# 새 버전을 배포할 대상 결정
if [[ -n "$CURRENT_APP" ]]; then
  TARGET="app_green"
else
  TARGET="app_blue"
fi

echo "배포할 대상: $TARGET"

# Docker 이미지 빌드
docker-compose build $TARGET

# 새로운 버전 실행
docker-compose up -d $TARGET

# 새로운 버전이 구동될 때까지 대기
echo "새로운 버전의 애플리케이션이 구동될 때까지 대기 중..."
sleep 15

# 오래된 버전 중지
if [[ "$TARGET" == "app_green" ]]; then
  OLD_APP="app_blue"
else
  OLD_APP="app_green"
fi

echo "오래된 버전 중지: $OLD_APP"
docker-compose stop $OLD_APP