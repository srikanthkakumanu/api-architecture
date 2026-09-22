# Saga Pattern

The **Saga pattern** maintains data consistency across multiple services by dividing a distributed business transaction into a sequence of **local transactions**. Each service commits its own change and, when a later step fails, the Saga runs **compensating actions** for previously completed steps.

Sagas avoid a distributed database transaction. They can be coordinated through **event-driven choreography** or a central **orchestrator**.

---

## What a Saga Is

A Saga represents a long-running business workflow, such as placing an order:

1. The **Order Service** creates an order.
2. The **Payment Service** processes the payment.
3. The **Inventory Service** reserves stock.
4. The **Shipping Service** prepares the shipment.

Each step is a separate transaction owned by one service. If inventory reservation fails after payment succeeds, the Saga may compensate by refunding the payment and cancelling the order.

A compensating action is a business operation, not a database rollback. Some real-world effects cannot be perfectly reversed, so compensation must be designed explicitly.

---

## Core Concepts

- **Local transaction:** A service updates only the data it owns and commits that change atomically.
- **Saga participant:** A service that performs a local transaction or compensation as part of the workflow.
- **Compensating action:** A business operation that semantically reverses or mitigates a completed step.
- **Saga state:** The current workflow step, completed actions, failures, and pending compensation.
- **Idempotency:** The ability to process a command or event more than once without producing an incorrect result.
- **Eventual consistency:** A period during which participating services may expose different views of the workflow state.

---

## Saga Coordination Approaches

### Choreography

With **choreography**, participants publish events and react to events from other services. There is no central workflow coordinator.

**Use when:** The workflow is short, has few participants, and the event relationships remain easy to understand.

**Watch for:** Hidden dependencies, event cycles, difficult troubleshooting, and business logic spread across many consumers.

### Orchestration

With **orchestration**, a central Saga orchestrator sends commands to participants, evaluates their responses, and decides which step or compensation runs next.

**Use when:** The workflow has many steps, conditional branches, timeouts, or complex compensation rules.

**Watch for:** Excessive business logic or infrastructure coupling in the orchestrator. The orchestrator coordinates the workflow but should not own participant data.

---

## Implementing Saga in Spring Boot

### 1. Add Dependencies

The following example uses Spring Web, Kafka, and Spring Data JPA:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
</dependencies>
```

Use a broker such as **Kafka** or **RabbitMQ** when Saga participants communicate asynchronously. The exact dependencies should match the messaging technology used by the application.

### 2. Implement Choreography

An order workflow might exchange the following events:

1. The Order Service creates an order and publishes `OrderCreatedEvent`.
2. The Payment Service processes payment and publishes `PaymentProcessedEvent` or `PaymentFailedEvent`.
3. The Inventory Service reserves stock and publishes `StockReservedEvent` or `StockReservationFailedEvent`.
4. A stock reservation failure causes the Payment Service to issue a refund and the Order Service to cancel the order.

```java
@KafkaListener(topics = "order-created")
public void handleOrderCreated(OrderCreatedEvent event) {
    try {
        processPayment(event.getOrderId());
        kafkaTemplate.send(
            "payment-processed",
            new PaymentProcessedEvent(event.getOrderId())
        );
    } catch (PaymentException exception) {
        kafkaTemplate.send(
            "payment-failed",
            new PaymentFailedEvent(event.getOrderId())
        );
    }
}
```

The listener should be **idempotent** because a broker may deliver the same event more than once. Publishing events reliably commonly requires the **Transactional Outbox** pattern.

### 3. Implement Orchestration

An orchestrator explicitly controls the workflow:

1. Send a payment command and evaluate the result.
2. Send an inventory reservation command and evaluate the result.
3. When inventory reservation fails, send a refund command.
4. Record the final Saga outcome.

```java
@Service
@RequiredArgsConstructor
public class OrderSagaOrchestrator {
    private final PaymentService paymentService;
    private final InventoryService inventoryService;

    public void startSaga(UUID orderId) {
        boolean paymentCompleted = paymentService.process(orderId);
        if (!paymentCompleted) {
            return;
        }

        boolean stockReserved = inventoryService.reserve(orderId);
        if (!stockReserved) {
            paymentService.refund(orderId);
        }
    }
}
```

This example demonstrates the control flow only. A production orchestrator should **persist Saga state**, resume after process restarts, enforce timeouts, retry transient failures, and make commands idempotent.

### 4. Define Compensation and Failure Handling

Document the forward action and compensation for every Saga step:

| Step | Forward Action | Compensation |
| --- | --- | --- |
| Order | Create pending order | Cancel order |
| Payment | Capture payment | Refund payment |
| Inventory | Reserve stock | Release stock |
| Shipping | Create shipment | Cancel shipment when possible |

Not every failure should trigger immediate compensation. A transient dependency failure may first be retried, while a rejected business operation may require compensation.

---

## Choreography and Orchestration Comparison

| Concern | Choreography | Orchestration |
| --- | --- | --- |
| Coordination | Distributed through events | Centralized in an orchestrator |
| Coupling | Participants depend on event contracts | Participants depend on orchestrator commands |
| Workflow visibility | Harder to understand end to end | Easier to inspect in one place |
| Change impact | New event reactions may be added independently | Workflow changes usually affect the orchestrator |
| Best fit | Short, stable workflows | Complex or long-running workflows |
| Main risk | Event chains become difficult to manage | The orchestrator becomes overly complex |

Neither approach is automatically more scalable or reliable. The better choice depends on workflow complexity, team ownership, observability, and failure-handling requirements.

---

## Practical Guidance for Java Developers

- Start with **choreography** for short workflows with clear event relationships.
- Prefer **orchestration** when the workflow has many steps, branches, deadlines, or compensation rules.
- Give every command and event a stable **correlation ID** and **Saga ID**.
- Make consumers and compensating actions **idempotent**.
- Use the **Transactional Outbox** pattern to publish events reliably with database changes.
- Store enough Saga state to diagnose failures and safely resume processing.
- Monitor stuck Sagas, repeated compensation failures, broker lag, and dead-letter queues.

A Saga provides consistency through coordinated business actions. It does not provide the immediate isolation guarantees of a single ACID transaction, so API contracts and user interfaces must account for intermediate states.
