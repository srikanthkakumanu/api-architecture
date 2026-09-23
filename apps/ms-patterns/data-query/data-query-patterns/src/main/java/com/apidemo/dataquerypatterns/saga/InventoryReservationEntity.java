package com.apidemo.dataquerypatterns.saga;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory_reservations")
public class InventoryReservationEntity {
    @Id
    public UUID id;
    public UUID orderId;
    public String status;
    public Instant createdAt;
}
