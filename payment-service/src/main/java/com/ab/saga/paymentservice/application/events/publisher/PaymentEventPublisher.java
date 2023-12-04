package com.ab.saga.paymentservice.application.events.publisher;

import com.ab.saga.paymentservice.application.dto.PaymentProcessedEventDto;
import com.ab.saga.paymentservice.application.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
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

    public void publishPaymentProcessedEvent(PaymentProcessedEventDto eventDto) {
        if (eventDto.getPaymentStatus().equals(PaymentStatus.PAYMENT_COMPLETED)) {
            rabbitTemplate.convertAndSend(paymentEventsExchange, paymentCompletedRoutingKey, eventDto);
        } else {
            rabbitTemplate.convertAndSend(paymentEventsExchange, paymentFailedRoutingKey, eventDto);
        }
    }

}
