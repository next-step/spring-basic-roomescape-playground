#!/bin/bash

REPO_URL="https://github.com/selee1012/spring-basic-roomescape-playground.git"
REPO_DIR="/home/ec2-user/roomescape"
PID_FILE="$REPO_DIR/application.pid"

# Git 저장소 존재 여부 확인 및 클론
if [ ! -d "$REPO_DIR/.git" ]; then
    git clone "$REPO_URL" "$REPO_DIR"
fi

# PID 파일 경로 설정 확인
echo "PID_FILE=$PID_FILE"

# 디렉토리 및 파일 권한 설정 (옵션)
chmod +x "$REPO_DIR/gradlew" "$REPO_DIR/deploy.sh"