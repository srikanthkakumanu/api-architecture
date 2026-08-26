<a id="top"></a>

# Architecture Styles

There are **two categories** of architecture styles:

- **Monolithic**
- **Distributed**

Each style has strengths, weaknesses and use cases.

## Table of Contents

- [Monolithic](#monolithic)
  - [Layered Architecture](#layered-architecture)
  - [Modular Architecture](#modular-architecture)
  - [Pipeline Architecture](#pipeline-architecture)
  - [Microkernel Architecture](#microkernel-architecture)
- [Distributed](#distributed)
  - [Service Oriented Architecture (SOA)](#service-oriented-architecture-soa)
  - [Event-Driven Architecture (EDA)](#event-driven-architecture-eda)
  - [Space-Based Architecture](#space-based-architecture)
  - [Orchestration-Driven Architecture](#orchestration-driven-architecture)
  - [Microservices Architecture](#microservices-architecture)

## Monolithic

### Layered Architecture

**Characteristics (What)**

- Layer Separation: Organizes an application into horizontal layers such as presentation, business logic, persistence, and database.

**Principles (Why)**

- Clear Responsibility: Separates responsibilities so the system is easier to understand, test, and maintain.

**Benefits (Why choose)**

- Simple Structure: Provides a simple structure with clear boundaries.
- CRUD Fit: Works well for CRUD-heavy applications.

**Weakness**

- Can become rigid when changes need to cross multiple layers.
- May add unnecessary overhead for very small applications.

**When to choose**

- Choose it for enterprise apps, internal systems, and applications with stable business rules.

[Back to top](#top)

### Modular Architecture

**Characteristics (What)**

- Business Modules: Organizes a single deployable application into clear business-focused modules.
- Controlled Interface: Each module owns a cohesive part of the domain and exposes a controlled interface.

**Principles (Why)**

- Simple Deployment: Keeps monolithic deployment simple while improving code ownership and maintainability.

**Benefits (Why choose)**

- Low Complexity: Provides many modularity benefits without distributed system complexity.
- Team Boundaries: Helps teams define boundaries before considering microservices.

**Weakness**

- Boundaries can erode if module access rules are not enforced.
- Large builds and deployments may still affect the whole application.

**When to choose**

- Choose it when the domain is still evolving and simpler operations are preferred.
- See [Modular Monolith Architecture](modular-monolith.md) for more detail.

[Back to top](#top)

### Pipeline Architecture

**Characteristics (What)**

- Sequential Steps: Processes data through a sequence of independent steps.
- Data Transformation: Each step transforms, filters, or enriches the input before passing it forward.

**Principles (Why)**

- Repeatable Stages: Useful when work can be broken into repeatable processing stages.

**Benefits (Why choose)**

- Easy Extension: Easy to extend by adding or replacing processing steps.
- Reusable Steps: Supports reusable steps for streaming or batch workflows.

**Weakness**

- Can be hard to debug when failures happen across many stages.
- Less suitable when processing steps need complex back-and-forth communication.

**When to choose**

- Choose it for data processing, compilers, ETL systems, image processing, and event streams.

[Back to top](#top)

### Microkernel Architecture

**Characteristics (What)**

- Small Core: Builds a small core system with optional plugins that add features.

**Principles (Why)**

- Core Stability: Allows the system to be extended without changing the core logic.

**Benefits (Why choose)**

- High Flexibility: Provides high flexibility and plugin support.
- Easy Customization: Makes customization easier for different users or products.

**Weakness**

- Plugin compatibility and versioning can become difficult to manage.
- Core design must be stable because many plugins depend on it.

**When to choose**

- Choose it for IDEs, workflow tools, browsers, product platforms, or systems needing third-party extensions.

[Back to top](#top)

## Distributed

### Service Oriented Architecture (SOA)

**Characteristics (What)**

- Reusable Services: Splits functionality into reusable services that communicate over a network.

**Principles (Why)**

- Shared Capability: Enables reuse, integration, and independent ownership of business capabilities.

**Benefits (Why choose)**

- Enterprise Reuse: Good for large enterprises with shared business services.
- System Integration: Supports cross-system communication and integration.

**Weakness**

- Network communication can add latency and failure points.
- Governance and service coordination can become complex.

**When to choose**

- Choose it when multiple applications need to share common business services.

[Back to top](#top)

### Event-Driven Architecture (EDA)

**Characteristics (What)**

- Business Events: Designs systems around events that represent things that have already happened.
- Async Reaction: Producers publish events, and consumers react to them asynchronously.

**Principles (Why)**

- Loose Coupling: Reduces direct coupling between components and supports responsive, scalable workflows.

**Benefits (Why choose)**

- Independent Reaction: Helps multiple services or modules react to business changes independently.
- Async Workflows: Works well for asynchronous processing, notifications, integrations, and audit trails.

**Weakness**

- Debugging and tracing workflows can be harder because work happens across many handlers.
- Requires careful handling of retries, duplicate messages, ordering, and eventual consistency.

**When to choose**

- Choose it when business events need to trigger work across modules, services, or external systems.
- See [Event-Driven Architecture](event-driven-architecture.md) for more detail.

[Back to top](#top)

### Space-Based Architecture

**Characteristics (What)**

- Distributed State: Distributes application state and processing across multiple nodes.
- Memory Grid: Often uses in-memory data grids to share data quickly.

**Principles (Why)**

- Bottleneck Reduction: Reduces database bottlenecks and supports high scalability.

**Benefits (Why choose)**

- High Throughput: Supports high throughput and fault tolerance.
- Elastic Scaling: Scales elastically during sudden traffic spikes.

**Weakness**

- Can be complex to design, operate, and troubleshoot.
- Data consistency can be harder to maintain across distributed nodes.

**When to choose**

- Choose it for systems with unpredictable traffic spikes, such as trading, ticket booking, or large-scale e-commerce.

[Back to top](#top)

### Orchestration-Driven Architecture

**Characteristics (What)**

- Central Control: Uses a central orchestrator to coordinate services, workflows, or business processes.

**Principles (Why)**

- Process Visibility: Provides control and visibility over complex multi-step operations.

**Benefits (Why choose)**

- Workflow Management: Centralizes workflow management and monitoring.
- Process Control: Gives clear process control for business operations.

**Weakness**

- The orchestrator can become a bottleneck or single point of failure.
- Workflows may become tightly coupled to the orchestration logic.

**When to choose**

- Choose it for order processing, payment workflows, approvals, and systems with long-running business processes.

[Back to top](#top)

### Microservices Architecture

**Characteristics (What)**

- Independent Services: Splits a system into independently deployable services.
- Owned Data: Each service owns a business capability, exposes explicit contracts, and usually owns its own data.

**Principles (Why)**

- Service Autonomy: Enables independent deployment, scaling, ownership, and failure isolation for different parts of a system.

**Benefits (Why choose)**

- Team Independence: Helps teams work independently around clear service boundaries.
- Independent Scaling: Supports different scaling, release, and technology needs per service.

**Weakness**

- Adds distributed system complexity such as networking, retries, observability, and data consistency.
- Debugging and testing end-to-end workflows can become harder across many services.

**When to choose**

- Choose it when service boundaries are stable and teams need independent deployment or scaling.
- See [Microservice Architecture](microservices.md) for more detail.

[Back to top](#top)
