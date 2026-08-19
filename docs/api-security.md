<a id="top"></a>

# API Security

This document captures API security concepts from the root README and connects them to architecture learning.

## Table of Contents

- [Overview](#overview)
- [Baseline Practices](#baseline-practices)
- [Authentication](#authentication)
- [Authorization](#authorization)
- [Basic Authentication](#basic-authentication)
- [OAuth](#oauth)
- [Architecture Learning Notes](#architecture-learning-notes)

## Overview

API security protects systems, data, users, and service contracts. Security should be part of API design from the beginning, not added only after endpoints exist.

[Back to top](#top)

## Baseline Practices

Common API security practices include:

- Validate all inputs.
- Use TLS for network communication.
- Validate content types.
- Maintain audit logs.
- Protect browser-facing flows from CSRF.
- Protect rendered content from XSS.
- Avoid leaking sensitive implementation details in errors.
- Apply rate limits where abuse is possible.

[Back to top](#top)

## Authentication

Authentication verifies who the caller is. Examples include username and password login, API keys, signed tokens, mutual TLS, and federated identity providers.

Learning question: when you inspect an endpoint, ask how the server knows who is making the request.

[Back to top](#top)

## Authorization

Authorization verifies what an authenticated caller is allowed to do. Authorization should be checked close to the business action being performed, not only at the edge.

Learning question: when you inspect an endpoint, ask whether the caller can access this resource, this action, and this specific data.

[Back to top](#top)

## Basic Authentication

Basic authentication sends an `Authorization` header containing a Base64-encoded username and password pair.

It is simple, but weak for most modern API use cases because:

- Applications may need to store credentials.
- Users cannot revoke access for only one application.
- Credentials often grant broad access.
- It must never be used without TLS.

[Back to top](#top)

## OAuth

OAuth lets users grant access to an application without sharing their password with that application. It also supports scoped permissions, so access can be limited to selected resources or actions.

OAuth is common for third-party integrations, user-delegated access, and APIs where clients need controlled access to user-owned data.

[Back to top](#top)

## Architecture Learning Notes

In a modular monolith, security rules can be centralized while still enforcing module-specific authorization near the domain logic.

In microservices, security must cross network boundaries. Services may need token validation, service identity, delegated user context, and consistent authorization decisions across multiple services.

[Back to top](#top)
