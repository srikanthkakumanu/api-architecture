package com.apidemo.dataquerypatterns.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "account_events")
public class AccountEventEntity {
    @Id
    public UUID id;
    public UUID accountId;
    public long sequenceNumber;
    public String eventType;
    public BigDecimal amount;
    public Instant createdAt;
}
