package com.example.borrowservice.event;

import com.example.borrowservice.dto.NotificationEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.user.exchange}")
    private String exchange;

    @Value("${rabbitmq.user.routing-key}")
    private String routingKey;

    public void publishBorrowCreationEvent(NotificationEventDto event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
