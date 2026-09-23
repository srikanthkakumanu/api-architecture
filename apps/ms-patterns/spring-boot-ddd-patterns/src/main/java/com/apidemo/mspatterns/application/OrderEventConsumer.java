package com.apidemo.mspatterns.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderEventConsumer {
    private final Ports.InboxRepository inbox;
    private final Ports.AuditLogRepository audit;

    public OrderEventConsumer(Ports.InboxRepository inbox, Ports.AuditLogRepository audit) {
        this.inbox = inbox;
        this.audit = audit;
    }

    @Transactional
    public boolean consume(Ports.OutboxMessage message) {
        if (inbox.alreadyConsumed(message.eventId())) {
            audit.record(message.correlationId(), "system", "DUPLICATE_MESSAGE_IGNORED", "Order", message.aggregateId().toString(), message.eventType());
            return false;
        }
        inbox.recordConsumed(message.eventId(), message.eventType(), message.correlationId());
        audit.record(message.correlationId(), "system", "MESSAGE_CONSUMED", "Order", message.aggregateId().toString(), message.eventType());
        return true;
    }
}
