#!/bin/bash

# Git 저장소 URL 및 클론할 디렉토리 설정
REPO_URL="https://github.com/dhkimgit/spring-basic-roomescape-playground.git"
REPO=~/app/deploy
PROJECT_NAME=spring-basic-roomescape-playground
BRANCH=dhkimgit
PID_FILE=~/app/pid

sudo apt-get install -y git

if [ -d "$REPO/$PROJECT_NAME" ]; then
    echo "Git 저장소가 이미 존재합니다."
    cd $REPO/$PROJECT_NAME/
else
    echo "Git 저장소 클론"
    mkdir -p $REPO
    git clone $REPO_URL $REPO/$PROJECT_NAME
    cd $REPO/$PROJECT_NAME/
    git checkout $BRANCH
fi

if [ ! -f "$PID_FILE" ]; then
    echo "> PID 파일을 생성합니다"
    touch $PID_FILE
fi

chmod +x ./gradlew
