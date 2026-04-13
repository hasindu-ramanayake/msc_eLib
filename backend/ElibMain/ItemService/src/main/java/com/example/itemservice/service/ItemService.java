package com.example.itemservice.service;

import com.example.itemservice.dto.ItemDto;
import com.example.itemservice.entity.Item;
import com.example.itemservice.exception.ResourceNotFoundException;
import com.example.itemservice.mapper.ItemMapper;
import com.example.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final RabbitMQService rabbitMQService;

    public ItemDto createItem(ItemDto dto) {
        Item item = itemMapper.toEntity(dto);
        Item saved= itemRepository.save(item);

        rabbitMQService.sendItemEvent(saved,"CREATE");
        return itemMapper.toDto(saved);
    }

    public ItemDto updateItem(UUID id, ItemDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        item.setIsbn13(dto.getIsbn13());
        item.setIsbn10(dto.getIsbn10());

        item.setTitle(dto.getTitle());
        item.setSubtitle(dto.getSubtitle());
        item.setAuthor(dto.getAuthor());

        item.setCategories(dto.getCategories());

        item.setThumbnail(dto.getThumbnail());
        item.setDescription(dto.getDescription());

        item.setPublishedYear(dto.getPublishedYear());
        item.setLanguage(dto.getLanguage());
        item.setAge(dto.getAge());

        item.setTotalStock(dto.getTotalStock());

        Item updated = itemRepository.save(item);

        rabbitMQService.sendItemEvent(updated,"UPDATE");

        return itemMapper.toDto(item);
    }

    public void deleteItem(UUID id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        itemRepository.delete(item);

        rabbitMQService.sendItemEvent(item, "DELETE");
    }

    public ItemDto getItemById(UUID id) {
        return itemRepository.findById(id)
                .map(itemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public ItemDto getItemByTitle(String title) {
        return itemRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .findFirst()
                .map(itemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public ItemDto getItemByIsbn(String isbn) {
        return itemRepository.findByIsbn13(isbn)
                .map(itemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public List<ItemDto> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(itemMapper::toDto)
                .toList();
    }

    public List<ItemDto> search(String keyword) {
        List<Item> results = itemRepository.findByTitleContainingIgnoreCase(keyword);

        if (results.isEmpty()) {
            results = itemRepository.findByAuthorContainingIgnoreCase(keyword);
        }

        return results.stream()
                .map(itemMapper::toDto)
                .toList();
    }

    public void increaseStock(UUID id, int quantity) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        item.increaseStock(quantity);
        Item saved = itemRepository.save(item);

        rabbitMQService.sendItemEvent(saved, "STOCK_INCREASE");
    }

    public void decreaseStock(UUID id, int quantity) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        item.decreaseStock(quantity);
        Item saved = itemRepository.save(item);

        rabbitMQService.sendItemEvent(saved, "STOCK_DECREASE");
    }
}
