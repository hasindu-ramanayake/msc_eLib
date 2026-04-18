# Item Service

The Item Service is responsible for managing items (books, etc.) in the eLib system. It provides APIs for CRUD operations, stock management, and searching.

## Technology Stack
- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA** (PostgreSQL)
- **Spring AMQP** (RabbitMQ)
- **Spring Cloud Netflix Eureka** (Service Discovery)
- **MapStruct & Lombok** (Data Mapping and Boilerplate reduction)

## Building the Service
To build the service using Docker:
```bash
# Run from the root of the project (backend/ElibMain)
docker build -t item-service -f ItemService/Dockerfile .
```

## Testing the Service (Isolated)
We use an **Isolater test docker container** to run integration tests in a clean, containerized environment. This approach ensures maximum isolation by spinning up a dedicated PostgreSQL and RabbitMQ instance for testing.

To run the tests, use only the following docker command:
```bash
# Run from ItemService directory
docker compose -f docker-compose.test.yml up --build --exit-code-from item-service-isolater
```

### Test Cases Included
The integration suite covers 5 core functional cases:
1. **shouldCreateItem**: Verifies item creation via POST API.
2. **shouldGetItemById**: Verifies retrieval of an item by its UUID.
3. **shouldUpdateItem**: Verifies updating item details via PUT API.
4. **shouldIncreaseStock**: Verifies stock management via PATCH API.
5. **shouldSearchItems**: Verifies keyword-based search functionality.

## API Endpoints
- `POST /api/v1/item`: Create a new item.
- `GET /api/v1/item/{id}`: Detailed view of an item.
- `PUT /api/v1/item/{id}`: Update item details.
- `PATCH /api/v1/item/{id}/increase`: Increase stock quantity.
- `GET /api/v1/item/search`: Search items by keyword.
- `POST /api/v1/item/load-csv`: Trigger background CSV data loading.
