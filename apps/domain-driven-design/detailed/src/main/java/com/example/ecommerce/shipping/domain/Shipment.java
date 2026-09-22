package com.example.ecommerce.shipping.domain;

import java.util.UUID;

/**
 * Aggregate Root: Shipment.
 * Models physical package dispatch and tracking.
 */
public class Shipment {
    private final String shipmentId;
    private final String sourceOrderId; // Referenced BY ID ONLY (No direct object reference to Order)
    private final DeliveryAddress destination;
    private final PackageWeight totalWeight;
    private ShipmentStatus status;
    private String carrierName;
    private String trackingNumber;

    public Shipment(String sourceOrderId, DeliveryAddress destination, PackageWeight totalWeight) {
        this(UUID.randomUUID().toString(), sourceOrderId, destination, totalWeight,
                ShipmentStatus.READY_FOR_PICKUP, null, null);
    }

    private Shipment(String shipmentId, String sourceOrderId, DeliveryAddress destination, PackageWeight totalWeight,
                      ShipmentStatus status, String carrierName, String trackingNumber) {
        if (sourceOrderId == null || sourceOrderId.isBlank()) {
            throw new IllegalArgumentException("Source order ID is mandatory");
        }
        this.shipmentId = shipmentId;
        this.sourceOrderId = sourceOrderId;
        this.destination = destination;
        this.totalWeight = totalWeight;
        this.status = status;
        this.carrierName = carrierName;
        this.trackingNumber = trackingNumber;
    }

    // Business Method: Carrier assignment
    public void assignCarrier(String carrier, String trackingNumber) {
        if (this.status == ShipmentStatus.DELIVERED) {
            throw new IllegalStateException("Cannot assign carrier to an already delivered shipment");
        }
        if (carrier == null || carrier.isBlank()) {
            throw new IllegalArgumentException("Carrier name cannot be blank");
        }
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("Tracking number cannot be blank");
        }
        this.carrierName = carrier;
        this.trackingNumber = trackingNumber;
        this.status = ShipmentStatus.DISPATCHED;
    }

    public void markDelivered() {
        if (this.status != ShipmentStatus.DISPATCHED) {
            throw new IllegalStateException("Shipment must be dispatched before it can be delivered");
        }
        this.status = ShipmentStatus.DELIVERED;
    }

    public String getShipmentId() { return shipmentId; }
    public String getSourceOrderId() { return sourceOrderId; }
    public DeliveryAddress getDestination() { return destination; }
    public PackageWeight getTotalWeight() { return totalWeight; }
    public ShipmentStatus getStatus() { return status; }
    public String getCarrierName() { return carrierName; }
    public String getTrackingNumber() { return trackingNumber; }

    /**
     * Persistence rehydration factory. Restores an aggregate exactly as previously
     * persisted (including its generated ID, status, carrier and tracking number)
     * without replaying the business methods above. Intended for use by
     * infrastructure-layer repository adapters only.
     */
    public static Shipment reconstruct(String shipmentId, String sourceOrderId, DeliveryAddress destination,
                                        PackageWeight totalWeight, ShipmentStatus status,
                                        String carrierName, String trackingNumber) {
        return new Shipment(shipmentId, sourceOrderId, destination, totalWeight, status, carrierName, trackingNumber);
    }
}
