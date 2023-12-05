package com.ab.saga.orderservice.application.service;

import com.ab.commonapi.dtos.PaymentEventDto;
import com.ab.commonapi.dtos.ShipmentEventDto;
import com.ab.saga.orderservice.application.dto.OrderRequestDto;
import com.ab.saga.orderservice.application.dto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    void cancelOrder(PaymentEventDto eventDto);

    void confirmOrder(ShipmentEventDto eventDto);
}
