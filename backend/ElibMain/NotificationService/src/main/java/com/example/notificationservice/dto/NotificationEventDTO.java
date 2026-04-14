package com.example.notificationservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
public class NotificationEventDTO {
    private UUID eventId;
    private EventType eventType;
    private UUID userId;
    private Map<String, String> payload;
    private Date occuredAt;
    private String jwtToken;
}