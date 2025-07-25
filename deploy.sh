set -e

PROJECT_ROOT="/home/ubuntu/spring-basic-roomescape-playground"
APP_NAME="spring-basic-roomescape-playground"
BRANCH="step3"
LOG_DIR="$PROJECT_ROOT/logs"
DEPLOY_LOG="$LOG_DIR/deploy_$(date +%Y%m%d_%H%M%S).log"
SERVER_LOG="$LOG_DIR/server_$(date +%Y-%m-%d).log"

on_error() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] ERROR: '$BASH_COMMAND' 실행 중 에러" >> "$DEPLOY_LOG"
  exit 1
}
trap on_error ERR

mkdir -p "$LOG_DIR"
exec > >(tee -a "$DEPLOY_LOG") 2>&1
echo "=== 배포 시작 $(date '+%Y-%m-%d %H:%M:%S') ==="

cd "$PROJECT_ROOT"
echo "git pull" >> $DEPLOY_LOG
git pull >> $DEPLOY_LOG 2>&1

echo "gradle build" >> $DEPLOY_LOG
chmod +x ./gradlew
./gradlew build >> $DEPLOY_LOG 2>&1

CURRENT_PID=$(pgrep -f "$APP_NAME")
if [[ -n "$CURRENT_PID" ]]; then
  echo "실행 중인 애플리케이션 종료 (PID=$CURRENT_PID)"
  kill -15 "$CURRENT_PID"
  sleep 5
else
  echo "실행 중인 애플리케이션이 없습니다"
fi

JAR_FILE=$(ls -t build/libs/*.jar | grep -v "\-plain.jar$" | head -n1)

nohup java -jar -spring.profiles.active=dev "$JAR_FILE" >> "$SERVER_LOG" 2>&1 &
NEW_PID=$!

if kill -0 "$NEW_PID" &>/dev/null; then
  echo "애플리케이션 실행 성공 (PID=$NEW_PID)" >> $DEPLOY_LOG
else
  echo "애플리케이션 실행 실패" >> $DEPLOY_LOG
  exit 1
fi

echo "=== 배포 성공 $(date '+%Y-%m-%d %H:%M:%S') ==="
