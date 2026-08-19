<a id="top"></a>

# Architecture Learning Lab

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
| API fundamentals, API value, and design considerations             | [API Fundamentals](docs/api-fundamentals.md)                         |
| REST, RPC, GraphQL, and request-response APIs                      | [API Paradigms](docs/api-paradigms.md)                               |
| Event-driven architecture, APIs, and design patterns               | [Event Driven Architecture](docs/event-driven-architecture.md)       |
| API documentation practices                                        | [API Documentation](docs/api-documentation.md)                       |
| API security, authentication, authorization, Basic Auth, and OAuth | [API Security](docs/api-security.md)                                 |
| Modular monolith architecture                                      | [Modular Monolith Architecture](docs/modular-monolith.md)            |
| Microservice architecture                                          | [Microservice Architecture](docs/microservices.md)                   |
| Microservice design patterns                                       | [Microservice Design Patterns](docs/microservice-design-patterns.md) |
| How the code projects map to the learning goals                    | [Repository Learning Map](docs/repo-learning-map.md)                 |
| Docs folder landing page                                           | [Architecture Learning Notes](docs/README.md)                        |

[Back to top](#top)

## Project Index

| Project                    | Purpose                                                                                                  |
| -------------------------- | -------------------------------------------------------------------------------------------------------- |
| [rest-spring](rest-spring/) | Spring Boot REST TODO API for learning REST, validation, JDBC, Flyway, and layered application structure |
| [grpc](grpc/)               | Java gRPC client/server sample for learning protobuf contracts, generated code, and RPC communication    |

[Back to top](#top)

## Suggested Reading Path

1. [API Fundamentals](docs/api-fundamentals.md)
2. [API Paradigms](docs/api-paradigms.md)
3. [Event Driven Architecture](docs/event-driven-architecture.md)
4. [API Documentation](docs/api-documentation.md)
5. [API Security](docs/api-security.md)
6. [Modular Monolith Architecture](docs/modular-monolith.md)
7. [Microservice Architecture](docs/microservices.md)
8. [Microservice Design Patterns](docs/microservice-design-patterns.md)
9. [Repository Learning Map](docs/repo-learning-map.md)

[Back to top](#top)

## Repository Notes

Every Markdown document should include:

- A table of contents.
- A reusable `<a id="top"></a>` anchor.
- `[Back to top](#top)` links.

[Back to top](#top)
