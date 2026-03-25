package com.example.notificationservice.builder;

import com.example.notificationservice.domain.Message;
import com.example.notificationservice.entity.MessageTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Replaces {{placeholder}} tokens in a MessageTemplate with actual values
// from the event payload.
//
// Example:
//   template.body = "Your item {{itemTitle}} is due in {{daysLeft}} days."
//   data          = { "itemTitle": "Harry Potter", "daysLeft": "3" }
//   result        = "Your item Harry Potter is due in 3 days."
@Component
public class TemplateEngine {

    // Matches any {{key}} token in a template string
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    public Message renderTemplate(MessageTemplate template, Map<String, String> data) {
        String subject = replacePlaceholders(template.getSubject(), data);
        String body    = replacePlaceholders(template.getBody(),    data);
        return new Message(subject, body, template.getType());
    }

    private String replacePlaceholders(String text, Map<String, String> data) {
        Matcher matcher = PLACEHOLDER.matcher(text);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String key   = matcher.group(1);
            String value = data.getOrDefault(key, "");  // unknown keys become empty string
            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}