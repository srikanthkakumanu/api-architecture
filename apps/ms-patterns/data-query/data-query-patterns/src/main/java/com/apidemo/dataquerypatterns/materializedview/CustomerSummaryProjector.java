package com.apidemo.dataquerypatterns.materializedview;

import com.apidemo.dataquerypatterns.outbox.OutboxService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class CustomerSummaryProjector {
    private final CustomerOrderSummaryRepository summaries;

    public CustomerSummaryProjector(CustomerOrderSummaryRepository summaries) {
        this.summaries = summaries;
    }

    public void apply(UUID messageId, String eventType, OutboxService.OrderEvent event) {
        if (!"OrderConfirmed".equals(eventType)) {
            return;
        }
        var summary = summaries.findById(event.customerId()).orElseGet(() -> {
            var created = new CustomerOrderSummaryEntity();
            created.customerId = event.customerId();
            created.confirmedOrderCount = 0;
            created.confirmedTotal = BigDecimal.ZERO;
            created.currency = event.currency();
            return created;
        });
        summary.confirmedOrderCount++;
        summary.confirmedTotal = summary.confirmedTotal.add(new BigDecimal(event.total()));
        summary.lastOrderId = event.orderId();
        summary.updatedAt = Instant.now();
        summaries.save(summary);
    }
}
