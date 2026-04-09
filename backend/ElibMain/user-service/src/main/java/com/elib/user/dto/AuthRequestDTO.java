package com.elib.user.dto;
//Frontend to Backend
public record AuthRequestDTO(
    String email,
    String password
) {}
