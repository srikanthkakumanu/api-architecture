package com.example.ecommerce.shipping.infrastructure.acl;

import com.example.ecommerce.contracts.OrderPaidIntegrationEvent;
import com.example.ecommerce.contracts.OrderPaidIntegrationEvent.ItemPayload;
import com.example.ecommerce.shipping.domain.Shipment;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderToShipmentTranslatorTest {

    private final OrderToShipmentTranslator translator = new OrderToShipmentTranslator();

    @Test
    void translatesACommaSeparatedRawAddress() {
        OrderPaidIntegrationEvent event = new OrderPaidIntegrationEvent(
                "order-1", "customer-1", "221B Baker Street, London, NW1, UK", List.of());

        Shipment shipment = translator.translateToShipment(event, 5.0);

        assertEquals("221B Baker Street", shipment.getDestination().street());
        assertEquals("London", shipment.getDestination().city());
        assertEquals("NW1", shipment.getDestination().postalCode());
        assertEquals("UK", shipment.getDestination().country());
        assertEquals("order-1", shipment.getSourceOrderId());
        assertEquals(5.0, shipment.getTotalWeight().kilograms());
    }

    @Test
    void fallsBackToDefaultsForABlankRawAddress() {
        OrderPaidIntegrationEvent event = new OrderPaidIntegrationEvent(
                "order-1", "customer-1", "  ", List.of());

        Shipment shipment = translator.translateToShipment(event, 5.0);

        assertEquals("Unknown", shipment.getDestination().street());
        assertEquals("Unknown", shipment.getDestination().city());
        assertEquals("00000", shipment.getDestination().postalCode());
        assertEquals("US", shipment.getDestination().country());
    }

    @Test
    void rejectsAWeightAboveTheCarrierLimit() {
        OrderPaidIntegrationEvent event = new OrderPaidIntegrationEvent(
                "order-1", "customer-1", "221B Baker Street, London, NW1, UK",
                List.of(new ItemPayload("SKU-1", 1)));

        assertThrows(IllegalArgumentException.class, () -> translator.translateToShipment(event, 60.0));
    }
}
