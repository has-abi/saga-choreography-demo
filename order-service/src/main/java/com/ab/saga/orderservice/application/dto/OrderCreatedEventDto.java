package com.ab.saga.orderservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEventDto {
    private Long orderId;
    private Long userId;
    private Double amount;
    private Date instant;
}
