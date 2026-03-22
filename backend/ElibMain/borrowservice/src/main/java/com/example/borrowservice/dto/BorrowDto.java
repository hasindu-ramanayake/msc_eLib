package com.example.borrowservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Data
public class BorrowDto {
    private UUID id;
    private UUID userId;
    private UUID itemId;
    private Date checkOutDate;
    private Date dueDate;
    private Boolean isReturned;
    private Date returnDate;
}