package com.example.ecommerce.ordering.application;

import com.example.ecommerce.contracts.OrderPaidIntegrationEvent;
import com.example.ecommerce.ordering.domain.Money;
import com.example.ecommerce.ordering.domain.Order;
import com.example.ecommerce.ordering.domain.OrderId;
import com.example.ecommerce.ordering.domain.OrderRepository;
import com.example.ecommerce.ordering.infrastructure.OrderEventPublisher;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderApplicationServiceTest {

    private static class InMemoryOrderRepository implements OrderRepository {
        private final Map<OrderId, Order> store = new HashMap<>();

        @Override
        public Optional<Order> findById(OrderId id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public void save(Order order) {
            store.put(order.getId(), order);
        }
    }

    private static class CapturingEventPublisher implements OrderEventPublisher {
        private OrderPaidIntegrationEvent published;

        @Override
        public void publish(OrderPaidIntegrationEvent event) {
            this.published = event;
        }
    }

    @Test
    void completePaymentMarksOrderPaidAndPublishesIntegrationEvent() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        CapturingEventPublisher publisher = new CapturingEventPublisher();
        OrderApplicationService service = new OrderApplicationService(repository, publisher);

        OrderId orderId = OrderId.generate();
        Order order = new Order(orderId, "customer-1", "221B Baker Street, London, NW1, UK");
        order.addItem("SKU-1", "Widget", new Money(new BigDecimal("10.00"), "USD"), 2);
        repository.save(order);

        service.completePayment(orderId, "TXN-9988");

        assertEquals(com.example.ecommerce.ordering.domain.OrderStatus.PAID,
                repository.findById(orderId).orElseThrow().getStatus());
        assertEquals(orderId.value().toString(), publisher.published.orderId());
        assertEquals("customer-1", publisher.published.customerId());
        assertEquals(1, publisher.published.items().size());
        assertEquals("SKU-1", publisher.published.items().get(0).productId());
        assertEquals(2, publisher.published.items().get(0).quantity());
    }

    @Test
    void completePaymentThrowsWhenOrderIsMissing() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        CapturingEventPublisher publisher = new CapturingEventPublisher();
        OrderApplicationService service = new OrderApplicationService(repository, publisher);

        assertThrows(IllegalArgumentException.class,
                () -> service.completePayment(OrderId.generate(), "TXN-1"));
    }
}
