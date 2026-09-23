package com.apidemo.cqrs.query;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderReadModelRepository extends JpaRepository<OrderReadModelEntity, UUID> {
    List<OrderReadModelEntity> findByCustomerIdOrderByUpdatedAtDesc(String customerId);

    List<OrderReadModelEntity> findByStatusOrderByUpdatedAtDesc(String status);
}
