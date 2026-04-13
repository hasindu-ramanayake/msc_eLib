package com.example.borrowservice.mapper;

import com.example.borrowservice.dto.NewWaitListDto;
import com.example.borrowservice.entity.Waitlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewWaitListMapper extends BaseMapper<NewWaitListDto, Waitlist> {
}
