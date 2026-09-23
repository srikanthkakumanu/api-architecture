<a id="top"></a>
# Tutorial: Single-Service Microservice Patterns

## Table of Contents

- [Data Ownership, Consistency, and Query Patterns](#data-ownership-consistency-and-query-patterns)
  - [1. Transactional Outbox](#1-transactional-outbox)
  - [2. Inbox and Idempotent Consumer](#2-inbox-and-idempotent-consumer)
- [Observability and Operations Patterns](#observability-and-operations-patterns)
  - [3. Health Check](#3-health-check)
  - [4. Correlation ID](#4-correlation-id)
  - [5. Audit Logging](#5-audit-logging)

[Back To Top](#top)

---

## Data Ownership, Consistency, and Query Patterns

Data patterns protect service autonomy while handling reliable events and duplicate message delivery inside the sample service.

[Back To Top](#top)

---

### 1. Transactional Outbox

#### Transactional Outbox: What It Is

- Stores domain events in the same database transaction as the business change.
- Avoids the problem of saving state successfully but failing to publish the matching event.
- A publisher later reads pending outbox rows and marks them as published.

#### Transactional Outbox: Implementation

##### `src/main/resources/db/migration/V1__create_pattern_tables.sql`

```sql
create table outbox_events (
    id uuid primary key,
    aggregate_id uuid not null,
    event_type varchar(120) not null,
    payload clob not null,
    correlation_id varchar(120) not null,
    status varchar(40) not null,
    created_at timestamp with time zone not null,
    published_at timestamp with time zone
);
```

- Adds a durable outbox table beside the business tables.
- `status` separates pending events from published events.

##### `src/main/java/com/apidemo/mspatterns/application/OrderApplicationService.java`

```java
@Transactional
public Order create(CreateOrderCommand command, String correlationId) {
    var items = command.items().stream()
            .map(item -> new OrderItem(UUID.randomUUID(), item.sku(), item.quantity(), new Money(item.unitPrice(), item.currency())))
            .toList();
    var order = Order.create(command.customerId(), items).orThrow();
    var event = OrderEvent.created(order.id(), correlationId);
    orders.save(order);
    outbox.append(event, event.type());
    audit.record(correlationId, command.actor(), "ORDER_CREATED", "Order", order.id().toString(), "Order created with " + order.itemCount() + " items");
    return order;
}
```

- Saves the order, outbox event, and audit row in one transaction.
- If the transaction rolls back, none of those changes are committed.

##### `src/main/java/com/apidemo/mspatterns/application/OutboxPublisher.java`

```java
@Transactional
public int publishPending() {
    var messages = outbox.findPending(50);
    messages.forEach(message -> {
        consumer.consume(message);
        outbox.markPublished(message.eventId());
    });
    return messages.size();
}
```

- Reads pending outbox rows.
- Dispatches them to the local consumer and marks them as published.

[Back To Top](#top)

---

### 2. Inbox and Idempotent Consumer

#### Inbox: What It Is

- Records consumed message IDs.
- Protects consumers from duplicate delivery.
- Makes handling the same message twice safe.

#### Inbox: Implementation

##### `src/main/resources/db/migration/V1__create_pattern_tables.sql`

```sql
create table inbox_messages (
    message_id uuid primary key,
    message_type varchar(120) not null,
    correlation_id varchar(120) not null,
    status varchar(40) not null,
    received_at timestamp with time zone not null
);
```

- Uses `message_id` as the idempotency key.
- A duplicate message has the same primary key and is detected before reprocessing.

##### `src/main/java/com/apidemo/mspatterns/application/OrderEventConsumer.java`

```java
if (inbox.alreadyConsumed(message.eventId())) {
    audit.record(message.correlationId(), "system", "DUPLICATE_MESSAGE_IGNORED",
            "Order", message.aggregateId().toString(), message.eventType());
    return false;
}
```

- Checks the inbox before handling an event.
- Duplicate messages are audited but not consumed again.

##### `src/main/java/com/apidemo/mspatterns/infrastructure/persistence/JpaInboxRepositoryAdapter.java`

```java
public boolean alreadyConsumed(UUID messageId) {
    return repository.existsById(messageId);
}

public void recordConsumed(UUID messageId, String messageType, String correlationId) {
    var entity = new InboxMessageJpaEntity();
    entity.messageId = messageId;
    entity.status = "CONSUMED";
    repository.save(entity);
}
```

- Stores successful consumption.
- Keeps idempotency logic behind an application port.

## Observability and Operations Patterns

Observability patterns make service behavior understandable during development and operations through health signals, request correlation, and audit trails.

[Back To Top](#top)

---

### 3. Health Check

#### Health Check: What It Is

- Reports whether the service is healthy enough to receive traffic.
- Can include database checks and application-specific readiness signals.
- Helps platforms and operators detect unhealthy instances.

#### Health Check: Implementation

##### `build.gradle`

```groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

- Adds Actuator endpoints such as `/actuator/health`.

##### `src/main/resources/application.yml`

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

patterns:
  outbox:
    backlog-threshold: 25
```

- Exposes health details.
- Configures the custom outbox backlog threshold.

##### `src/main/java/com/apidemo/mspatterns/infrastructure/health/OutboxBacklogHealthIndicator.java`

```java
public Health health() {
    var pending = outbox.pendingCount();
    var builder = pending <= threshold ? Health.up() : Health.down();
    return builder
            .withDetail("pendingOutboxEvents", pending)
            .withDetail("threshold", threshold)
            .build();
}
```

- Reports health based on pending outbox work.
- Helps show how business-specific readiness can extend standard health checks.

[Back To Top](#top)

---

### 4. Correlation ID

#### Correlation ID: What It Is

- A shared ID that follows one request or workflow.
- Helps connect logs, events, audit rows, and API responses.
- Usually comes from a request header or is generated at the edge.

#### Correlation ID: Implementation

##### `src/main/java/com/apidemo/mspatterns/infrastructure/observability/CorrelationId.java`

```java
public static final String HEADER = "X-Correlation-ID";
public static final String MDC_KEY = "correlationId";
```

- Defines one header and logging key for the whole project.

##### `src/main/java/com/apidemo/mspatterns/infrastructure/observability/CorrelationIdFilter.java`

```java
var correlationId = Optional.ofNullable(request.getHeader(CorrelationId.HEADER))
        .filter(value -> !value.isBlank())
        .orElseGet(() -> UUID.randomUUID().toString());
MDC.put(CorrelationId.MDC_KEY, correlationId);
response.setHeader(CorrelationId.HEADER, correlationId);
```

- Reuses an incoming correlation ID or creates one.
- Adds it to response headers and logging context.

##### `src/main/java/com/apidemo/mspatterns/interfaces/rest/OrderController.java`

```java
return RestDtos.OrderResponse.from(service.create(command, correlationId()));

private static String correlationId() {
    return MDC.get(CorrelationId.MDC_KEY);
}
```

- Passes the correlation ID from the HTTP boundary into the application layer.
- The application layer stores it in outbox and audit records.

[Back To Top](#top)

---

### 5. Audit Logging

#### Audit Logging: What It Is

- Records important business and operational actions.
- Answers who did what, to which thing, and under which correlation ID.
- Should be append-only in real systems.

#### Audit Logging: Implementation

##### `src/main/resources/db/migration/V1__create_pattern_tables.sql`

```sql
create table audit_logs (
    id uuid primary key,
    correlation_id varchar(120) not null,
    actor varchar(120) not null,
    action varchar(120) not null,
    target_type varchar(80) not null,
    target_id varchar(120) not null,
    details varchar(1000) not null,
    created_at timestamp with time zone not null
);
```

- Adds an append-style table for business and operational events.
- Stores correlation ID, actor, action, target, and details.

##### `src/main/java/com/apidemo/mspatterns/infrastructure/persistence/JpaAuditLogRepositoryAdapter.java`

```java
public void record(String correlationId, String actor, String action, String targetType, String targetId, String details) {
    var entity = new AuditLogJpaEntity();
    entity.id = UUID.randomUUID();
    entity.correlationId = correlationId;
    entity.actor = actor;
    entity.action = action;
    entity.targetType = targetType;
    entity.targetId = targetId;
    entity.details = details;
    repository.save(entity);
}
```

- Converts audit calls into persisted audit rows.
- Keeps audit persistence behind an application port.

##### `src/main/java/com/apidemo/mspatterns/interfaces/rest/PatternController.java`

```java
@GetMapping("/audit-logs")
public List<Ports.AuditLog> auditLogs(@RequestParam UUID orderId) {
    return audit.findByOrderId(orderId);
}
```

- Exposes audit entries for learning and verification.

[Back To Top](#top)
