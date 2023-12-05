package com.ab.saga.shipmentservice.application.service;

import com.ab.commonapi.dtos.PaymentEventDto;

public interface ShipmentService {
    void createdShipment(PaymentEventDto eventDto);
}
