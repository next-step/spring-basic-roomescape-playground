#!/bin/bash
export PROJECT=next-step/spring-basic-roomescape-playground
export BRANCH=soundbar91

sudo apt-get install git

mkdir -p ~/app/deploy
cd ~/app/deploy

git clone https://github.com/${PROJECT}.git
cd ~/app/deploy/${PROJECT}

git checkout ${BRANCH}

chmod +x ./gradlew

./gradlew build -x test
