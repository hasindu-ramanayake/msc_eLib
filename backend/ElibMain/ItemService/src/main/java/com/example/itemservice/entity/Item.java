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

    @Column(columnDefinition = "TEXT")
    private String isbn13;
    @Column(columnDefinition = "TEXT")
    private String isbn10;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "TEXT")
    private String subtitle;
    @Column(columnDefinition = "TEXT")
    private String author;
    @Column(columnDefinition = "TEXT")
    private String categories;
    @Column(columnDefinition = "TEXT")
    private String thumbnail;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String publishedYear;
    @Column(columnDefinition = "TEXT")
    private String language;
    @Column(columnDefinition = "TEXT")
    private String age;
//    private double averageRating;
//    private int numPages;
//    private int ratingsCount;

    private int totalStock;

    @CreatedDate
    private Date dateCreated;

    @LastModifiedDate
    private Date lastUpdated;

//    @Enumerated(EnumType.STRING)
//    private Genre genre;

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
