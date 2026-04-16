# ItemService

## Description
The `ItemService` manages the physical and digital collection of the eLibrary. It is responsible for cataloging items (books, movies, games), tracking inventory levels, and synchronizing with the `SearchService` for indexed searching.

## Tech Stack
- **Framework**: Spring Boot 3.x
- **Language**: Java 21
- **Database**: PostgreSQL
- **Messaging**: RabbitMQ (Used for broadcasting item updates to SearchService)
- **Monitoring**: Actuator & OpenDoc for Swagger UI.

## API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/item` | List all available items |
| `GET` | `/api/v1/item/{id}` | Get specific item details by ID |
| `GET` | `/api/v1/item/title` | Find item by exact title match |
| `GET` | `/api/v1/item/isbn` | Find item by ISBN |
| `GET` | `/api/v1/item/search` | Basic keyword search (DB-backed) |
| `POST` | `/api/v1/item` | Add a new item to the library |
| `PUT` | `/api/v1/item/{id}` | Update existing item metadata |
| `DELETE` | `/api/v1/item/{id}` | Remove an item from the catalog |
| `PATCH` | `/api/v1/item/{id}/increase` | Increase inventory stock |
| `PATCH` | `/api/v1/item/{id}/decrease` | Decrease inventory stock |
| `POST` | `/api/v1/item/load-csv` | Bulk upload items from CSV file |

## Key Components
- **ItemController**: Entry point for RESTful catalog management.
- **RabbitMQService**: Handles the publication of messages whenever an item is added, updated, or deleted to ensure SearchService remains consistent.
- **Inventory Tracking**: Managed via direct updates to the database with safety checks on stock levels.

## Dependencies
- **DiscoveryServer**: Registered for service discovery.
- **SearchService**: Consumes updates via RabbitMQ to update the Elasticsearch index.
- **ApiGateway**: Routes all external catalog requests here.
