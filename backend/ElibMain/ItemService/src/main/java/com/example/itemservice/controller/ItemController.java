package com.example.itemservice.controller;

import com.example.itemservice.dto.ItemDto;
import com.example.itemservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto dto) {
        return itemService.createItem(dto);
    }

    @PutMapping("/{id}")
    public ItemDto updateItem(@PathVariable UUID id, @RequestBody ItemDto dto) {
        return itemService.updateItem(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable UUID id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable UUID id) {
        return itemService.getItemById(id);
    }

    @GetMapping("/title")
    public ItemDto getByTitle(@RequestParam String title) {
        return itemService.getItemByTitle(title);
    }

    @GetMapping("/isbn")
    public ItemDto getByIsbn(@RequestParam String isbn) {
        return itemService.getItemByIsbn(isbn);
    }

    @GetMapping
    public List<ItemDto> getAll() {
        return itemService.getAllItems();
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String keyword) {
        return itemService.search(keyword);
    }

    @PatchMapping("/{id}/increase")
    public void increaseStock(@PathVariable UUID id, @RequestParam int quantity) {
        itemService.increaseStock(id, quantity);
    }

    @PatchMapping("/{id}/decrease")
    public void decreaseStock(@PathVariable UUID id, @RequestParam int quantity) {
        itemService.decreaseStock(id, quantity);
    }
}
