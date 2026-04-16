# BorrowBatchService

## Description
The `BorrowBatchService` is a dedicated background processing microservice responsible for maintenance tasks related to borrowing records. Unlike other services, it does not expose a public REST API; instead, it runs scheduled jobs to automate administrative processes like detecting overdue items and updating user consistency.

## Tech Stack
- **Framework**: Spring Boot 3.x
- **Language**: Java 21
- **Database**: PostgreSQL (Shared or direct access to borrowing schemas)
- **Scheduling**: Spring `@Scheduled`

## Scheduled Tasks

| Task Name | Cron Expression | Description |
| :--- | :--- | :--- |
| `runOverdueCheck` | `0 0 2 * * *` | Runs daily at 2:00 AM. Scans active borrows where the due date has passed and marks them as "OVERDUE". |

## Key Components
- **BorrowScheduler**: Contains the `@Scheduled` methods that trigger batch jobs.
- **BorrowService (Internal)**: Contains the business logic for mass-updating records and potentially triggering notification events for overdue items.
- **Batch Processing**: Designed to handle large volumes of records efficiently outside of peak usage hours (configured for 2 AM).

## Dependencies
- **DiscoveryServer**: Registered as a service, though it primarily acts as a client for internal processing.
- **Database**: Requires connection to the same PostgreSQL instances used by `borrowservice` (or a replicated view) to perform bulk updates.
- **NotificationService**: May trigger overdue alerts (indirectly through event-driven mechanisms if integrated).
