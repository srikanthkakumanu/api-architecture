package com.apidemo.mspatterns.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxPublisher {
    private final Ports.OutboxRepository outbox;
    private final OrderEventConsumer consumer;
    private final Ports.AuditLogRepository audit;

    public OutboxPublisher(Ports.OutboxRepository outbox, OrderEventConsumer consumer, Ports.AuditLogRepository audit) {
        this.outbox = outbox;
        this.consumer = consumer;
        this.audit = audit;
    }

    @Transactional
    public int publishPending() {
        var messages = outbox.findPending(50);
        messages.forEach(message -> {
            consumer.consume(message);
            outbox.markPublished(message.eventId());
            audit.record(message.correlationId(), "system", "OUTBOX_PUBLISHED", "Order", message.aggregateId().toString(), message.eventType());
        });
        return messages.size();
    }
}
