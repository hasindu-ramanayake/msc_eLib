package com.example.borrowservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class NotificationEventDto {
    private UUID eventId;
    private EventType eventType;
    private UUID userId;
    private Map<String, String> payload;
    private Date occuredAt;
    private String jwtToken;
    private List<String> notificationPreferences;
}
