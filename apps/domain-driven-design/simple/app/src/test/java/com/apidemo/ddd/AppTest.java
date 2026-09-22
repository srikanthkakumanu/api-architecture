package com.apidemo.ddd;

import com.apidemo.ddd.order.InMemoryOrderRepository;
import com.apidemo.ddd.order.Order;
import com.apidemo.ddd.order.OrderId;
import com.apidemo.ddd.order.OrderRepository;
import com.apidemo.ddd.order.OrderStatus;
import com.apidemo.ddd.order.PaymentCompletedHandler;
import com.apidemo.ddd.order.ProductId;
import com.apidemo.ddd.payment.InMemoryPaymentRepository;
import com.apidemo.ddd.payment.Payment;
import com.apidemo.ddd.payment.PaymentCompletedEvent;
import com.apidemo.ddd.payment.PaymentId;
import com.apidemo.ddd.payment.PaymentRepository;
import com.apidemo.ddd.payment.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Exercises the "Step 5: Putting It Together" flow from the DDD tutorial:
 * Order created (PLACED) -> Payment initiated (PENDING) -> Payment completes
 * (emits PaymentCompletedEvent) -> Order context consumes the event (PAID).
 */
class AppTest {

    @Test
    void orderIsMarkedPaidAfterPaymentCompletesAndEventIsHandled() {
        OrderRepository orderRepository = new InMemoryOrderRepository();
        PaymentRepository paymentRepository = new InMemoryPaymentRepository();
        PaymentCompletedHandler paymentCompletedHandler = new PaymentCompletedHandler(orderRepository);

        OrderId orderId = OrderId.generate();
        Order order = new Order(orderId);
        order.addItem(new ProductId("SKU-100"), 2);
        orderRepository.save(order);
        assertEquals(OrderStatus.PLACED, orderRepository.findById(orderId).getStatus());

        Payment payment = new Payment(PaymentId.generate(), orderId);
        paymentRepository.save(payment);
        assertEquals(PaymentStatus.PENDING, payment.getStatus());

        payment.complete();
        paymentRepository.save(payment);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());

        paymentCompletedHandler.handle(new PaymentCompletedEvent(payment.getOrderId()));

        assertEquals(OrderStatus.PAID, orderRepository.findById(orderId).getStatus());
    }
}
