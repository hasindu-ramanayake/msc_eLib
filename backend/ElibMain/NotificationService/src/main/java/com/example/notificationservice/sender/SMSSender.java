package com.example.notificationservice.sender;

import com.example.notificationservice.domain.ChannelResult;
import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.NotificationChannel;
import com.example.notificationservice.entity.UserPreferences;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// Sends notifications via SMS using SMSClient.
// Stub implementation (replace with real SMSClient (e.g. Twilio)).
@Slf4j
@Component
public class SMSSender implements NotificationSender {

    @Override
    public ChannelResult send(UserPreferences user, Message message) {
        try {
            if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
                return ChannelResult.failure(NotificationChannel.SMS,
                        "No phone number on file for userId=" + user.getUserId());
            }
            // TODO: inject and call real SMSClient
            log.info("SMS notification sent to {} subject='{}'",
                    user.getPhoneNumber(), message.getSubject());
            return ChannelResult.ok(NotificationChannel.SMS);
        } catch (Exception e) {
            log.error("Failed to send SMS notification to userId={}", user.getUserId(), e);
            return ChannelResult.failure(NotificationChannel.SMS, e.getMessage());
        }
    }
}