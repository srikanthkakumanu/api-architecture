package com.example.ecommerce.ordering.infrastructure;

import com.example.ecommerce.contracts.OrderPaidIntegrationEvent;

/**
 * Outbound port for publishing Published Language integration events
 * to whatever channel or broker the Ordering context is wired to.
 */
public interface OrderEventPublisher {
    void publish(OrderPaidIntegrationEvent event);
}
