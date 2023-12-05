package com.ab.saga.shipmentservice.application.events.listener;

import com.ab.commonapi.dtos.PaymentEventDto;
import com.ab.saga.shipmentservice.application.service.ShipmentService;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class PaymentEventListener {

    private final ShipmentService shipmentService;

    @RabbitListener(queues = "${amqp.queues.payment_completed}", ackMode = "MANUAL")
    public void handlePaymentCompletedEvent(PaymentEventDto eventDto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag)
            throws IOException {
        try {
            shipmentService.createdShipment(eventDto);
            channel.basicAck(tag, false);
        } catch (RuntimeException | IOException ex) {
            ex.printStackTrace();
            channel.basicNack(tag, false, false);
        }
    }
}
