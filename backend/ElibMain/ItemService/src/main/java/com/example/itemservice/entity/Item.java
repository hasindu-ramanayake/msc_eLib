package com.example.itemservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedDate
    private Date dateCreated;

    @LastModifiedDate
    private Date lastUpdated;

    private UUID itemId;
    private String isbn;
    private String title;
    private String author;
    private int totalStock;
    private Date publicationDate;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    public void increaseStock(int quantity) {
        this.totalStock += quantity;
    }

    public void decreaseStock(int quantity) {
        if (this.totalStock < quantity) {
            throw new IllegalArgumentException("Not enough stock");
        }
        this.totalStock -= quantity;
    }

}
