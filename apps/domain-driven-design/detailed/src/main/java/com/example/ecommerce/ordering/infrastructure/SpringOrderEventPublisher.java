package com.example.ecommerce.ordering.infrastructure;

import com.example.ecommerce.contracts.OrderPaidIntegrationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Adapter: publishes the Published Language contract onto Spring's
 * in-process application event bus (the "Event Channel / Broker" from
 * the end-to-end execution trace).
 */
@Component
public class SpringOrderEventPublisher implements OrderEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringOrderEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(OrderPaidIntegrationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
