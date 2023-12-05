package com.ab.saga.paymentservice.application.events.publisher;

import com.ab.commonapi.dtos.PaymentEventDto;
import com.ab.commonapi.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentEventPublisher {

    @Value("${amqp.exchange.payment_events}")
    private String paymentEventsExchange;
    @Value("${amqp.routingKey.payment_completed_events}")
    private String paymentCompletedRoutingKey;

    @Value("${amqp.routingKey.payment_failed_events}")
    private String paymentFailedRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void publishPaymentEvent(PaymentEventDto eventDto) {
        if (eventDto.getPaymentStatus().equals(PaymentStatus.PAYMENT_COMPLETED)) {
            rabbitTemplate.convertAndSend(paymentEventsExchange, paymentCompletedRoutingKey, eventDto);
        } else {
            rabbitTemplate.convertAndSend(paymentEventsExchange, paymentFailedRoutingKey, eventDto);
        }
    }

}
