package com.example.borrowservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Data
public class NewBorrowDto {
    @NotNull
    @Valid
    private UUID userId;

    @NotNull
    private UUID itemId;

    @NotNull
    @Future
    private Date dueDate;
}
