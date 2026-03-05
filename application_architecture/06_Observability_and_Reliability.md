# Observability: Monitoring, Logging, and Tracing

In a distributed system, you can no longer "SSH into the server and check the logs." You need a centralized observability stack to understand why a request failed or why the system is slow.

## 1. The Three Pillars of Observability

### A. Metrics (Monitoring)
*   **What it is:** Numeric data measured over time (e.g., CPU usage, Request Count, Error Rate, 99th Percentile Latency).
*   **The Stack:** **Prometheus** (scrapes metrics from your Spring Boot apps via the `/actuator/prometheus` endpoint) and **Grafana** (visualizes the data in beautiful dashboards).
*   **Golden Signals:** Latency, Traffic, Errors, and Saturation (how "full" your service is).

### B. Centralized Logging
*   **What it is:** The detailed, text-based records of what happened during execution.
*   **The Stack:** **ELK Stack** (Elasticsearch for storage, Logstash for processing, Kibana for searching) or **PLG Stack** (Promtail, Loki, Grafana).
*   **Structured Logging:** Never log plain text. Always log in **JSON format**. This allows Elasticsearch to index specific fields like `userId`, `errorCode`, or `orderId`, making searches incredibly fast.

### C. Distributed Tracing
*   **What it is:** Visualizing the path of a single request as it hops through 10 different microservices.
*   **The Problem:** Log lines from Service A and Service B look unrelated.
*   **The Solution:** **Trace IDs**. When a request enters the system at the API Gateway, a unique `Trace ID` is generated and injected into the headers of every subsequent internal call.
*   **The Stack:** **Zipkin** or **Jaeger**. You can search for a specific `Trace ID` and see a "Gantt chart" showing exactly how much time was spent in each service and where the error occurred.

## 2. Health Checks and Self-Healing
*   **Liveness Probe:** "Is the process running?" If this fails, the orchestrator (Kubernetes) kills and restarts the pod.
*   **Readiness Probe:** "Is the application ready to receive traffic?" (e.g., has it finished loading its cache or connecting to the DB?). If this fails, the load balancer stops sending traffic to this instance.
*   **Spring Boot Actuator:** Provides these endpoints (`/health/liveness`, `/health/readiness`) out of the box.

## 3. Error Budgets and SLOs (SRE Principles)
*   **SLI (Indicator):** The metric you measure (e.g., Availability %).
*   **SLO (Objective):** The target you want to hit (e.g., 99.9% availability).
*   **SLA (Agreement):** The legal contract (e.g., "If we hit 99.0%, we pay you back money").
*   **Error Budget:** If your SLO is 99.9%, you are allowed to be down for ~43 minutes per month. If you've used up your budget, you must stop deploying new features and focus exclusively on stability.
