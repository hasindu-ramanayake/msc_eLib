package com.example.itemservice.repository;

import com.example.itemservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID>{
    Optional<Item> findByItemId(UUID itemId);
    Optional<Item> findByTitle(String title);
    Optional<Item> findByIsbn(String isbn);

}
