package com.example.notificationservice;

import com.example.notificationservice.dto.NotificationEventDTO;
import com.example.notificationservice.dto.NotificationResponseDTO;
import com.example.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // GET /api/v1/notifications/users/{userId}
    // Returns all notifications for a user, most recent first.
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getUserNotifications(
            @PathVariable UUID userId) {

        List<NotificationResponseDTO> notifications = notificationService
                .getNotificationsForUser(userId)
                .stream()
                .map(NotificationResponseDTO::from)
                .toList();

        return ResponseEntity.ok(notifications);
    }

    // POST /api/v1/notifications/events
    // Synchronous entry point — useful for testing or internal service calls.
    // Production traffic arrives via EventConsumer (Kafka/RabbitMQ).
    @PostMapping("/events")
    public ResponseEntity<Void> handleEvent(@RequestBody NotificationEventDTO event) {
        notificationService.handleEvent(event);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}