<a id="top"></a>

# Simple gRPC Server and Client

This project is a Java gRPC sample demonstrating Protocol Buffer contract definitions, automated code generation, and client/server RPC communication.

> [!NOTE]
> All RPC design guidelines, Protocol Buffer standards, and API comparisons are driven from the master [API Design Guide](../docs/api-design.md).

---

## Table of Contents

- [Pre-requisites](#pre-requisites)
- [Steps to Execute](#steps-to-execute)
- [Architecture & API Design Reference](#architecture--api-design-reference)

[Back to top](#top)

---

## Pre-requisites

1. Oracle Java or OpenJDK (Latest LTS version)
2. Gradle (Latest version)

[Back to top](#top)

---

## Steps to Execute

1. Navigate to the `grpc` root directory (where `settings.gradle` exists).
2. Build and install distribution binaries:
   ```bash
   ./gradlew installDist
   ```
3. Start the gRPC server:
   ```bash
   ./app/build/install/app/bin/hello-server
   ```
4. In a separate terminal window, run the gRPC client:
   ```bash
   ./app/build/install/app/bin/hello-client World
   ```

[Back to top](#top)

---

## Architecture & API Design Reference

For in-depth contract design principles, Protobuf schema evolution rules, and service-to-service RPC patterns, see the master [API Design Guide](../docs/api-design.md).

[Back to top](#top)
