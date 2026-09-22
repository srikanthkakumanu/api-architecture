package com.example.ecommerce.shipping.infrastructure.acl;

import com.example.ecommerce.contracts.OrderPaidIntegrationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Wires the framework-free {@link OrderPaidEventListener} onto Spring's
 * application event bus. Kept separate so the ACL listener itself stays
 * unit-testable without a Spring context.
 */
@Component
public class SpringOrderPaidEventListener {

    private final OrderPaidEventListener delegate;

    public SpringOrderPaidEventListener(OrderPaidEventListener delegate) {
        this.delegate = delegate;
    }

    @EventListener
    public void onOrderPaid(OrderPaidIntegrationEvent event) {
        delegate.onOrderPaid(event);
    }
}
