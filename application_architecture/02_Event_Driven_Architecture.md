# Event-Driven Architecture (EDA)

As systems transition to microservices, relying entirely on synchronous REST or gRPC communication creates fragile, tightly coupled architectures. Event-Driven Architecture introduces asynchronous, decoupled communication.

## 1. Synchronous vs. Asynchronous Communication

### The Synchronous Problem
Service A calls Service B via REST and waits for the HTTP response.
*   **Temporal Coupling:** Both services must be up and healthy at the exact same time. If Service B is down, Service A fails (or blocks until it times out).
*   **Latency Chain:** If A calls B, B calls C, and C calls D, the user waits for the sum of all their latencies.
*   **Cascading Failures:** A failure in a downstream service can quickly exhaust thread pools in the upstream services, taking down the entire system.

### The Asynchronous Solution (EDA)
Service A completes its local transaction and publishes an "Event" (a fact that happened, e.g., `OrderPlacedEvent`) to a Message Broker (like Kafka or RabbitMQ). Service A immediately returns success to the user.
*   **Decoupling:** Service B, C, and D listen to the broker and process the event independently.
*   **Resilience:** If Service B is down, the broker holds the message. When Service B reboots, it picks up where it left off. Service A is unaffected.

## 2. Core EDA Patterns

### 1. Publish/Subscribe (Pub/Sub)
*   A publisher broadcasts an event to a topic.
*   Zero, one, or multiple independent consumer groups subscribe to that topic.
*   *Benefit:* Extreme extensibility. If the analytics team wants to start tracking orders, they just spin up a new service and subscribe to the `OrderPlacedEvent` topic. The Order team changes zero code.

### 2. Event Sourcing
Instead of storing the *current state* of an entity in a database table (e.g., an Account table where `balance = 50`), you store an immutable, append-only log of *every event* that led to that state.
*   `AccountCreated(0)` -> `Deposited(100)` -> `Withdrew(50)`.
*   To get the current balance, you load all events and "replay" them.
*   *Benefits:* Perfect audit trail, impossible to lose historical data, and you can reconstruct the system state at any point in time. (Kafka is often used as the primary event store here).

### 3. CQRS (Command Query Responsibility Segregation)
Often paired with Event Sourcing. It dictates separating the write model (Commands) from the read model (Queries).
*   **Command Side:** Receives an `UpdateUser` request, processes business logic, and publishes an event. It writes to an optimized write-database (like an Event Log or a highly normalized SQL DB).
*   **Query Side:** Subscribes to those events and updates a completely different database optimized exclusively for fast reads (like a denormalized MongoDB document or an Elasticsearch index).
*   *Benefit:* You can scale the heavy read-traffic independently of the write-traffic.

## 3. The Challenges of EDA

*   **Eventual Consistency:** The biggest trade-off. When the user clicks "Buy," the Order DB updates instantly, but it might take 200 milliseconds (or minutes, if a service is down) for the Inventory DB to update. UIs must be designed to hide this delay (e.g., "Your order is processing...").
*   **Message Delivery Guarantees:**
    *   *At-Most-Once:* Fire and forget. Messages might be lost.
    *   *At-Least-Once:* Guaranteed delivery, but a network blip might cause a message to be delivered twice. This requires all consumers to be strictly **Idempotent** (processing the same message twice must not alter the final state).
    *   *Exactly-Once:* Very difficult to achieve. Kafka provides transactional guarantees to simulate this.
*   **Tracing:** Debugging an error is difficult when a business flow spans 5 different asynchronous services. Distributed tracing (adding a unique `TraceID` to headers/events) is mandatory.
