package com.example.ecommerce.ordering.application;

import com.example.ecommerce.contracts.OrderPaidIntegrationEvent;
import com.example.ecommerce.contracts.OrderPaidIntegrationEvent.ItemPayload;
import com.example.ecommerce.ordering.domain.*;
import com.example.ecommerce.ordering.infrastructure.OrderEventPublisher;

import java.util.List;

public class OrderApplicationService {
    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    public OrderApplicationService(OrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public void completePayment(OrderId orderId, String transactionId) {
        // 1. Fetch domain aggregate
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId.value()));

        // 2. Execute business rule on aggregate root
        order.markAsPaid(transactionId);

        // 3. Persist updated aggregate state
        orderRepository.save(order);

        // 4. Translate domain state into Published Language Integration Event
        List<ItemPayload> itemPayloads = order.getItems().stream()
                .map(item -> new ItemPayload(item.productId(), item.quantity()))
                .toList();

        OrderPaidIntegrationEvent event = new OrderPaidIntegrationEvent(
                order.getId().value().toString(),
                order.getCustomerId(),
                order.getShippingAddress(),
                itemPayloads
        );

        // 5. Publish to cross-context integration event channel
        eventPublisher.publish(event);
    }
}
