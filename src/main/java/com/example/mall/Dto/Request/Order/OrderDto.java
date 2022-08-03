package com.example.mall.Dto.Request.Order;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Schema(description = "주문 정보 입력 시 사용하는 dto")
@Data
public class OrderDto {

    //주문자 이름
    @Schema(description = "주문자 이름")
    @NotBlank(message = "주문자 이름을 입력해야 합니다!")
    private String orderName;

    //주문자 휴대전화
    @Schema(description = "주문자 휴대전화")
    @NotBlank(message = "주문자 이름을 입력해야 합니다!")
    private String orderPhone;

    //주문자 이메일
    @Schema(description = "주문자 이메일")
    @Email(message = "주문자 이메일을 입력해야 합니다!")
    private String orderEmail;

    @Schema(description = "수령자 이름")
    @NotBlank(message = "수령자 이름을 입력해야 합니다!")
    private String receiptName;

    //주문자 휴대전화
    @Schema(description = "수령자 휴대전화")
    @NotBlank(message = "수령자 휴대전화 입력해야 합니다!")
    private String receiptPhone;

    @Schema(description = "배송지 코드")
    @NotBlank(message = "주문 시 주소 코드를 입력해야 합니다!")
    private String code;

    //주문자 주소
    @Schema(description = "배송지 주소")
    @NotBlank(message = "주문 시 주소를 입력해야 합니다!")
    String addr1;

    @Schema(description = "배송지 주소")
    @NotBlank(message = "주문 시 주소를 입력해야 합니다!")
    String addr2;

    String memo;

    @Schema(description = "주문 목록 dto")
    Set<OrderListDto> orderList;


}