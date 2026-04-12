package com.example.borrowservice.client;

import com.example.borrowservice.config.FeignClientConfiguration;
import com.example.borrowservice.dto.ItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ItemService", configuration = FeignClientConfiguration.class)
public interface ItemClient {

    @GetMapping("/api/v1/item/{id}")
    ResponseEntity<ItemDto> getItemById(@PathVariable UUID id);
}
