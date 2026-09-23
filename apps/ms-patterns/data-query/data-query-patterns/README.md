# Data Query Patterns Microservice

This Spring Boot service implements the data ownership, consistency, and query patterns that sit beside the existing CQRS example.

## Technology

- Java 27
- Spring Boot 4.1.1
- Gradle Groovy DSL
- Spring Data JPA / Hibernate
- MapStruct dependency available for mapper-focused examples
- Flyway
- H2
- Spring Cache
- Actuator

## Patterns Implemented

- Saga: `OrderSagaService` orchestrates order placement, inventory reservation, payment authorization, confirmation, and compensation.
- Transactional Outbox: saga state changes append rows to `outbox_messages` in the same transaction.
- Inbox and Idempotent Consumer: `InboxService` records consumed message IDs per consumer so replayed messages are ignored.
- Materialized View: `CustomerSummaryProjector` updates `customer_order_summaries` from confirmed order events.
- Event Sourcing: `AccountEventSourcingService` stores account changes only as events and rebuilds balance from the event stream.
- Cache-Aside: `ProductCatalogService` caches product reads and evicts the cache when prices change.

## Run

```bash
gradle bootRun
```

From this repository, you can also reuse the existing wrapper:

```bash
../CQRS/gradlew bootRun
```

If the wrapper is not present in `CQRS`, use another Gradle wrapper from the repo, for example:

```bash
../../spring-boot-ddd-patterns/gradlew bootRun
```

## Test

```bash
gradle test
```

Or:

```bash
../../spring-boot-ddd-patterns/gradlew test
```

## API Flow

Create an order and start the saga:

```bash
curl -i -X POST http://localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -d '{"customerId":"customer-7","totalAmount":120.00,"currency":"USD"}'
```

Advance the saga:

```bash
curl -X PATCH http://localhost:8080/orders/{orderId}/advance
```

Publish outbox events:

```bash
curl -X POST http://localhost:8080/internal/outbox/publish
```

Read the materialized customer summary:

```bash
curl http://localhost:8080/summaries/customers/customer-7
```

Replay a published message to see the idempotent inbox protection:

```bash
curl -X POST http://localhost:8080/internal/outbox/replay/{messageId}
```

Use event sourcing:

```bash
curl -i -X POST http://localhost:8080/accounts \
  -H 'Content-Type: application/json' \
  -d '{"openingBalance":100.00}'

curl -X POST http://localhost:8080/accounts/{accountId}/deposits \
  -H 'Content-Type: application/json' \
  -d '{"amount":25.00}'

curl http://localhost:8080/accounts/{accountId}
```

Use cache-aside:

```bash
curl http://localhost:8080/products/BOOK-1

curl -X PATCH http://localhost:8080/products/BOOK-1/price \
  -H 'Content-Type: application/json' \
  -d '{"price":45.00}'
```

See [TUTORIAL.md](TUTORIAL.md) for a pattern-by-pattern walkthrough.
