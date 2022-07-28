package com.example.hello.Dto.In.Item;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "상품 수정 시 사용하는 dto")
@Data
@ToString
public class ItemUpdateDto {

    @Schema(description = "상품 이름")
    @Size(min = 2, message = "상품 이름은 최소 2자 이상이어야 합니다!")
    @Size(max = 50, message = "상품 이름은 최대 50자 이하여야 합니다!")
    private String itemName;

    @Schema(description = "상품 카테고리")
    @NotBlank
    private String itemCategory;

    @Schema(description = "성별 카테고리")
    @NotBlank
    private String gender;

    @Schema(description = "상품 설명", nullable = false)
    @Size(min = 2, message = "상품 설명은 최소 2자 이상이어야 합니다!")
    @Size(max = 2000, message = "상품 설명은 최대 500자 이하여야 합니다!")
    private String itemDescription;

    @Schema(description = "상품 가격", nullable = false)
    @Min(value = 0, message = "상품 가격은 최소 0원 이상이어야 합니다!")
    private int itemPrice;
}
