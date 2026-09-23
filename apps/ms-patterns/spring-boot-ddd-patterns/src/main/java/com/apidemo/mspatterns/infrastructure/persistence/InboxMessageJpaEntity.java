package com.apidemo.mspatterns.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inbox_messages")
public class InboxMessageJpaEntity {
    @Id
    public UUID messageId;
    public String messageType;
    public String correlationId;
    public String status;
    public Instant receivedAt;
}
