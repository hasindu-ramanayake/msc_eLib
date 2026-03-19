package com.searchService.searchService.repositories;

import com.searchService.searchService.docs.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CatalogRepository extends ElasticsearchRepository<Book, String> {
}
