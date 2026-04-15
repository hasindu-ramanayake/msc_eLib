package com.example.itemservice.loader;

import com.example.itemservice.entity.Item;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.service.RabbitMQService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemCsvLoader {

    private final ItemRepository itemRepository;
    private final RabbitMQService rabbitMQService;

    @PostConstruct
    public void loadCsv() {

        // Comment out for testing
        if (itemRepository.count() > 0) {
            return;
        }

        try (Reader reader = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("books.csv"),
                StandardCharsets.UTF_8);
                CSVReader csvReader = new CSVReaderBuilder(reader)
                        .withSkipLines(1)
                        .build()) {

            List<String[]> rows = csvReader.readAll();
            List<Item> items = new ArrayList<>();

            for (String[] row : rows) {
                // log.info("Processing row: {}", row);
                Item item = new Item();

                item.setIsbn13(row[0]);
                item.setIsbn10(row[1]);
                item.setTitle(row[2]);
                item.setSubtitle(row[3]);
                item.setAuthor(row[4]);
                item.setCategories(row[5]);
                item.setThumbnail(row[6]);
                item.setDescription(row[7]);
                item.setPublishedYear(row[8]);
                // item.setAverageRating(Double.parseDouble(row[9]));
                // item.setNumPages(Integer.parseInt(row[10]));
                // item.setRatingsCount(Integer.parseInt(row[11]));
                item.setLanguage(row[12]);
                item.setAge(row[13]);

                item.setTotalStock(10);

                items.add(item);
            }

            List<Item> savedItems = itemRepository.saveAll(items);

            for (Item item : savedItems) {
                rabbitMQService.sendItemEvent(item, "CREATE");
            }

            System.out.println("CSV loaded + events sent");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load CSV file into database", e);
        }
    }
}
