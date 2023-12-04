package com.ab.saga.orderservice.doman.entity;

import com.ab.saga.orderservice.doman.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
