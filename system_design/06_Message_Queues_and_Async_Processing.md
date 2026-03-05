# Message Queues and Async Processing

Synchronous request-response is simple but fragile. If a downstream service is slow or down, the entire call chain blocks. Message queues decouple producers from consumers, enabling asynchronous, resilient, and scalable architectures.

## 1. Queue vs Topic (Point-to-Point vs Pub/Sub)

| Model             | Behavior                                          | Use Case                          |
|--------------------|---------------------------------------------------|-----------------------------------|
| **Queue** (P2P)   | Message consumed by **exactly one** consumer.     | Task distribution, job processing |
| **Topic** (Pub/Sub)| Message delivered to **all** subscribers.         | Event broadcasting, notifications |

## 2. Delivery Guarantees

| Guarantee          | Meaning                                                   | Trade-off               |
|--------------------|-----------------------------------------------------------|-------------------------|
| **At-Most-Once**   | Message may be lost, but never delivered twice.            | Fast, no ACK needed     |
| **At-Least-Once**  | Message always delivered, but may be duplicated.           | Requires idempotency    |
| **Exactly-Once**   | Message delivered exactly once. (Extremely hard in distributed systems.) | Kafka transactions, high overhead |

**In practice:** Most systems use **at-least-once + idempotent consumers**.

## 3. Key Concepts

### Dead Letter Queue (DLQ)
Messages that fail processing after `N` retries are moved to a DLQ for manual inspection. Without a DLQ, poison messages block the entire queue.

### Backpressure
When consumers can't keep up with producers:
*   **Bounded queues:** Reject or block producers when the queue is full.
*   **Flow control:** Dynamically adjust the producer's send rate.
*   **Auto-scaling:** Spin up more consumer instances based on queue depth.

### Message Ordering
*   **Kafka:** Guarantees ordering within a partition (not across partitions).
*   **SQS Standard:** No ordering guarantee. **SQS FIFO:** Ordered within message groups.
*   **RabbitMQ:** Ordered per queue, but not across queues.

### Visibility Timeout / Acknowledgment
*   After a consumer picks up a message, the queue hides it for a timeout period.
*   If the consumer doesn't ACK within the timeout, the message becomes visible again for re-processing.
*   Prevents message loss if a consumer crashes mid-processing.

## 4. Technology Comparison

| Feature               | Kafka                    | RabbitMQ                  | AWS SQS                |
|------------------------|--------------------------|---------------------------|------------------------|
| **Model**             | Distributed log (Pub/Sub) | Message broker (both)     | Managed queue          |
| **Ordering**          | Per-partition             | Per-queue                 | FIFO queues only       |
| **Retention**         | Configurable (days/weeks) | Until consumed            | Up to 14 days          |
| **Throughput**        | Very high (millions/sec)  | Moderate (tens of thousands) | High (managed)      |
| **Consumer groups**   | Yes (built-in)           | Requires exchanges/bindings | No native concept     |
| **Replay**            | Yes (offset-based)       | No (message deleted after ACK) | No                |
| **Best for**          | Event streaming, logs, CDC | Task queues, RPC, routing | Simple async decoupling |

## 5. Common Async Patterns

### Fire-and-Forget
Producer sends a message and doesn't wait for a result. Used for logging, analytics, non-critical notifications.

### Request-Reply (Async RPC)
Producer sends a request message with a `correlationId` and listens on a reply queue. Used when async processing still needs a result (e.g., payment processing).

### Event-Driven Saga
Multiple services coordinate through a sequence of events on a message bus. Each service publishes an event after completing its step. (See file `08_Distributed_Transactions_and_Saga_Pattern.md`).

### Fan-Out
One event triggers multiple independent consumers. Example: An `OrderPlaced` event triggers inventory service, email service, and analytics service simultaneously.

## 6. When to Use a Message Queue

| Scenario                                        | Queue Helps?  |
|-------------------------------------------------|---------------|
| Decoupling two services with different SLAs      | ✅ Yes        |
| Smoothing traffic spikes (write buffer)          | ✅ Yes        |
| Distributing CPU-intensive tasks across workers  | ✅ Yes        |
| Real-time, low-latency request-response          | ❌ No (Use sync HTTP/gRPC) |
| Simple CRUD with one DB                          | ❌ No (Overengineering) |

## 7. Interview Tips

*   **"How would you handle a slow downstream service?"** → Introduce a message queue between them. The producer writes to the queue and returns immediately; the consumer processes at its own pace.
*   **"What if the consumer crashes?"** → Visibility timeout + DLQ. The message re-appears after timeout and is eventually moved to DLQ after max retries.
*   **Know when NOT to use queues.** Adding a queue to a simple CRUD flow adds unnecessary complexity, latency, and operational burden.
