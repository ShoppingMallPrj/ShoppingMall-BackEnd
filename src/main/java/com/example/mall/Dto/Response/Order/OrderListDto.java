package com.example.mall.Dto.Response.Order;

import com.example.mall.Entity.OrderListEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderListDto {

    @Schema(description = "주문 상품의 고유 id", nullable = false)
    @NotNull
    int itemId;

    @Schema(description = "주문 수량", nullable = false)
    @Min(1)
    @Max(99)
    private int itemStock;

    @Schema(description = "상품 옵션의 id", nullable = false)
    private int optionId;

    private String itemProfile;

    private String itemName;

    private String optionContent;

    @Schema(description = "상품 대표 이미지 src")
    private String itemImage;

    private int itemPrice;

    public static OrderListDto from(OrderListEntity orderListEntity){
        OrderListDto orderListDto = new OrderListDto();

        orderListDto.setItemName(orderListEntity.getItemEntity().getItemName());
        orderListDto.setItemImage(orderListEntity.getItemEntity().getItemProfile());
        orderListDto.setItemStock(orderListEntity.getItemStock());
        orderListDto.setOptionContent(orderListEntity.getItemOptionEntity().getOptionContent());
        orderListDto.setItemId(orderListEntity.getItemEntity().getItemId());
        orderListDto.setOptionId(orderListEntity.getItemOptionEntity().getOptionId());
        orderListDto.setItemImage(orderListEntity.getItemEntity().getItemProfile());
        orderListDto.setItemPrice(orderListEntity.getItemEntity().getItemPrice());
        return orderListDto;
    }

    public static Set<OrderListDto> from(Set<OrderListEntity> orderListEntities){

        Set<OrderListDto> orderListDtos = new HashSet<>();

        for(OrderListEntity orderList : orderListEntities){

            OrderListDto orderListDto = OrderListDto.from(orderList);
            orderListDtos.add(orderListDto);
        }
        return orderListDtos;
    }
}
