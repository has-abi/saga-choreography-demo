package com.ab.saga.orderservice.application.events.listener;

import com.ab.commonapi.dtos.PaymentEventDto;
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
public class PaymentEventListener {

    private final OrderService orderService;

    @RabbitListener(queues = "${amqp.queues.payment_failed}", ackMode = "MANUAL")
    public void handlePaymentFailedEvent(PaymentEventDto eventDto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag)
            throws IOException {
        try {
            orderService.cancelOrder(eventDto);
            channel.basicAck(tag, false);
        } catch (RuntimeException | IOException ex) {
            ex.printStackTrace();
            channel.basicNack(tag, false, false);
        }
    }
}
