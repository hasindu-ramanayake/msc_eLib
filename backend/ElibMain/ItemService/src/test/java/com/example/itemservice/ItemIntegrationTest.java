package com.example.itemservice;

import com.example.itemservice.dto.ItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "noauth"})
class ItemIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpClient client;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        client = HttpClient.newHttpClient();
        baseUrl = "http://localhost:" + port + "/api/v1/item";
    }

    @Test
    void shouldCreateItem() throws Exception {
        ItemDto dto = new ItemDto();
        dto.setIsbn13("9780132350884");
        dto.setTitle("Clean Code");
        dto.setAuthor("Robert C. Martin");
        dto.setTotalStock(5);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(dto)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        ItemDto created = objectMapper.readValue(response.body(), ItemDto.class);
        assertNotNull(created.getId());
        assertEquals("Clean Code", created.getTitle());
    }

    @Test
    void shouldGetItemById() throws Exception {
        UUID id = createAndGetId("Refactoring", "Martin Fowler");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        ItemDto fetched = objectMapper.readValue(response.body(), ItemDto.class);
        assertEquals(id, fetched.getId());
        assertEquals("Refactoring", fetched.getTitle());
    }

    @Test
    void shouldUpdateItem() throws Exception {
        UUID id = createAndGetId("Old Title", "Old Author");

        ItemDto updateDto = new ItemDto();
        updateDto.setTitle("New Title");
        updateDto.setAuthor("New Author");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(updateDto)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        ItemDto updated = objectMapper.readValue(response.body(), ItemDto.class);
        assertEquals("New Title", updated.getTitle());
    }

    @Test
    void shouldIncreaseStock() throws Exception {
        UUID id = createAndGetId("Stock Book", "Stock Author");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id + "/increase?quantity=10"))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Verify stock
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .GET()
                .build();
        HttpResponse<String> getRes = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        ItemDto fetched = objectMapper.readValue(getRes.body(), ItemDto.class);
        assertEquals(10, fetched.getTotalStock());
    }

    @Test
    void shouldSearchItems() throws Exception {
        createAndGetId("Searchable Book", "Search Author");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/search?keyword=Searchable"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<?> results = objectMapper.readValue(response.body(), List.class);
        assertFalse(results.isEmpty());
    }

    private UUID createAndGetId(String title, String author) throws Exception {
        ItemDto dto = new ItemDto();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setTotalStock(0);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(dto)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ItemDto created = objectMapper.readValue(response.body(), ItemDto.class);
        return created.getId();
    }
}
