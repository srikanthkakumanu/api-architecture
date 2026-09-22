package com.example.ecommerce.contracts;

import java.util.List;

/**
 * Published Language Contract (Integration Event DTO).
 * Shared across bounded contexts for asynchronous or synchronous messaging.
 */
public record OrderPaidIntegrationEvent(
        String orderId,
        String customerId,
        String rawAddress,
        List<ItemPayload> items
) {
    public record ItemPayload(String productId, int quantity) {}
}
