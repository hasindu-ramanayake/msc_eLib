# borrowservice

## Description
The `borrowservice` manages the lifecycle of borrowing transactions for library items. It tracks which items are borrowed by which users, handles due dates, returns, and maintains a user's credit score based on their borrowing habits. It also includes a waiting list (Waitlist) functionality for items that are currently out of stock.

## Tech Stack
- **Framework**: Spring Boot 3.x
- **Language**: Java 21
- **Database**: PostgreSQL
- **Messaging**: RabbitMQ (Sends events for borrowing success and return reminders)

## API Endpoints

### Borrow Management (`/api/v1/borrows`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/` | Borrow an item (verifies stock and user credit) |
| `GET` | `/` | List all borrow records |
| `GET` | `/{id}` | Get specific borrow details |
| `PATCH` | `/{id}/return` | Mark an item as returned |
| `GET` | `/users/{userId}` | Get borrowing history for a specific user |
| `GET` | `/users/{userId}/overdue` | List overdue items for a user |
| `GET` | `/users/{userId}/underdue` | List active, non-overdue borrows for a user |
| `GET` | `/users/{userId}/credit` | Retrieve the calculated credit score for a user |
| `GET` | `/available/{itemId}` | Check if an item is currently available for borrowing |

### Waitlist Management (`/api/v1/waitlist`)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/` | Join the waitlist for an out-of-stock item |
| `GET` | `/{userId}` | View a user's current waitlist positions |
| `DELETE` | `/{id}` | Remove a user from a specific waitlist |

## Key Components
- **BorrowController**: Handles borrowing requests and credit checks.
- **WaitlistController**: Manages reservations for out-of-stock items.
- **NotificationEventPublisher**: Sens events to RabbitMQ when an item is borrowed or returned, triggering notifications.
- **Credit Scoring Logic**: Internal logic that updates user "trust" based on on-time returns vs overdue returns.

## Dependencies
- **DiscoveryServer**: Registered as a microservice.
- **ItemService**: Verifies item existence and stock levels before borrowing.
- **UserService**: Validates user identity and roles.
- **NotificationService**: Triggered by events from this service.
