package com.apidemo.cqrs.query;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_read_models")
public class OrderReadModelEntity {
    @Id
    public UUID orderId;
    public String customerId;
    public String status;
    public String total;
    public int itemCount;
    public String lineSummary;
    public long version;
    public UUID lastEventId;
    public Instant updatedAt;
}
