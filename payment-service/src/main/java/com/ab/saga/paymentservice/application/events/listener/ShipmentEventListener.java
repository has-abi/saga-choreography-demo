package com.ab.saga.paymentservice.application.events.listener;

import com.ab.saga.paymentservice.application.dto.ShipmentFailedEventDto;
import com.ab.saga.paymentservice.application.service.PaymentService;
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

    private final PaymentService paymentService;

    @RabbitListener(queues = "${amqp.queues.shipment_failed}", ackMode = "MANUAL")
    public void handleShipmentFailedEvent(ShipmentFailedEventDto eventDto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            paymentService.cancelPayment(eventDto);
            channel.basicAck(tag, false);
        } catch (RuntimeException | IOException ex) {
            ex.printStackTrace();
            channel.basicNack(tag, false, false);
        }
    }

}
