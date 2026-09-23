package com.apidemo.dataquerypatterns.saga;

import com.apidemo.dataquerypatterns.orders.OrderEntity;
import com.apidemo.dataquerypatterns.orders.OrderRepository;
import com.apidemo.dataquerypatterns.orders.OrderStatus;
import com.apidemo.dataquerypatterns.orders.SagaStatus;
import com.apidemo.dataquerypatterns.outbox.OutboxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class OrderSagaService {
    private static final BigDecimal PAYMENT_DECLINE_THRESHOLD = new BigDecimal("500.00");

    private final OrderRepository orders;
    private final InventoryReservationRepository inventory;
    private final PaymentRepository payments;
    private final OutboxService outbox;

    public OrderSagaService(OrderRepository orders, InventoryReservationRepository inventory, PaymentRepository payments, OutboxService outbox) {
        this.orders = orders;
        this.inventory = inventory;
        this.payments = payments;
        this.outbox = outbox;
    }

    @Transactional
    public OrderEntity placeOrder(CreateOrderCommand command) {
        var now = Instant.now();
        var order = new OrderEntity();
        order.id = UUID.randomUUID();
        order.customerId = command.customerId();
        order.status = OrderStatus.PENDING;
        order.sagaStatus = SagaStatus.STARTED;
        order.totalAmount = command.totalAmount();
        order.currency = command.currency();
        order.createdAt = now;
        order.updatedAt = now;
        orders.save(order);
        append(order, "OrderCreated");
        return order;
    }

    @Transactional
    public OrderEntity advance(UUID orderId) {
        var order = orders.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.sagaStatus == SagaStatus.STARTED) {
            reserveInventory(order);
            order.sagaStatus = SagaStatus.INVENTORY_RESERVED;
            append(order, "InventoryReserved");
        }
        if (order.sagaStatus == SagaStatus.INVENTORY_RESERVED) {
            if (order.totalAmount.compareTo(PAYMENT_DECLINE_THRESHOLD) > 0) {
                compensate(order);
            } else {
                authorizePayment(order);
                order.sagaStatus = SagaStatus.PAYMENT_AUTHORIZED;
                append(order, "PaymentAuthorized");
            }
        }
        if (order.sagaStatus == SagaStatus.PAYMENT_AUTHORIZED) {
            order.status = OrderStatus.CONFIRMED;
            order.sagaStatus = SagaStatus.COMPLETED;
            order.updatedAt = Instant.now();
            append(order, "OrderConfirmed");
        }
        return orders.save(order);
    }

    private void reserveInventory(OrderEntity order) {
        var reservation = new InventoryReservationEntity();
        reservation.id = UUID.randomUUID();
        reservation.orderId = order.id;
        reservation.status = "RESERVED";
        reservation.createdAt = Instant.now();
        inventory.save(reservation);
    }

    private void authorizePayment(OrderEntity order) {
        var payment = new PaymentEntity();
        payment.id = UUID.randomUUID();
        payment.orderId = order.id;
        payment.status = "AUTHORIZED";
        payment.amount = order.totalAmount;
        payment.currency = order.currency;
        payment.createdAt = Instant.now();
        payments.save(payment);
    }

    private void compensate(OrderEntity order) {
        inventory.findByOrderId(order.id).forEach(reservation -> {
            reservation.status = "RELEASED";
            inventory.save(reservation);
        });
        order.status = OrderStatus.REJECTED;
        order.sagaStatus = SagaStatus.COMPENSATED;
        order.updatedAt = Instant.now();
        append(order, "OrderRejected");
    }

    private void append(OrderEntity order, String eventType) {
        outbox.append(order.id, eventType, new OutboxService.OrderEvent(
                order.id,
                order.customerId,
                order.status.name(),
                order.totalAmount.toPlainString(),
                order.currency
        ));
    }

    public record CreateOrderCommand(String customerId, BigDecimal totalAmount, String currency) {
    }
}
