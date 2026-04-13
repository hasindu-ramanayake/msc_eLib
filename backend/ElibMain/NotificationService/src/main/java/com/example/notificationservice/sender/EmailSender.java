package com.example.notificationservice.sender;

import com.example.notificationservice.domain.ChannelResult;
import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.NotificationChannel;
import com.example.notificationservice.entity.UserPreferences;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender implements NotificationSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${mail.from-name}")
    private String fromName;

    @Override
    public ChannelResult send(UserPreferences user, Message message) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return ChannelResult.failure(NotificationChannel.EMAIL,
                    "No email address for userId=" + user.getUserId());
        }

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(fromName + " <" + fromEmail + ">");
            mail.setTo(user.getEmail());
            mail.setSubject(message.getSubject());
            mail.setText(message.getContent());

            mailSender.send(mail);

            log.info("EMAIL sent to {} subject='{}'",
                    user.getEmail(), message.getSubject());
            return ChannelResult.ok(NotificationChannel.EMAIL);

        } catch (Exception e) {
            log.error("Failed to send EMAIL to userId={}", user.getUserId(), e);
            return ChannelResult.failure(NotificationChannel.EMAIL, e.getMessage());
        }
    }
}