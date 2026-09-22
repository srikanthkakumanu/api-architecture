package com.apidemo.ddd.order;

import com.apidemo.ddd.payment.PaymentCompletedEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentCompletedHandlerTest {

    @Test
    void handleMarksTheOrderAsPaid() {
        OrderRepository orderRepository = new InMemoryOrderRepository();
        OrderId orderId = OrderId.generate();
        orderRepository.save(new Order(orderId));

        PaymentCompletedHandler handler = new PaymentCompletedHandler(orderRepository);
        handler.handle(new PaymentCompletedEvent(orderId));

        assertEquals(OrderStatus.PAID, orderRepository.findById(orderId).getStatus());
    }
}
