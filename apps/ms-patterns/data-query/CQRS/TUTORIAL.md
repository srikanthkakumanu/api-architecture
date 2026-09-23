<a id="top"></a>
# Tutorial: CQRS Microservice Pattern

## Table of Contents

- [1. What CQRS Separates](#1-what-cqrs-separates)
- [2. Command Side](#2-command-side)
- [3. Event Log](#3-event-log)
- [4. Query Side](#4-query-side)
- [5. Projection](#5-projection)
- [6. End-to-End Flow](#6-end-to-end-flow)

[Back To Top](#top)

---

## 1. What CQRS Separates

CQRS separates the model used to change state from the model used to answer queries.

In this project:

- Commands use `/commands/orders`.
- Queries use `/queries/orders`.
- Command data is stored in `orders` and `order_lines`.
- Query data is stored in `order_read_models`.
- `domain_events` is the handoff between the write side and the read side.

[Back To Top](#top)

---

## 2. Command Side

The command side validates intent and changes the source of truth.

### `OrderCommandController`

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public RestDtos.CommandResponse create(@Valid @RequestBody RestDtos.CreateOrderRequest request) {
    return mapper.toResponse(service.createOrder(mapper.toCommand(request)));
}
```

- Accepts command requests.
- Does not return a query view.
- Returns the command result and event ID.

### `OrderCommandService`

```java
@Transactional
public CommandResult createOrder(CreateOrderCommand command) {
    var order = new OrderWriteEntity();
    order.id = UUID.randomUUID();
    order.customerId = command.customerId();
    order.status = OrderStatus.CREATED;
    orders.save(order);
    var event = eventStore.append("OrderCreated", order.id, EventStore.OrderSnapshot.from(order, 1));
    return new CommandResult(order.id, order.status, money(order), event.id);
}
```

- Writes the normalized command model.
- Appends an event in the same transaction.
- Does not update the query table directly.

[Back To Top](#top)

---

## 3. Event Log

The event log records facts produced by commands. It is the boundary between the command model and the query model.

### `domain_events`

```sql
create table domain_events (
    id uuid primary key,
    aggregate_id uuid not null,
    event_type varchar(120) not null,
    payload clob not null,
    projected boolean not null,
    created_at timestamp with time zone not null,
    projected_at timestamp with time zone
);
```

- `payload` stores the data needed by the projection.
- `projected` marks whether the read model has caught up.
- The command API can succeed before the query API reflects the change.

[Back To Top](#top)

---

## 4. Query Side

The query side reads a denormalized model designed for API responses.

### `order_read_models`

```sql
create table order_read_models (
    order_id uuid primary key,
    customer_id varchar(80) not null,
    status varchar(40) not null,
    total varchar(40) not null,
    item_count integer not null,
    line_summary varchar(1000) not null,
    version bigint not null,
    last_event_id uuid not null,
    updated_at timestamp with time zone not null
);
```

### `OrderQueryController`

```java
@GetMapping("/{id}")
public RestDtos.OrderQueryResponse get(@PathVariable UUID id) {
    return mapper.toResponse(service.get(id));
}
```

- Reads only `order_read_models`.
- Supports query-specific search by customer or status.
- Does not call the command repository.

[Back To Top](#top)

---

## 5. Projection

Projection turns events into query rows.

### `OrderProjectionService`

```java
@Transactional
public int projectPending() {
    var pending = events.findTop100ByProjectedFalseOrderByCreatedAtAsc();
    pending.forEach(event -> {
        var payload = eventStore.readPayload(event);
        readModels.save(mapper.toReadModel(payload, event));
        event.projected = true;
        event.projectedAt = Instant.now();
        events.save(event);
    });
    return pending.size();
}
```

- Reads pending domain events.
- Upserts a query-optimized row.
- Marks events as projected.

### `OrderReadModelMapper`

```java
@Mapper(componentModel = "spring", imports = Instant.class)
public interface OrderReadModelMapper {
    @Mapping(target = "orderId", source = "payload.orderId")
    @Mapping(target = "lastEventId", source = "event.id")
    @Mapping(target = "updatedAt", expression = "java(Instant.now())")
    OrderReadModelEntity toReadModel(EventStore.OrderSnapshot payload, DomainEventEntity event);
}
```

- Uses MapStruct to map event payloads to the read model.
- Keeps projection mapping explicit and testable.

[Back To Top](#top)

---

## 6. End-to-End Flow

1. `POST /commands/orders` creates an order and appends `OrderCreated`.
2. `GET /queries/orders/{id}` is not available yet because projection has not run.
3. `POST /internal/projections/orders/run` builds the read model.
4. `GET /queries/orders/{id}` returns the denormalized order view.
5. `PATCH /commands/orders/{id}/status` appends `OrderStatusChanged`.
6. The query response remains stale until projection runs again.
7. Projection updates `order_read_models` to the next version.

This is the important CQRS tradeoff: write operations stay focused on business intent, read operations use a model shaped for queries, and the system must handle eventual consistency between them.

[Back To Top](#top)
