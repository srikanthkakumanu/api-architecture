package com.apidemo.cqrs.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderQueryService {
    private final OrderReadModelRepository readModels;

    public OrderQueryService(OrderReadModelRepository readModels) {
        this.readModels = readModels;
    }

    @Transactional(readOnly = true)
    public OrderReadModelEntity get(UUID orderId) {
        return readModels.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order read model not found"));
    }

    @Transactional(readOnly = true)
    public List<OrderReadModelEntity> search(String customerId, String status) {
        if (customerId != null && !customerId.isBlank()) {
            return readModels.findByCustomerIdOrderByUpdatedAtDesc(customerId);
        }
        if (status != null && !status.isBlank()) {
            return readModels.findByStatusOrderByUpdatedAtDesc(status);
        }
        return readModels.findAll();
    }
}
