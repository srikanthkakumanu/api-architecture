package com.example.ecommerce.ordering.infrastructure;

import com.example.ecommerce.ordering.domain.Order;
import com.example.ecommerce.ordering.domain.OrderId;
import com.example.ecommerce.ordering.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adapter: implements the domain-owned {@link OrderRepository} port using
 * Spring Data JPA, translating between the pure {@link Order} aggregate and
 * its {@link OrderJpaEntity} persistence shadow.
 */
@Repository
public class JpaOrderRepository implements OrderRepository {

    private final OrderSpringDataRepository springDataRepository;

    public JpaOrderRepository(OrderSpringDataRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return springDataRepository.findById(id.value().toString()).map(OrderJpaEntity::toDomain);
    }

    @Override
    public void save(Order order) {
        springDataRepository.save(OrderJpaEntity.fromDomain(order));
    }
}
