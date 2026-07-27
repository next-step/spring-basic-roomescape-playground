#!/bin/bash

REPOSITORY=/home/ubuntu/app
REPO_URL="https://github.com/htdufhc-bit/spring-basic-roomescape-playground"
PID_FILE="$REPOSITORY/app.pid"

echo "[INFO] 시스템 패키지 업데이트"
sudo apt update

echo "[INFO] JDK 21 설치"
sudo apt install -y openjdk-21-jdk

echo "[INFO] 프로젝트 디렉토리 생성 및 이동"
mkdir -p $REPOSITORY
cd $REPOSITORY

echo "[INFO] Git Repository 클론 또는 최신화"
if [ -d ".git" ]; then
    echo "이미 git repository가 존재합니다. 최신 코드를 받아옵니다."
    git pull
else
    git clone $REPO_URL .
fi

echo "[INFO] 초기 설정 완료"
