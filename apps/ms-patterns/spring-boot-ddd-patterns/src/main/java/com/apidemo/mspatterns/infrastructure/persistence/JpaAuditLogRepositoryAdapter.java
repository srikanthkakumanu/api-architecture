package com.apidemo.mspatterns.infrastructure.persistence;

import com.apidemo.mspatterns.application.Ports;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class JpaAuditLogRepositoryAdapter implements Ports.AuditLogRepository {
    private final AuditLogJpaRepository repository;

    public JpaAuditLogRepositoryAdapter(AuditLogJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void record(String correlationId, String actor, String action, String targetType, String targetId, String details) {
        var entity = new AuditLogJpaEntity();
        entity.id = UUID.randomUUID();
        entity.correlationId = correlationId;
        entity.actor = actor;
        entity.action = action;
        entity.targetType = targetType;
        entity.targetId = targetId;
        entity.details = details;
        entity.createdAt = Instant.now();
        repository.save(entity);
    }

    @Override
    public List<Ports.AuditLog> findByOrderId(UUID orderId) {
        return repository.findByTargetTypeAndTargetIdOrderByCreatedAtAsc("Order", orderId.toString()).stream()
                .map(entity -> new Ports.AuditLog(entity.id, entity.correlationId, entity.actor, entity.action, entity.targetType, entity.targetId, entity.details))
                .toList();
    }
}
