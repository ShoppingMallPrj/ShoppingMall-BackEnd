package com.example.mall.Dto.Response.Item;

import com.example.mall.Entity.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;

@Schema(description = "상품 데이터 클라이언트에 전달 시 사용하는 dto")
@Data
public class ItemDto {

    private int itemId;

    private String itemName;

    private String category;

    private String itemDescription;

    private String itemProfile;

    private int itemPrice;

    private String gender;

    //연관 아이템들
    private Set<ItemListDto> related = new HashSet<>();

    private Set<ItemOptionDto> options = new HashSet<>();

    private Set<ItemImageDto> images = new HashSet<>();

    private Set<ItemReviewDto> reviews = new HashSet<>();

    public static ItemDto from(ItemEntity itemEntity, ModelMapper modelMapper){

        ItemDto itemDto = modelMapper.map(itemEntity, ItemDto.class);

        itemDto.setOptions(ItemOptionDto.from(itemEntity.getOption(), modelMapper));
        itemDto.setImages(ItemImageDto.from(itemEntity.getImage(), modelMapper));
        itemDto.setReviews(ItemReviewDto.from(itemEntity.getReview(), modelMapper));

        return itemDto;
    }

}
