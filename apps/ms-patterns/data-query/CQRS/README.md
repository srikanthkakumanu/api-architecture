# CQRS Microservice

This project is a complete Spring Boot implementation of the Command Query Responsibility Segregation pattern.

Commands write to a normalized order model and append domain events. Queries never read the command tables. A separate projection step consumes unprojected events and updates a denormalized read model.

## Technology

- Java 27
- Spring Boot 4.1.1
- Gradle Groovy DSL
- Spring Data JPA / Hibernate
- MapStruct
- Flyway
- H2
- Actuator

## Pattern Implemented

- CQRS with separate command and query APIs
- Command-side write model in `orders` and `order_lines`
- Event log in `domain_events`
- Query-side read model in `order_read_models`
- Projector that updates the read model independently from command handling

## Run

```bash
gradle bootRun
```

You can also run it from the repository using an existing Gradle wrapper:

```bash
../spring-boot-ddd-patterns/gradlew bootRun
```

## Test

```bash
gradle test
```

Or:

```bash
../spring-boot-ddd-patterns/gradlew test
```

## API Flow

Create an order through the command API:

```bash
curl -i -X POST http://localhost:8080/commands/orders \
  -H 'Content-Type: application/json' \
  -d '{
    "customerId": "customer-7",
    "items": [
      {"sku": "BOOK-1", "quantity": 2, "unitPrice": 15.50, "currency": "USD"},
      {"sku": "PEN-1", "quantity": 3, "unitPrice": 2.00, "currency": "USD"}
    ]
  }'
```

Before projection, the query model does not have the order yet:

```bash
curl http://localhost:8080/queries/orders/{orderId}
```

Project pending events:

```bash
curl -X POST http://localhost:8080/internal/projections/orders/run
```

Read the denormalized query model:

```bash
curl http://localhost:8080/queries/orders/{orderId}
```

Search the query model:

```bash
curl 'http://localhost:8080/queries/orders?customerId=customer-7'
curl 'http://localhost:8080/queries/orders?status=PAID'
```

Change status through the command API:

```bash
curl -X PATCH http://localhost:8080/commands/orders/{orderId}/status \
  -H 'Content-Type: application/json' \
  -d '{"status": "PAID"}'
```

Run projection again to update the query model.

See [TUTORIAL.md](TUTORIAL.md) for a step-by-step walkthrough.
