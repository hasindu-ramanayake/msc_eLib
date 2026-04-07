# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy parent pom
COPY pom.xml .
# Copy submodules poms
COPY ApiGateway/pom.xml ApiGateway/
COPY SearchService/pom.xml SearchService/
COPY DiscoveryServer/pom.xml DiscoveryServer/
COPY user-service/pom.xml user-service/
COPY NotificationService/pom.xml NotificationService/
COPY EventService/pom.xml EventService/
COPY borrowservice/pom.xml borrowservice/

# Build dependencies for user-service
RUN mvn install -N && cd user-service && mvn dependency:go-offline

# Copy user-service source
COPY user-service/src user-service/src

# Build user-service
RUN cd user-service && mvn -DskipTests clean package

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
