package com.example.mall.Dto.Request.Order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(description = "주문 요소 1개의 상세정보")
@Data
public class OrderListDto {

    @Schema(description = "주문 상품의 고유 id", nullable = false)
    @NotNull
    int itemId;

    @Schema(description = "주문 수량", nullable = false)
    @Min(1)
    @Max(99)
    private int itemStock;

    @Schema(description = "상품 옵션의 고유 id", nullable = false)
    private int optionId;

}
