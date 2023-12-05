package com.ab.saga.paymentservice.application.service;

import com.ab.commonapi.dtos.OrderCreatedEventDTO;
import com.ab.commonapi.dtos.ShipmentEventDto;

public interface PaymentService {
    void processPayment(OrderCreatedEventDTO eventDto);

    void cancelPayment(ShipmentEventDto eventDto);
}
