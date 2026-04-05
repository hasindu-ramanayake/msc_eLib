package com.example.notificationservice.builder;

import com.example.notificationservice.entity.NotificationType;
import com.example.notificationservice.repository.TemplateRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

// Handles ITEM_OVERDUE events.
// Expected payload keys: itemTitle, daysOverdue
@Component
public class OverdueMessageBuilder extends AbstractMessageBuilder {

    public OverdueMessageBuilder(TemplateRepository templateRepository,
                                 TemplateEngine templateEngine) {
        super(templateRepository, templateEngine);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.OVERDUE;
    }

    @Override
    protected void validate(Map<String, String> data) {
        requireKey(data, "itemTitle");
        requireKey(data, "daysOverdue");
    }

    private void requireKey(Map<String, String> data, String key) {
        if (!data.containsKey(key) || data.get(key).isBlank()) {
            throw new IllegalArgumentException(
                    "Missing required payload key for OVERDUE: " + key);
        }
    }
}