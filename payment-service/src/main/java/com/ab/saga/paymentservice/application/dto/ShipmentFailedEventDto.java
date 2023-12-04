package com.ab.saga.paymentservice.application.dto;

import com.ab.saga.paymentservice.application.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentFailedEventDto {
    private Long userId;
    private Long orderId;
    private ShipmentStatus shipmentStatus;
    private Date instant;
}
