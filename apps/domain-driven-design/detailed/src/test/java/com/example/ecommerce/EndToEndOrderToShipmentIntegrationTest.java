package com.example.ecommerce;

import com.example.ecommerce.ordering.application.OrderApplicationService;
import com.example.ecommerce.ordering.domain.Money;
import com.example.ecommerce.ordering.domain.Order;
import com.example.ecommerce.ordering.domain.OrderId;
import com.example.ecommerce.ordering.domain.OrderRepository;
import com.example.ecommerce.ordering.domain.OrderStatus;
import com.example.ecommerce.shipping.domain.Shipment;
import com.example.ecommerce.shipping.domain.ShipmentStatus;
import com.example.ecommerce.shipping.infrastructure.JpaShipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Exercises the full "Section 5: End-to-End Micro-Level Execution Trace" flow
 * with real Spring-managed, JPA-backed beans and Spring's synchronous
 * application event bus standing in for the "Event Channel / Broker":
 *
 * Client -> OrderApplicationService.completePayment(...)
 *        -> Order aggregate marks itself PAID and is persisted
 *        -> OrderPaidIntegrationEvent is published
 *        -> ACL (SpringOrderPaidEventListener -> OrderPaidEventListener) consumes it
 *        -> OrderToShipmentTranslator builds a native Shipment
 *        -> ShipmentApplicationService persists it as READY_FOR_PICKUP
 */
@SpringBootTest
@Transactional
class EndToEndOrderToShipmentIntegrationTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JpaShipmentRepository shipmentRepository;

    @Test
    void completingPaymentCreatesAMatchingShipmentThroughTheAcl() {
        OrderId orderId = OrderId.generate();
        Order order = new Order(orderId, "customer-42", "221B Baker Street, London, NW1, UK");
        order.addItem("SKU-1", "Widget", new Money(new BigDecimal("10.00"), "USD"), 2);
        orderRepository.save(order);

        orderApplicationService.completePayment(orderId, "TXN-9988");

        Order paidOrder = orderRepository.findById(orderId).orElseThrow();
        assertEquals(OrderStatus.PAID, paidOrder.getStatus());

        List<Shipment> shipmentsForOrder = shipmentRepository.findAllBySourceOrderId(orderId.value().toString());
        assertEquals(1, shipmentsForOrder.size(), "ACL should have created exactly one shipment for the paid order");

        Shipment shipment = shipmentsForOrder.get(0);
        assertEquals(ShipmentStatus.READY_FOR_PICKUP, shipment.getStatus());
        assertEquals("221B Baker Street", shipment.getDestination().street());
        assertEquals("London", shipment.getDestination().city());
        // 2 units * 0.75 kg per unit = 1.5 kg
        assertEquals(1.5, shipment.getTotalWeight().kilograms(), 0.0001);
    }
}
