package com.example.mall.Repository;

import com.example.mall.Entity.OrderListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderListRepository extends JpaRepository<OrderListEntity, Integer> {
}
