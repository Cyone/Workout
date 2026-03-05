# Data Pipelines and Stream Processing

Modern applications generate massive amounts of data — user events, logs, IoT telemetry, transactions. Data pipelines move, transform, and deliver this data to analytics, ML models, and operational dashboards.

## 1. Batch vs Stream Processing

| Aspect          | Batch Processing             | Stream Processing              |
|-----------------|------------------------------|--------------------------------|
| **Data**        | Bounded (finite datasets)    | Unbounded (continuous flow)    |
| **Latency**     | Minutes to hours             | Milliseconds to seconds        |
| **Processing**  | MapReduce, Spark             | Kafka Streams, Flink, Kinesis  |
| **Use case**    | Daily reports, ETL, ML training | Real-time alerts, dashboards, fraud |
| **Trigger**     | Scheduled (cron, Airflow)    | Event-driven (continuous)      |

## 2. Lambda Architecture

Combines batch and stream processing to handle all use cases.

```
           ┌─────────────────────┐
           │   Message Bus       │
           │   (Kafka)           │
           └──────┬──────┬───────┘
                  │      │
         ┌────────▼──┐  ┌▼────────────┐
         │ Batch     │  │ Speed       │
         │ Layer     │  │ Layer       │
         │ (Spark)   │  │ (Flink)     │
         └────┬──────┘  └──────┬──────┘
              │                │
         ┌────▼────────────────▼──────┐
         │   Serving Layer            │
         │   (Merged view for queries)│
         └────────────────────────────┘
```

*   **Batch Layer:** Processes all historical data. Produces accurate, complete results (but with latency).
*   **Speed Layer:** Processes real-time data. Produces approximate, low-latency results.
*   **Serving Layer:** Merges both views for queries.

**Downside:** Maintaining two codebases (batch + stream) for the same logic is expensive.

## 3. Kappa Architecture ✅ (Modern Preference)

Uses **only stream processing** for everything. Historical data is reprocessed by replaying the event log (Kafka with long retention).

```
Raw Events → Kafka (immutable log) → Stream Processor (Flink) → Serving Layer
                 ↑
       Replay from beginning for reprocessing
```

**Advantage:** One codebase, simpler to maintain. Kafka's long retention makes replay feasible.

## 4. Windowing in Stream Processing

How do you aggregate data over time in an infinite stream?

| Window Type     | Behavior                                              | Use Case                    |
|-----------------|-------------------------------------------------------|-----------------------------|
| **Tumbling**    | Fixed, non-overlapping intervals (every 5 min)        | Hourly reports              |
| **Sliding**     | Fixed size, slides by interval (5 min window, 1 min slide) | Moving averages         |
| **Session**     | Groups events by activity gaps (no event for 30 min = new session) | User session analytics |

### Late Data and Watermarks

*   **Problem:** Events arrive late (network delays, out-of-order). An event timestamped at `10:01` might arrive at `10:05`.
*   **Watermark:** A marker saying "I believe all events up to time T have arrived." Events arriving after the watermark are **late**.
*   **Allowed lateness:** Define a tolerance window (e.g., 5 minutes). Late events within the window update the result. Events beyond it are dropped or sent to a side output.

## 5. Technology Comparison

| Tool               | Type           | Strengths                                    |
|--------------------|----------------|----------------------------------------------|
| **Apache Flink**   | Stream-first   | True streaming, exactly-once semantics, complex event processing |
| **Apache Spark Streaming** | Micro-batch | Unified batch + stream, large ecosystem |
| **Kafka Streams**  | Library        | Embedded in app (no separate cluster), Kafka-native |
| **AWS Kinesis**    | Managed stream | Serverless, tight AWS integration            |
| **Apache Beam**    | Abstraction    | Write once, run on Flink/Spark/Dataflow      |

## 6. Common Pipeline Patterns

### ETL (Extract, Transform, Load)
```
Source DB → Extract → Transform (clean, enrich) → Load → Data Warehouse
```

### CDC (Change Data Capture)
```
Source DB (WAL/binlog) → Debezium → Kafka → Target (Elasticsearch, warehouse)
```
*   Captures every insert, update, delete from the source database.
*   Enables real-time data replication without polling.

### Event Sourcing Pipeline
```
Events → Event Store (Kafka) → Projections (consumers) → Read Models
```

## 7. Interview Tips

*   **"How would you build a real-time analytics dashboard?"** → Kafka → Flink (tumbling windows, aggregations) → Redis/Elasticsearch → Frontend (WebSocket).
*   **Know Lambda vs Kappa** and why Kappa is preferred in modern systems.
*   When discussing late data, mention **watermarks** and **allowed lateness** — it shows depth.
*   **"How do you reprocess historical data?"** → Replay from Kafka (long retention) through the same stream pipeline. This is the Kappa architecture advantage.
