package com.example.notificationservice.builder;

import com.example.notificationservice.entity.NotificationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// Produces the correct AbstractMessageBuilder for a given NotificationType.
// Spring injects all builders automatically — adding a new type only requires
// creating a new @Component subclass, no change needed here.
@Component
public class MessageBuilderFactory {

    private final Map<NotificationType, AbstractMessageBuilder> builders;

    public MessageBuilderFactory(List<AbstractMessageBuilder> builderList) {
        this.builders = builderList.stream()
                .collect(Collectors.toMap(
                        AbstractMessageBuilder::getType,
                        Function.identity()
                ));
    }

    public AbstractMessageBuilder forType(NotificationType type) {
        AbstractMessageBuilder builder = builders.get(type);
        if (builder == null) {
            throw new IllegalArgumentException("No builder registered for type: " + type);
        }
        return builder;
    }
}