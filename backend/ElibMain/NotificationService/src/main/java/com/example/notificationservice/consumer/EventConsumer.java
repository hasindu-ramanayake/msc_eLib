package com.example.notificationservice.consumer;

import com.example.notificationservice.dto.NotificationEventDTO;
import com.example.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

// Entry point for asynchronous events coming from other microservices.
// KafkaListener ?
// The REST endpoint in NotificationController serves as a synchronous
// alternative (useful for testing and internal calls).
@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void onEvent(NotificationEventDTO event) {
        log.info("Event received via RabbitMQ: type={} userId={} eventId={}",
                event.getEventType(), event.getUserId(), event.getEventId());
        notificationService.handleEvent(event);
    }

    // #JF — listens to user-service registration events on the dedicated user.events queue
    @RabbitListener(queues = "${rabbitmq.user.queue}") // #JF
    public void onUserEvent(NotificationEventDTO event) {
        log.info("User event received via RabbitMQ: type={} userId={} eventId={}",
                event.getEventType(), event.getUserId(), event.getEventId());
        notificationService.handleEvent(event);
    }