FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY target/NotificationService-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]