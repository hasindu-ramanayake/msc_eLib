package com.example.borrowservice.mapper;

import com.example.borrowservice.dto.BorrowDto;
import com.example.borrowservice.entity.Borrow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowMapper  extends BaseMapper<BorrowDto, Borrow>{
}
