package com.example.hello.Dto.Out.Order;

import com.example.hello.Dto.In.Order.OrderListInDto;
import com.example.hello.Dto.Out.Item.ItemListOutDto;
import com.example.hello.Entity.OrderEntity;
import com.example.hello.Entity.OrderListEntity;
import com.example.hello.Types.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderOutDto {

    private int userId;

    private int orderId;

    //주문일
    @Schema(description = "주문 생성일")
    private LocalDateTime orderDate;

    //주문 상태(주문 완료, 배송중, 배송완료...)
    @Schema(description = "배송 상태")
    private OrderStatus orderStatus;

    //주문자 이름
    @Schema(description = "주문자 이름")
    private String orderName;

    //주문자 휴대전화
    @Schema(description = "주문자 휴대전화")
    private String orderPhone;

    //주문자 이메일
    @Schema(description = "주문자 이메일")
    private String orderEmail;

    @Schema(description = "수령자 이름")
    private String receiptName;

    //주문자 휴대전화
    @Schema(description = "수령자 휴대전화")
    private String receiptPhone;

    @Schema(description = "배송지 코드")
    private String code;

    //주문자 주소
    @Schema(description = "배송지 주소")
    String addr1;

    @Schema(description = "배송지 주소")
    String addr2;

    String memo;

    @Schema(description = "주문 목록")
    Set<OrderListOutDto> orderList = new HashSet<>();

    public static OrderOutDto from(OrderEntity orderEntity, ModelMapper modelMapper){

        OrderOutDto orderOutDto = modelMapper.map(orderEntity, OrderOutDto.class);
        orderOutDto.setOrderList(OrderListOutDto.from(orderEntity.getOrders()));
        return orderOutDto;
    }

    //엔티티 페이지 -> dto 페이지로 변환.
    public static Page<OrderOutDto> from(Page<OrderEntity> orderEntities, ModelMapper modelMapper){

        Page<OrderOutDto> orderOutDtos = orderEntities.map(entity -> {
            OrderOutDto dto = OrderOutDto.from(entity, modelMapper);
            return dto;
        });

        return orderOutDtos;

    }
}
