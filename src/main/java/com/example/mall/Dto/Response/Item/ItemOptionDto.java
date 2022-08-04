package com.example.mall.Dto.Response.Item;

import com.example.mall.Entity.ItemOptionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;

@Schema(description = "")
@Data
public class ItemOptionDto {

    private int optionId;

    //옵션의 내용 (ex : size-XL, 색상-검정...)
    private String optionContent;

    //해당 옵션이 가진 재고 수량
    private int optionStock;

    public static ItemOptionDto from(ItemOptionEntity itemOptionEntity, ModelMapper modelMapper) {

        return modelMapper.map(itemOptionEntity, ItemOptionDto.class);
    }

    public static Set<ItemOptionDto> from(Set<ItemOptionEntity> itemOptionEntities, ModelMapper modelMapper) {

        Set<ItemOptionDto> itemOptionDtos = new HashSet<>();

        for (ItemOptionEntity itemOptionEntity : itemOptionEntities) {

            ItemOptionDto itemOptionDto = from(itemOptionEntity, modelMapper);
            itemOptionDtos.add(itemOptionDto);
        }
        return itemOptionDtos;
    }


}
