package com.example.notificationservice.sender;

import com.example.notificationservice.domain.ChannelResult;
import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.NotificationChannel;
import com.example.notificationservice.entity.UserPreferences;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SMSSender implements NotificationSender {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public ChannelResult send(UserPreferences user, Message message) {
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
            return ChannelResult.failure(NotificationChannel.SMS,
                    "No phone number for userId=" + user.getUserId());
        }

        try {
            com.twilio.rest.api.v2010.account.Message sms =
                    com.twilio.rest.api.v2010.account.Message.creator(
                            new PhoneNumber(user.getPhoneNumber()),
                            new PhoneNumber(fromNumber),
                            message.getSubject() + "\n\n" + message.getContent()
                    ).create();

            log.info("SMS sent to {} sid={}", user.getPhoneNumber(), sms.getSid());
            return ChannelResult.ok(NotificationChannel.SMS);

        } catch (ApiException e) {
            log.error("Twilio error for userId={}: {}", user.getUserId(), e.getMessage());
            return ChannelResult.failure(NotificationChannel.SMS, e.getMessage());
        }
    }
}