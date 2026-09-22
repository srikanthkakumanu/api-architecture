package com.example.ecommerce.shipping.application;

import com.example.ecommerce.shipping.domain.Shipment;
import com.example.ecommerce.shipping.domain.ShipmentRepository;

public class ShipmentApplicationService {
    private final ShipmentRepository shipmentRepository;

    public ShipmentApplicationService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public void registerShipment(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    public void dispatchShipment(String shipmentId, String carrier, String trackingNumber) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found: " + shipmentId));

        shipment.assignCarrier(carrier, trackingNumber);
        shipmentRepository.save(shipment);
    }
}
