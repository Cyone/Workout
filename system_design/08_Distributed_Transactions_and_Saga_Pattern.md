# Distributed Transactions and the Saga Pattern

In a monolith, a single database transaction guarantees ACID. In microservices, each service owns its own database — there is no shared transaction boundary. How do you ensure consistency across multiple services?

## 1. The Problem: Distributed Transactions

**Example: E-Commerce Order Flow**
1.  **Order Service** creates an order.
2.  **Payment Service** charges the customer.
3.  **Inventory Service** reserves stock.
4.  **Shipping Service** schedules delivery.

If Payment succeeds but Inventory fails (out of stock), you need to **undo** the payment. A local `@Transactional` can't span across services.

## 2. Two-Phase Commit (2PC) — The Traditional Answer

A coordinator orchestrates the transaction across all participants.

**Phase 1 — Prepare:** Coordinator asks all participants: "Can you commit?"  
**Phase 2 — Commit/Rollback:** If **all** say YES → Coordinator sends COMMIT. If **any** say NO → Coordinator sends ROLLBACK.

### Why 2PC is Problematic for Microservices

| Issue             | Impact                                                    |
|-------------------|-----------------------------------------------------------|
| **Blocking**      | All participants lock resources until the coordinator decides. |
| **Single point of failure** | If the coordinator crashes between Phase 1 and 2, participants are stuck holding locks. |
| **Latency**       | Synchronous coordination across services adds latency.    |
| **Tight coupling**| All services must support the 2PC protocol (XA transactions). |

**Verdict:** 2PC works for databases within the same infrastructure (e.g., two tables in Oracle), but is **not suitable** for independently deployed microservices.

## 3. The Saga Pattern — The Microservices Answer

A Saga is a sequence of **local transactions**. Each service executes its local transaction and publishes an event/message to trigger the next step. If a step fails, **compensating transactions** are executed to undo previous steps.

### Saga: Choreography (Event-Driven)

Each service listens for events and decides what to do. No central coordinator.

```
Order Service: CreateOrder → publishes "OrderCreated"
    ↓
Payment Service: hears "OrderCreated" → ChargeCard → publishes "PaymentCompleted"  
    ↓
Inventory Service: hears "PaymentCompleted" → ReserveStock → publishes "StockReserved"
    ↓
Shipping Service: hears "StockReserved" → ScheduleShipment → publishes "OrderCompleted"

--- If Inventory fails ---
Inventory Service: publishes "StockReservationFailed"
    ↓
Payment Service: hears "StockReservationFailed" → RefundPayment (compensating transaction)
    ↓
Order Service: hears "PaymentRefunded" → MarkOrderFailed (compensating transaction)
```

| Pros                          | Cons                                        |
|-------------------------------|---------------------------------------------|
| Fully decoupled               | Hard to track the overall flow              |
| No single point of failure    | Difficult to debug (events are everywhere)  |
| Easy to add new participants  | Risk of cyclic dependencies between events  |

### Saga: Orchestration (Central Coordinator)

A **Saga Orchestrator** (a dedicated service) tells each participant what to do and handles failure/compensation logic.

```
Saga Orchestrator:
  1. Tell Order Service → CreateOrder
  2. Tell Payment Service → ChargeCard
  3. Tell Inventory Service → ReserveStock
  4. Tell Shipping Service → ScheduleShipment

  On failure at step 3:
  1. Tell Payment Service → RefundPayment
  2. Tell Order Service → CancelOrder
```

| Pros                            | Cons                                    |
|---------------------------------|-----------------------------------------|
| Clear, centralized flow logic   | Orchestrator can become a bottleneck    |
| Easy to monitor and debug       | Risk of becoming a "God service"        |
| Complex compensation logic is manageable | Single point of failure (mitigated by replication) |

## 4. The Transactional Outbox Pattern

**Problem:** After a service commits its local transaction, it needs to publish an event. But what if publishing fails after the DB commit? The local state and the event are inconsistent.

**Solution:** Write the event to an **Outbox table** in the **same** database transaction as the business data. A separate process (CDC or poller) reads the Outbox and publishes to the message broker.

```java
@Transactional
public void createOrder(Order order) {
    // 1. Save business data
    orderRepository.save(order);
    
    // 2. Write event to Outbox (same DB transaction!)
    outboxRepository.save(new OutboxEvent("OrderCreated", order.toJson()));
}

// Separate process (Debezium CDC or scheduled poller):
// Reads Outbox table → Publishes to Kafka → Marks as published
```

**Debezium** (CDC) is the preferred approach — it tails the database WAL/binlog, avoiding polling overhead.

## 5. Choosing Between Choreography and Orchestration

| Factor                    | Choreography           | Orchestration            |
|---------------------------|------------------------|--------------------------|
| **Number of services**    | 2-3 (simple flows)     | 4+ (complex flows)       |
| **Team structure**        | Independent teams      | Centralized platform team|
| **Compensation logic**    | Simple                 | Complex (branching)      |
| **Debugging needs**       | Low (simple flows)     | High (need observability)|

## 6. Interview Tips

*   **Never say "just use a distributed transaction."** Interviewers want to hear Saga + Outbox.
*   Be specific about **compensating transactions** — they're the hardest part ("How do you un-send an email?").
*   Draw the event flow for both choreography and orchestration.
*   Mention **idempotency** — compensating transactions might be received multiple times.
