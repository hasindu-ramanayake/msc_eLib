package com.example.notificationservice.domain;

import com.example.notificationservice.entity.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;

// Returned by each NotificationSender after a send attempt.
// Allows NotificationDispatcher to record per-channel success/failure
// without stopping the whole dispatch on a single channel failure.
@Getter
@AllArgsConstructor
public class ChannelResult {
    private final NotificationChannel channel;
    private final boolean success;
    private final String errorMessage;   // null when success = true

    public static ChannelResult ok(NotificationChannel channel) {
        return new ChannelResult(channel, true, null);
    }

    public static ChannelResult failure(NotificationChannel channel, String reason) {
        return new ChannelResult(channel, false, reason);
    }
}