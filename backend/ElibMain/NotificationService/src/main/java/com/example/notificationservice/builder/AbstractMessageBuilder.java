package com.example.notificationservice.builder;

import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.MessageTemplate;
import com.example.notificationservice.entity.NotificationType;
import com.example.notificationservice.exception.ResourceNotFoundException;
import com.example.notificationservice.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;

import java.util.Map;

// Defines the skeleton of the message-building algorithm (Template Method pattern).
// Each concrete subclass targets one NotificationType and inherits the full
// fetch → validate → render pipeline for free.
@RequiredArgsConstructor
public abstract class AbstractMessageBuilder {

    private final TemplateRepository templateRepository;
    private final TemplateEngine templateEngine;

    // Returns the NotificationType this builder handles.
    public abstract NotificationType getType();

    // Hook called before rendering — subclasses can assert required payload keys.
    protected abstract void validate(Map<String, String> data);

    // Full pipeline: load template → validate payload → render → return Message.
    public Message build(Map<String, String> data) {
        validate(data);

        MessageTemplate template = templateRepository.findByType(getType())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No template found for type: " + getType()));

        return templateEngine.renderTemplate(template, data);
    }
}