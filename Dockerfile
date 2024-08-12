# OpenJDK 17을 기반으로 하는 이미지를 사용합니다
FROM openjdk:17-jdk-slim

# 컨테이너 내의 작업 디렉터리를 설정합니다
WORKDIR /app

# 호스트의 JAR 파일을 컨테이너로 복사합니다
COPY ./app.jar app.jar

# 애플리케이션이 사용할 포트를 노출합니다
EXPOSE 8080

# JAR 파일을 실행하는 명령을 지정합니다
CMD ["java", "-jar", "app.jar"]