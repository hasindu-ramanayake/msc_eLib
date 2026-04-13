package com.example.borrowservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "waitlists")
public class Waitlist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedDate
    @Column(updatable = false)
    private Date dateCreated;

    @LastModifiedDate
    private Date lastUpdated;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID itemId;
}
