#!/bin/bash
set -e

echo "Start deployment"
echo "Pull repository"
git pull

echo "Gradle Build"
chmod +x ./gradlew
./gradlew build

CURRENT_PID=$(pgrep -f "spring-basic-roomescape-playground.*\.jar")

if [ -z "$CURRENT_PID" ]; then
    echo "No running application"
else
    echo "Terminate running application"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "Deploy new application"

JAR_FILE=$(ls build/libs/spring-basic-roomescape-playground*.jar | grep -v "\-plain.jar$" | head -n 1)

LOG_PATH="/home/ubuntu/spring-basic-roomescape-playground/logs/server_log_$(date +%Y-%m-%d).txt"

nohup java -jar --spring.profiles.active=dev "$JAR_FILE" >> "$LOG_PATH" 2>&1 &

find build/libs -type f -name "*.jar" -mtime +3 -delete

echo "Deployment complete"
