package com.ab.saga.orderservice.application.service;

import com.ab.saga.orderservice.application.dto.OrderRequestDto;
import com.ab.saga.orderservice.application.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
}
