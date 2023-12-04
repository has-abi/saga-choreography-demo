package com.ab.saga.orderservice.application.events.listener;

import com.ab.saga.orderservice.application.dto.ShipmentEventDto;
import com.ab.saga.orderservice.application.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class ShipmentEventListener {

    private final OrderService orderService;

    @RabbitListener(queues = "${amqp.queues.shipment_completed}", ackMode = "MANUAL")
    public void handleShipmentCompletedEvent(ShipmentEventDto eventDto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            orderService.confirmOrder(eventDto);
            channel.basicAck(tag, false);
        } catch (RuntimeException | IOException ex) {
            ex.printStackTrace();
            channel.basicNack(tag, false, false);
        }
    }

}
