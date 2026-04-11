package com.searchService.searchService.service;

import com.searchService.searchService.docs.Book;
import com.searchService.searchService.events.ItemEvent;
import com.searchService.searchService.repositories.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemEventListener {

    private final CatalogRepository catalogRepository;

    @RabbitListener(queues = "itemQueue")
    public void handleItemEvent(ItemEvent event) {
        switch (event.getEventType()) {
            case "CREATE", "UPDATE", "STOCK_INCREASE", "STOCK_DECREASE" -> {
                Book book = new Book();
                book.setId(event.getId());
                book.setTitle(event.getTitle());
                book.setSubtitle(event.getSubtitle());
                book.setAuthors(event.getAuthors());
                book.setIsbn13(event.getIsbn13());
                book.setIsbn10(event.getIsbn10());
                book.setCategories(event.getCategories());
                book.setThumbnail(event.getThumbnail());
                book.setDescription(event.getDescription());
                book.setPublished_year(event.getPublishedYear());
                book.setLanguage(event.getLanguage());
                book.setAge(event.getAge());

                // Save to Elasticsearch (update the search index)
                catalogRepository.save(book);
            }

            case "DELETE" -> {
                // Delete the item from Elasticsearch
                catalogRepository.deleteById(event.getId());
            }
        }
    }
}
