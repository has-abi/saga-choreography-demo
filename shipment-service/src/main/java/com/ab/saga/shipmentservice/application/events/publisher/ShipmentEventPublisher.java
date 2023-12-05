package com.ab.saga.shipmentservice.application.events.publisher;

import com.ab.commonapi.dtos.ShipmentEventDto;
import com.ab.commonapi.enums.ShipmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ShipmentEventPublisher {
    @Value("${amqp.exchange.shipment_events}")
    private String shipmentEventsExchange;
    @Value("${amqp.routingKey.shipment_completed_events}")
    private String shipmentCompletedRoutingKey;
    @Value("${amqp.routingKey.shipment_failed_events}")
    private String shipmentFailedRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void publishShipmentEvent(ShipmentEventDto eventDto) {
        if (eventDto.getShipmentStatus().equals(ShipmentStatus.SHIPMENT_COMPLETED)) {
            rabbitTemplate.convertAndSend(shipmentEventsExchange, shipmentCompletedRoutingKey, eventDto);
        } else {
            rabbitTemplate.convertAndSend(shipmentEventsExchange, shipmentFailedRoutingKey, eventDto);
        }
    }
}
