package com.example.borrowservice.mapper;

import com.example.borrowservice.dto.WaitListDto;
import com.example.borrowservice.entity.Waitlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WaitListMapper extends BaseMapper<WaitListDto, Waitlist> {
}
