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

# Build dependencies for borrowservice
RUN mvn -pl borrowservice -am dependency:go-offline

# Copy borrowservice source
COPY borrowservice/src borrowservice/src

# Build borrowservice
RUN mvn -pl borrowservice -DskipTests clean package

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/borrowservice/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
