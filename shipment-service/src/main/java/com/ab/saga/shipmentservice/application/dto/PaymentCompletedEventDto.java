package com.ab.saga.shipmentservice.application.dto;

import com.ab.saga.shipmentservice.application.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCompletedEventDto {
    private Long userId;
    private Long orderId;
    private PaymentStatus paymentStatus;
    private Date instant;
}
