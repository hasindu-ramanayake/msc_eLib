package com.example.borrowservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class NewWaitListDto {
    @NotNull
    @Valid
    private UUID userId;

    @NotNull
    @Valid
    private UUID itemId;
}
