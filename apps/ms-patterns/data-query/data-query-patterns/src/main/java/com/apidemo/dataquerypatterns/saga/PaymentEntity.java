package com.apidemo.dataquerypatterns.saga;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    public UUID id;
    public UUID orderId;
    public String status;
    public BigDecimal amount;
    public String currency;
    public Instant createdAt;
}
