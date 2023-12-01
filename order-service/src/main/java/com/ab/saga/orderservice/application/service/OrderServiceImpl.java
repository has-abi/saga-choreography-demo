package com.ab.saga.orderservice.application.service;

import com.ab.saga.orderservice.application.dto.OrderCreatedEventDto;
import com.ab.saga.orderservice.application.dto.OrderRequestDto;
import com.ab.saga.orderservice.application.dto.OrderResponseDto;
import com.ab.saga.orderservice.application.events.publisher.OrderEventPublisher;
import com.ab.saga.orderservice.application.mapper.OrderMapper;
import com.ab.saga.orderservice.doman.entity.Order;
import com.ab.saga.orderservice.doman.enums.OrderStatus;
import com.ab.saga.orderservice.doman.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.orderRequestDtoToOrder(orderRequestDto);

        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        Order savedOrder = orderRepository.save(order);
        OrderCreatedEventDto eventDto = orderMapper.orderToOrderCreatedEventDto(order);
        orderEventPublisher.publishOrderCreatedEvent(eventDto);

        return orderMapper.orderToOrderResponseDto(savedOrder);
    }
}
