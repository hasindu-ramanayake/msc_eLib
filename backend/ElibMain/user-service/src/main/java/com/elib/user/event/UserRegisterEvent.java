package com.elib.user.event;

import com.elib.user.dto.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Event published to RabbitMQ when a new user successfully registers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterEvent {

    /** Unique identifier for this event (used for de-duplication). */
    private UUID eventId;

    /** The type of event (e.g. "USER_REGISTERED"). */
    private EventType eventType;

    /** ID of the newly registered user. */
    private UUID userId;

//    /** Arbitrary key/value payload (e.g. email, firstName). */
//    private Map<String, String> payload;
//
//    /** Timestamp of when the event occurred. */
//    private Date occuredAt;
}