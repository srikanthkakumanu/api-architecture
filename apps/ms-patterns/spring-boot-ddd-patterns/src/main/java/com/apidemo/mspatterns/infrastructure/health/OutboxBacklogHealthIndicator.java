package com.apidemo.mspatterns.infrastructure.health;

import com.apidemo.mspatterns.application.Ports;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class OutboxBacklogHealthIndicator implements HealthIndicator {
    private final Ports.OutboxRepository outbox;
    private final long threshold;

    public OutboxBacklogHealthIndicator(Ports.OutboxRepository outbox, @Value("${patterns.outbox.backlog-threshold}") long threshold) {
        this.outbox = outbox;
        this.threshold = threshold;
    }

    @Override
    public Health health() {
        var pending = outbox.pendingCount();
        var builder = pending <= threshold ? Health.up() : Health.down();
        return builder
                .withDetail("pendingOutboxEvents", pending)
                .withDetail("threshold", threshold)
                .build();
    }
}
