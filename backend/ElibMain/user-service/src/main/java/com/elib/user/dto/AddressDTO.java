package com.elib.user.dto;

public record AddressDTO(
    String addressLine1,
    String addressLine2,
    String eircode,
    String county
) {}
