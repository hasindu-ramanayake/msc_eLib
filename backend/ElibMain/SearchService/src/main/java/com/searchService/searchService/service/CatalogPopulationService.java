package com.searchService.searchService.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.searchService.searchService.docs.Book;
import com.searchService.searchService.docs.CatalogType;
import com.searchService.searchService.repositories.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogPopulationService {

    @Autowired
    private CatalogRepository catalogRepository;

    public void populateFromCsv() {
        catalogRepository.deleteAll();
        
        try (Reader reader = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("books.csv"), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            List<String[]> rows = csvReader.readAll();
            List<Book> books = new ArrayList<>();

            for (String[] row : rows) {
                if (row.length >= 14) {
                    Book book = new Book();
                    book.setIsbn13(row[0]);
                    book.setIsbn10(row[1]);
                    book.setTitle(row[2]);
                    book.setSubtitle(row[3]);
                    book.setAuthors(row[4]);
                    book.setCategories(row[5]);
                    book.setThumbnail(row[6]);
                    book.setDescription(row[7]);
                    book.setPublished_year(row[8]);
                    book.setLanguage(row[12]);
                    book.setAge(row[13]);
                    book.setType(CatalogType.BOOK);
                    books.add(book);
                }
            }
            catalogRepository.saveAll(books);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
