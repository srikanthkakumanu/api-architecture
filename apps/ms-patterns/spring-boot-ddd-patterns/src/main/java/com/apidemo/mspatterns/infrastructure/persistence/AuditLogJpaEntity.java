package com.apidemo.mspatterns.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLogJpaEntity {
    @Id
    public UUID id;
    public String correlationId;
    public String actor;
    public String action;
    public String targetType;
    public String targetId;
    public String details;
    public Instant createdAt;
}
