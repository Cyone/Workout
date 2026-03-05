# CQRS and Event Sourcing

CQRS and Event Sourcing are advanced architectural patterns that shine in systems with complex domain logic, high read/write asymmetry, or audit requirements.

## 1. CQRS — Command Query Responsibility Segregation

### The Core Idea
Split the application into two distinct models:
*   **Command Model (Write):** Handles commands that change state (`CreateOrder`, `UpdateProfile`). Optimized for business rule enforcement and consistency.
*   **Query Model (Read):** Handles queries that return data (`GetOrderDetails`, `ListRecentOrders`). Optimized for fast reads, denormalized views.

```
Client → Command (Write) → Write Model → Write DB (normalized, ACID)
                                            │ (sync or async)
                                            ▼
                                    Event / Projection
                                            │
                                            ▼
Client → Query (Read)  → Read Model  → Read DB (denormalized, fast)
```

### Why Separate Read and Write?
*   **Different optimization needs:** Writes need normalized schemas with constraints. Reads need denormalized views with pre-joined data.
*   **Independent scaling:** Read-heavy systems can scale the read side independently (more read replicas, caching, Elasticsearch).
*   **Simpler models:** The write model focuses purely on invariants. The read model focuses purely on presentation.

### When CQRS is Overkill
*   Simple CRUD applications with balanced read/write ratios.
*   Small teams — the added complexity of maintaining two models may not be justified.
*   When you don't need independent scaling of reads and writes.

## 2. Event Sourcing — The Event Log as Source of Truth

### Traditional Approach (State-Based)
The database stores the **current state** of an entity. Updates overwrite previous values.
```
users table: { id: 1, name: "Alice", email: "alice@new.com" }
-- Previous email is lost forever --
```

### Event Sourcing Approach
The database stores an **append-only log of events** (facts). The current state is derived by replaying events.

```
Event Store:
  1. UserCreated      { id: 1, name: "Alice", email: "alice@old.com" }
  2. EmailChanged     { id: 1, email: "alice@new.com" }
  3. NameChanged      { id: 1, name: "Alice Smith" }

Current state = replay all events → { id: 1, name: "Alice Smith", email: "alice@new.com" }
```

### Benefits of Event Sourcing

| Benefit                 | Description                                                    |
|-------------------------|----------------------------------------------------------------|
| **Complete audit trail**| Every change is recorded as an immutable event. Critical for finance, healthcare. |
| **Time travel**         | Rebuild the state at any point in time by replaying events up to that moment. |
| **Event replay**        | If you add a new read model, replay all events to build it from scratch. |
| **Debugging**           | "Why is this order in state X?" → Look at the event log.       |
| **Decoupling**          | Other services can subscribe to events to build their own projections. |

### Challenges of Event Sourcing

| Challenge              | Mitigation                                                     |
|------------------------|-----------------------------------------------------------------|
| **Rebuilding state is slow** | Use **snapshots** — periodically save the current state. Replay only events after the snapshot. |
| **Schema evolution**    | Events are immutable. Use versioning (`V1`, `V2`) and upcasters to transform old events. |
| **Querying is hard**    | You can't `SELECT * WHERE email = 'x'` on an event log. Use **projections** (read models). |
| **Eventual consistency**| Projections may lag behind the event store.                    |

## 3. Projections — Building Read Models from Events

A **Projection** is a function that processes events and builds a read-optimized view.

```java
// Projection: Build a "UserSummary" read model from events
public class UserSummaryProjection {
    
    @EventHandler
    public void on(UserCreated event) {
        readDb.insert(new UserSummary(event.id, event.name, event.email, 0));
    }
    
    @EventHandler
    public void on(OrderPlaced event) {
        readDb.incrementOrderCount(event.userId);
    }
}

// Read Model (denormalized, pre-computed):
// user_summary table: { id, name, email, total_orders }
// No JOINs needed at query time!
```

You can have **multiple projections** from the same event stream — one for the dashboard, one for search (Elasticsearch), one for analytics.

## 4. CQRS + Event Sourcing Together

They pair naturally but are **independent concepts**:
*   **CQRS without Event Sourcing:** Write to a regular DB, project changes to a read-optimized DB.
*   **Event Sourcing without CQRS:** Store events, rebuild state from events, but use the same model for reads and writes.
*   **Both together:** Commands produce events → Events are stored in an Event Store → Projections consume events to build read models.

### Common Technology Stack

| Component        | Technology                                           |
|------------------|------------------------------------------------------|
| **Event Store**  | EventStoreDB, Axon Server, Kafka (as log), PostgreSQL (append-only table) |
| **Write Model**  | Axon Framework (Java), EventSourcing libs            |
| **Read Model**   | PostgreSQL (denormalized), Elasticsearch, Redis      |
| **Projections**  | Kafka Consumers, Axon Projections, custom listeners  |

## 5. Interview Tips

*   **Start with CQRS alone.** Only introduce Event Sourcing if the interviewer asks about audit trails, time travel, or complex domain logic.
*   Know the **consistency trade-off:** Read models are eventually consistent. Explain how you handle "I just placed an order but don't see it in my list."
*   Frameworks: Mention **Axon Framework** (Java/Kotlin) — it's the most common in-interview answer for JVM-based event sourcing.
*   Event Sourcing is a good answer to "How would you design a banking system?" — every transaction is an immutable event.
