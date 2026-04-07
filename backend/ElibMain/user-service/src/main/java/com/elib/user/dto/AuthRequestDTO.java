package com.elib.user.dto;

public record AuthRequestDTO(
    String email,
    String password
) {}
