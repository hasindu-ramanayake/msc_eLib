package com.searchService.searchService.service;

import com.searchService.searchService.docs.Book;
import com.searchService.searchService.docs.CatalogType;
import com.searchService.searchService.dto.CatalogDto;
import com.searchService.searchService.dto.CatalogMapper;
import com.searchService.searchService.repositories.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogSearchService {

    @Autowired
    private CatalogMapper catalogMapper;
    private final CatalogRepository repo;

    public CatalogSearchService(CatalogRepository repo) {
        this.repo = repo;
    }

    public List<CatalogDto> search(String keyword, String filter) {
        List<Book> results;
        if (filter != null && !filter.isEmpty()) {
            CatalogType type = CatalogType.valueOf(filter.toUpperCase());
            results = repo.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseAndType(keyword, keyword, type);
        } else {
            results = repo.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
        }
        return results.stream()
                .map(catalogMapper::toDto)
                .map(CatalogDto.class::cast)
                .toList();
    }

    // public BookDto getById(String title) {
    // Book book = // ... fetch book
    // return catalogMapper.toDto(book);
    // }
}