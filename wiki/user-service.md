# user-service

## Description
The `user-service` is a core microservice responsible for user management, authentication, and authorization within the `msc_eLib` system. It handles user registration, secure login via JWT, profile management, and role-based access control (RBAC).

## Tech Stack
- **Framework**: Spring Boot 3.x
- **Language**: Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security, JWT (JJWT)
- **Messaging**: RabbitMQ (Publishing user-related events)

## API Endpoints

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/users/test` | Connectivity test endpoint | No |
| `POST` | `/api/v1/users/jwt/parse` | Internal utility to parse JWT tokens | No |
| `GET` | `/api/v1/users` | List all registered users | Yes (Admin) |
| `GET` | `/api/v1/users/{id}` | Get user details by UUID | Yes |
| `POST` | `/api/v1/users/register` | Register a new user account | No |
| `POST` | `/api/v1/users/login` | Authenticate user and receive JWT | No |
| `GET` | `/api/v1/users/me` | Fetch profile of current authenticated user | Yes |
| `PUT` | `/api/v1/users/edit-profile` | Update profile information | Yes |
| `DELETE` | `/api/v1/users/{id}` | Remove a user account | Yes (Admin) |

## Key Components
- **UserService**: Business logic for user CRUD, registration, and login.
- **JwtUtil**: Utility for generating and validating JSON Web Tokens.
- **NotificationEventPublisher**: Publishes events (e.g., user created) to RabbitMQ for indexing or notification purposes.
- **SecurityConfig**: Configures stateless security and JWT filters.

## Dependencies
- **DiscoveryServer**: Registered as a Eureka client.
- **NotificationService**: Sends registration confirmations (indirectly via RabbitMQ).
- **ApiGateway**: Accessed through the unified gateway entry point.
