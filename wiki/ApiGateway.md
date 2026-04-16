# ApiGateway

## Description
The `ApiGateway` serves as the centralized entry point for all client requests entering the `msc_eLib` microservices ecosystem. It handles request routing, load balancing, cross-cutting concerns (like OAuth2 redirection), and provides fault tolerance using Circuit Breakers.

## Tech Stack
- **Framework**: Spring Cloud Gateway (MVC Based)
- **Language**: Java 21
- **Circuit Breaker**: Resilience4j
- **Documentation**: SpringDoc OpenAPI (Aggregated Swagger UI)

## API Routing Table

| Service | Path Predicate | Target URI (Load Balanced) | Features |
| :--- | :--- | :--- | :--- |
| `user-service` | `/api/v1/users/**` | `lb://user-service` | Circuit Breaker, Fallback |
| `ItemService` | `/api/v1/item/**` | `lb://ItemService` | Circuit Breaker, Fallback |
| `borrowservice` | `/api/v1/borrows/**` | `lb://borrowservice` | Load Balanced |
| `SearchService` | `/api/v1/search/**` | `lb://searchService` | Load Balanced |
| `NotificationService` | `/api/v1/notifications/**` | `lb://NotificationService` | Load Balanced |
| `OAuth2 Auth` | `/oauth2/**` | `lb://user-service` | Login handling |

## Key Components
- **Manual Routing**: Configured in `application.properties` to map specific path patterns to microservices registered in Eureka.
- **Circuit Breakers**: Configured for critical services (`user-service`, `ItemService`) to route to internal fallback endpoints during downtime.
- **FallbackController**: Handles redirection when a microservice is unresponsive, providing friendly error responses instead of timeout errors.
- **Swagger Aggregation**: Dynamically aggregates the `/v3/api-docs` from all child services into a single UI accessible at `/swagger-ui.html`.

## Dependencies
- **DiscoveryServer (Eureka)**: Used to resolve service names to actual IP/port instances.
- **Microservices**: All backend services are downstream dependencies for routing.
