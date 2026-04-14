package com.example.borrowbatchservice.service;

import com.example.borrowbatchservice.dto.EventType;
import com.example.borrowbatchservice.dto.NotificationEventDto;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class NotificationService {

    public NotificationEventDto generateOverDueEvent(UUID userId, String itemTitle){
        var event = new NotificationEventDto();
        event.setEventType(EventType.ITEM_OVERDUE);
        event.setUserId(userId);
        event.setPayload(Map.of(
                "itemTitle", itemTitle
        ));
        event.setOccuredAt(Date.from(Instant.now()));
        return event;
    }
}
