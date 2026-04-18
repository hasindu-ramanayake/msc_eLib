# User Service

The **User Service** is a core microservice of the eLib system, responsible for user management, authentication, and authorization. It integrates with Google OAuth2 for external authentication and uses JWT for internal session management.

## Features
- User Registration and Login (JWT based).
- Google OAuth2 Integration.
- Profile Management.
- RBAC (Role-Based Access Control) with ADMIN, STAFF, and CUSTOMER roles.
- Asynchronous notification publishing via RabbitMQ.
- Circuit Breaking with Resilience4j.
- API Documentation with Swagger/OpenAPI.

## Technology Stack
- **Framework**: Spring Boot 3.x
- **Language**: Java 21
- **Database**: PostgreSQL
- **Messaging**: RabbitMQ
- **Service Discovery**: Eureka Client
- **Security**: Spring Security, OAuth2, JWT
- **Build Tool**: Maven

## Dependencies
- `spring-boot-starter-data-jpa`: Database persistence.
- `spring-boot-starter-web`: REST API support.
- `spring-cloud-starter-netflix-eureka-client`: Service discovery.
- `spring-boot-starter-security`: Authentication and authorization.
- `spring-boot-starter-oauth2-client`: Google OAuth2 support.
- `jjwt-api`: JSON Web Token implementation.
- `spring-boot-starter-amqp`: RabbitMQ messaging.
- `resilience4j-spring-boot3`: Circuit breaker and resilience patterns.
- `springdoc-openapi-starter-webmvc-ui`: Swagger UI.

## Configuration (Environment Variables)
The service requires the following environment variables (defined in `.env` or passed via Docker):

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | PostgreSQL Connection URL | - |
| `SPRING_DATASOURCE_USERNAME` | DB Username | - |
| `SPRING_DATASOURCE_PASSWORD` | DB Password | - |
| `GOOGLE_CLIENT_ID` | Google OAuth2 Client ID | - |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 Client Secret | - |
| `JWT_SECRET` | Secret key for signing JWTs | - |
| `SPRING_RABBITMQ_HOST` | RabbitMQ Host | `localhost` |
| `SPRING_RABBITMQ_PORT` | RabbitMQ Port | `5672` |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | Eureka Server URL | `http://localhost:8761/eureka/` |

## How to Build
To build the service as a Docker image:
```bash
docker build -t user-service .
```

## How to Test
The service includes an **isolated testing environment** that allows you to run integration tests without having Java or PostgreSQL/RabbitMQ installed on your local machine.

### Prerequisites
- Docker and Docker Compose installed.

### Run tests in Docker
Execute the following command in the `user-service` directory:
```bash
docker compose -f docker-compose.test.yml up --build --exit-code-from user-service-test
```
This command will:
1. Spin up ephemeral PostgreSQL and RabbitMQ instances.
2. Build a Maven container to run the tests.
3. Clean up all resources after the tests complete.

## API Documentation
Once the service is running, you can access the Swagger UI at:
`http://localhost:8081/swagger-ui.html`

## Main Endpoints
- `GET /api/v1/users/test`: Health check.
- `POST /api/v1/users/register`: Register a new user.
- `POST /api/v1/users/login`: Authenticate and receive a JWT.
- `GET /api/v1/users/me`: Get current user profile.
- `PUT /api/v1/users/edit-profile`: Update profile info.
- `GET /api/v1/users`: List all users (Admin only).
