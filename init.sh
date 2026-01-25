#!/bin/bash

PROJECT_NAME="spring-basic-roomescape-playground"
REPOSITORY_URL="https://github.com/kimsky247-coder/spring-basic-roomescape-playground.git"

# 1. Git 저장소 존재 여부 확인 및 클론
if [ -d "$PROJECT_NAME" ]; then
    echo "> 이미 프로젝트 폴더가 존재합니다."
else
    echo "> 프로젝트 클론을 시작합니다."
    git clone $REPOSITORY_URL
fi

cd $PROJECT_NAME

# 2. PID 파일 경로 설정 확인
echo "> 프로젝트 경로 진입: $(pwd)"

# 3. 필요한 디렉토리 및 파일 권한 설정
echo "> 실행 권한 설정 (gradlew, 스크립트 파일)"
if [ -f "./gradlew" ]; then
    chmod +x ./gradlew
fi