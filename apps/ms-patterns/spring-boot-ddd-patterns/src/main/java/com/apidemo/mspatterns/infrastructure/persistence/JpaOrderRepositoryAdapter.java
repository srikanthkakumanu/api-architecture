package com.apidemo.mspatterns.infrastructure.persistence;

import com.apidemo.mspatterns.application.Ports;
import com.apidemo.mspatterns.domain.Order;
import com.apidemo.mspatterns.infrastructure.mapper.OrderJpaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaOrderRepositoryAdapter implements Ports.OrderRepository {
    private final OrderJpaRepository repository;
    private final OrderJpaMapper mapper;

    public JpaOrderRepositoryAdapter(OrderJpaRepository repository, OrderJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(Order order) {
        var entity = mapper.toEntity(order);
        entity.items.forEach(item -> item.order = entity);
        repository.save(entity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}
