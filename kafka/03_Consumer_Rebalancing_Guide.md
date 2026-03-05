# Deep Dive: Consumer Rebalancing

Rebalancing is the process where a group of consumers negotiates which consumer is responsible for which partition. It is necessary for scalability but is often the source of production latency spikes.

## 1. When does Rebalancing happen?
1.  **Membership Change:** A new consumer joins the group (scale up) or a consumer leaves/crashes (scale down).
2.  **Topic Change:** A topic is added to the subscription or the number of partitions changes.

## 2. The Mechanics (The "Group Coordinator")
One of the Kafka Brokers is elected as the **Group Coordinator** for a specific consumer group.
1.  Consumers send **Heartbeats** to the Coordinator to say "I am alive."
2.  If a consumer misses too many heartbeats (`session.timeout.ms`), the Coordinator considers it dead and triggers a rebalance.

## 3. Rebalancing Strategies

### A. Eager Rebalancing (The "Stop the World" Protocol)
*   **Behavior:** All consumers give up *all* their partitions. Usage stops completely. The Coordinator re-assigns partitions from scratch.
*   **Problem:** If you have a massive state (e.g., Kafka Streams application), rebuilding that state takes time. The application effectively pauses.
*   **Visual:**
    *   *Before:* Consumer A (P0, P1), Consumer B (P2, P3)
    *   *During:* **STOP**. (Nobody owns anything).
    *   *After:* Consumer A (P0, P2), Consumer B (P1, P3)

### B. Cooperative / Incremental Rebalancing (The Modern Standard)
*   **Behavior:** Only partitions that *need* to be moved are revoked. Consumers continue processing their other partitions during the rebalance.
*   **Benefit:** Zero "stop the world" pause for unaffected partitions.
*   **Mechanism:** It happens in two phases (revoking only the specific partitions to be moved, then assigning them to the new owner).

## 4. Static Membership (Advanced Optimization)
By default, every time a consumer restarts (e.g., a rolling deployment), it gets a new `member.id`, triggering a rebalance.
*   **Solution:** Configure `group.instance.id`.
*   **Effect:** The Coordinator recognizes the consumer is the "same" one coming back. If it returns within the `session.timeout.ms`, no rebalance is triggered.
*   **Use Case:** Preventing rebalance storms during Kubernetes rolling updates.

## 5. Common Issues
*   **Long GC Pauses:** If a Java Consumer does a long Garbage Collection "Stop the World" > `session.timeout.ms`, the broker thinks it died and triggers a rebalance.
*   **Slow Processing:** If `poll()` processing takes too long (`max.poll.interval.ms`), the consumer is considered "stuck" and ejected.
