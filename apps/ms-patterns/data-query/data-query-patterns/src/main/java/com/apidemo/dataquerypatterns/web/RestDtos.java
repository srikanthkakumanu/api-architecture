package com.apidemo.dataquerypatterns.web;

import com.apidemo.dataquerypatterns.account.AccountEventSourcingService;
import com.apidemo.dataquerypatterns.cache.ProductEntity;
import com.apidemo.dataquerypatterns.materializedview.CustomerOrderSummaryEntity;
import com.apidemo.dataquerypatterns.orders.OrderEntity;
import com.apidemo.dataquerypatterns.orders.OrderStatus;
import com.apidemo.dataquerypatterns.orders.SagaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public final class RestDtos {
    private RestDtos() {
    }

    public record CreateOrderRequest(@NotBlank String customerId, @NotNull @Positive BigDecimal totalAmount, @NotBlank String currency) {
    }

    public record OrderResponse(UUID id, String customerId, OrderStatus status, SagaStatus sagaStatus, String total, String currency) {
        public static OrderResponse from(OrderEntity order) {
            return new OrderResponse(order.id, order.customerId, order.status, order.sagaStatus, order.totalAmount.toPlainString(), order.currency);
        }
    }

    public record PublishResponse(int published) {
    }

    public record ReplayResponse(boolean consumed) {
    }

    public record CustomerSummaryResponse(String customerId, int confirmedOrderCount, String confirmedTotal, String currency, UUID lastOrderId) {
        public static CustomerSummaryResponse from(CustomerOrderSummaryEntity summary) {
            return new CustomerSummaryResponse(summary.customerId, summary.confirmedOrderCount, summary.confirmedTotal.toPlainString(), summary.currency, summary.lastOrderId);
        }
    }

    public record OpenAccountRequest(@NotNull @Positive BigDecimal openingBalance) {
    }

    public record MoneyRequest(@NotNull @Positive BigDecimal amount) {
    }

    public record AccountResponse(UUID accountId, String balance, int version) {
        public static AccountResponse from(AccountEventSourcingService.AccountView view) {
            return new AccountResponse(view.accountId(), view.balance().toPlainString(), view.version());
        }
    }

    public record ProductResponse(String sku, String name, String price, String currency) {
        public static ProductResponse from(ProductEntity product) {
            return new ProductResponse(product.sku, product.name, product.price.toPlainString(), product.currency);
        }
    }

    public record ChangePriceRequest(@NotNull @Positive BigDecimal price) {
    }
}
