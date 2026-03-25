package com.example.notificationservice.builder;

import com.example.notificationservice.entity.NotificationType;
import com.example.notificationservice.repository.TemplateRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

// Handles WAITLIST_AVAILABLE events.
// Expected payload keys: itemTitle
@Component
public class WaitlistAvailableMessageBuilder extends AbstractMessageBuilder {

    public WaitlistAvailableMessageBuilder(TemplateRepository templateRepository,
                                           TemplateEngine templateEngine) {
        super(templateRepository, templateEngine);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.WAITLIST_AVAILABLE;
    }

    @Override
    protected void validate(Map<String, String> data) {
        requireKey(data, "itemTitle");
    }

    private void requireKey(Map<String, String> data, String key) {
        if (!data.containsKey(key) || data.get(key).isBlank()) {
            throw new IllegalArgumentException(
                    "Missing required payload key for WAITLIST_AVAILABLE: " + key);
        }
    }
}