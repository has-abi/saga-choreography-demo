package com.ab.saga.orderservice.presentation.ws;

import com.ab.saga.orderservice.application.dto.OrderRequestDto;
import com.ab.saga.orderservice.application.dto.OrderResponseDto;
import org.springframework.http.ResponseEntity;

public interface OrderController {
    ResponseEntity<OrderResponseDto> createOrder(OrderRequestDto orderRequestDto);
}
