<a id="top"></a>

# Architecture Learning Notes

This folder organizes the repository notes for learning API design, modular monoliths, and microservice architecture.

## Table of Contents

- [Purpose](#purpose)
- [Reading Path](#reading-path)
- [Documents](#documents)
- [Repository Projects](#repository-projects)
- [Documentation Rules](#documentation-rules)

## Purpose

The root README introduces API paradigms such as REST, RPC, GraphQL, WebHooks, WebSockets, and HTTP streaming. The docs in this folder split those ideas into smaller, focused documents and add architecture notes for modular monolith and microservice learning.

[Back to top](#top)

## Reading Path

1. Start with [API Fundamentals](api-fundamentals.md).
2. Compare request-response styles in [API Paradigms](api-paradigms.md).
3. Review event-driven architecture in [Event Driven Architecture](event-driven-architecture.md).
4. Review documentation practices in [API Documentation](api-documentation.md).
5. Learn security basics in [API Security](api-security.md).
6. Study internal structure with [Modular Monolith Architecture](modular-monolith.md).
7. Study distributed design with [Microservice Architecture](microservices.md).
8. Study recurring solutions in [Microservice Design Patterns](microservice-design-patterns.md).
9. Connect the notes back to code using [Repository Learning Map](repo-learning-map.md).

[Back to top](#top)

## Documents

| Document | Focus |
| --- | --- |
| [API Fundamentals](api-fundamentals.md) | What APIs are, why they exist, and design considerations |
| [API Paradigms](api-paradigms.md) | REST, RPC, GraphQL, and when to use each |
| [Event Driven Architecture](event-driven-architecture.md) | Event-driven architecture, APIs, design patterns, delivery, and consistency |
| [API Documentation](api-documentation.md) | Documentation formats, examples, contracts, and ownership |
| [API Security](api-security.md) | Authentication, authorization, and API security practices |
| [Modular Monolith Architecture](modular-monolith.md) | Module boundaries, data ownership, and deployment as one unit |
| [Microservice Architecture](microservices.md) | Service boundaries, distributed data, integration, and operations |
| [Microservice Design Patterns](microservice-design-patterns.md) | Decomposition, communication, data, reliability, observability, and deployment patterns |
| [Repository Learning Map](repo-learning-map.md) | How `rest-spring` and `grpc` support the learning path |

[Back to top](#top)

## Repository Projects

- `rest-spring`: A Spring Boot REST API for studying request-response APIs, layered architecture, JDBC repositories, validation, and application configuration.
- `grpc`: A Java gRPC client and server for studying RPC contracts, protobuf, generated code, and service-to-service communication.

[Back to top](#top)

## Documentation Rules

- Every Markdown file should include a table of contents.
- Every Markdown file should include a reusable top anchor.
- Every major section should end with a `[Back to top](#top)` link.
- Prefer small, focused documents over one very large file.

[Back to top](#top)
