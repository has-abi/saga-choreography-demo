package com.ab.commonapi.dtos;

import com.ab.commonapi.enums.ShipmentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ShipmentEventDto {
    private Long userId;
    private Long orderId;
    private ShipmentStatus shipmentStatus;
    private Date instant = new Date();

    public ShipmentEventDto(Long userId, Long orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }
}
