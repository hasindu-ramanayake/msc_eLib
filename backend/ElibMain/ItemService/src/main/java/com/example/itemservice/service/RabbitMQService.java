package com.example.itemservice.service;

import com.example.itemservice.entity.Item;
import com.example.itemservice.events.ItemEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQService {

    private final AmqpTemplate amqpTemplate;

    public void sendItemEvent(Item item, String eventType) {

        ItemEvent event = new ItemEvent();

        event.setId(item.getId().toString());
        event.setTitle(item.getTitle());
        event.setSubtitle(item.getSubtitle());
        event.setAuthors(item.getAuthor());
        event.setIsbn13(item.getIsbn13());
        event.setIsbn10(item.getIsbn10());
        event.setCategories(item.getCategories());
        event.setThumbnail(item.getThumbnail());
        event.setDescription(item.getDescription());
        event.setPublishedYear(item.getPublishedYear());
        event.setLanguage(item.getLanguage());
        event.setAge(item.getAge());
        event.setEventType(eventType);

        amqpTemplate.convertAndSend("itemQueue", event);
    }
}
