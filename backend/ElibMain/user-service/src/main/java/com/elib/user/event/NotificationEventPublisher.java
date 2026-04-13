package com.elib.user.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
     *
     * @param event the event to publish
     */
    public void publishUserRegisteredEvent(UserRegisterEvent event) {
        log.info("Publishing USER_REGISTERED event to exchange='{}' routingKey='{}' userId={}",
                exchange, routingKey, event.getUserId());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
        log.debug("USER_REGISTERED event published successfully for userId={}", event.getUserId());
    }
}
