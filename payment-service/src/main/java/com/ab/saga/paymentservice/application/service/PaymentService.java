package com.ab.saga.paymentservice.application.service;

import com.ab.saga.paymentservice.application.dto.OrderCreatedEventDto;
import com.ab.saga.paymentservice.application.dto.ShipmentFailedEventDto;

public interface PaymentService {
    void processPayment(OrderCreatedEventDto eventDto);

    void rollbackPayment(ShipmentFailedEventDto eventDto);
}
