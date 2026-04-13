package com.example.notificationservice.builder;

// #JF
import com.example.notificationservice.entity.NotificationType;
import com.example.notificationservice.repository.TemplateRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

// Handles USER_REGISTERED events — sends a welcome message to newly registered users.
// Expected payload keys: firstName, email (both set by user-service)
@Component
public class WelcomeMessageBuilder extends AbstractMessageBuilder { // #JF

    public WelcomeMessageBuilder(TemplateRepository templateRepository,
                                 TemplateEngine templateEngine) {
        super(templateRepository, templateEngine);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.WELCOME;
    }

    @Override
    protected void validate(Map<String, String> data) {
        requireKey(data, "firstName");
        requireKey(data, "email");
    }

    private void requireKey(Map<String, String> data, String key) {
        if (!data.containsKey(key) || data.get(key).isBlank()) {
            throw new IllegalArgumentException(
                    "Missing required payload key for WELCOME: " + key);
        }
    }
}
