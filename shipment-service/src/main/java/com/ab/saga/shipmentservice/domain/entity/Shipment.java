package com.ab.saga.shipmentservice.domain.entity;

import com.ab.saga.shipmentservice.domain.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long orderId;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;
}
