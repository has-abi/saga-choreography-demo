package com.ab.saga.paymentservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentFailedEventDto {
    private Long userId;
    private Long orderId;
}
