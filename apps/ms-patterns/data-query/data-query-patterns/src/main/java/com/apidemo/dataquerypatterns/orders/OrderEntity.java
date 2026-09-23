package com.apidemo.dataquerypatterns.orders;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    public UUID id;
    public String customerId;
    @Enumerated(EnumType.STRING)
    public OrderStatus status;
    @Enumerated(EnumType.STRING)
    public SagaStatus sagaStatus;
    public BigDecimal totalAmount;
    public String currency;
    public Instant createdAt;
    public Instant updatedAt;
}
