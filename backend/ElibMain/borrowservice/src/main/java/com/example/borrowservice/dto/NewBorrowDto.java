package com.example.borrowservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Data
public class NewBorrowDto {
    private UUID userId;
    private UUID itemId;
    private Date checkOutDate;
    private Date dueDate;
}
