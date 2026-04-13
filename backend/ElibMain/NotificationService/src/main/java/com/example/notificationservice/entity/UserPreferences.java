package com.example.notificationservice.entity;

import com.example.notificationservice.dto.UserServiceResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    private String email;
    private String phoneNumber;

    @Column(nullable = false)
    private boolean inAppEnabled = true;

    @Column(nullable = false)
    private boolean emailEnabled = false;

    @Column(nullable = false)
    private boolean smsEnabled = false;

    public static UserPreferences fromUserServiceResponse(UserServiceResponseDTO dto) {
        UserPreferences prefs = new UserPreferences();
        prefs.setUserId(dto.getId());
        prefs.setEmail(dto.getEmail());
        prefs.setPhoneNumber(null); // not provided by User Service yet
        prefs.setInAppEnabled(dto.getNotificationPreferences().contains("IN_APP"));
        prefs.setEmailEnabled(dto.getNotificationPreferences().contains("EMAIL"));
        prefs.setSmsEnabled(dto.getNotificationPreferences().contains("SMS"));
        return prefs;
    }
}