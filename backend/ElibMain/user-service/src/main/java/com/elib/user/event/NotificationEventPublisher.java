package com.elib.user.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Publishes domain events from the user-service to RabbitMQ.
 * The NotificationService consumes these events via its {@code EventConsumer}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.user.exchange}")
    private String exchange;

    @Value("${rabbitmq.user.routing-key}")
    private String routingKey;

    /**
     * Publishes a {@link UserRegisterEvent} to the notification exchange.
     * Maps the internal event to a format compatible with NotificationService.
     *
     * @param event the event to publish
     */
    public void publishUserRegisteredEvent(UserRegisterEvent event) {
        log.info("Publishing USER_REGISTERED event to exchange='{}' routingKey='{}' userId={}",
                exchange, routingKey, event.getUserId());

        // Create a map to avoid package-specific class name issues (__TypeId__ header)
        // and ensure compatibility with the NotificationService DTO.
        Map<String, Object> message = new HashMap<>();
        message.put("eventId", event.getEventId() != null ? event.getEventId() : UUID.randomUUID());
        message.put("eventType", "USER_REGISTERED");
        message.put("userId", event.getUserId());
        message.put("occuredAt", event.getOccuredAt());
        message.put("payload", event.getPayload());
        message.put("notificationPreferences", event.getNotificationPreferences());

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        log.debug("USER_REGISTERED event published successfully for userId={}", event.getUserId());
    }
}
