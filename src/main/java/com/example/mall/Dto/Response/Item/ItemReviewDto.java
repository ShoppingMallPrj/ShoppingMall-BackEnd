package com.example.mall.Dto.Response.Item;

import com.example.mall.Entity.ItemReviewEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Schema(description = "간략화된 리뷰 데이터")
@Data
public class ItemReviewDto {

    @Schema(description = "리뷰 고유 id", nullable = false)
    private int reviewId;

    @Schema(description = "리뷰 작성 시간", nullable = false)
    private LocalDateTime reviewDate;

    @Schema(description = "리뷰 별점 (1~5)", nullable = false)
    private int reviewStar;

    @Schema(description = "리뷰 내용", nullable = false)
    private String reviewContent;

    public static ItemReviewDto from(ItemReviewEntity itemReviewEntity, ModelMapper modelMapper) {

        ItemReviewDto itemReviewDto = modelMapper.map(itemReviewEntity, ItemReviewDto.class);

        return  itemReviewDto;
    }

    public static Set<ItemReviewDto> from(Set<ItemReviewEntity> itemReviewEntity, ModelMapper modelMapper) {

        Set<ItemReviewDto> itemReviewDtos = new HashSet<>();

        Iterator<ItemReviewEntity> iterator = itemReviewEntity.iterator();

        while (iterator.hasNext()) {
            itemReviewDtos.add(ItemReviewDto.from(iterator.next(), modelMapper));
        }
        return  itemReviewDtos;
    }
}
