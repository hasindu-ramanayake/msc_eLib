package com.searchService.searchService.dto;

import com.searchService.searchService.docs.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

// componentModel = "spring" allows you to @Autowired this interface
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CatalogMapper {
    BookDto toDto(Book book);
    Book toEntity(BookDto bookDto);
}
