# Database Replication Patterns

Replication copies data across multiple nodes for availability (survive failures) and performance (distribute reads). The choice of replication topology defines the consistency and availability trade-offs of your system.

## 1. Single-Leader (Primary-Secondary) Replication

One node (**leader/primary**) accepts all writes. Changes are replicated to **followers/secondaries** which serve reads.

```
Client (Write) → Leader → Replication Stream → Follower 1 (Read)
                                              → Follower 2 (Read)
                                              → Follower 3 (Read)
```

### Synchronous vs Asynchronous Replication

| Mode             | Behavior                                               | Trade-off                    |
|------------------|---------------------------------------------------------|------------------------------|
| **Synchronous**  | Leader waits for follower ACK before confirming write   | Strong consistency, higher latency |
| **Asynchronous** | Leader confirms write immediately, replicates later     | Better performance, risk of data loss |
| **Semi-synchronous** | 1 follower synchronous, rest async                  | Balance of safety and speed  |

### Replication Lag
Async replication means followers may be slightly behind the leader. This creates:
*   **Read-after-write inconsistency:** User writes data, then reads from a follower that hasn't caught up yet → sees stale data.
*   **Monotonic read violations:** Two consecutive reads hit different followers → user sees data "go backwards."

**Mitigations:**
*   Read-your-own-writes: Route reads to the leader for recently modified data.
*   Sticky sessions: Same user always reads from the same follower.
*   Causal consistency: Track timestamps/versions to ensure monotonic reads.

### Failover
When the leader dies:
1.  **Detection:** Followers detect leader absence (missed heartbeats).
2.  **Election:** A follower is promoted to leader (manual or automatic).
3.  **Reconfiguration:** Clients and other followers point to the new leader.

**Risk during failover:**
*   Async followers may have unreplicated writes → data loss.
*   Split-brain: Two nodes believe they're the leader → conflict.

## 2. Multi-Leader Replication

Multiple nodes accept writes. Each leader replicates to the others.

```
Leader A (US-East) ←→ Leader B (EU-West) ←→ Leader C (AP-Southeast)
```

**Use case:** Multi-region deployments where users need low-latency writes in their region.

### Conflict Resolution

When two leaders modify the same record simultaneously:

| Strategy                   | How                                               |
|----------------------------|----------------------------------------------------|
| **Last Write Wins (LWW)**  | Highest timestamp wins. Simple but loses data.     |
| **Custom merge logic**     | Application resolves conflicts (e.g., merge fields)|
| **CRDTs**                  | Conflict-free data types that auto-merge (counters, sets) |
| **Versioning (vector clocks)** | Track causal history, surface conflicts to user |

**CRDTs (Conflict-free Replicated Data Types):**
*   G-Counter: Grow-only counter. Each node increments its own counter. Merge = sum of all counters.
*   LWW-Register: Last-writer-wins register. Simple but lossy.
*   OR-Set: Observed-Remove set. Handles concurrent add/remove without conflicts.

## 3. Leaderless Replication

No designated leader. Any node can accept reads and writes. The client sends requests to **multiple nodes**.

**Used by:** Cassandra, DynamoDB, Riak.

### Quorum Reads and Writes

*   `N` = total replicas (e.g., 3)
*   `W` = write quorum (nodes that must ACK a write, e.g., 2)
*   `R` = read quorum (nodes that must respond to a read, e.g., 2)

**Consistency rule:** `W + R > N` → at least one node in the read set has the latest write.

```
N=3, W=2, R=2:
Write to Node A, B (quorum met) → read from Node B, C (B has latest) → consistent!
```

### Sloppy Quorum and Hinted Handoff
*   **Sloppy quorum:** If the "home" node is unavailable, write to a temporary node.
*   **Hinted handoff:** The temporary node sends the data to the correct node when it recovers.
*   Improves availability at the cost of weaker consistency guarantees.

### Anti-Entropy and Read Repair
*   **Read repair:** On read, if a node returns stale data, the coordinator sends the latest version to that node.
*   **Anti-entropy (Merkle trees):** Background process compares data across replicas and fixes discrepancies.

## 4. Comparison Table

| Aspect             | Single-Leader        | Multi-Leader          | Leaderless            |
|--------------------|----------------------|-----------------------|-----------------------|
| **Write target**   | One node             | Multiple designated   | Any node              |
| **Consistency**    | Strongest (if sync)  | Eventual (conflicts)  | Tunable (quorum)      |
| **Availability**   | Degraded on failover | High (multi-region)   | Highest               |
| **Complexity**     | Low                  | High (conflict mgmt)  | Medium                |
| **Used by**        | PostgreSQL, MySQL    | CockroachDB (multi-DC)| Cassandra, DynamoDB   |

## 5. Interview Tips

*   **"How do you replicate data across regions?"** → Multi-leader for low-latency regional writes, or single-leader with async cross-region followers for reads.
*   **"What happens when the primary goes down?"** → Describe failover: detection, election, reconfiguration, and the risk of unreplicated writes.
*   **Know the quorum formula** `W + R > N` — explain it with a concrete example.
*   When discussing DynamoDB or Cassandra, mention **leaderless replication** with tunable consistency (`ONE`, `QUORUM`, `ALL`).
