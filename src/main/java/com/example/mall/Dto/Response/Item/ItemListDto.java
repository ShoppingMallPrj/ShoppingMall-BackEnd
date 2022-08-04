package com.example.mall.Dto.Response.Item;

import com.example.mall.Entity.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//목록으로 전달할 경우 아이템의 데이터 (간략화된 데이터)
@Schema(description = "목록으로 전달되는 아이템의 간략화된 데이터")
@Data
public class ItemListDto {

    @Schema(description = "아이템 고유 아이디")
    private int itemId;

    @Schema(description = "성별")
    private String gender;

    @Schema(description = "아이템 카테고리")
    private String category;

    @Schema(description = "아이템 프로필")
    private String itemProfile;

    @Schema(description = "아이템 이름")
    private String itemName;

    @Schema(description = "아이템 가격")
    private String itemPrice;

    @Schema(description = "아이템 옵션들")
    private Set<ItemOptionDto> options = new HashSet<>();


    public static ItemListDto from(ItemEntity itemEntity, ModelMapper modelMapper) {

        ItemListDto itemListDto = modelMapper.map(itemEntity, ItemListDto.class);
        itemListDto.setOptions(ItemOptionDto.from(itemEntity.getOption(), modelMapper));

        return itemListDto;
    }

    public static Set<ItemListDto> from(Set<ItemEntity> itemEntities, ModelMapper modelMapper) {

        Set<ItemListDto> itemListDtos = new HashSet<>();

        Iterator<ItemEntity> iterator = itemEntities.iterator();
        while (iterator.hasNext()) {
            itemListDtos.add(ItemListDto.from(iterator.next(), modelMapper));
        }
        return itemListDtos;
    }

    //Entitiy Page에서 Entity dto로 변환
    public static Page<ItemListDto> from(Page<ItemEntity> itemEntityPage, ModelMapper modelMapper) {

            Page<ItemListDto> itemListDtos = itemEntityPage.map(entity -> {
                ItemListDto dto = ItemListDto.from(entity, modelMapper);
                return dto;
            });
            return itemListDtos;
        }
}
