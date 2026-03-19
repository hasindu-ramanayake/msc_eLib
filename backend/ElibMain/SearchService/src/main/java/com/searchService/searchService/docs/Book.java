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
    private String author;
    private String isbn;
    private CatalogType type;


    @Override
    public void printCatalogItem() {
    }

    @Override
    public CatalogType getCatalogType() {
        return type;
    }
}
