package com.apidemo.mspatterns.domain;

import java.time.Instant;
import java.util.UUID;

public record OrderEvent(
        UUID eventId,
        UUID orderId,
        String type,
        String correlationId,
        Instant occurredAt
) {
    public static OrderEvent created(UUID orderId, String correlationId) {
        return new OrderEvent(UUID.randomUUID(), orderId, "OrderCreated", correlationId, Instant.now());
    }

    public static OrderEvent statusChanged(UUID orderId, String correlationId) {
        return new OrderEvent(UUID.randomUUID(), orderId, "OrderStatusChanged", correlationId, Instant.now());
    }
}
