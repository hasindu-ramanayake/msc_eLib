package com.searchService.searchService.controllers;

import com.searchService.searchService.dto.CatalogDto;
import com.searchService.searchService.service.CatalogSearchService;
import com.searchService.searchService.service.CatalogPopulationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private CatalogSearchService catSearch;
    private CatalogPopulationService catPopulate;

    @GetMapping
    public ResponseEntity<List<CatalogDto>> catalogSearch(
            // http://localhost:8080/api/v1/search?keyword=Dune&filter=BOOK
            @RequestParam() String keyword,
            @RequestParam(required = false) String filter) {

        List<CatalogDto> results = catSearch.search(keyword, filter);

        // HTTP 200 OK with the results
        return ResponseEntity.ok(results);
    }

    @GetMapping("/populate")
    public ResponseEntity<String> populate() {
        catPopulate.populateFromCsv();
        return ResponseEntity.ok("Population completed");
    }
}
