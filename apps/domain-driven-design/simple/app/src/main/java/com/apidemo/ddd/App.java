package com.apidemo.ddd;

import com.apidemo.ddd.order.InMemoryOrderRepository;
import com.apidemo.ddd.order.Order;
import com.apidemo.ddd.order.OrderId;
import com.apidemo.ddd.order.OrderRepository;
import com.apidemo.ddd.order.PaymentCompletedHandler;
import com.apidemo.ddd.order.ProductId;
import com.apidemo.ddd.payment.InMemoryPaymentRepository;
import com.apidemo.ddd.payment.Payment;
import com.apidemo.ddd.payment.PaymentCompletedEvent;
import com.apidemo.ddd.payment.PaymentId;
import com.apidemo.ddd.payment.PaymentRepository;

/**
 * Runs the Step-By-Step Implementation Tutorial flow end to end:
 * Order created (PLACED) -> Payment initiated (PENDING) -> Payment completes,
 * emitting PaymentCompletedEvent -> Order context consumes the event and marks
 * the order PAID.
 */
public class App {

    public static void main(String[] args) {
        OrderRepository orderRepository = new InMemoryOrderRepository();
        PaymentRepository paymentRepository = new InMemoryPaymentRepository();
        PaymentCompletedHandler paymentCompletedHandler = new PaymentCompletedHandler(orderRepository);

        // 1. Order is created -> status = PLACED
        OrderId orderId = OrderId.generate();
        Order order = new Order(orderId);
        order.addItem(new ProductId("SKU-100"), 2);
        orderRepository.save(order);
        System.out.println("Order created:   " + order.getId().value() + " -> " + order.getStatus());

        // 2. Payment initiated -> status = PENDING
        Payment payment = new Payment(PaymentId.generate(), orderId);
        paymentRepository.save(payment);
        System.out.println("Payment created: " + payment.getId().value() + " -> " + payment.getStatus());

        // 3. Payment completes -> emits PaymentCompletedEvent
        payment.complete();
        paymentRepository.save(payment);
        System.out.println("Payment paid:    " + payment.getId().value() + " -> " + payment.getStatus());
        PaymentCompletedEvent event = new PaymentCompletedEvent(payment.getOrderId());

        // 4. Order context consumes event -> marks order as PAID
        paymentCompletedHandler.handle(event);
        Order paidOrder = orderRepository.findById(orderId);
        System.out.println("Order after event: " + paidOrder.getId().value() + " -> " + paidOrder.getStatus());
    }
}
