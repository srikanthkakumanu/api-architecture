package com.apidemo.cqrs.command;

import com.apidemo.cqrs.events.EventStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class OrderCommandService {
    private final OrderWriteRepository orders;
    private final EventStore eventStore;

    public OrderCommandService(OrderWriteRepository orders, EventStore eventStore) {
        this.orders = orders;
        this.eventStore = eventStore;
    }

    @Transactional
    public CommandResult createOrder(CreateOrderCommand command) {
        if (command.items().isEmpty()) {
            throw new IllegalArgumentException("At least one order line is required");
        }

        var currency = command.items().getFirst().currency();
        var now = Instant.now();
        var order = new OrderWriteEntity();
        order.id = UUID.randomUUID();
        order.customerId = command.customerId();
        order.status = OrderStatus.CREATED;
        order.currency = currency;
        order.createdAt = now;
        order.updatedAt = now;

        command.items().forEach(item -> {
            if (!currency.equals(item.currency())) {
                throw new IllegalArgumentException("All order lines must use the same currency");
            }
            var line = new OrderLineWriteEntity();
            line.id = UUID.randomUUID();
            line.sku = item.sku();
            line.quantity = item.quantity();
            line.unitPrice = item.unitPrice();
            line.order = order;
            order.lines.add(line);
        });

        order.totalAmount = total(order);
        orders.save(order);
        var event = eventStore.append("OrderCreated", order.id, EventStore.OrderSnapshot.from(order, 1));
        return new CommandResult(order.id, order.status, money(order), event.id);
    }

    @Transactional
    public CommandResult changeStatus(UUID orderId, OrderStatus status) {
        var order = orders.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.status == OrderStatus.CANCELLED && status != OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cancelled orders cannot move to another status");
        }
        order.status = status;
        order.updatedAt = Instant.now();
        orders.save(order);
        var event = eventStore.append("OrderStatusChanged", order.id, EventStore.OrderSnapshot.from(order, eventStore.nextVersion(order.id)));
        return new CommandResult(order.id, order.status, money(order), event.id);
    }

    private static BigDecimal total(OrderWriteEntity order) {
        return order.lines.stream()
                .map(line -> line.unitPrice.multiply(BigDecimal.valueOf(line.quantity)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static String money(OrderWriteEntity order) {
        return order.totalAmount + " " + order.currency;
    }

    public record CreateOrderCommand(String customerId, java.util.List<Item> items) {
        public record Item(String sku, int quantity, BigDecimal unitPrice, String currency) {
        }
    }
}
