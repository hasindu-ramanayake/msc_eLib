package com.example.borrowservice.client;

import com.example.borrowservice.config.FeignClientConfiguration;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "user-service", configuration = FeignClientConfiguration.class)
public interface UserClient {
    @GetMapping("/api/v1/users/{id}")
    ResponseEntity<?> getUserById(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String auth, @PathVariable UUID id);
}
