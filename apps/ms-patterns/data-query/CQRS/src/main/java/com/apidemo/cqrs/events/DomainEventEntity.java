package com.apidemo.cqrs.events;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "domain_events")
public class DomainEventEntity {
    @Id
    public UUID id;
    public UUID aggregateId;
    public String eventType;
    @Lob
    public String payload;
    public boolean projected;
    public Instant createdAt;
    public Instant projectedAt;
}
