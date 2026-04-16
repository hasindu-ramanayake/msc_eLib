# DiscoveryServer

## Description
The `DiscoveryServer` is the central registry for all microservices in the `msc_eLib` platform. It uses Netflix Eureka to enable "service discovery," allowing microservices to find and communicate with each other dynamically without hardcoded IP addresses or port numbers.

## Tech Stack
- **Framework**: Spring Cloud Netflix Eureka Server
- **Language**: Java 21

## Dashboard
The Eureka dashboard is accessible at:
- `http://localhost:8761` (Local)
- `http://discovery-server:8761` (Within Docker network)

## Configuration
- **Port**: `8761`
- **Self-Preservation**: Enabled by default to prevent accidental eviction of services during network partitions.
- **Client Configuration**: Every other microservice in the system includes the `spring-cloud-starter-netflix-eureka-client` dependency and points to this server's URL.

## Key Components
- **EurekaServerApplication**: The main entry point annotated with `@EnableEurekaServer`.
- **Service Registration**: Microservices register themselves upon startup with their application name (e.g., `USER-SERVICE`).
- **Heartbeat Mechanism**: Services send periodic heartbeats to let the server know they are still healthy.

## Role in Architecture
Without the `DiscoveryServer`, the `ApiGateway` would not be able to load balance requests to the appropriate service instances. It acts as the "phonebook" of the entire system.
