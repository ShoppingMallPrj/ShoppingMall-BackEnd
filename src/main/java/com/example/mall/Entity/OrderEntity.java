package com.example.mall.Entity;

import com.example.mall.Types.OrderStatus;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`order`")
@Data
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    //주문자
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    //주문 아이템들
    @OneToMany(targetEntity = OrderListEntity.class)
    @JoinColumn(name = "order_id")
    Set<OrderListEntity> orders = new HashSet<>();

    //주문일
    @Column(name = "order_date")
    @CreatedDate
    private LocalDateTime orderDate;

    //주문 상태(주문 완료, 배송중, 배송완료...)
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    //주문자 이름
    @NotBlank(message = "주문자 이름을 입력해야 합니다!")
    private String orderName;

    //주문자 휴대전화
    @NotBlank(message = "주문자 휴대전화를 입력해야 합니다!")
    private String orderPhone;

    //주문자 이메일
    @Email(message = "주문자 이메일을 입력해야 합니다!")
    private String orderEmail;

    @NotBlank(message = "수령자 이름을 입력해야 합니다!")
    @Column(name = "receipt_name")
    private String receiptName;

    //주문자 휴대전화
    @NotBlank(message = "수령자 휴대전화 입력해야 합니다!")
    @Column(name = "receipt_phone")
    private String receiptPhone;

    @NotBlank(message = "주문 시 주소 코드를 입력해야 합니다!")
    private String code;

    //주문자 주소
    @NotBlank(message = "주문 시 주소를 입력해야 합니다!")
    String addr1;

    @NotBlank(message = "주문 시 주소를 입력해야 합니다!")
    String addr2;

    String memo;

    //전체 금액
    @Column(name = "total")
    private int total;



}
