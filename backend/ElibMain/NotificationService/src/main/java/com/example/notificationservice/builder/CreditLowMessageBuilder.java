package com.example.notificationservice.builder;

import com.example.notificationservice.entity.NotificationType;
import com.example.notificationservice.repository.TemplateRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

// Handles CREDIT_UPDATED events.
// Expected payload keys: currentScore
@Component
public class CreditLowMessageBuilder extends AbstractMessageBuilder {

    public CreditLowMessageBuilder(TemplateRepository templateRepository,
                                   TemplateEngine templateEngine) {
        super(templateRepository, templateEngine);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.CREDIT_LOW;
    }

    @Override
    protected void validate(Map<String, String> data) {
        requireKey(data, "currentScore");
    }

    private void requireKey(Map<String, String> data, String key) {
        if (!data.containsKey(key) || data.get(key).isBlank()) {
            throw new IllegalArgumentException(
                    "Missing required payload key for CREDIT_LOW: " + key);
        }
    }
}