package com.example.ecommerce.shipping.infrastructure.acl;

import com.example.ecommerce.contracts.OrderPaidIntegrationEvent;
import com.example.ecommerce.shipping.domain.DeliveryAddress;
import com.example.ecommerce.shipping.domain.PackageWeight;
import com.example.ecommerce.shipping.domain.Shipment;

/**
 * ACL Translator: Converts external Published Language DTOs into pure Shipping Domain Models.
 */
public class OrderToShipmentTranslator {

    public Shipment translateToShipment(OrderPaidIntegrationEvent event, double calculatedWeightKg) {
        // 1. Translate and sanitize unformatted upstream address into a DeliveryAddress Value Object
        DeliveryAddress address = parseAddress(event.rawAddress());

        // 2. Wrap primitive weight into the PackageWeight Value Object (enforces <= 50kg rule)
        PackageWeight weight = new PackageWeight(calculatedWeightKg);

        // 3. Construct native Shipment Aggregate Root
        return new Shipment(event.orderId(), address, weight);
    }

    private DeliveryAddress parseAddress(String rawAddress) {
        if (rawAddress == null || rawAddress.isBlank()) {
            return new DeliveryAddress("Unknown", "Unknown", "00000", "US");
        }

        // Example parsing strategy for comma-separated addresses
        String[] parts = rawAddress.split(",");
        String street = parts.length > 0 ? parts[0].trim() : "Default St";
        String city = parts.length > 1 ? parts[1].trim() : "Default City";
        String zip = parts.length > 2 ? parts[2].trim() : "00000";
        String country = parts.length > 3 ? parts[3].trim() : "US";

        return new DeliveryAddress(street, city, zip, country);
    }
}
