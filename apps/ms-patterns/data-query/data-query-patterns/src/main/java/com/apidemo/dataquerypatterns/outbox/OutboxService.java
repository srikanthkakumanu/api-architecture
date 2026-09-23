package com.apidemo.dataquerypatterns.outbox;

import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Service
public class OutboxService {
    private final OutboxMessageRepository outbox;
    private final ObjectMapper objectMapper;

    public OutboxService(OutboxMessageRepository outbox, ObjectMapper objectMapper) {
        this.outbox = outbox;
        this.objectMapper = objectMapper;
    }

    public OutboxMessageEntity append(UUID aggregateId, String eventType, Object payload) {
        var message = new OutboxMessageEntity();
        message.id = UUID.randomUUID();
        message.aggregateId = aggregateId;
        message.eventType = eventType;
        message.payload = toJson(payload);
        message.status = "PENDING";
        message.createdAt = Instant.now();
        return outbox.save(message);
    }

    public OrderEvent readPayload(OutboxMessageEntity message) {
        try {
            return objectMapper.readValue(message.payload, OrderEvent.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to read outbox payload " + message.id, ex);
        }
    }

    private String toJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to serialize outbox payload", ex);
        }
    }

    public record OrderEvent(UUID orderId, String customerId, String status, String total, String currency) {
    }
}
