package com.ab.saga.orderservice.application.events.publisher;

import com.ab.commonapi.dtos.OrderCreatedEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderEventPublisher {
    @Value("${amqp.exchange.order_events}")
    private String orderEventsExchange;
    @Value("${amqp.routingKey.order_created_events}")
    private String orderPlacedRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void publishOrderCreatedEvent(OrderCreatedEventDTO eventDto) {
        rabbitTemplate.convertAndSend(orderEventsExchange, orderPlacedRoutingKey, eventDto);
    }

}
