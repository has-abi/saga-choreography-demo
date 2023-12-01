package com.ab.saga.orderservice.doman.repository;

import com.ab.saga.orderservice.doman.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
