package com.example.mall.Service;


import com.example.mall.Dto.Request.Order.OrderListDto;
import com.example.mall.Dto.Response.Order.OrderDto;
import com.example.mall.Entity.ItemEntity;
import com.example.mall.Entity.OrderEntity;
import com.example.mall.Entity.OrderListEntity;
import com.example.mall.Entity.UserEntity;
import com.example.mall.Repository.*;
import com.example.mall.Types.OrderStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderListRepository orderListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemOptionRepository itemOptionRepository;

    @Autowired
    MailService mailService;

    @Autowired
    ModelMapper modelMapper;


    //주문 소유자인지 확인
    public boolean isOwner(int userId, int orderId){
        return orderRepository.isOwnerOfOrder(userId, orderId);
    }

    //주문을 목록으로 읽는다. 상태에 따라 분류가 가능하다.
    public Page<OrderDto> readList(OrderStatus orderStatus, Pageable pageable){

        Page<OrderEntity> orderEntities = orderRepository.findAllByCondition(orderStatus, pageable);
        Page<OrderDto> orderOutDtos = OrderDto.from(orderEntities, modelMapper);

        return orderOutDtos;
    }

    //유저의 주문을 목록으로 읽는다.
    public Page<OrderDto> readUserOrderList(int userId, Pageable pageable){

        Page<OrderEntity> orderEntities = orderRepository.findAllByUser(userId, pageable);
        Page<OrderDto> orderOutDtos = OrderDto.from(orderEntities, modelMapper);

        return orderOutDtos;
    }

    //주문 하나를 읽는다.
    public OrderDto read(int orderId){

        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = OrderDto.from(orderEntity, modelMapper);

        return orderDto;
    }

    //주문을 받는다.
    @Transactional
    public OrderEntity createOrder(int userId, com.example.mall.Dto.Request.Order.OrderDto orderDto) throws MessagingException {

        System.out.println(orderDto.getOrderList());
        UserEntity userEntity = userRepository.getById(userId);

        //Order 엔티티 생성
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(userEntity);

        orderEntity.setOrderName(orderDto.getOrderName());
        orderEntity.setOrderPhone(orderDto.getOrderPhone());
        orderEntity.setOrderEmail(orderDto.getOrderEmail());

        orderEntity.setReceiptName(orderDto.getReceiptName());
        orderEntity.setReceiptPhone(orderDto.getReceiptPhone());

        orderEntity.setCode(orderDto.getCode());
        orderEntity.setAddr1(orderDto.getAddr1());
        orderEntity.setAddr2(orderDto.getAddr2());

        orderEntity.setMemo(orderDto.getMemo());
        orderEntity.setOrderStatus(OrderStatus.준비중);

        //임시로 총액 설정
        orderEntity.setTotal(100);

        orderRepository.save(orderEntity);

        int total = 0;

        //orderList 생성
        Set<OrderListEntity> orderListEntities = new HashSet<>();

        for(OrderListDto orderListDto : orderDto.getOrderList()){

            ItemEntity itemEntity = itemRepository.getById(orderListDto.getItemId());

            OrderListEntity orderList = new OrderListEntity();

            orderList.setOrderId(orderEntity.getOrderId());
            orderList.setItemEntity(itemEntity);
            orderList.setItemStock(orderListDto.getItemStock());
            orderList.setItemOptionEntity(itemOptionRepository.getById(orderListDto.getOptionId()));

            orderListEntities.add(orderList);
            System.out.println(itemEntity.getItemPrice());
            System.out.println(orderListDto.getItemStock());
            total += itemEntity.getItemPrice()* orderListDto.getItemStock();
        }
        orderEntity.setTotal(total);
        //orderRepository.save(orderEntity);

        orderEntity.setOrders(orderListEntities);
        orderListRepository.saveAll(orderListEntities);

        //주문 완료 이메일 발송
        mailService.sendOrderConfirmMail(userEntity.getUserEmail(), orderEntity);

        return orderEntity;
    }

    //주문을 취소한다.
    @Transactional
    public void cancelOrder(int orderId){

        OrderEntity order = orderRepository.findByOrderId(orderId);
        OrderStatus orderStatus = order.getOrderStatus();

        //준비중일 때 만 취소가 가능하다.
        if(orderStatus == OrderStatus.준비중) {
            order.setOrderStatus(OrderStatus.주문취소);
        }
    }

    //주문 상태를 변경한다. (배송중, 배송완료, 배송취소...)
    @Transactional
    public void changeOrderStatus(OrderStatus orderStatus, int orderId){

        OrderEntity order = orderRepository.findByOrderId(orderId);
        order.setOrderStatus(orderStatus);
    }

}
