package com.example.notificationservice.domain;

import com.example.notificationservice.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

// Represents a fully rendered notification message, ready to be sent.
// Built by AbstractMessageBuilder, consumed by NotificationSender implementations.
@Getter
@AllArgsConstructor
public class Message {
    private final String subject;
    private final String content;
    private final NotificationType type;
}