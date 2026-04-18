package com.elib.user;

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
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "noauth"})
class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpClient client;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        client = HttpClient.newHttpClient();
        baseUrl = "http://localhost:" + port + "/api/v1/users";
    }

    @Test
    void shouldRegisterUser() throws Exception {
        String email = "reg-" + System.currentTimeMillis() + "@example.com";
        Map<String, Object> user = Map.of(
            "firstName", "John",
            "lastName", "Doe",
            "email", email,
            "password", "password123",
            "phoneNumber", "1234567890",
            "role", "CUSTOMER",
            "notificationPreferences", Set.of("EMAIL")
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Map responseBody = objectMapper.readValue(response.body(), Map.class);
        assertEquals(email, responseBody.get("email"));
        assertEquals("John", responseBody.get("firstName"));
    }

    @Test
    void shouldLoginUser() throws Exception {
        String email = "login-" + System.currentTimeMillis() + "@example.com";
        registerUser(email, "password123");

        Map<String, String> creds = Map.of(
            "email", email,
            "password", "password123"
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(creds)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Map responseBody = objectMapper.readValue(response.body(), Map.class);
        assertNotNull(responseBody.get("token"));
    }

    @Test
    void shouldFetchCurrentUserProfile() throws Exception {
        String email = "me-" + System.currentTimeMillis() + "@example.com";
        registerUser(email, "password123");
        String token = loginAndGetToken(email, "password123");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/me"))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Map responseBody = objectMapper.readValue(response.body(), Map.class);
        assertEquals(email, responseBody.get("email"));
    }

    @Test
    void shouldFetchUserById() throws Exception {
        String email = "id-" + System.currentTimeMillis() + "@example.com";
        Map<String, Object> user = Map.of(
            "firstName", "Jane",
            "lastName", "Smith",
            "email", email,
            "password", "password123",
            "phoneNumber", "0987654321",
            "role", "CUSTOMER"
        );

        HttpRequest regReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        HttpResponse<String> regRes = client.send(regReq, HttpResponse.BodyHandlers.ofString());
        String id = objectMapper.readValue(regRes.body(), Map.class).get("id").toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Map responseBody = objectMapper.readValue(response.body(), Map.class);
        assertEquals(email, responseBody.get("email"));
    }

    @Test
    void shouldUpdateUserProfile() throws Exception {
        String email = "update-" + System.currentTimeMillis() + "@example.com";
        registerUser(email, "password123");
        String token = loginAndGetToken(email, "password123");

        Map<String, Object> updateRequest = Map.of(
            "firstName", "Johnny",
            "lastName", "User",
            "email", email,
            "role", "CUSTOMER",
            "phoneNumber", "5555555555",
            "notificationPreferences", Set.of("EMAIL", "SMS")
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/edit-profile"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(updateRequest)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Map responseBody = objectMapper.readValue(response.body(), Map.class);
        assertEquals("Johnny", responseBody.get("firstName"));
        assertEquals("5555555555", responseBody.get("phoneNumber"));
        
        String prefsStr = responseBody.get("notificationPreferences").toString();
        assertTrue(prefsStr.contains("EMAIL"));
        assertTrue(prefsStr.contains("SMS"));
    }

    private void registerUser(String email, String password) throws Exception {
        Map<String, Object> user = Map.of(
            "firstName", "Test",
            "lastName", "User",
            "email", email,
            "password", password,
            "role", "CUSTOMER"
        );
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(user)))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        Map<String, String> creds = Map.of("email", email, "password", password);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(creds)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), Map.class).get("token").toString();
    }
}
