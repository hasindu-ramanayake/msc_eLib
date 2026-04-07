package com.elib.user.dto;

import com.elib.user.entity.Role;
import com.elib.user.entity.NotificationPreference;
import java.util.UUID;
import java.util.Set;

public record UserDTO(
    UUID id,
    String firstName,
    String lastName,
    String email,
    Role role,
    Set<NotificationPreference> notificationPreferences,
    AddressDTO address
) {}
