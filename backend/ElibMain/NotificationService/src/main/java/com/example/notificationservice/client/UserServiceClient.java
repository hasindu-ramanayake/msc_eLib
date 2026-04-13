package com.example.notificationservice.client;

import com.example.notificationservice.dto.UserServiceResponseDTO;
import com.example.notificationservice.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.UUID;

// Calls the User Service to fetch user data.
// Uses the JWT token passed from the original request
// so the User Service can authenticate the call.
@Slf4j
@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.user-service.url}")
    private String userServiceUrl;

    public UserServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public UserServiceResponseDTO getUserById(UUID userId, String jwtToken) {
        String url = userServiceUrl + "/api/v1/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserServiceResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserServiceResponseDTO.class
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("User not found: " + userId);
        } catch (Exception e) {
            log.error("Failed to call User Service for userId={}: {}", userId, e.getMessage());
            throw new ResourceNotFoundException("Could not fetch user data for userId=" + userId);
        }
    }
}