package com.example.ecommerce.shipping.infrastructure.acl;

import com.example.ecommerce.contracts.OrderPaidIntegrationEvent;
import com.example.ecommerce.shipping.application.ShipmentApplicationService;
import com.example.ecommerce.shipping.domain.Shipment;

/**
 * ACL Adapter / Listener: Consumes upstream events and invokes translator and application service.
 */
public class OrderPaidEventListener {
    private final OrderToShipmentTranslator translator;
    private final ShipmentApplicationService shipmentService;

    public OrderPaidEventListener(OrderToShipmentTranslator translator, ShipmentApplicationService shipmentService) {
        this.translator = translator;
        this.shipmentService = shipmentService;
    }

    public void onOrderPaid(OrderPaidIntegrationEvent event) {
        // Determine physical item weights (e.g., querying local warehouse inventory cache)
        double totalWeightKg = event.items().stream()
                .mapToDouble(item -> item.quantity() * 0.75) // 0.75 kg per unit
                .sum();

        // Pass through Anti-Corruption Layer Translator
        Shipment shipment = translator.translateToShipment(event, totalWeightKg);

        // Dispatch to domain application service
        shipmentService.registerShipment(shipment);
    }
}
