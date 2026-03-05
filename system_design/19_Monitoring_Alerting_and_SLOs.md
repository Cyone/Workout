# Monitoring, Alerting, and SLOs

You can't improve what you can't measure. Observability is the practice of understanding the **internal state** of a system by examining its **external outputs** — logs, metrics, and traces.

## 1. The Three Pillars of Observability

| Pillar       | What                                    | Tool Examples                     |
|--------------|-----------------------------------------|-----------------------------------|
| **Metrics**  | Numeric measurements over time (counters, gauges, histograms) | Prometheus, Datadog, CloudWatch |
| **Logs**     | Discrete events with context            | ELK Stack, Loki, CloudWatch Logs  |
| **Traces**   | Request path across distributed services| Jaeger, Zipkin, AWS X-Ray, Tempo  |

### The Four Golden Signals (Google SRE)

Every service should track these four metrics:

| Signal        | What It Measures                          | Example                          |
|---------------|-------------------------------------------|----------------------------------|
| **Latency**   | Time to serve a request                   | p50=20ms, p95=100ms, p99=500ms  |
| **Traffic**   | Demand (requests per second)              | 1500 QPS                        |
| **Errors**    | Rate of failed requests                   | 0.5% of requests return 5xx     |
| **Saturation**| How full your resources are               | CPU 80%, heap 90%, DB connections 95% |

## 2. SLI, SLO, and SLA

| Term    | Definition                                  | Example                             |
|---------|---------------------------------------------|--------------------------------------|
| **SLI** | Service Level **Indicator** — the metric    | "99.5% of requests complete in <200ms" |
| **SLO** | Service Level **Objective** — the target    | "Availability SLO: 99.9% uptime"    |
| **SLA** | Service Level **Agreement** — contractual   | "If uptime drops below 99.9%, customer gets credits" |

**Relationship:** SLIs are measured → compared against SLOs → SLAs are the business consequences of missing SLOs.

### Defining Good SLOs

1.  **Availability SLO:** `successful_requests / total_requests >= 99.9%`
    *   99.9% = 43.8 minutes of downtime per month.
    *   99.99% = 4.38 minutes of downtime per month.
2.  **Latency SLO:** `p99 latency < 500ms for 99% of time windows`
3.  **Freshness SLO (for data systems):** `data available within 5 minutes of production`

## 3. Error Budgets

The **error budget** is the allowed amount of unreliability: `Error Budget = 1 - SLO`.

*   SLO = 99.9% → Error Budget = 0.1% → ~43 minutes/month of allowed downtime.
*   If the budget is not exhausted → team can ship risky features.
*   If the budget is exhausted → freeze deployments, focus on reliability.

**Error budgets align incentives:** Developers want to ship fast. SREs want stability. Error budgets give a quantitative balance.

## 4. Structured Logging

**Unstructured (bad):**
```
2024-01-15 10:23:45 ERROR Failed to process order 12345 for user 67890
```

**Structured (good):**
```json
{
  "timestamp": "2024-01-15T10:23:45Z",
  "level": "ERROR",
  "service": "order-service",
  "traceId": "abc123",
  "userId": "67890",
  "orderId": "12345",
  "message": "Failed to process order",
  "error": "InsufficientFundsException",
  "duration_ms": 245
}
```

**Benefits:** Machine-parseable, filterable (`userId=67890 AND level=ERROR`), correlatable via `traceId`.

## 5. Distributed Tracing

In microservices, a single user request traverses multiple services. A **trace** captures the entire journey.

```
Trace ID: abc123
├── Span: API Gateway (15ms)
│   └── Span: User Service (8ms)
│       └── Span: PostgreSQL Query (3ms)
│   └── Span: Order Service (120ms)
│       └── Span: Payment Service (90ms)
│           └── Span: Stripe API (80ms)  ← bottleneck!
│       └── Span: Inventory Service (10ms)
```

**Implementation:**
1.  **Trace propagation:** Pass `traceId` and `spanId` in HTTP headers (`traceparent` in W3C Trace Context).
2.  **Instrumentation:** Auto-instrument with OpenTelemetry agents or manually instrument critical paths.
3.  **Collect and visualize:** Export spans to Jaeger/Tempo/Zipkin for flame graph visualization.

### OpenTelemetry (OTel)

*   **The standard** for observability instrumentation. Vendor-neutral.
*   Provides SDKs for Java, Python, Go, etc.
*   Exports to any backend (Jaeger, Prometheus, Datadog, etc.).
*   Replaces older tools (Jaeger client, Zipkin client) with one unified API.

## 6. Alerting Best Practices

### Alert on Symptoms, Not Causes
*   ❌ "CPU > 80%" — too noisy, may not affect users.
*   ✅ "Error rate > 1% for 5 minutes" — directly impacts users.

### Alert Tiers

| Tier       | Severity        | Action                            |
|------------|-----------------|-----------------------------------|
| **P1**     | Service down     | Page on-call immediately          |
| **P2**     | Degraded         | Notify in Slack, investigate soon |
| **P3**     | Warning          | Review in next business day       |

### Reduce Alert Fatigue
*   **Group related alerts** — don't fire 10 alerts for the same incident.
*   **Auto-resolve** alerts when the condition clears.
*   **Runbooks:** Link every alert to a runbook with investigation steps.

## 7. Metrics Architecture (Prometheus + Grafana)

```
Services → /metrics endpoint (Prometheus format)
    ↓
Prometheus (scrapes every 15s, stores time-series)
    ↓
Grafana (dashboards, queries via PromQL)
    ↓
Alertmanager (routes alerts to Slack, PagerDuty)
```

### Key Metric Types
*   **Counter:** Monotonically increasing (total requests, total errors).
*   **Gauge:** Current value, goes up/down (CPU %, active connections).
*   **Histogram:** Distribution of values (request latency buckets).

## 8. Interview Tips

*   **"How do you monitor your services?"** → Four Golden Signals + Prometheus + Grafana + distributed tracing with OpenTelemetry.
*   **"How do you set reliability targets?"** → SLIs → SLOs → Error Budgets.
*   **"How do you debug a slow request in microservices?"** → Distributed tracing. Look at the trace to find which service/query is the bottleneck.
*   Mention **structured logging with correlation IDs** — ties logs across services to a single request.
