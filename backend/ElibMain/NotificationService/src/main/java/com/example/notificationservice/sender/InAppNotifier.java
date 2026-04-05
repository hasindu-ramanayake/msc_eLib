package com.example.notificationservice.sender;

import com.example.notificationservice.domain.ChannelResult;
import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.NotificationChannel;
import com.example.notificationservice.entity.UserPreferences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// Sends in-app notifications via InAppGateway.
// Stub implementation (replace with real gateway client when available).
@Slf4j
@Component
public class InAppNotifier implements NotificationSender {

    @Override
    public ChannelResult send(UserPreferences user, Message message) {
        try {
            // TODO: inject and call real InAppGateway client
            log.info("IN_APP notification sent to userId={} subject='{}'",
                    user.getUserId(), message.getSubject());
            return ChannelResult.ok(NotificationChannel.IN_APP);
        } catch (Exception e) {
            log.error("Failed to send IN_APP notification to userId={}", user.getUserId(), e);
            return ChannelResult.failure(NotificationChannel.IN_APP, e.getMessage());
        }
    }
}