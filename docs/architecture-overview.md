<a id="top"></a>

# Architecture Overview

This document provides a comprehensive architectural overview of the concepts, paradigms, design patterns, and concrete code implementations in this repository. It serves as the central architectural blueprint connecting theoretical software architecture principles with practical implementations in [`rest-spring`](../rest-spring/) and [`grpc`](../grpc/).

---

## Table of Contents

- [Architectural Philosophy](#architectural-philosophy)
- [Architectural Styles Continuum](#architectural-styles-continuum)
- [Internal Component Architecture](#internal-component-architecture)
- [API and Inter-Service Communication Taxonomy](#api-and-inter-service-communication-taxonomy)
- [Data Architecture and Distributed Consistency](#data-architecture-and-distributed-consistency)
- [Cross-Cutting Architectural Concerns](#cross-cutting-architectural-concerns)
- [Repository Implementation Mapping](#repository-implementation-mapping)
- [Architectural Decision Framework](#architectural-decision-framework)
- [Documentation Index and Learning Paths](#documentation-index-and-learning-paths)

---

## Architectural Philosophy

Modern software architecture is not about finding a universally "perfect" design; it is the discipline of managing **trade-offs** under specific business, organizational, and operational constraints.

```mermaid
flowchart LR
    subgraph Forces["Architectural Forces"]
        A["Business Velocity"]
        B["System Scalability"]
        C["Operational Simplicity"]
        D["Fault Isolation"]
        E["Data Consistency"]
    end

    subgraph Decisions["Architectural Decisions"]
        S["Style (Monolith vs Microservices)"]
        P["Protocols (REST, gRPC, EDA)"]
        D1["Data Strategy (Shared vs Isolated)"]
    end

    Forces --> Decisions
```

This repository emphasizes four core architectural tenets:

1. **Modularity Before Distribution**: Establish clear domain boundaries and contracts within a single process before introducing network latency, serialization overhead, and distributed failure modes.
2. **Explicit Contracts and Strong Typing**: APIs (whether synchronous REST/gRPC or asynchronous events) represent binding commitments between software boundaries. Schema-first design and contract testing preserve consumer trust.
3. **Decoupled Data Ownership**: Data storage must be owned by the boundary that governs its business logic. Shared mutable databases across boundaries create fragile coupling.
4. **Resilience and Observability as First-Class Citizens**: In distributed networks, latency and partial failures are inevitable. Systems must incorporate timeouts, circuit breakers, structured telemetry, and correlation IDs from day one.

[Back to top](#top)

---

## Architectural Styles Continuum

Software architecture exists on a spectrum from unified single-process systems to highly distributed networks of autonomous micro-runtimes.

```mermaid
flowchart TB
    subgraph Monolith["Single Deployable Unit"]
        M1["Layered Monolith<br/>(Horizontal technical slices)"]
        M2["Modular Monolith<br/>(Vertical domain modules)"]
    end

    subgraph Distributed["Multi-Deployable Network"]
        D1["Microservices<br/>(Independently deployed services)"]
        D2["Event-Driven Architecture<br/>(Asynchronous decoupled emitters/consumers)"]
    end

    M1 -->|Refactor to domain boundaries| M2
    M2 -->|Extract high-value bounded contexts| D1
    D1 -->|Adopt event streams for loose coupling| D2
```

### Comparative Trade-Off Matrix

| Dimension | Layered Monolith | Modular Monolith | Microservices | Event-Driven Architecture |
| :--- | :--- | :--- | :--- | :--- |
| **Deployment Unit** | Single artifact (`.jar`, `.war`) | Single artifact (`.jar`) | Multiple autonomous containers | Heterogeneous independent nodes |
| **Process Boundary** | In-process method calls | In-process module interfaces | Network calls (HTTP/gRPC) | Asynchronous message broker |
| **Data Boundary** | Single shared database | Shared DB with logical schemas | Database-per-service | Event log / local projections |
| **Transaction Model** | ACID (Local transactions) | ACID across modules | BASE / Sagas / Eventual | Eventual consistency |
| **Operational Overhead** | Minimal (Single pipeline) | Low (Single deployment) | High (Service discovery, mesh) | High (Brokers, dead-letter queues) |
| **Team Topologies** | Single team / low isolation | Multi-team on single codebase | Autonomous cross-functional teams | Highly autonomous producers/consumers |
| **Failure Blast Radius** | High (Entire app crashes) | High (Process-wide failure) | Low (Isolated service failure) | Very Low (Decoupled by queues) |

[Back to top](#top)

---

## Internal Component Architecture

Within any given service or module boundary, the internal code structure dictates maintainability, testability, and adherence to domain rules.

```mermaid
flowchart TD
    subgraph Layered["Traditional Layered (N-Tier)"]
        L_UI["Presentation / Web Controller Layer"]
        L_SVC["Business Service Layer"]
        L_DAO["Data Access / Repository Layer"]
        L_DB[(Database)]
        L_UI --> L_SVC --> L_DAO --> L_DB
    end

    subgraph Hexagonal["Hexagonal / Clean Architecture (Ports & Adapters)"]
        direction TB
        subgraph Core["Domain & Application Core"]
            DOMAIN["Pure Domain Entities & Value Objects"]
            USECASE["Application Use Cases / Inbound Ports"]
            OPORT["Outbound Port Interfaces"]
        end
        ADP_REST["REST Inbound Adapter"] --> USECASE
        ADP_GRPC["gRPC Inbound Adapter"] --> USECASE
        USECASE --> DOMAIN
        USECASE --> OPORT
        OPORT --> ADP_SQL["JPA/JDBC Outbound Adapter"]
        OPORT --> ADP_EVT["Kafka/Event Outbound Adapter"]
    end
```

### Key Internal Architectural Paradigms

- **Layered Architecture (N-Tier)**:
  - Modules are separated by technical function (Controller $\rightarrow$ Service $\rightarrow$ Repository).
  - Simple to implement and standard in small-to-medium Spring Boot applications (as demonstrated in [`rest-spring`](../rest-spring/)).
  - Risk: Domain logic tends to bleed into controllers or database queries, creating an anemic domain model.
- **Hexagonal Architecture (Ports and Adapters)**:
  - Isolates core business domain logic from external technologies, frameworks, and protocols.
  - Inbound ports (use cases) receive commands from REST or gRPC controllers.
  - Outbound ports (interfaces) abstract persistence, event publishing, and third-party APIs.
- **Domain-Driven Design (DDD) Foundations**:
  - **Bounded Context**: Explicit linguistic and conceptual boundaries where a domain model applies.
  - **Aggregates and Entities**: Encapsulated state and business invariants that change together.
  - **Domain Events**: Explicit in-process notifications that communicate state transitions (e.g., `TodoCreatedEvent`).

[Back to top](#top)

---

## API and Inter-Service Communication Taxonomy

Communication patterns define how data and commands transition across network boundaries (North-South client ingress vs. East-West internal traffic).

```mermaid
flowchart TD
    subgraph Ingress["North-South Ingress (Edge / API Gateway)"]
        ClientWeb["Web Browser / SPA"]
        ClientMobile["Mobile Client (iOS/Android)"]
        Gateway["API Gateway / BFF (Backend for Frontend)"]
        ClientWeb -->|HTTPS / REST / JSON| Gateway
        ClientMobile -->|HTTPS / GraphQL| Gateway
    end

    subgraph Mesh["East-West Internal Communication"]
        Gateway -->|gRPC / HTTP/2| SvcA["Todo Service (rest-spring)"]
        Gateway -->|gRPC / Protobuf| SvcB["Notification Service (grpc)"]
        SvcA -->|Protobuf RPC| SvcB
        SvcA -.->|Publish Domain Event| Broker[(Message Broker / Kafka)]
        Broker -.->|Consume Async Event| SvcC["Audit / Analytics Service"]
    end
```

### Communication Protocols Comparison

| Style | Protocol / Payload | Primary Use Case | Strengths | Trade-offs |
| :--- | :--- | :--- | :--- | :--- |
| **REST** | HTTP/1.1 or HTTP/2<br/>JSON / XML | Public APIs, CRUD, North-South ingress | Universal browser support, HTTP caching, human-readable | Chatty, over/under-fetching, larger payloads |
| **gRPC** | HTTP/2<br/>Protocol Buffers | Internal East-West microservice communication | High throughput, binary serialization, bi-directional streaming | Requires Protobuf tooling, limited direct browser support |
| **GraphQL** | HTTP<br/>JSON queries | Aggregated UI views, mobile BFFs | Single query for nested data, client-specified schema | Caching complexity, server-side N+1 query overhead |
| **Event-Driven** | AMQP / Kafka / MQTT<br/>Binary / Avro / JSON | Asynchronous business workflows, decoupled integration | Temporal decoupling, high elasticity, fan-out broadcast | Eventual consistency, complex tracing and debugging |

[Back to top](#top)

---

## Data Architecture and Distributed Consistency

When transitioning from monolithic to distributed systems, database design transitions from ACID transactions to distributed data patterns.

```mermaid
sequenceDiagram
    autonumber
    participant Client as Client Application
    participant OrderSvc as Order Service
    participant Broker as Event Broker (Outbox)
    participant InventorySvc as Inventory Service
    participant PaymentSvc as Payment Service

    Client->>OrderSvc: 1. POST /orders (Create Order)
    activate OrderSvc
    Note over OrderSvc: Save Order (Pending) &<br/>Write to Outbox Table (Local ACID Tx)
    OrderSvc-->>Client: 202 Accepted (Order ID)
    deactivate OrderSvc

    OrderSvc->>Broker: 2. Publish OrderCreatedEvent
    Broker->>InventorySvc: 3. Deliver OrderCreatedEvent
    activate InventorySvc
    Note over InventorySvc: Reserve Stock
    InventorySvc->>Broker: 4. Publish StockReservedEvent
    deactivate InventorySvc

    Broker->>PaymentSvc: 5. Deliver StockReservedEvent
    activate PaymentSvc
    Note over PaymentSvc: Process Payment
    PaymentSvc->>Broker: 6. Publish PaymentCapturedEvent
    deactivate PaymentSvc

    Broker->>OrderSvc: 7. Deliver PaymentCapturedEvent
    activate OrderSvc
    Note over OrderSvc: Update Order Status -> Confirmed
    deactivate OrderSvc
```

### Core Distributed Data Patterns

1. **Database-per-Service**:
   - Each microservice possesses exclusive ownership of its data store. No external service may bypass the API to query database tables directly.
2. **Transactional Outbox Pattern**:
   - Solves the dual-write problem (writing to a database and publishing to a message broker simultaneously).
   - Writes state changes and outbox event records within the same local database transaction. A change-data-capture (CDC) tailer or polling relay publishes events to the broker safely.
3. **Saga Pattern**:
   - Coordinates long-running business transactions spanning multiple microservices without two-phase commit (2PC).
   - **Choreography**: Each service produces and listens to events, making local decisions.
   - **Orchestration**: A centralized orchestrator service directs participating services via command messages.
4. **CQRS (Command Query Responsibility Segregation)**:
   - Separates the write model (handling mutations, validation, and domain invariants) from the read model (optimized denormalized projections for fast UI queries).

[Back to top](#top)

---

## Cross-Cutting Architectural Concerns

Production-grade architectures require robust infrastructure foundations across four critical operational dimensions:

```mermaid
mindmap
  root((Cross-Cutting Pillars))
    Security
      Zero Trust Architecture
      OAuth2 / OIDC & JWT Tokens
      mTLS Internal Service Mesh
      Rate Limiting & WAF
    Resilience
      Circuit Breakers (Resilience4j)
      Exponential Backoff & Jitter
      Bulkhead Isolation
      Graceful Degradation
    Observability
      Structured JSON Logging (MDC)
      Distributed Tracing (OpenTelemetry / W3C)
      Metrics & SLOs (Prometheus / Grafana)
      Health Probes (Liveness & Readiness)
    Cloud-Native
      Twelve-Factor Methodology
      Stateless Process Execution
      Externalized Config & Secrets
      Containerization & Docker
```

### 1. Security Architecture
- **Zero Trust Model**: Authenticate and authorize every request, regardless of whether it originates outside the network perimeter or inside the cluster.
- **Identity Propagation**: Use OAuth2/OIDC for edge token exchange and pass cryptographically signed claims (JWT) downstream.

### 2. Resilience and Fault Isolation
- **Circuit Breakers**: Prevent cascading outages by fast-failing downstream calls when failure thresholds are exceeded.
- **Timeouts and Deadlines**: Every remote call must have strict timeout thresholds to prevent thread starvation.
- **Idempotency**: All mutating operations and message handlers must handle duplicate deliveries gracefully (via unique idempotency keys).

### 3. Distributed Observability
- **Trace Context Propagation**: Propagate `traceparent` (W3C Trace Context) across HTTP headers, gRPC metadata, and messaging headers to construct end-to-end distributed execution graphs.
- **Correlation IDs**: Inject uniform request/correlation identifiers into logging frameworks (e.g., Mapped Diagnostic Context in Java/SLF4J).

[Back to top](#top)

---

## Repository Implementation Mapping

This repository contains concrete, runnable implementations illustrating key architectural patterns and communication protocols.

```mermaid
graph LR
    subgraph Repo["api-architecture Repository"]
        subgraph REST_Proj["rest-spring (Spring Boot 3.x)"]
            RC["TodoController<br/>(REST API)"]
            RS["TodoService<br/>(Business Logic)"]
            RR["TodoRepository<br/>(Spring JDBC)"]
            FW["Flyway<br/>(DB Migrations)"]
            PG[(PostgreSQL)]
            RC --> RS --> RR --> PG
            FW --> PG
        end

        subgraph GRPC_Proj["grpc (Java / Protocol Buffers)"]
            Proto["hello.proto<br/>(IDL Contract)"]
            GenCode["Generated Stubs<br/>(Protoc Plugin)"]
            Server["HelloServer<br/>(gRPC Server)"]
            Client["HelloClient<br/>(Blocking Stub)"]
            Proto --> GenCode
            GenCode --> Server
            GenCode --> Client
            Client -->|HTTP/2 Protobuf RPC| Server
        end
    end
```

### Project Capabilities Summary

| Project | Architectural Role | Key Technologies | Concepts Demonstrated |
| :--- | :--- | :--- | :--- |
| [`rest-spring`](../rest-spring/) | Resource-Oriented Web Service | Spring Boot 3, Spring JDBC, Flyway, PostgreSQL, Docker Compose, Gradle | • Layered architecture<br/>• RESTful resource design and validation (`jakarta.validation`)<br/>• Schema evolution via Flyway migrations<br/>• Containerized backing services (`docker-compose.yml`)<br/>• CI/CD pipeline definition (`Jenkinsfile`) |
| [`grpc`](../grpc/) | High-Performance RPC Microservice | Java, gRPC, Protocol Buffers (proto3), Netty | • Schema-first contract definition (`.proto`)<br/>• Automated stub and model compilation<br/>• Synchronous unary RPC execution over HTTP/2<br/>• In-process gRPC testing and stub lifecycle management |

[Back to top](#top)

---

## Architectural Decision Framework

When designing new components or evolving existing services, use the following decision tree to guide architectural selections:

```mermaid
flowchart TD
    Start["New Feature / Service Need"] --> Q1{"Is high throughput / ultra-low latency internal RPC required?"}
    Q1 -- Yes --> UseGRPC["Use gRPC (Protocol Buffers over HTTP/2)"]
    Q1 -- No --> Q2{"Is asynchronous, temporal decoupling or broadcast needed?"}
    Q2 -- Yes --> UseEDA["Use Event-Driven Architecture (Kafka / RabbitMQ)"]
    Q2 -- No --> Q3{"Is the API consumed by diverse public/mobile clients?"}
    Q3 -- Yes --> Q4{"Does the client require flexible, nested graph queries?"}
    Q4 -- Yes --> UseGQL["Use GraphQL / BFF Layer"]
    Q4 -- No --> UseREST["Use RESTful HTTP/JSON (OpenAPI Documented)"]
    Q3 -- No --> UseREST
```

### Architectural Review Checklist

Before implementing a new architectural boundary:

1. **Domain Boundary**: Does this component represent a distinct bounded context or an unnecessary technical split?
2. **Data Governance**: Who is the single source of truth for the data? Is direct database sharing avoided?
3. **Contract Stability**: Is there a documented schema (OpenAPI, Protobuf, AsyncAPI) with backward-compatibility guidelines?
4. **Resilience Strategy**: Are fallback mechanisms, circuit breakers, and timeout configurations defined?
5. **Observability Readiness**: Are structured logs, health checks (`/actuator/health`), and distributed trace headers configured?

[Back to top](#top)

---

## Documentation Index and Learning Paths

Navigate to deep-dive documentation across the repository:

### Core Architecture Foundations
- [API Design Guide](api-design.md): Master design standards, URI conventions, HTTP semantics, RFC 7807, gRPC, and cross-references.
- [API Fundamentals](api-fundamentals.md): Principles of interface design, coupling, and API qualities.
- [API Paradigms](api-paradigms.md): In-depth comparison of REST, RPC, and GraphQL.
- [API Documentation](api-documentation.md): API contract documentation, OpenAPI specifications, and ownership.
- [API Security](api-security.md): Authentication, authorization, OAuth2, and defense-in-depth patterns.

### Architectural Styles and Patterns
- [Modular Monolith Architecture](modular-monolith.md): In-process modularity, boundary enforcement, and data separation.
- [Microservice Architecture](microservices.md): Distributed systems, Twelve-Factor app alignment, and operational foundations.
- [Microservice Design Patterns](microservice-design-patterns.md): Catalog of decomposition, data, communication, and reliability patterns.
- [Event-Driven Architecture](event-driven-architecture.md): Message brokers, event sourcing, stream processing, and delivery guarantees.
- [Repository Learning Map](repo-learning-map.md): Guided roadmap mapping learning milestones directly to codebase exercises.

[Back to top](#top)
