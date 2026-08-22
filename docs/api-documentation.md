<a id="top"></a>

# API Documentation

This document captures documentation practices for API learning and architecture notes. For actionable API design patterns and contract structures, see the [API Design Guide](api-design.md).

## Table of Contents

- [Purpose](#purpose)
- [Documentation Formats](#documentation-formats)
- [What to Document](#what-to-document)
- [Examples and Contracts](#examples-and-contracts)
- [Architecture Learning Notes](#architecture-learning-notes)

## Purpose

API documentation helps developers understand what an API does, how to call it, how it fails, and how it evolves. Documentation is part of the API contract, not a separate afterthought.

[Back to top](#top)

## Documentation Formats

Common documentation formats include:

- Markdown for repository notes and tutorials.
- reStructuredText for documentation sites.
- OpenAPI for REST contracts.
- Protobuf files for gRPC contracts.
- Architecture Decision Records for important design choices.
- GitHub Pages or Read the Docs for published documentation.

[Back to top](#top)

## What to Document

Good API documentation should explain:

- The purpose of the API.
- The target users or clients.
- Authentication and authorization requirements.
- Request and response examples.
- Error responses and status codes.
- Versioning rules.
- Rate limits and operational constraints.
- Ownership and support expectations.

[Back to top](#top)

## Examples and Contracts

Examples help humans learn quickly. Contracts help systems integrate safely.

For REST APIs, document:

- Resource URLs.
- HTTP methods.
- Request headers.
- Request bodies.
- Response bodies.
- Status codes.

For gRPC APIs, document:

- Protobuf package names.
- Service definitions.
- RPC methods.
- Request and response messages.
- Field compatibility rules.
- Error status conventions.

[Back to top](#top)

## Architecture Learning Notes

In a modular monolith, documentation should describe module boundaries, public module APIs, and ownership of data.

In microservices, documentation should also describe network contracts, schema evolution, deployment ownership, observability, and failure handling.

[Back to top](#top)
