package com.ab.saga.orderservice.application.dto;

import com.ab.saga.orderservice.application.enums.ShipmentStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ShipmentEventDto {
    private Long userId;
    private Long orderId;
    private ShipmentStatus shipmentStatus;
    private Date instant;
}
