package com.example.ecommerce.shipping.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ShipmentSpringDataRepository extends JpaRepository<ShipmentJpaEntity, String> {
    List<ShipmentJpaEntity> findAllBySourceOrderId(String sourceOrderId);
}
