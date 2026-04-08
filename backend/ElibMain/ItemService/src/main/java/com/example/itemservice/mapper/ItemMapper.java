package com.example.itemservice.mapper;

import com.example.itemservice.dto.ItemDto;
import com.example.itemservice.entity.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper extends BaseMapper<ItemDto, Item>{
}
