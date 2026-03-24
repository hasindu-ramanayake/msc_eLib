package com.searchService.searchService.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.searchService.searchService.docs.CatalogType;

import lombok.Data;


//look at the "type" field to know which child class to use
// add class to map here
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value=BookDto.class, name = "BOOK")
//        ,@JsonSubTypes.Type(value=MovieDto.class, name = "MOVIE")
})
public abstract class CatalogDto {
    private String id;
    private CatalogType type;
}
