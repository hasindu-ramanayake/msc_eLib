package com.example.itemservice.repository;

import com.example.itemservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID>{
    List<Item> findByTitleContainingIgnoreCase(String title);
    List<Item> findByAuthorContainingIgnoreCase(String author);
    Optional<Item> findByIsbn13(String isbn13);

}
