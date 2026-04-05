package com.example.borrowservice.mapper;

import com.example.borrowservice.dto.NewBorrowDto;
import com.example.borrowservice.entity.Borrow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewBorrowMapper extends BaseMapper<NewBorrowDto, Borrow> {
}
