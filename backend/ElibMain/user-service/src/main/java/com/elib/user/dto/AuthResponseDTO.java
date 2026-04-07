package com.elib.user.dto;

public record AuthResponseDTO(
    String token,
    String refreshToken,
    String email,
    String role,
    String firstName,
    String lastName
) {}
