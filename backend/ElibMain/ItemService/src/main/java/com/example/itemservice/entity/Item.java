package com.example.itemservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "isbn13"),
                @UniqueConstraint(columnNames = "isbn10")
        }
)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT", unique = true)
    private String isbn13;
    @Column(columnDefinition = "TEXT", unique = true)
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

    private int totalStock;

    @CreatedDate
    private Date dateCreated;

    @LastModifiedDate
    private Date lastUpdated;


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
