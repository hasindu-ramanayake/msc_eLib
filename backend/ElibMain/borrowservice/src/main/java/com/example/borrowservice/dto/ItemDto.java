package com.example.borrowservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class ItemDto {
    private UUID itemId;
    private String isbn;
    private String title;
    private String author;
    private int totalStock;
    private String genre;
    private Date publicationDate;
}