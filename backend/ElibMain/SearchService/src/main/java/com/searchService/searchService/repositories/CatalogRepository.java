package com.searchService.searchService.repositories;

import com.searchService.searchService.docs.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import com.searchService.searchService.docs.CatalogType;

public interface CatalogRepository extends ElasticsearchRepository<Book, String> {
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndType(String title, String author, CatalogType type);
}
