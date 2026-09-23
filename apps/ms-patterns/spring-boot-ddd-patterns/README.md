# Spring Boot DDD Microservice Patterns

This project is a single Spring Boot service that demonstrates several microservice design patterns.

It uses an Order Management bounded context and keeps the code intentionally small so the patterns are easy to see.

## Technology

- Java 27
- Spring Boot 4.1.1
- Gradle Groovy DSL
- YAML configuration
- Spring Data JPA
- MapStruct
- H2
- Flyway
- Actuator

## Patterns Demonstrated

- Transactional Outbox
- Inbox / Idempotent Consumer
- Health Check
- Correlation ID
- Audit Logging

## Architecture

The project follows DDD layering:

- `domain`: pure records, value objects, domain rules, and domain events.
- `application`: use cases, ports, command handling, outbox publishing, event consumption.
- `infrastructure`: JPA entities, Spring Data repositories, MapStruct mappers, health checks, correlation filter.
- `interfaces`: REST controllers and request/response DTOs.

The domain model is not annotated with JPA. Persistence is mapped through infrastructure entities and MapStruct.

## Run

```bash
./gradlew bootRun
```

## Test

```bash
./gradlew test
```

This machine must run Gradle with a JDK compatible with Gradle and compile with a JDK that supports Java 27.

## API Flow

Create an order:

```bash
curl -i -X POST http://localhost:8080/orders \
  -H 'Content-Type: application/json' \
  -H 'X-Correlation-ID: demo-123' \
  -d '{
    "customerId": "customer-7",
    "actor": "api-user",
    "items": [
      {"sku": "BOOK-1", "quantity": 2, "unitPrice": 15.50, "currency": "USD"}
    ]
  }'
```

Publish pending outbox events:

```bash
curl -X POST http://localhost:8080/internal/outbox/publish
```

Read audit logs:

```bash
curl 'http://localhost:8080/audit-logs?orderId={orderId}'
```

Check health:

```bash
curl http://localhost:8080/actuator/health
```

See [TUTORIAL.md](TUTORIAL.md) for a pattern-by-pattern walkthrough.
