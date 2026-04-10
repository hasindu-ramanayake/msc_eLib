package com.searchService.searchService.docs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Catalog{
    @org.springframework.data.annotation.Id
    private String id;
    private String title;
    private String subtitle;
    private String authors;
    private String isbn13;
    private String isbn10;
    private String categories;
    private String thumbnail;
    private String description;
    private String published_year;
    private CatalogType type;


    @Override
    public void printCatalogItem() {
    }

    @Override
    public CatalogType getCatalogType() {
        return type;
    }
}
