FROM eclipse-temurin:21-jre-alpine

WORKDIR /spring-boot

# JAR 파일을 복사할 경로를 설정
ARG JAR_FILE=build/libs/*SNAPSHOT.jar

# 위에서 설정한 JAR 파일을 컨테이너의 app.jar로 복사
COPY ${JAR_FILE} app.jar

# 컨테이너가 실행될 때 동작하는 명령어 정의
ENTRYPOINT ["java", "-Dspring.profiles.active=${USE_PROFILE}", "-Duser.timezone=Asia/Seoul", "-jar", "/spring-boot/app.jar"]
