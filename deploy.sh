#!/bin/bash

echo "Start deployment"
echo "Pull repository"
git pull

echo "Gradle Build"
chmod +x ./gradlew
./gradlew build -x test

CURRENT_PID=$(pgrep -f ".jar")

if [ -z "$CURRENT_PID" ]; then
    echo "No running application"
else
    echo "Terminate running application"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "Deploy new application"

JAR_FILE=$(ls -t build/libs/*.jar | grep -v "\-plain.jar$" | head -n 1)

nohup java -jar "$JAR_FILE" &

echo "Deployment complete"
