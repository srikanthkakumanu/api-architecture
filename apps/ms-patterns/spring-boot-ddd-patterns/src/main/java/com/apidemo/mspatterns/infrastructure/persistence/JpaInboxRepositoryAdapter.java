package com.apidemo.mspatterns.infrastructure.persistence;

import com.apidemo.mspatterns.application.Ports;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public class JpaInboxRepositoryAdapter implements Ports.InboxRepository {
    private final InboxMessageJpaRepository repository;

    public JpaInboxRepositoryAdapter(InboxMessageJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean alreadyConsumed(UUID messageId) {
        return repository.existsById(messageId);
    }

    @Override
    public void recordConsumed(UUID messageId, String messageType, String correlationId) {
        var entity = new InboxMessageJpaEntity();
        entity.messageId = messageId;
        entity.messageType = messageType;
        entity.correlationId = correlationId;
        entity.status = "CONSUMED";
        entity.receivedAt = Instant.now();
        repository.save(entity);
    }
}
