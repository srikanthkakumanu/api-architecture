package com.apidemo.mspatterns.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventJpaEntity, UUID> {
    List<OutboxEventJpaEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);
    long countByStatus(String status);
}
