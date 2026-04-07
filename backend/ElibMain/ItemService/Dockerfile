# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent pom
COPY pom.xml .
# Copy ALL submodules poms to satisfy dependency graph
COPY ApiGateway/pom.xml ApiGateway/
COPY SearchService/pom.xml SearchService/
COPY DiscoveryServer/pom.xml DiscoveryServer/
COPY NotificationService/pom.xml NotificationService/
COPY EventService/pom.xml EventService/
COPY borrowservice/pom.xml borrowservice/
COPY ItemService/pom.xml ItemService/

# Build dependencies for EventService
RUN mvn -pl ItemService -am dependency:go-offline

# Copy EventService source
COPY ItemService/src ItemService/src

# Build EventService
RUN mvn -pl ItemService -DskipTests clean package

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/ItemService/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
