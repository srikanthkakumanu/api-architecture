<a id="top"></a>
# Tutorial: Data Query Microservice Patterns

## Table of Contents

- [1. Saga](#1-saga)
- [2. Transactional Outbox](#2-transactional-outbox)
- [3. Inbox and Idempotent Consumer](#3-inbox-and-idempotent-consumer)
- [4. Event Sourcing](#4-event-sourcing)
- [5. Materialized View](#5-materialized-view)
- [6. Cache-Aside](#6-cache-aside)
- [7. End-to-End Flow](#7-end-to-end-flow)

[Back To Top](#top)

---

## 1. Saga

The saga coordinates an order workflow without one global transaction.

### `OrderSagaService`

```java
@Transactional
public OrderEntity advance(UUID orderId) {
    var order = orders.findById(orderId).orElseThrow();
    reserveInventory(order);
    authorizePayment(order);
    order.status = OrderStatus.CONFIRMED;
    order.sagaStatus = SagaStatus.COMPLETED;
    append(order, "OrderConfirmed");
    return orders.save(order);
}
```

- `STARTED` orders reserve inventory.
- Reserved orders authorize payment.
- Authorized orders become confirmed.
- Orders above the demo payment threshold are compensated and rejected.

[Back To Top](#top)

---

## 2. Transactional Outbox

The service writes business state and event messages in the same database transaction.

### `outbox_messages`

```sql
create table outbox_messages (
    id uuid primary key,
    aggregate_id uuid not null,
    event_type varchar(120) not null,
    payload clob not null,
    status varchar(40) not null,
    created_at timestamp with time zone not null,
    published_at timestamp with time zone
);
```

### `OutboxService`

```java
public OutboxMessageEntity append(UUID aggregateId, String eventType, Object payload) {
    var message = new OutboxMessageEntity();
    message.id = UUID.randomUUID();
    message.aggregateId = aggregateId;
    message.eventType = eventType;
    message.payload = toJson(payload);
    message.status = "PENDING";
    return outbox.save(message);
}
```

- The saga appends events while changing order state.
- `OutboxPublisher` later publishes pending messages and marks them as published.

[Back To Top](#top)

---

## 3. Inbox and Idempotent Consumer

The inbox prevents duplicate processing by storing the message ID and consumer name.

### `inbox_messages`

```sql
create table inbox_messages (
    message_id uuid not null,
    consumer_name varchar(120) not null,
    message_type varchar(120) not null,
    status varchar(40) not null,
    received_at timestamp with time zone not null,
    primary key (message_id, consumer_name)
);
```

### `InboxService`

```java
public boolean recordIfFirst(UUID messageId, String consumerName, String messageType) {
    if (inbox.existsByMessageIdAndConsumerName(messageId, consumerName)) {
        return false;
    }
    inbox.save(message);
    return true;
}
```

- A first delivery records the message and lets the consumer run.
- A replay with the same ID returns `false` and does not update the view again.

[Back To Top](#top)

---

## 4. Event Sourcing

The account example stores changes as events instead of updating a current-state row.

### `account_events`

```sql
create table account_events (
    id uuid primary key,
    account_id uuid not null,
    sequence_number bigint not null,
    event_type varchar(80) not null,
    amount numeric(12, 2) not null,
    created_at timestamp with time zone not null
);
```

### `AccountEventSourcingService`

```java
public AccountView get(UUID accountId) {
    var stream = events.findByAccountIdOrderBySequenceNumberAsc(accountId);
    var balance = BigDecimal.ZERO;
    for (var event : stream) {
        balance = switch (event.eventType) {
            case "AccountOpened", "MoneyDeposited" -> balance.add(event.amount);
            case "MoneyWithdrawn" -> balance.subtract(event.amount);
            default -> throw new IllegalStateException();
        };
    }
    return new AccountView(accountId, balance, stream.size());
}
```

- `POST /accounts` appends `AccountOpened`.
- Deposits and withdrawals append new events.
- `GET /accounts/{id}` rebuilds balance from the stream.

[Back To Top](#top)

---

## 5. Materialized View

The customer summary is a denormalized read model maintained from order events.

### `customer_order_summaries`

```sql
create table customer_order_summaries (
    customer_id varchar(80) primary key,
    confirmed_order_count integer not null,
    confirmed_total numeric(12, 2) not null,
    currency varchar(3) not null,
    last_order_id uuid not null,
    updated_at timestamp with time zone not null
);
```

### `CustomerSummaryProjector`

```java
public void apply(UUID messageId, String eventType, OutboxService.OrderEvent event) {
    if (!"OrderConfirmed".equals(eventType)) {
        return;
    }
    summary.confirmedOrderCount++;
    summary.confirmedTotal = summary.confirmedTotal.add(new BigDecimal(event.total()));
    summaries.save(summary);
}
```

- The summary is optimized for customer reporting.
- It updates asynchronously through the outbox publisher.

[Back To Top](#top)

---

## 6. Cache-Aside

The product catalog reads through a cache and evicts stale entries on writes.

### `ProductCatalogService`

```java
@Cacheable(cacheNames = "products", key = "#sku")
@Transactional(readOnly = true)
public ProductEntity get(String sku) {
    return products.findById(sku).orElseThrow();
}

@CacheEvict(cacheNames = "products", key = "#sku")
@Transactional
public ProductEntity changePrice(String sku, BigDecimal price) {
    var product = products.findById(sku).orElseThrow();
    product.price = price;
    return products.save(product);
}
```

- Reads check the cache first.
- Writes update the database and evict the cached entry.
- The next read repopulates the cache.

[Back To Top](#top)

---

## 7. End-to-End Flow

1. `POST /orders` creates an order and appends `OrderCreated`.
2. `PATCH /orders/{id}/advance` runs the saga and appends inventory, payment, and final order events.
3. `POST /internal/outbox/publish` publishes pending events.
4. The materialized summary consumer records each message in `inbox_messages`.
5. Only `OrderConfirmed` changes `customer_order_summaries`.
6. `POST /internal/outbox/replay/{messageId}` returns `false` for an already-consumed message.
7. `/accounts` demonstrates event sourcing with an append-only stream.
8. `/products/{sku}` demonstrates cache-aside reads and write-time eviction.

[Back To Top](#top)
