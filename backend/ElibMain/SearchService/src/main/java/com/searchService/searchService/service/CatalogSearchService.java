package com.searchService.searchService.service;

import com.searchService.searchService.docs.Book;
import com.searchService.searchService.docs.CatalogType;
import com.searchService.searchService.dto.CatalogDto;
import com.searchService.searchService.dto.CatalogMapper;
import com.searchService.searchService.repositories.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Page<Book> results;
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        
        if (!hasKeyword) {
            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "published_year.keyword"));
            if (filter != null && !filter.isEmpty()) {
                CatalogType type = CatalogType.valueOf(filter.toUpperCase());
                results = repo.findByType(type, pageable);
            } else {
                results = (Page<Book>) repo.findAll(pageable);
            }
        } else {
            Pageable pageable = PageRequest.of(0, 10);
            if (filter != null && !filter.isEmpty()) {
                CatalogType type = CatalogType.valueOf(filter.toUpperCase());
                results = repo.findByTitleContainingIgnoreCaseOrAuthorsContainingIgnoreCaseAndType(keyword, keyword, type, pageable);
            } else {
                results = repo.findByTitleContainingIgnoreCaseOrAuthorsContainingIgnoreCase(keyword, keyword, pageable);
            }
        }
        
        return results.getContent().stream()
                .map(catalogMapper::toDto)
                .map(CatalogDto.class::cast)
                .toList();
    }

    // public BookDto getById(String title) {
    // Book book = // ... fetch book
    // return catalogMapper.toDto(book);
    // }
}