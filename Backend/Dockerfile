# 1. JDK 이미지 선택 (21 버전)
FROM openjdk:21-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사 (Gradle 빌드 산출물)
COPY build/libs/*.jar app.jar

# 4. 외부 접근 포트 열기
EXPOSE 8080

# 5. 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]
