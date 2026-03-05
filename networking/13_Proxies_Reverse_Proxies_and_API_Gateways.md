# Proxies, Reverse Proxies, and API Gateways

These three concepts form the backbone of modern production traffic management. Interviewers often ask about their distinctions, the API Gateway pattern, and how service meshes differ.

## 1. Forward Proxy vs. Reverse Proxy

The distinction is about whose perspective you take.

### Forward Proxy (Client-Side)
A forward proxy sits *in front of clients*, acting on their behalf.
*   **Who configures it?** The client. (You configure your browser or OS to route traffic through a proxy server.)
*   **What does it do?**
    *   **Hide client identity:** The server only sees the proxy's IP, not the client's. (Anonymization, VPNs).
    *   **Content filtering:** Inspect and block outbound requests (corporate firewalls).
    *   **Caching:** Cache responses for multiple internal clients (reduces bandwidth).
*   **Who knows about it?** The client. The server sees requests from the proxy and does not know the original client.
*   **Common tools:** Squid Proxy.

### Reverse Proxy (Server-Side)
A reverse proxy sits *in front of servers*, acting on their behalf.
*   **Who configures it?** The server operator, transparently.
*   **What does it do?**
    *   **Load balancing:** Distribute traffic across multiple backend instances.
    *   **TLS termination:** Decrypt HTTPS traffic so backend servers handle plain HTTP.
    *   **Static file serving:** Serve `index.html`, CSS, JS directly (no need to forward to a Spring Boot app).
    *   **Caching:** Cache and serve popular responses without hitting the origin.
    *   **Compression:** `gzip`/`brotli` compress responses.
    *   **Security:** Acts as a shield — the actual backend server IPs are never exposed externally.
*   **Who knows about it?** The server. The client has no idea. It thinks it's talking to a single server at `api.example.com`.
*   **Common tools:** **NGINX**, HAProxy, Traefik, Envoy.

---

## 2. NGINX as a Production Reverse Proxy

NGINX is one of the most widely used web servers and reverse proxies. Understanding it at a conceptual level is a common senior interview topic.

**Typical Production Setup:**

```
Client → NGINX (Reverse Proxy) → Spring Boot App (Port 8080)
   HTTPS:443  ↘   ↗ HTTP:8080
```

**NGINX handles:**
*   TLS termination (holds the SSL certificate).
*   Serving `/static/*` files directly from disk without proxying.
*   Routing `/api/*` traffic to the backend application.
*   Rate limiting at the network level.
*   `gzip` compression.

**Why not just expose Spring Boot directly?**
*   NGINX's event-driven architecture is far more efficient at handling thousands of concurrent slow connections than a thread-per-connection model.
*   Centralizes TLS management — your Spring Boot app doesn't need to know about certificates.
*   NGINX's configuration is more powerful for URL routing and static asset serving than application code.

---

## 3. API Gateway Pattern

An API Gateway is a specialized, feature-rich reverse proxy that acts as the **single point of entry** for all client requests into a microservices backend.

### Core Responsibilities (Cross-Cutting Concerns)
The API Gateway handles concerns shared by all services, so individual services don't have to:

| Responsibility | Detail |
|---|---|
| **Request Routing** | Route `/users/*` to the User Service, `/orders/*` to the Order Service. |
| **Authentication** | Validate JWT tokens / API keys before forwarding requests. Services trust the Gateway. |
| **Rate Limiting** | Enforce per-customer API quotas. |
| **Aggregation** | Combine multiple downstream service calls into a single response (BFF pattern). |
| **Protocol Translation** | Accept REST from clients, translate to gRPC for internal services. |
| **SSL Termination** | Handle TLS so internal services communicate in plaintext. |
| **Request/Response Transformation** | Add/remove headers, reshape payloads. |
| **Logging & Monitoring** | Centralized access logs, request tracing IDs. |
| **Circuit Breaking** | Stop forwarding to a failing downstream service. |

### Common API Gateways
*   **AWS API Gateway** — Managed, serverless, integrates with Lambda, IAM. No server management.
*   **Kong** — Open source, plugin-based, deployable anywhere. Very popular in Kubernetes environments.
*   **Spring Cloud Gateway** — Java-native, integrates tightly with the Spring ecosystem and service discovery (Eureka).
*   **NGINX Plus / Traefik** — Can serve as both a reverse proxy and a lightweight API gateway.

---

## 4. API Gateway vs. Service Mesh — The Critical Distinction

This is a classic senior interview question. Both involve proxies. What's the difference?

| Dimension | API Gateway | Service Mesh |
|---|---|---|
| **Traffic type** | **North-South** (external client ↔ cluster) | **East-West** (service ↔ service, internal) |
| **Who configures it?** | Platform/API teams | DevOps/SRE / cluster operators |
| **Auth focus** | External clients (JWTs, API keys, OAuth) | Service-to-service identity (mTLS certificates) |
| **Granularity** | Per-route/API level | Per-service-call level |
| **Examples** | Kong, AWS API Gateway, Spring Cloud Gateway | Istio (Envoy sidecar), Linkerd |

**The Complementary Model:** Most production systems use **both**:
1.  An **API Gateway** at the cluster ingress for external traffic (auth, rate limiting, routing).
2.  A **Service Mesh** inside the cluster for zero-trust mTLS between services, observability (distributed tracing), and traffic management (canary deployments, retries).

---

## 5. The Sidecar Proxy Pattern (Service Mesh Internals)

A service mesh deploys a tiny **sidecar proxy** (typically an Envoy proxy) next to every microservice instance, as a separate container in the same Kubernetes Pod.

```
┌─────────────── Pod ────────────────┐
│  ┌──────────────┐  ┌────────────┐  │
│  │ Spring Boot  │  │   Envoy    │  │
│  │   :8080      │←→│  Sidecar   │  │
│  └──────────────┘  │  :15001    │  │
└─────────────────────────────────--─┘
           ↕ mTLS
┌─────────────── Pod ────────────────┐
│  ┌──────────────┐  ┌────────────┐  │
│  │  Order Svc   │  │   Envoy    │  │
│  │   :8080      │←→│  Sidecar   │  │
│  └──────────────┘  └────────────┘  │
└────────────────────────────────────┘
```

*   The Spring Boot application communicates with `localhost` in **plain HTTP** — zero code changes required.
*   The Envoy sidecar intercepts all inbound and outbound traffic, performing mTLS, load balancing, circuit breaking, and telemetry collection transparently.
*   A **control plane** (Istiod in Istio) pushes policy configuration to all Envoy sidecars dynamically, without any restarts.

**Why this matters:** The Spring Boot developer doesn't write networking code. All resilience patterns (retries, circuit breaking, mTLS) are infrastructure-level concerns delegated to the mesh.

---

## 6. Circuit Breaker at the Network Layer

The **Circuit Breaker** pattern (from Resilience4j, Hystrix, or implemented at the service mesh level by Envoy) prevents cascade failures.

**States:**
1.  **Closed (Normal):** Requests flow through. Failure rate is monitored.
2.  **Open (Tripped):** Failure rate exceeded the threshold. Requests are immediately rejected/fail-fast without attempting to contact the failing service. A timeout is started.
3.  **Half-Open (Testing):** After the timeout, a single test request is let through. If it succeeds, the circuit closes. If it fails, it reopens.

**Why it matters for microservices:** Without circuit breaking, Service A continuously makes requests to a failing Service B, blocking threads, piling up connection pool exhaustion, and eventually causing Service A itself to crash — a cascade failure that takes down the entire system. The circuit breaker breaks this chain.
