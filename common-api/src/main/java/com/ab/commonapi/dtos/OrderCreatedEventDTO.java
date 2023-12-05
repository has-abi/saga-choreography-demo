package com.ab.commonapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEventDTO {
    private Long orderId;
    private Long userId;
    private Double amount;
    private Date instant = new Date();
}