package com.example.notificationservice.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class UserServiceResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private List<String> notificationPreferences;
}