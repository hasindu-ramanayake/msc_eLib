package com.example.borrowbatchservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
public class NotificationEventDto {
    private UUID eventId;
    private EventType eventType;
    private UUID userId;
    private Map<String, String> payload;
    private Date occuredAt;
}
