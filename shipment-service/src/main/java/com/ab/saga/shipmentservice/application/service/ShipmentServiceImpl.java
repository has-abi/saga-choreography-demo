package com.ab.saga.shipmentservice.application.service;

import com.ab.commonapi.dtos.PaymentEventDto;
import com.ab.commonapi.dtos.ShipmentEventDto;
import com.ab.commonapi.enums.PaymentStatus;
import com.ab.commonapi.enums.ShipmentStatus;
import com.ab.saga.shipmentservice.application.events.publisher.ShipmentEventPublisher;
import com.ab.saga.shipmentservice.domain.entity.Shipment;
import com.ab.saga.shipmentservice.domain.repository.ShipmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentEventPublisher shipmentEventPublisher;

    @Transactional
    @Override
    public void createdShipment(PaymentEventDto eventDto) {
        var shipmentEventDto = new ShipmentEventDto(eventDto.getUserId(), eventDto.getOrderId());
        if (eventDto.getPaymentStatus().equals(PaymentStatus.PAYMENT_COMPLETED)) {
            try {
                handleCreateShipment(eventDto);
                shipmentEventDto.setShipmentStatus(ShipmentStatus.SHIPMENT_COMPLETED);

                log.info("ShipmentService#createdShipment: Shipment completed for orderId={}, userId={}",
                        eventDto.getOrderId(), eventDto.getUserId());
            } catch (Exception ex) {
                shipmentEventDto.setShipmentStatus(ShipmentStatus.SHIPMENT_FAILED);
                log.error("ShipmentService#createdShipment: Shipment failed for orderId={}, userId={}",
                        eventDto.getOrderId(), eventDto.getUserId());

                ex.printStackTrace();
            }

            shipmentEventPublisher.publishShipmentEvent(shipmentEventDto);
        }
    }


    private void handleCreateShipment(PaymentEventDto eventDto) {
        if (eventDto.getUserId() == 999) {
            throw new RuntimeException("Shipment failed");
        }
        Shipment shipment = new Shipment();
        shipment.setUserId(eventDto.getUserId());
        shipment.setOrderId(eventDto.getOrderId());
        shipment.setShipmentStatus(ShipmentStatus.SHIPMENT_COMPLETED);

        shipmentRepository.save(shipment);
    }
}
