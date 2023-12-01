package com.ab.saga.paymentservice.application.dto;

import com.ab.saga.paymentservice.application.enums.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PaymentProcessedEventDto {
    private Long userId;
    private Long orderId;
    private PaymentStatus paymentStatus = PaymentStatus.PAYMENT_FAILED;
    private Date instant = new Date();

    public PaymentProcessedEventDto(Long userId, Long orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }
}
