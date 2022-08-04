package com.example.mall.Dto.Response.Item;


import com.example.mall.Entity.ItemImageEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;

@Schema(description = "응답으로 나가는 상품 이미지의 src")
@Data
public class ItemImageDto {

    private int imageId;

    private String imageSrc;

    //프로필 사진 여부
    private boolean isProfile;

    public static ItemImageDto from(ItemImageEntity itemImageEntity, ModelMapper modelMapper){

        return modelMapper.map(itemImageEntity, ItemImageDto.class);
    }

    public static Set<ItemImageDto> from(Set<ItemImageEntity> itemImageEntities, ModelMapper modelMapper){

        Set<ItemImageDto> itemImageDtos = new HashSet<>();

        for(ItemImageEntity itemImageEntity : itemImageEntities){

            ItemImageDto itemImageDto = from(itemImageEntity, modelMapper);
            itemImageDtos.add(itemImageDto);
        }

        return itemImageDtos;
    }


}
