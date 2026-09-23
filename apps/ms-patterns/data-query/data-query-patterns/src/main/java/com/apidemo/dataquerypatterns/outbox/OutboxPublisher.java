package com.apidemo.dataquerypatterns.outbox;

import com.apidemo.dataquerypatterns.inbox.InboxService;
import com.apidemo.dataquerypatterns.materializedview.CustomerSummaryProjector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class OutboxPublisher {
    private static final String SUMMARY_CONSUMER = "customer-summary-projector";

    private final OutboxMessageRepository outbox;
    private final OutboxService outboxService;
    private final InboxService inbox;
    private final CustomerSummaryProjector projector;

    public OutboxPublisher(OutboxMessageRepository outbox, OutboxService outboxService, InboxService inbox, CustomerSummaryProjector projector) {
        this.outbox = outbox;
        this.outboxService = outboxService;
        this.inbox = inbox;
        this.projector = projector;
    }

    @Transactional
    public int publishPending() {
        var pending = outbox.findTop100ByStatusOrderByCreatedAtAsc("PENDING");
        pending.forEach(this::publish);
        return pending.size();
    }

    @Transactional
    public boolean replay(UUID messageId) {
        return outbox.findById(messageId).map(this::consume).orElseThrow(() -> new IllegalArgumentException("Outbox message not found"));
    }

    private void publish(OutboxMessageEntity message) {
        consume(message);
        message.status = "PUBLISHED";
        message.publishedAt = Instant.now();
        outbox.save(message);
    }

    private boolean consume(OutboxMessageEntity message) {
        if (!inbox.recordIfFirst(message.id, SUMMARY_CONSUMER, message.eventType)) {
            return false;
        }
        projector.apply(message.id, message.eventType, outboxService.readPayload(message));
        return true;
    }
}
