package com.example.borrowservice.service;

import com.example.borrowservice.client.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;

    @CircuitBreaker(name = "UserService", fallbackMethod = "fallback")
    public ResponseEntity<?> getUserById(UUID id){
        return userClient.getUserById(id);
    }
}
