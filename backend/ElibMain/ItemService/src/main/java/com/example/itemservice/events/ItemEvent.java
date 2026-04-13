package com.example.itemservice.events;

import lombok.Data;

import java.io.Serializable;

@Data
public class ItemEvent implements Serializable {

    private String id;

    private String title;
    private String subtitle;
    private String authors;

    private String isbn13;
    private String isbn10;

    private String categories;
    private String thumbnail;
    private String description;

    private String publishedYear;
    private String language;
    private String age;

    private int totalStock;

    private String eventType;
}
