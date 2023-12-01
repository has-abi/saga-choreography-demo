package com.ab.saga.orderservice.application.events.publisher;

import com.ab.saga.orderservice.application.dto.OrderCreatedEventDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {
    @Value("${amqp.exchange.order_events}")
    private String orderEventsExchange;
    @Value("${amqp.routingKey.order_created_events}")
    private String orderPlacedRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrderCreatedEvent(OrderCreatedEventDto orderPlacedEventDto) {
        rabbitTemplate.convertAndSend(orderEventsExchange, orderPlacedRoutingKey, orderPlacedEventDto);
    }

}
