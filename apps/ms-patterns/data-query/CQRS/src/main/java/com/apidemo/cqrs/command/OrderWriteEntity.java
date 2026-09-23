package com.apidemo.cqrs.command;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderWriteEntity {
    @Id
    public UUID id;
    public String customerId;
    @Enumerated(EnumType.STRING)
    public OrderStatus status;
    public BigDecimal totalAmount;
    public String currency;
    public Instant createdAt;
    public Instant updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<OrderLineWriteEntity> lines = new ArrayList<>();
}
