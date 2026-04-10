package com.elib.user.dto;

public record UserRegistrationDTO(
    String firstName,
    String lastName,
    String email,
    String password,
    AddressDTO address
) {}
