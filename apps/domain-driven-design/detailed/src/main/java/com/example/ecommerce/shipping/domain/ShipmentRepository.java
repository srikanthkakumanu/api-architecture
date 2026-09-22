package com.example.ecommerce.shipping.domain;

import java.util.Optional;

public interface ShipmentRepository {
    Optional<Shipment> findById(String shipmentId);
    void save(Shipment shipment);
}
