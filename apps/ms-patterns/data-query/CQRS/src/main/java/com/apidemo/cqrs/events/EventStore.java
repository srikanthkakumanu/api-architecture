package com.apidemo.cqrs.events;

import com.apidemo.cqrs.command.OrderLineWriteEntity;
import com.apidemo.cqrs.command.OrderWriteEntity;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class EventStore {
    private final DomainEventRepository events;
    private final ObjectMapper objectMapper;

    public EventStore(DomainEventRepository events, ObjectMapper objectMapper) {
        this.events = events;
        this.objectMapper = objectMapper;
    }

    public DomainEventEntity append(String eventType, UUID aggregateId, OrderSnapshot snapshot) {
        var event = new DomainEventEntity();
        event.id = UUID.randomUUID();
        event.aggregateId = aggregateId;
        event.eventType = eventType;
        event.payload = toJson(snapshot);
        event.projected = false;
        event.createdAt = Instant.now();
        return events.save(event);
    }

    public long nextVersion(UUID aggregateId) {
        return events.countByAggregateId(aggregateId) + 1;
    }

    public OrderSnapshot readPayload(DomainEventEntity event) {
        try {
            return objectMapper.readValue(event.payload, OrderSnapshot.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to read event payload " + event.id, ex);
        }
    }

    private String toJson(OrderSnapshot snapshot) {
        try {
            return objectMapper.writeValueAsString(snapshot);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to serialize event payload", ex);
        }
    }

    public record OrderSnapshot(UUID orderId, String customerId, String status, String total, int itemCount, String lineSummary, long version) {
        public static OrderSnapshot from(OrderWriteEntity order, long version) {
            return new OrderSnapshot(
                    order.id,
                    order.customerId,
                    order.status.name(),
                    order.totalAmount + " " + order.currency,
                    order.lines.size(),
                    order.lines.stream().map(OrderSnapshot::summarize).collect(Collectors.joining(", ")),
                    version
            );
        }

        private static String summarize(OrderLineWriteEntity line) {
            return line.sku + " x" + line.quantity;
        }
    }
}
