package com.elib.user.dto;

import com.elib.user.entity.Role;
import com.elib.user.entity.NotificationPreference;
import java.util.Set;

public record UserUpdateDTO(
    String firstName,
    String lastName,
    String email,
    String password,
    String phoneNumber,
    Role role,
    Set<NotificationPreference> notificationPreferences,
    AddressDTO address
) {}
