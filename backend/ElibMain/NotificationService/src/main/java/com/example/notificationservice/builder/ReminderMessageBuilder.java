package com.example.notificationservice.builder;

import com.example.notificationservice.entity.NotificationType;
import com.example.notificationservice.repository.TemplateRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

// Handles ITEM_DUE_SOON events.
// Expected payload keys: itemTitle, daysLeft
@Component
public class ReminderMessageBuilder extends AbstractMessageBuilder {

    public ReminderMessageBuilder(TemplateRepository templateRepository,
                                  TemplateEngine templateEngine) {
        super(templateRepository, templateEngine);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.REMINDER;
    }

    @Override
    protected void validate(Map<String, String> data) {
        requireKey(data, "itemTitle");
        requireKey(data, "daysLeft");
    }

    private void requireKey(Map<String, String> data, String key) {
        if (!data.containsKey(key) || data.get(key).isBlank()) {
            throw new IllegalArgumentException(
                    "Missing required payload key for REMINDER: " + key);
        }
    }
}