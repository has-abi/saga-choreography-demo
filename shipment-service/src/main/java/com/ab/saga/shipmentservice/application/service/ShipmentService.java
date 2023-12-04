package com.ab.saga.shipmentservice.application.service;

import com.ab.saga.shipmentservice.application.dto.PaymentCompletedEventDto;

public interface ShipmentService {
    void createdShipment(PaymentCompletedEventDto eventDto);
}
