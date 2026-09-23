package com.apidemo.dataquerypatterns.outbox;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_messages")
public class OutboxMessageEntity {
    @Id
    public UUID id;
    public UUID aggregateId;
    public String eventType;
    @Lob
    public String payload;
    public String status;
    public Instant createdAt;
    public Instant publishedAt;
}
