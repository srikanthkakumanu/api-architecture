package com.example.ecommerce.ordering.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

interface OrderSpringDataRepository extends JpaRepository<OrderJpaEntity, String> {
}
