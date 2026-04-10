package com.example.notificationservice.sender;

import com.example.notificationservice.domain.ChannelResult;
import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.NotificationChannel;
import com.example.notificationservice.entity.UserPreferences;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class EmailSender implements NotificationSender {

    private final SendGrid sendGrid;
    private final String fromEmail;
    private final String fromName;

    public EmailSender(@Value("${sendgrid.api-key}")   String apiKey,
                       @Value("${sendgrid.from-email}") String fromEmail,
                       @Value("${sendgrid.from-name}")  String fromName) {
        this.sendGrid  = new SendGrid(apiKey);
        this.fromEmail = fromEmail;
        this.fromName  = fromName;
    }

    @Override
    public ChannelResult send(UserPreferences user, Message message) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return ChannelResult.failure(NotificationChannel.EMAIL,
                    "No email address for userId=" + user.getUserId());
        }

        try {
            Mail mail = new Mail(
                    new Email(fromEmail, fromName),
                    message.getSubject(),
                    new Email(user.getEmail()),
                    new Content("text/plain", message.getContent())
            );

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.info("EMAIL sent to {} subject='{}'",
                        user.getEmail(), message.getSubject());
                return ChannelResult.ok(NotificationChannel.EMAIL);
            } else {
                log.error("SendGrid error status={} body={}",
                        response.getStatusCode(), response.getBody());
                return ChannelResult.failure(NotificationChannel.EMAIL,
                        "SendGrid returned status " + response.getStatusCode());
            }

        } catch (IOException e) {
            log.error("Failed to send EMAIL to userId={}", user.getUserId(), e);
            return ChannelResult.failure(NotificationChannel.EMAIL, e.getMessage());
        }
    }
}