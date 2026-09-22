package com.example.ecommerce.shipping.infrastructure.acl;

import com.example.ecommerce.contracts.OrderPaidIntegrationEvent;
import com.example.ecommerce.contracts.OrderPaidIntegrationEvent.ItemPayload;
import com.example.ecommerce.shipping.application.ShipmentApplicationService;
import com.example.ecommerce.shipping.domain.Shipment;
import com.example.ecommerce.shipping.domain.ShipmentRepository;
import com.example.ecommerce.shipping.domain.ShipmentStatus;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderPaidEventListenerTest {

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
    void onOrderPaidTranslatesAndRegistersAShipment() {
        InMemoryShipmentRepository repository = new InMemoryShipmentRepository();
        ShipmentApplicationService shipmentApplicationService = new ShipmentApplicationService(repository);
        OrderPaidEventListener listener = new OrderPaidEventListener(new OrderToShipmentTranslator(), shipmentApplicationService);

        OrderPaidIntegrationEvent event = new OrderPaidIntegrationEvent(
                "order-1", "customer-1", "221B Baker Street, London, NW1, UK",
                List.of(new ItemPayload("SKU-1", 2), new ItemPayload("SKU-2", 1)));

        listener.onOrderPaid(event);

        assertEquals(1, repository.store.size());
        Shipment shipment = repository.store.values().iterator().next();
        assertEquals("order-1", shipment.getSourceOrderId());
        assertEquals(ShipmentStatus.READY_FOR_PICKUP, shipment.getStatus());
        // (2 + 1) units * 0.75 kg per unit = 2.25 kg
        assertEquals(2.25, shipment.getTotalWeight().kilograms(), 0.0001);
    }
}
