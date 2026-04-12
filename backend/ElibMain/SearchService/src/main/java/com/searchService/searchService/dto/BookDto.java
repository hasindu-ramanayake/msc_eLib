package com.searchService.searchService.dto;

import com.searchService.searchService.docs.CatalogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookDto extends CatalogDto {
    private String title;
    private String subtitle;
    private String authors;
    private String isbn13;
    private String isbn10;
    private String categories;
    private String thumbnail;
    private String description;
    private String published_year;
    private String language;
    private String age;

    public BookDto() {
        // Automatically set the enum type
        this.setType(CatalogType.BOOK);
    }
}