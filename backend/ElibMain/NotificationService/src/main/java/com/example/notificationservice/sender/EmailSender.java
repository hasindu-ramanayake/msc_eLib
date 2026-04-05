package com.example.notificationservice.sender;

import com.example.notificationservice.domain.ChannelResult;
import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.NotificationChannel;
import com.example.notificationservice.entity.UserPreferences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// Sends notifications via email using EmailClient.
// Stub implementation (replace with real EmailClient (e.g. SendGrid, JavaMailSender)).
@Slf4j
@Component
public class EmailSender implements NotificationSender {

    @Override
    public ChannelResult send(UserPreferences user, Message message) {
        try {
            if (user.getEmail() == null || user.getEmail().isBlank()) {
                return ChannelResult.failure(NotificationChannel.EMAIL,
                        "No email address on file for userId=" + user.getUserId());
            }
            // TODO: inject and call real EmailClient
            log.info("EMAIL notification sent to {} subject='{}'",
                    user.getEmail(), message.getSubject());
            return ChannelResult.ok(NotificationChannel.EMAIL);
        } catch (Exception e) {
            log.error("Failed to send EMAIL notification to userId={}", user.getUserId(), e);
            return ChannelResult.failure(NotificationChannel.EMAIL, e.getMessage());
        }
    }
}