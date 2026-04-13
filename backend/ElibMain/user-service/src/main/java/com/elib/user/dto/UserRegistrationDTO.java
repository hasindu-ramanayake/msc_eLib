package com.elib.user.dto;

import com.elib.user.entity.Role; // Added to support role selection during registration
import com.elib.user.entity.NotificationPreference;
import java.util.Set;

public record UserRegistrationDTO(
    String firstName,
    String lastName,
    String email,
    String password,
    String phoneNumber,
    Role role, // Added role field to allow admin/staff registration via Postman
    Set<NotificationPreference> notificationPreferences, // Added to fix null preferences on registration
    AddressDTO address
) {}
