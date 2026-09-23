package com.apidemo.mspatterns.application;

import com.apidemo.mspatterns.domain.Order;
import com.apidemo.mspatterns.domain.OrderEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class Ports {
    private Ports() {}

    public interface OrderRepository {
        void save(Order order);
        Optional<Order> findById(UUID id);
    }

    public interface OutboxRepository {
        void append(OrderEvent event, String payload);
        List<OutboxMessage> findPending(int limit);
        void markPublished(UUID eventId);
        long pendingCount();
    }

    public interface InboxRepository {
        boolean alreadyConsumed(UUID messageId);
        void recordConsumed(UUID messageId, String messageType, String correlationId);
    }

    public interface AuditLogRepository {
        void record(String correlationId, String actor, String action, String targetType, String targetId, String details);
        List<AuditLog> findByOrderId(UUID orderId);
    }

    public record OutboxMessage(UUID eventId, UUID aggregateId, String eventType, String payload, String correlationId) {}
    public record AuditLog(UUID id, String correlationId, String actor, String action, String targetType, String targetId, String details) {}
}
