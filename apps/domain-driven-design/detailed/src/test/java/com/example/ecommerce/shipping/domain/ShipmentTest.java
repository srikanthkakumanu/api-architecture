package com.example.ecommerce.shipping.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShipmentTest {

    private static final DeliveryAddress DESTINATION = new DeliveryAddress("221B Baker Street", "London", "NW1", "UK");
    private static final PackageWeight WEIGHT = new PackageWeight(1.5);

    @Test
    void newShipmentStartsReadyForPickup() {
        Shipment shipment = new Shipment("order-1", DESTINATION, WEIGHT);

        assertEquals(ShipmentStatus.READY_FOR_PICKUP, shipment.getStatus());
    }

    @Test
    void assignCarrierDispatchesTheShipment() {
        Shipment shipment = new Shipment("order-1", DESTINATION, WEIGHT);

        shipment.assignCarrier("DHL", "TRACK-123");

        assertEquals(ShipmentStatus.DISPATCHED, shipment.getStatus());
        assertEquals("DHL", shipment.getCarrierName());
        assertEquals("TRACK-123", shipment.getTrackingNumber());
    }

    @Test
    void markDeliveredRequiresDispatchFirst() {
        Shipment shipment = new Shipment("order-1", DESTINATION, WEIGHT);

        assertThrows(IllegalStateException.class, shipment::markDelivered);
    }

    @Test
    void markDeliveredTransitionsFromDispatched() {
        Shipment shipment = new Shipment("order-1", DESTINATION, WEIGHT);
        shipment.assignCarrier("DHL", "TRACK-123");

        shipment.markDelivered();

        assertEquals(ShipmentStatus.DELIVERED, shipment.getStatus());
    }

    @Test
    void assignCarrierThrowsOnceDelivered() {
        Shipment shipment = new Shipment("order-1", DESTINATION, WEIGHT);
        shipment.assignCarrier("DHL", "TRACK-123");
        shipment.markDelivered();

        assertThrows(IllegalStateException.class, () -> shipment.assignCarrier("UPS", "TRACK-456"));
    }

    @Test
    void reconstructRestoresAggregateStateWithoutReplayingBusinessMethods() {
        Shipment restored = Shipment.reconstruct("shipment-1", "order-1", DESTINATION, WEIGHT,
                ShipmentStatus.DISPATCHED, "DHL", "TRACK-123");

        assertEquals("shipment-1", restored.getShipmentId());
        assertEquals(ShipmentStatus.DISPATCHED, restored.getStatus());
        assertEquals("DHL", restored.getCarrierName());
    }
}
