package com.apidemo.dataquerypatterns.materializedview;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customer_order_summaries")
public class CustomerOrderSummaryEntity {
    @Id
    public String customerId;
    public int confirmedOrderCount;
    public BigDecimal confirmedTotal;
    public String currency;
    public UUID lastOrderId;
    public Instant updatedAt;
}
