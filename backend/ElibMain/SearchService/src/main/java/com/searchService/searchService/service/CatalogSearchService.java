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

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import java.util.ArrayList;

@Service
public class CatalogSearchService {

    @Autowired
    private CatalogMapper catalogMapper;
    private final CatalogRepository repo;
    
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public CatalogSearchService(CatalogRepository repo) {
        this.repo = repo;
    }

    public List<CatalogDto> search(String keyword, List<String> formats, List<String> genres, List<String> ages, String language) {
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        if (hasKeyword) {
            boolQueryBuilder.must(QueryBuilders.multiMatch(m -> m
                    .query(keyword)
                    .fields(List.of("title", "authors", "subtitle"))
            ));
        }

        if (formats != null && !formats.isEmpty()) {
            List<FieldValue> formatValues = formats.stream()
                    .map(f -> FieldValue.of(f.toUpperCase()))
                    .toList();
            boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                    .field("type.keyword")
                    .terms(ts -> ts.value(formatValues))
            ));
        }

        if (genres != null && !genres.isEmpty()) {
            List<FieldValue> genreValues = genres.stream()
                    .map(FieldValue::of)
                    .toList();
            boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                    .field("categories.keyword")
                    .terms(ts -> ts.value(genreValues))
            ));
        }

        if (ages != null && !ages.isEmpty()) {
            List<FieldValue> ageValues = ages.stream()
                    .map(FieldValue::of)
                    .toList();
            boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                    .field("age.keyword")
                    .terms(ts -> ts.value(ageValues))
            ));
        }

        if (language != null && !language.trim().isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.term(t -> t
                    .field("language.keyword")
                    .value(language)
            ));
        }

        Query finalQuery;
        // If entirely empty parameters, fall back to "match_all"
        if (!hasKeyword && (formats == null || formats.isEmpty()) && (genres == null || genres.isEmpty()) 
            && (ages == null || ages.isEmpty()) && (language == null || language.trim().isEmpty())) {
            finalQuery = QueryBuilders.matchAll(m -> m);
        } else {
            finalQuery = Query.of(q -> q.bool(boolQueryBuilder.build()));
        }

        Pageable pageable = hasKeyword 
            ? PageRequest.of(0, 10) 
            : PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "published_year.keyword"));

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(finalQuery)
                .withPageable(pageable)
                .build();

        SearchHits<Book> searchHits = elasticsearchOperations.search(nativeQuery, Book.class);

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(catalogMapper::toDto)
                .map(CatalogDto.class::cast)
                .toList();
    }

    // public BookDto getById(String title) {
    // Book book = // ... fetch book
    // return catalogMapper.toDto(book);
    // }
}