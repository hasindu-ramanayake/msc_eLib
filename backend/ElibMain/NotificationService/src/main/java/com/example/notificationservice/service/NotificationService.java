package com.example.notificationservice.service;

import com.example.notificationservice.builder.AbstractMessageBuilder;
import com.example.notificationservice.builder.MessageBuilderFactory;
import com.example.notificationservice.dispatcher.ChannelStrategyFactory;
import com.example.notificationservice.dispatcher.NotificationDispatcher;
import com.example.notificationservice.domain.ChannelResult;
import com.example.notificationservice.domain.Message;
import com.example.notificationservice.dto.NotificationEventDTO;
import com.example.notificationservice.entity.*;
import com.example.notificationservice.exception.ResourceNotFoundException;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.repository.PreferencesRepository;
import com.example.notificationservice.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository    notificationRepository;
    private final PreferencesRepository     preferencesRepository;
    private final MessageBuilderFactory     messageBuilderFactory;
    private final ChannelStrategyFactory    channelStrategyFactory;
    private final NotificationDispatcher    notificationDispatcher;

    // Maps an incoming EventType to the corresponding NotificationType.
    private static final Map<com.example.notificationservice.dto.EventType, NotificationType> EVENT_TO_TYPE =
            Map.of(
                    com.example.notificationservice.dto.EventType.ITEM_DUE_SOON,        NotificationType.REMINDER,
                    com.example.notificationservice.dto.EventType.ITEM_OVERDUE,         NotificationType.OVERDUE,
                    com.example.notificationservice.dto.EventType.WAITLIST_AVAILABLE,   NotificationType.WAITLIST_AVAILABLE,
                    com.example.notificationservice.dto.EventType.CREDIT_UPDATED,       NotificationType.CREDIT_LOW
            );

    // Central orchestration method
    // 1. Resolve NotificationType from EventType
    // 2. Load user preferences
    // 3. Build the rendered Message via the builder pipeline
    // 4. Determine active channel strategies
    // 5. Dispatch to all active channels
    // 6. Persist one Notification record per successful channel
    @Transactional
    public void handleEvent(NotificationEventDTO event) {
        log.info("Handling event type={} userId={}", event.getEventType(), event.getUserId());

        NotificationType notificationType = resolveType(event);

        UserPreferences prefs = preferencesRepository.findByUserId(event.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No preferences found for userId=" + event.getUserId()));

        AbstractMessageBuilder builder = messageBuilderFactory.forType(notificationType);
        Message message = builder.build(event.getPayload());

        List<NotificationSender> strategies = channelStrategyFactory.strategiesFor(prefs);
        List<ChannelResult> results = notificationDispatcher.dispatch(strategies, prefs, message);

        results.forEach(result -> persistNotification(event.getUserId(), message, result));
    }

    @Transactional(readOnly = true)
    public List<com.example.notificationservice.entity.Notification> getNotificationsForUser(UUID userId) {
        return notificationRepository.findByUserIdAndChannelOrderByCreatedAtDesc(
                userId, NotificationChannel.IN_APP);
    }

    // -------------------------------------------------------------------------

    private NotificationType resolveType(NotificationEventDTO event) {
        NotificationType type = EVENT_TO_TYPE.get(event.getEventType());
        if (type == null) {
            throw new IllegalArgumentException("Unhandled event type: " + event.getEventType());
        }
        return type;
    }

    private void persistNotification(UUID userId, Message message, ChannelResult result) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(message.getType());
        notification.setTitle(message.getSubject());
        notification.setBody(message.getContent());
        notification.setChannel(result.getChannel());
        notification.setStatus(result.isSuccess() ? NotificationStatus.SENT : NotificationStatus.FAILED);
        notification.setDeliveredAt(result.isSuccess() ? new Date() : null);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        notificationRepository.markAllAsReadForUser(userId);
    }
}