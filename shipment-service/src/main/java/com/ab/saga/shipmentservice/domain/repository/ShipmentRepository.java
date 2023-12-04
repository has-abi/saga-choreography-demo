package com.ab.saga.shipmentservice.domain.repository;

import com.ab.saga.shipmentservice.domain.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
