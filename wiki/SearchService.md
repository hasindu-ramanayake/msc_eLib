# SearchService

## Description
The `SearchService` provides high-performance, advanced search capabilities for the eLibrary catalog. It utilizes Elasticsearch to allow users to filter items by various criteria such as keywords, genres, formats, and age ratings. It synchronizes its search index in real-time by consuming events from the `ItemService`.

## Tech Stack
- **Framework**: Spring Boot 3.x
- **Language**: Java 21
- **Search Engine**: Elasticsearch 8.x
- **Messaging**: RabbitMQ (Consumes item lifecycle events)

## API Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/search` | Advanced search with multi-filter support (keyword, genre, format, etc.) |

## Key Components
- **SearchController**: Primary interface for all search queries.
- **ItemEventListener**: Listens for RabbitMQ messages from the `ItemService` to keep the Elasticsearch index up to date (syncing additions, updates, and deletions).
- **SearchRepository**: Interfaces with Elasticsearch using Spring Data Elasticsearch.
- **Filtering Logic**: Implements complex query builders to support faceted search and filtering.

## Dependencies
- **DiscoveryServer**: Registered as a search provider.
- **Elasticsearch**: The primary data source for search queries.
- **ItemService**: The source of truth for items; search index is updated via events from this service.
- **ApiGateway**: Routes client search requests to this service.
