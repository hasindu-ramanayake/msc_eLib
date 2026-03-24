package com.searchService.searchService.dto;

import com.searchService.searchService.docs.CatalogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookDto extends CatalogDto {
    private String title;
    private String author;
    private String isbn;

    public BookDto() {
        // Automatically set the enum type
        this.setType(CatalogType.BOOK);
    }
}