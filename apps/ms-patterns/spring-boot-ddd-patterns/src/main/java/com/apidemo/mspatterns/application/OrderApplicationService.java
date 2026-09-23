package com.apidemo.mspatterns.application;

import com.apidemo.mspatterns.domain.Money;
import com.apidemo.mspatterns.domain.Order;
import com.apidemo.mspatterns.domain.OrderEvent;
import com.apidemo.mspatterns.domain.OrderItem;
import com.apidemo.mspatterns.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderApplicationService {
    private final Ports.OrderRepository orders;
    private final Ports.OutboxRepository outbox;
    private final Ports.AuditLogRepository audit;

    public OrderApplicationService(Ports.OrderRepository orders, Ports.OutboxRepository outbox, Ports.AuditLogRepository audit) {
        this.orders = orders;
        this.outbox = outbox;
        this.audit = audit;
    }

    @Transactional
    public Order create(CreateOrderCommand command, String correlationId) {
        var items = command.items().stream()
                .map(item -> new OrderItem(UUID.randomUUID(), item.sku(), item.quantity(), new Money(item.unitPrice(), item.currency())))
                .toList();
        var order = Order.create(command.customerId(), items).orThrow();
        var event = OrderEvent.created(order.id(), correlationId);
        orders.save(order);
        outbox.append(event, event.type());
        audit.record(correlationId, command.actor(), "ORDER_CREATED", "Order", order.id().toString(), "Order created with " + order.itemCount() + " items");
        return order;
    }

    @Transactional
    public Order changeStatus(UUID orderId, OrderStatus status, String actor, String correlationId) {
        var current = orders.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        var changed = current.changeStatus(status).orThrow();
        var event = OrderEvent.statusChanged(changed.id(), correlationId);
        orders.save(changed);
        outbox.append(event, event.type());
        audit.record(correlationId, actor, "ORDER_STATUS_CHANGED", "Order", changed.id().toString(), "Status changed to " + status);
        return changed;
    }

    public Order get(UUID orderId) {
        return orders.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}
