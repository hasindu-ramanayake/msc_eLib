# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent pom
COPY pom.xml .
# Copy submodules poms
COPY ApiGateway/pom.xml ApiGateway/
COPY SearchService/pom.xml SearchService/
COPY DiscoveryServer/pom.xml DiscoveryServer/
COPY NotificationService/pom.xml NotificationService/
COPY EventService/pom.xml EventService/
COPY borrowservice/pom.xml borrowservice/

# Build dependencies for ApiGateway
RUN mvn -pl ApiGateway -am dependency:go-offline

# Copy ApiGateway source
COPY ApiGateway/src ApiGateway/src

# Build ApiGateway
RUN mvn -pl ApiGateway -DskipTests clean package

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/ApiGateway/target/*.jar app.jar

EXPOSE 8765
ENTRYPOINT ["java", "-jar", "app.jar"]
