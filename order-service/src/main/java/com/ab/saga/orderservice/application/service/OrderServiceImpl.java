package com.ab.saga.orderservice.application.service;

import com.ab.commonapi.dtos.OrderCreatedEventDTO;
import com.ab.commonapi.dtos.PaymentEventDto;
import com.ab.commonapi.dtos.ShipmentEventDto;
import com.ab.commonapi.enums.PaymentStatus;
import com.ab.commonapi.enums.ShipmentStatus;
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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    @Transactional
    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.orderRequestDtoToOrder(orderRequestDto);

        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        Order savedOrder = orderRepository.save(order);
        OrderCreatedEventDTO eventDto = orderMapper.orderToOrderCreatedEventDto(order);
        orderEventPublisher.publishOrderCreatedEvent(eventDto);

        log.info("OrderService#createOrder: Order created with orderId={} for userId={}",
                savedOrder.getId(), savedOrder.getUserId());
        return orderMapper.orderToOrderResponseDto(savedOrder);
    }

    @Transactional
    @Override
    public void cancelOrder(PaymentEventDto eventDto) {
        if (eventDto.getPaymentStatus().equals(PaymentStatus.PAYMENT_FAILED)) {
            var orderToCancel = orderRepository.findById(eventDto.getOrderId());

            orderToCancel.ifPresent(order -> {
                order.setOrderStatus(OrderStatus.ORDER_FAILED);
                log.info("OrderService#cancelOrder: Order canceled orderId={}, userId={}",
                        order.getId(), order.getUserId());
            });

        }
    }

    @Transactional
    @Override
    public void confirmOrder(ShipmentEventDto eventDto) {
        if (eventDto.getShipmentStatus().equals(ShipmentStatus.SHIPMENT_COMPLETED)) {
            var orderToConfirm = orderRepository.findById(eventDto.getOrderId());

            orderToConfirm.ifPresent(order -> {
                order.setOrderStatus(OrderStatus.ORDER_COMPLETED);
                log.info("OrderService#cancelOrder: Order completed orderId={}, userId={}",
                        order.getId(), order.getUserId());
            });
        }
    }
}
