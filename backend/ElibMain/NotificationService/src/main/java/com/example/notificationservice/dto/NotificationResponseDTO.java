package com.example.notificationservice.dto;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.NotificationChannel;
import com.example.notificationservice.entity.NotificationStatus;
import com.example.notificationservice.entity.NotificationType;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

// Exposed via the REST API — never return the Notification entity directly.
// Decouples the API contract from the database schema.
@Getter
public class NotificationResponseDTO {

    private final UUID id;
    private final UUID userId;
    private final NotificationType type;
    private final String title;
    private final String body;
    private final NotificationChannel channel;
    private final NotificationStatus status;
    private final Date deliveredAt;
    private final Date createdAt;

    private NotificationResponseDTO(Notification n) {
        this.id          = n.getId();
        this.userId      = n.getUserId();
        this.type        = n.getType();
        this.title       = n.getTitle();
        this.body        = n.getBody();
        this.channel     = n.getChannel();
        this.status      = n.getStatus();
        this.deliveredAt = n.getDeliveredAt();
        this.createdAt   = n.getCreatedAt();
    }

    public static NotificationResponseDTO from(Notification notification) {
        return new NotificationResponseDTO(notification);
    }
}