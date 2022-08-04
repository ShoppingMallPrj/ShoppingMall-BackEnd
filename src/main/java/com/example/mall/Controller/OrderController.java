package com.example.mall.Controller;

import com.example.mall.Annotation.Auth;
import com.example.mall.Annotation.User;
import com.example.mall.Annotation.UserDetails;
import com.example.mall.Dto.Response.Order.OrderDto;
import com.example.mall.Exception.ErrorCode;
import com.example.mall.Exception.NoAuthException;
import com.example.mall.Service.OrderService;
import com.example.mall.Types.OrderStatus;
import com.example.mall.Types.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Tag(name = "주문", description = "주문 관련 api")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Operation(summary = "주문 생성", description = "주문을 하나 생성한다.. 관리자 권한 필요")
    @Auth(userRole = UserRole.USER)
    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(
            @User UserDetails userDetails,
            @Valid @RequestBody com.example.mall.Dto.Request.Order.OrderDto orderDto
    ) throws MessagingException {
        System.out.println(orderDto.toString());
        orderService.createOrder(userDetails.getUserId(), orderDto);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    @Operation(summary = "주문내역 전체보기", description = "주문 내역을 전부 가져온다. 관리자 권한 필요")
    @Auth(userRole = UserRole.ADMIN)
    @GetMapping("/list")
    public Page<OrderDto> readAll(
            @Parameter(description = "주문의 상태, 조건이 없으면 무시됨 ex:) /?status=준비중", in = ParameterIn.QUERY)
            @RequestParam(name = "status", required = false) OrderStatus orderStatus,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        return orderService.readList(orderStatus, pageable);
    }

    @Operation(summary = "유저가 가진 주문내역", description = "id 기준으로 유저가 가진 주문내역을 전부 가져온다.")
    @Auth(userRole = UserRole.USER)
    @GetMapping("/user")
    public Page<OrderDto> readUserOrder(
            @Parameter(hidden = true)
            @User UserDetails userDetails,
            @PageableDefault(page = 0, size = 5) Pageable pageable

    ) {
        return orderService.readUserOrderList(userDetails.getUserId(), pageable);
    }

    @Operation(summary = "주문 상세정보", description = "주문 하나의 상세정보를 가져온다.")
    @Auth(userRole = UserRole.USER)
    @GetMapping("/{orderId}")
    public OrderDto readOrder(
            @User UserDetails userDetails,
            @PathVariable int orderId
    ) {
        checkIsOwner(userDetails, orderId);

        OrderDto orderDto = orderService.read(orderId);
        return orderDto;
    }

    @Operation(summary = "주문 환불, 취소요청", description = "주문의 환불, 취소 요청을 한다. 소유자 권한 필요.")
    @Auth(userRole = UserRole.USER)
    @PutMapping("/{orderId}/cancel")
    public void cancelOrder(
            @User UserDetails userDetails,
            @PathVariable int orderId) {
        checkIsOwner(userDetails, orderId);
        orderService.cancelOrder(orderId);
    }

    @Operation(summary = "주문 배송중, 배송완료 상태변경", description = "주문의 상태를 변경시킨다. 관리자 권한 필요.")
    @Auth(userRole = UserRole.ADMIN)
    @PutMapping("/{orderId}/status")
    public void changeOrder(
            @PathVariable int orderId,
            @Parameter(description = "변경할 상태", in = ParameterIn.QUERY)
            @RequestParam OrderStatus orderStatus
    ) {
        orderService.changeOrderStatus(orderStatus, orderId);
    }

    //user가 order의 주인인지 검사, 아니면 401 에러를 던진다.
    private void checkIsOwner(UserDetails userDetails, int orderId) {

        if (userDetails.getUserRole() == UserRole.ADMIN) return;

        boolean result = orderService.isOwner(userDetails.getUserId(), orderId);
        if (!result) {
            throw new NoAuthException(ErrorCode.NO_AUTHORIZATION_ERROR);
        }
    }
}
