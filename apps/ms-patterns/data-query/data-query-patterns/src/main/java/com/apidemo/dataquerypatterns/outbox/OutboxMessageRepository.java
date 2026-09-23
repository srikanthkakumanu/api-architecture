package com.apidemo.dataquerypatterns.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessageEntity, UUID> {
    List<OutboxMessageEntity> findTop100ByStatusOrderByCreatedAtAsc(String status);

    long countByStatus(String status);
}
