package com.ab.saga.orderservice.application.service;

import com.ab.saga.orderservice.application.dto.OrderRequestDto;
import com.ab.saga.orderservice.application.dto.OrderResponseDto;
import com.ab.saga.orderservice.application.dto.PaymentProcessedEventDto;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);
    void cancelOrder(PaymentProcessedEventDto eventDto);
}
