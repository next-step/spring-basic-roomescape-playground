git pull code JPA4
./gradlew clean build
PID=$(cat application.pid 2>/dev/null)

if [ -n "$PID" ]; then
    kill -15 "$PID"
    sleep 5
fi

JAR=$(find build/libs -name "*.jar" | head -n 1)

nohup java -jar "$JAR" > application.log 2>&1 &

echo $! > application.pid
