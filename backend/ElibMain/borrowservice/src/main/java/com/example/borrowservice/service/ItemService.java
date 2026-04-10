package com.example.borrowservice.service;

import com.example.borrowservice.client.ItemClient;
import com.example.borrowservice.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemClient itemClient;

    @CircuitBreaker(name = "ItemService", fallbackMethod = "fallback")
    public ResponseEntity<?> getItemById(UUID id){
        return itemClient.getItemById(id);
    }

    public ResponseEntity<?> fallback(UUID id, Throwable throwable) throws Throwable {
        throw throwable;
    }
}
