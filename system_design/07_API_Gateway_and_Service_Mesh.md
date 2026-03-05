# API Gateway and Service Mesh

As systems grow from monolith to microservices, two infrastructure patterns become essential: **API Gateways** (managing external traffic) and **Service Meshes** (managing internal service-to-service traffic).

## 1. API Gateway

An API Gateway is a single entry point for all client requests. It sits between external clients and your internal microservices.

### Core Responsibilities

| Function               | Description                                                  |
|------------------------|--------------------------------------------------------------|
| **Routing**            | Routes `/users/**` to User Service, `/orders/**` to Order Service |
| **Authentication**     | Validates JWT tokens, API keys before requests hit services  |
| **Rate Limiting**      | Protects services from abuse (per client, per endpoint)      |
| **Load Balancing**     | Distributes requests across service instances                |
| **Request Transformation** | Converts protocols, aggregates responses, transforms payloads |
| **SSL Termination**    | Handles HTTPS, offloading encryption from backend services   |
| **Caching**            | Caches frequent GET responses to reduce backend load         |
| **Circuit Breaking**   | Stops forwarding to a failing service (fail-fast)            |

### Backend for Frontend (BFF) Pattern
Instead of one generic API Gateway, create **separate gateways per client type**:
*   **Web BFF** — optimized for browser (large payloads, SSR support).
*   **Mobile BFF** — optimized for mobile (compressed payloads, less data).
*   **Third-Party BFF** — simplified public API with strict rate limits.

Each BFF aggregates and transforms data specifically for its client, avoiding the "one-size-fits-all" bloated response problem.

### Popular API Gateways

| Tool                    | Type       | Notes                                     |
|-------------------------|------------|-------------------------------------------|
| **Kong**                | Open-source | Plugin-based, Lua/Go extensible           |
| **AWS API Gateway**     | Managed    | Lambda integration, WebSocket support     |
| **Spring Cloud Gateway**| Java/Kotlin| Reactive (Project Reactor), Spring ecosystem |
| **Envoy**               | Open-source | L7 proxy, used as sidecar in service meshes |
| **Nginx/HAProxy**       | Open-source | Traditional reverse proxy with API GW features |

## 2. Service Mesh

A service mesh manages **service-to-service (east-west) communication** within a cluster. It extracts networking concerns (retries, mTLS, observability) out of application code and into infrastructure.

### The Sidecar Proxy Pattern
Each service instance gets a **sidecar proxy** (e.g., Envoy) deployed alongside it. All inbound and outbound traffic flows through the sidecar.

```
┌──────────────────┐      ┌──────────────────┐
│   Service A      │      │   Service B      │
│  ┌────────────┐  │      │  ┌────────────┐  │
│  │  App Code  │  │      │  │  App Code  │  │
│  └─────┬──────┘  │      │  └─────▲──────┘  │
│        ▼         │      │        │         │
│  ┌────────────┐  │      │  ┌────────────┐  │
│  │  Sidecar   │──┼──────┼──│  Sidecar   │  │
│  │  (Envoy)   │  │      │  │  (Envoy)   │  │
│  └────────────┘  │      │  └────────────┘  │
└──────────────────┘      └──────────────────┘
         ▲                         ▲
         └─────── Control Plane ───┘
                  (Istiod/Linkerd)
```

### What the Service Mesh Handles

| Feature                | Without Mesh (in-app)         | With Mesh (infrastructure)      |
|------------------------|-------------------------------|---------------------------------|
| **mTLS**              | Manually configure certs      | Automatic cert rotation         |
| **Retries/Timeouts**  | Library-level (Resilience4j)  | Configured in mesh policy       |
| **Observability**     | Instrument each service       | Automatic distributed tracing   |
| **Traffic Splitting** | Feature flags in code         | Canary deployments via config   |
| **Circuit Breaking**  | Library per service           | Uniform policy across all       |

### Istio vs Linkerd

| Aspect        | Istio                         | Linkerd                       |
|---------------|-------------------------------|-------------------------------|
| **Proxy**     | Envoy (heavier, more features)| Linkerd2-proxy (Rust, ultra-light) |
| **Complexity**| High (steep learning curve)   | Low (simpler, opinionated)    |
| **Features**  | Full (traffic mgmt, security, observability) | Core features, less extensible |
| **Best for**  | Large orgs needing fine control | Teams wanting simplicity      |

## 3. API Gateway vs Service Mesh

| Concern               | API Gateway            | Service Mesh                |
|------------------------|------------------------|-----------------------------|
| **Traffic type**       | North-South (external) | East-West (internal)        |
| **Deployed as**        | Centralized proxy      | Distributed sidecars        |
| **Auth**               | External client auth   | mTLS between services       |
| **Rate Limiting**      | Per API key/client     | Per service (internal)      |
| **Typically used**     | Always (even monoliths)| When you have many microservices |

**They are complementary, not competing.** A typical architecture uses both:
`Client → API Gateway → Service Mesh (Service A ↔ Service B ↔ Service C)`

## 4. Interview Tips

*   When asked "How do clients talk to your microservices?" → API Gateway.
*   When asked "How do services talk to each other securely?" → Service Mesh with mTLS.
*   Mention **BFF pattern** when the interviewer asks about different client types (mobile vs web).
*   Know that adding a service mesh is **not free** — it adds latency (extra hop through sidecar), memory overhead, and operational complexity. Only justified at scale.
