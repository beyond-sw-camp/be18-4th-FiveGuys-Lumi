FROM openjdk:21-jdk
WORKDIR /app
COPY build/libs/*.jar app.jar
COPY .env /app/.env
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
