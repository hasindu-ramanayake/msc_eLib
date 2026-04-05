package com.searchService.searchService.dto;

import com.searchService.searchService.docs.Book;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-04T03:29:52+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class CatalogMapperImpl implements CatalogMapper {

    @Override
    public BookDto toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        bookDto.setId( book.getId() );
        bookDto.setType( book.getType() );
        bookDto.setTitle( book.getTitle() );
        bookDto.setAuthor( book.getAuthor() );
        bookDto.setIsbn( book.getIsbn() );

        return bookDto;
    }

    @Override
    public Book toEntity(BookDto bookDto) {
        if ( bookDto == null ) {
            return null;
        }

        Book book = new Book();

        book.setId( bookDto.getId() );
        book.setTitle( bookDto.getTitle() );
        book.setAuthor( bookDto.getAuthor() );
        book.setIsbn( bookDto.getIsbn() );
        book.setType( bookDto.getType() );

        return book;
    }
}
