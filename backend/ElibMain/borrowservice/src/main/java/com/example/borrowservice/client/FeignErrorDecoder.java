package com.example.borrowservice.client;

import com.example.borrowservice.exception.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        String message = extractResponseMessage(response);

        return switch (status) {
            case NOT_FOUND -> new ResourceNotFoundException(message);
            case INTERNAL_SERVER_ERROR -> new RuntimeException("Internal server error");
            case SERVICE_UNAVAILABLE -> new RuntimeException("Service Unavailable");
            default -> new Exception("Unexpected error: " + message);
        };
    }

    private String extractResponseMessage(Response response) {
        if (response.body() == null) {
            return "No response body";
        }

        try {
            String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            JsonNode json = new ObjectMapper().readTree(body);
            return json.get("message").asString();
        } catch (IOException ex) {
            return "Error reading response body";
        }
    }
}
