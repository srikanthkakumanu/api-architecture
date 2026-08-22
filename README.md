<a id="top"></a>

# Architecture Learning Hub

This repository is a learning and documentation workspace for API design, modular monolith architecture, and microservice architecture.

Detailed notes live in the [docs](docs/) folder. The root README is intentionally a placeholder and navigation page.

## Table of Contents

- [Learning Goals](#learning-goals)
- [Documentation Index](#documentation-index)
- [Project Index](#project-index)
- [Suggested Reading Path](#suggested-reading-path)
- [Repository Notes](#repository-notes)

## Learning Goals

- Understand API fundamentals and design trade-offs.
- Compare REST, RPC, GraphQL, and event-driven API styles.
- Use `rest-spring` to study REST APIs and Spring Boot application structure.
- Use `grpc` to study RPC contracts and service-to-service communication.
- Document modular monolith and microservice architecture patterns.
- Grow the repository into a practical architecture learning lab.

[Back to top](#top)

## Documentation Index

| Topic                                                              | Document                                                            |
| ------------------------------------------------------------------ | ------------------------------------------------------------------- |
| High-level system architecture, styles continuum, and repository mapping | [Architecture Overview](docs/architecture-overview.md)              |
| Master API design guide, RESTful standards, gRPC, security, and paradigms | [API Design Guide](docs/api-design.md)                              |
| Modular monolith architecture                                      | [Modular Monolith Architecture](docs/modular-monolith.md)            |
| Microservice architecture                                          | [Microservice Architecture](docs/microservices.md)                   |
| Microservice design patterns                                       | [Microservice Design Patterns](docs/microservice-design-patterns.md) |
| Event-driven architecture and asynchronous messaging               | [Event Driven Architecture](docs/event-driven-architecture.md)       |
| How the code projects map to the learning goals                    | [Repository Learning Map](docs/repo-learning-map.md)                 |
| Docs folder landing page                                           | [Docs Index](docs/README.md)                                         |

> [!NOTE]
> All API-related topics—including API fundamentals, request-response paradigms (REST, RPC, GraphQL), API security, and API documentation practices—are consolidated and driven directly from the [API Design Guide](docs/api-design.md).

[Back to top](#top)

## Project Index

| Project                    | Purpose                                                                                                  |
| -------------------------- | -------------------------------------------------------------------------------------------------------- |
| [rest-spring](rest-spring/) | Spring Boot REST TODO API for learning REST, validation, JDBC, Flyway, and layered application structure |
| [grpc](grpc/)               | Java gRPC client/server sample for learning protobuf contracts, generated code, and RPC communication    |

[Back to top](#top)

## Suggested Reading Path

1. [Architecture Overview](docs/architecture-overview.md)
2. [API Design Guide](docs/api-design.md) *(Drives all API fundamentals, paradigms, contracts, documentation, and security)*
3. [Modular Monolith Architecture](docs/modular-monolith.md)
4. [Microservice Architecture](docs/microservices.md)
5. [Microservice Design Patterns](docs/microservice-design-patterns.md)
6. [Event Driven Architecture](docs/event-driven-architecture.md)
7. [Repository Learning Map](docs/repo-learning-map.md)

[Back to top](#top)

## Repository Notes

Every Markdown document should include:

- A table of contents.
- A reusable `<a id="top"></a>` anchor.
- `[Back to top](#top)` links.

[Back to top](#top)
