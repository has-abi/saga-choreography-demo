package com.ab.saga.orderservice.application.dto;

import com.ab.saga.orderservice.application.enums.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PaymentProcessedEventDto {
    private Long userId;
    private Long orderId;
    private PaymentStatus paymentStatus;
    private Date instant;
}
