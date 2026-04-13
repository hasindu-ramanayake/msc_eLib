package com.example.borrowservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AvailableResponseDto {
    UUID itemId;
    boolean isAvailable;
    long amount;
}
