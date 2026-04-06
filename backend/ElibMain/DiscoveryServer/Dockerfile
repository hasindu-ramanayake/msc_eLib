# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent pom
COPY pom.xml .
# Copy submodules poms to satisfy dependency graph
COPY ApiGateway/pom.xml ApiGateway/
COPY SearchService/pom.xml SearchService/
COPY DiscoveryServer/pom.xml DiscoveryServer/
COPY NotificationService/pom.xml NotificationService/
COPY EventService/pom.xml EventService/
COPY borrowservice/pom.xml borrowservice/

# Build dependencies for DiscoveryServer
RUN mvn -pl DiscoveryServer -am dependency:go-offline

# Copy DiscoveryServer source
COPY DiscoveryServer/src DiscoveryServer/src

# Build DiscoveryServer
RUN mvn -pl DiscoveryServer -DskipTests clean package

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/DiscoveryServer/target/*.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
