package com.apidemo.mspatterns.infrastructure.persistence;

import com.apidemo.mspatterns.application.Ports;
import com.apidemo.mspatterns.domain.OrderEvent;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class JpaOutboxRepositoryAdapter implements Ports.OutboxRepository {
    private final OutboxEventJpaRepository repository;

    public JpaOutboxRepositoryAdapter(OutboxEventJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void append(OrderEvent event, String payload) {
        var entity = new OutboxEventJpaEntity();
        entity.id = event.eventId();
        entity.aggregateId = event.orderId();
        entity.eventType = event.type();
        entity.payload = payload;
        entity.correlationId = event.correlationId();
        entity.status = "PENDING";
        entity.createdAt = event.occurredAt();
        repository.save(entity);
    }

    @Override
    public List<Ports.OutboxMessage> findPending(int limit) {
        return repository.findTop50ByStatusOrderByCreatedAtAsc("PENDING").stream()
                .limit(limit)
                .map(entity -> new Ports.OutboxMessage(entity.id, entity.aggregateId, entity.eventType, entity.payload, entity.correlationId))
                .toList();
    }

    @Override
    public void markPublished(UUID eventId) {
        var entity = repository.findById(eventId).orElseThrow();
        entity.status = "PUBLISHED";
        entity.publishedAt = Instant.now();
        repository.save(entity);
    }

    @Override
    public long pendingCount() {
        return repository.countByStatus("PENDING");
    }
}
