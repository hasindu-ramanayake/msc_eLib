package com.searchService.searchService.repositories;

import com.searchService.searchService.docs.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import com.searchService.searchService.docs.CatalogType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;

public interface CatalogRepository extends ElasticsearchRepository<Book, String> {

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"authors\", \"subtitle\"]}}")
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorsContainingIgnoreCase(String title, String authors, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"authors\", \"subtitle\"]}}, {\"match\": {\"type\": \"?2\"}}]}}")
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorsContainingIgnoreCaseAndType(String title, String authors, CatalogType type, Pageable pageable);

    Page<Book> findByType(CatalogType type, Pageable pageable);
}
