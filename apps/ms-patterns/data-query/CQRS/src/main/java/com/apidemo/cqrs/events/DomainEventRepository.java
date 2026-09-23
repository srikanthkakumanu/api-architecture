package com.apidemo.cqrs.events;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DomainEventRepository extends JpaRepository<DomainEventEntity, UUID> {
    List<DomainEventEntity> findTop100ByProjectedFalseOrderByCreatedAtAsc();

    long countByAggregateId(UUID aggregateId);
}
