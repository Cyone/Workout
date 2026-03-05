# Distributed Systems: CAP Theorem and BASE

When you split a database across multiple nodes to handle massive scale, you enter the realm of Distributed Systems. In this realm, the laws of physics and network reliability enforce strict trade-offs, summarized by the CAP Theorem.

## 1. The CAP Theorem

Formulated by Eric Brewer, the CAP theorem states that a distributed data store can only provide **two out of the following three guarantees** simultaneously:

### 1. Consistency (C)
Every read receives the most recent write, or an error. If a client writes "X=5" to Node A, and immediately queries Node B, Node B *must* return "5". There is no stale data.

### 2. Availability (A)
Every request receives a non-error response, without the guarantee that it contains the most recent write. If Node A goes down, the system stays online and returns data from Node B, even if Node B hasn't received the latest updates yet.

### 3. Partition Tolerance (P)
The system continues to operate despite an arbitrary number of messages being dropped or delayed by the network between nodes (a Network Partition).

---

## 2. The Illusion of Choice

In a true distributed system, **Network Partitions (P) are unavoidable**. Cables get cut, routers fail, and data centers lose connectivity.

Therefore, the CAP theorem isn't a choice of any two; it is a choice between **Consistency (C)** and **Availability (A)** *when* a partition (P) occurs.

### Scenario: Network connection breaks between Node A and Node B.
A client sends a new write to Node A. What does the system do?

### CP Systems (Consistency over Availability)
*   **The Choice:** Node A rejects the write (returns an error/timeout). It refuses to accept data because it knows it cannot sync with Node B, which would result in inconsistent reads later.
*   **Result:** The system is Consistent, but not Available for writes.
*   **Examples:** Traditional Relational Databases (PostgreSQL, MySQL) in synchronous replication mode, MongoDB (single leader), ZooKeeper, HBase.
*   **Use Case:** Financial transactions, billing, where returning an error is preferable to processing a transaction with stale/incorrect state.

### AP Systems (Availability over Consistency)
*   **The Choice:** Node A accepts the write and returns success to the client. Node A and Node B are now out of sync. When the network heals, they will eventually reconcile.
*   **Result:** The system is Highly Available, but reads from Node B will yield stale data (Inconsistent).
*   **Examples:** Cassandra, DynamoDB, CouchDB, Riak.
*   **Use Case:** Social media feeds, shopping carts, recommendation engines—places where keeping the system online and accepting user actions is more important than showing the absolute latest state globally.

---

## 3. ACID vs. BASE

These terms describe the transaction models of databases, aligning closely with CP and AP.

### ACID (Relational SQL Databases)
Favors CP.
*   **A**tomic: All or nothing.
*   **C**onsistent: Database rules are never violated.
*   **I**solated: Concurrent transactions don't interfere.
*   **D**urable: Committed data is saved permanently.

### BASE (NoSQL / Distributed Systems)
Favors AP.
*   **B**asically **A**vailable: The system guarantees availability.
*   **S**oft state: The state of the system may change over time, even without input, due to background replication.
*   **E**ventual consistency: The system will become consistent over time, given that it doesn't receive new input. (If you stop writing to DynamoDB, eventually all nodes worldwide will hold the exact same data).
