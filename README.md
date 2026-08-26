<a id="top"></a>

# Architecture Learning Hub

This repository is a learning and documentation workspace for software architecture styles, design patterns, and API design.

Detailed notes live in the [docs](docs/) folder. The root README is intentionally a placeholder and navigation page.

## Table of Contents

- [Learning Goals](#learning-goals)
- [Documentation Index](#documentation-index)
- [Project Index](#project-index)
- [Suggested Reading Path](#suggested-reading-path)
- [Repository Notes](#repository-notes)

## Learning Goals

- Understand how architecture styles shape structure, communication, scalability, and operational trade-offs.
- Compare design patterns that solve common decomposition, communication, data, reliability, and integration problems.
- Understand API fundamentals, API paradigms, and API design trade-offs as part of the larger architecture picture.
- Use `rest-spring` to study REST APIs and Spring Boot application structure.
- Use `grpc` to study RPC contracts and typed communication.
- Grow the repository into a practical architecture learning lab.

[Back to top](#top)

## Documentation Index

| Topic                                                              | Document                                                            |
| ------------------------------------------------------------------ | ------------------------------------------------------------------- |
| High-level system architecture, style comparison, and repository mapping | [Architecture Overview](docs/architecture-overview.md)              |
| Architecture style categories, trade-offs, strengths, and weaknesses | [Architecture Styles](docs/architecture-styles.md)                  |
| API fundamentals, paradigms, contracts, security, and documentation | [API Design Guide](docs/api-design.md)                              |
| How the code projects map to the learning goals                    | [Repository Learning Map](docs/repo-learning-map.md)                 |
| Docs folder landing page                                           | [Docs Index](docs/README.md)                                         |

> [!NOTE]
> API-related topics, including API fundamentals, request-response paradigms, API security, and API documentation practices, are grouped in the [API Design Guide](docs/api-design.md) as supporting material for the broader architecture learning path.

[Back to top](#top)

## Project Index

| Project                    | Purpose                                                                                                  |
| -------------------------- | -------------------------------------------------------------------------------------------------------- |
| [rest-spring](apps/rest-spring/) | Spring Boot REST TODO API for learning REST, validation, JDBC, Flyway, and layered application structure |
| [grpc](apps/grpc/)               | Java gRPC client/server sample for learning protobuf contracts, generated code, and RPC communication    |

[Back to top](#top)

## Suggested Reading Path

1. [Architecture Overview](docs/architecture-overview.md)
2. [Architecture Styles](docs/architecture-styles.md)
3. [API Design Guide](docs/api-design.md) *(API fundamentals, paradigms, contracts, documentation, and security in architectural context)*
4. [Repository Learning Map](docs/repo-learning-map.md)

[Back to top](#top)

## Repository Notes

Every Markdown document should include:

- A table of contents.
- A reusable `<a id="top"></a>` anchor.
- `[Back to top](#top)` links.

[Back to top](#top)
