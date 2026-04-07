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

    public ItemDto createItem(ItemDto dto) {
        Item item = itemMapper.toEntity(dto);
        item.setItemId(UUID.randomUUID());
        return itemMapper.toDto(itemRepository.save(item));
    }

    public ItemDto updateItem(UUID itemId, ItemDto dto) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        item.setTitle(dto.getTitle());
        item.setAuthor(dto.getAuthor());
        item.setIsbn(dto.getIsbn());
        item.setGenre(dto.getGenre());
        item.setPublicationDate(dto.getPublicationDate());

        return itemMapper.toDto(itemRepository.save(item));
    }

    public void deleteItem(UUID itemId) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        itemRepository.delete(item);
    }

    public ItemDto getItemById(UUID itemId) {
        return itemRepository.findByItemId(itemId)
                .map(itemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public ItemDto getItemByTitle(String title) {
        return itemRepository.findByTitle(title)
                .map(itemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public ItemDto getItemByIsbn(String isbn) {
        return itemRepository.findByIsbn(isbn)
                .map(itemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }

    public List<ItemDto> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(itemMapper::toDto)
                .toList();
    }

    public void increaseStock(UUID itemId, int quantity) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        item.increaseStock(quantity);
        itemRepository.save(item);
    }

    public void decreaseStock(UUID itemId, int quantity) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        item.decreaseStock(quantity);
        itemRepository.save(item);
    }
}
