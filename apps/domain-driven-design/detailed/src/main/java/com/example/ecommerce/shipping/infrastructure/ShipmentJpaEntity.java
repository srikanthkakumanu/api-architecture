package com.example.ecommerce.shipping.infrastructure;

import com.example.ecommerce.shipping.domain.DeliveryAddress;
import com.example.ecommerce.shipping.domain.PackageWeight;
import com.example.ecommerce.shipping.domain.Shipment;
import com.example.ecommerce.shipping.domain.ShipmentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA persistence model for the {@link Shipment} aggregate. The aggregate itself
 * carries zero framework imports; this entity exists purely so the
 * infrastructure layer can map it to and from relational storage.
 */
@Entity
@Table(name = "shipments")
class ShipmentJpaEntity {

    @Id
    private String shipmentId;
    private String sourceOrderId;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private double weightKg;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    private String carrierName;
    private String trackingNumber;

    protected ShipmentJpaEntity() {
        // required by JPA
    }

    static ShipmentJpaEntity fromDomain(Shipment shipment) {
        ShipmentJpaEntity entity = new ShipmentJpaEntity();
        entity.shipmentId = shipment.getShipmentId();
        entity.sourceOrderId = shipment.getSourceOrderId();
        entity.street = shipment.getDestination().street();
        entity.city = shipment.getDestination().city();
        entity.postalCode = shipment.getDestination().postalCode();
        entity.country = shipment.getDestination().country();
        entity.weightKg = shipment.getTotalWeight().kilograms();
        entity.status = shipment.getStatus();
        entity.carrierName = shipment.getCarrierName();
        entity.trackingNumber = shipment.getTrackingNumber();
        return entity;
    }

    Shipment toDomain() {
        DeliveryAddress destination = new DeliveryAddress(street, city, postalCode, country);
        PackageWeight totalWeight = new PackageWeight(weightKg);
        return Shipment.reconstruct(shipmentId, sourceOrderId, destination, totalWeight, status, carrierName, trackingNumber);
    }
}
