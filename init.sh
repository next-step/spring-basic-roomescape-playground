#!/bin/bash
REPOSITORY=next-step/spring-basic-roomescape-playground
BRANCH=krseonghyeon

sudo apt-get install -y git

mkdir -p ~/app/deploy
cd ~/app/deploy

if [ -d "$REPOSITORY" ];  then
    echo "> 저장소가 이미 존재합니다."
else
    git clone https://github.com/$REPOSITORY.git
fi

cd $REPOSITORY
git checkout $BRANCH

chmod +x ./gradlew

./gradlew build
