package com.example.ecommerce.shipping.application;

import com.example.ecommerce.shipping.domain.DeliveryAddress;
import com.example.ecommerce.shipping.domain.PackageWeight;
import com.example.ecommerce.shipping.domain.Shipment;
import com.example.ecommerce.shipping.domain.ShipmentRepository;
import com.example.ecommerce.shipping.domain.ShipmentStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShipmentApplicationServiceTest {

    private static class InMemoryShipmentRepository implements ShipmentRepository {
        private final Map<String, Shipment> store = new HashMap<>();

        @Override
        public Optional<Shipment> findById(String shipmentId) {
            return Optional.ofNullable(store.get(shipmentId));
        }

        @Override
        public void save(Shipment shipment) {
            store.put(shipment.getShipmentId(), shipment);
        }
    }

    @Test
    void registerShipmentPersistsIt() {
        InMemoryShipmentRepository repository = new InMemoryShipmentRepository();
        ShipmentApplicationService service = new ShipmentApplicationService(repository);
        Shipment shipment = new Shipment("order-1",
                new DeliveryAddress("221B Baker Street", "London", "NW1", "UK"),
                new PackageWeight(1.5));

        service.registerShipment(shipment);

        assertEquals(ShipmentStatus.READY_FOR_PICKUP,
                repository.findById(shipment.getShipmentId()).orElseThrow().getStatus());
    }

    @Test
    void dispatchShipmentAssignsCarrierAndPersists() {
        InMemoryShipmentRepository repository = new InMemoryShipmentRepository();
        ShipmentApplicationService service = new ShipmentApplicationService(repository);
        Shipment shipment = new Shipment("order-1",
                new DeliveryAddress("221B Baker Street", "London", "NW1", "UK"),
                new PackageWeight(1.5));
        repository.save(shipment);

        service.dispatchShipment(shipment.getShipmentId(), "DHL", "TRACK-123");

        Shipment dispatched = repository.findById(shipment.getShipmentId()).orElseThrow();
        assertEquals(ShipmentStatus.DISPATCHED, dispatched.getStatus());
        assertEquals("DHL", dispatched.getCarrierName());
    }

    @Test
    void dispatchShipmentThrowsWhenShipmentIsMissing() {
        InMemoryShipmentRepository repository = new InMemoryShipmentRepository();
        ShipmentApplicationService service = new ShipmentApplicationService(repository);

        assertThrows(IllegalArgumentException.class,
                () -> service.dispatchShipment("missing", "DHL", "TRACK-123"));
    }
}
