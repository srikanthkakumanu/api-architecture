package com.apidemo.mspatterns.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Order(
        UUID id,
        String customerId,
        OrderStatus status,
        List<OrderItem> items,
        Money total,
        Instant createdAt,
        Instant updatedAt
) {
    public Order {
        items = List.copyOf(items);
    }

    public static DomainResult<Order> create(String customerId, List<OrderItem> items) {
        if (customerId == null || customerId.isBlank()) {
            return DomainResult.fail("Customer id is required");
        }
        if (items == null || items.isEmpty()) {
            return DomainResult.fail("At least one item is required");
        }
        var total = items.stream()
                .map(OrderItem::lineTotal)
                .reduce(Money::add)
                .orElseThrow();
        var now = Instant.now();
        return DomainResult.ok(new Order(UUID.randomUUID(), customerId, OrderStatus.CREATED, items, total, now, now));
    }

    public DomainResult<Order> changeStatus(OrderStatus next) {
        if (next == null) {
            return DomainResult.fail("Status is required");
        }
        if (status == OrderStatus.CANCELLED && next != OrderStatus.CANCELLED) {
            return DomainResult.fail("Cancelled orders cannot move to another status");
        }
        if (status == OrderStatus.SHIPPED && next != OrderStatus.SHIPPED) {
            return DomainResult.fail("Shipped orders cannot move to another status");
        }
        return DomainResult.ok(new Order(id, customerId, next, items, total, createdAt, Instant.now()));
    }

    public int itemCount() {
        return items.stream().mapToInt(OrderItem::quantity).sum();
    }
}
