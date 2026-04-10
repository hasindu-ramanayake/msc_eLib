package com.example.borrowservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "borrows")
public class Borrow {
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

    @Column(nullable = false)
    private Date checkOutDate;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private Boolean isReturned;

    private Date returnedDate;

    @PrePersist
    public void prePersist(){
        if(isReturned == null) isReturned = false;
    }
}
