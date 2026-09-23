package com.apidemo.cqrs.command;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderWriteRepository extends JpaRepository<OrderWriteEntity, UUID> {
}
