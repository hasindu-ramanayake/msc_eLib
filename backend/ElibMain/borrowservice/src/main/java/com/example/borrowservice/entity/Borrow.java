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
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedDate
    private Date dateCreated;

    @LastModifiedDate
    private Date lastUpdated;

    private UUID userId;
    private UUID itemId;
    private Date checkOutDate;
    private Date dueDate;
    private Boolean isReturned;
    private Date returnedDate;

    @PrePersist
    public void prePersist(){
        if(isReturned == null) isReturned = false;
    }
}
