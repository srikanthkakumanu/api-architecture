package com.example.ecommerce.config;

import com.example.ecommerce.ordering.application.OrderApplicationService;
import com.example.ecommerce.ordering.domain.OrderRepository;
import com.example.ecommerce.ordering.infrastructure.OrderEventPublisher;
import com.example.ecommerce.shipping.application.ShipmentApplicationService;
import com.example.ecommerce.shipping.domain.ShipmentRepository;
import com.example.ecommerce.shipping.infrastructure.acl.OrderPaidEventListener;
import com.example.ecommerce.shipping.infrastructure.acl.OrderToShipmentTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wires the framework-free application-service and ACL classes (which take
 * their collaborators via plain constructor injection) as Spring beans.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public OrderApplicationService orderApplicationService(OrderRepository orderRepository,
                                                             OrderEventPublisher orderEventPublisher) {
        return new OrderApplicationService(orderRepository, orderEventPublisher);
    }

    @Bean
    public ShipmentApplicationService shipmentApplicationService(ShipmentRepository shipmentRepository) {
        return new ShipmentApplicationService(shipmentRepository);
    }

    @Bean
    public OrderToShipmentTranslator orderToShipmentTranslator() {
        return new OrderToShipmentTranslator();
    }

    @Bean
    public OrderPaidEventListener orderPaidEventListener(OrderToShipmentTranslator translator,
                                                           ShipmentApplicationService shipmentApplicationService) {
        return new OrderPaidEventListener(translator, shipmentApplicationService);
    }
}
