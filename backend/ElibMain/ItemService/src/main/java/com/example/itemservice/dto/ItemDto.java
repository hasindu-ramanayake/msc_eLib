package com.example.itemservice.dto;

import com.example.itemservice.entity.Genre;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ItemDto {
    private UUID id;
    private String isbn13;
    private String isbn10;
    private String title;
    private String subtitle;
    private String author;
    private String categories;
    private String thumbnail;
    private String description;
    private String publishedYear;
    private String language;
    private String age;

    private int totalStock;
}
