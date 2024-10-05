#!/bin/bash

REPOSITORY_URL=https://github.com/next-step/spring-basic-roomescape-playground.git
REPOSITORY_DIR=~/app/deploy
PROJECT_NAME=spring-basic-roomescape-playground
PID_FILE=~/app/pid
BRANCH=kih1015

# Git 저장소 존재 여부 확인 및 클론
if [ -d "$REPOSITORY_DIR/$PROJECT_NAME" ]; then
    echo "Git 저장소가 이미 존재합니다."
    cd REPOSITORY_DIR/PROJECT_NAME
else
    echo "Git 저장소 클론"
    mkdir -p REPOSITORY_DIR
    cd REPOSITORY_DIR
    git clone $REPOSITORY_URL
    cd PROJECT_NAME
    git checkout ${BRANCH}
fi

# PID 파일 경로 확인
if [ ! -f "$PID_FILE" ]; then
    echo "PID 파일을 생성"
    touch $PID_FILE
fi

# 필요한 디렉토리 및 파일 권한 설정
chmod +x ./gradlew
