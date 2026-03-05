# Kafka Transactions and Idempotency

For mission-critical systems (like financial processing), "At-Least-Once" delivery is not enough. You need **Exactly-Once Semantics (EOS)** to ensure that every message is processed once and only once, even if producers or consumers crash.

## 1. Idempotent Producer
A network error occurs after the producer sends a message but before it receives the acknowledgment. The producer retries. Without idempotency, the message is duplicated in the topic.

*   **Mechanism:** `enable.idempotence=true` (Default in modern Kafka).
*   **How it works:** Kafka assigns a unique **Producer ID (PID)** and a **Sequence Number** to every batch of messages. If the broker receives a message with a PID and Sequence Number it has already seen, it discards the duplicate but returns a success acknowledgment to the producer.

## 2. Kafka Transactions
Transactions allow a producer to send a batch of messages to **multiple topics and partitions** atomically. Either all messages are visible to consumers, or none are.

*   **Transactional ID:** You must provide a static `transactional.id` to the producer. This allows the producer to resume a transaction after a crash.
*   **The Transaction Coordinator:** A special internal broker component that manages the transaction state (Ongoing, Prepare_Commit, Committed, Aborted).
*   **The Two-Phase Commit (2PC):**
    1.  **Prepare:** The producer sends messages. The broker marks them as "uncommitted."
    2.  **Commit:** The producer sends a commit command. The Transaction Coordinator writes a "Commit Marker" to the topic partitions.

## 3. Exactly-Once Semantics (EOS)
To achieve true Exactly-Once processing in a "Consume -> Process -> Produce" pipeline (e.g., reading an order, calculating tax, and producing a billing event):

1.  **Consumer Offsets in the Transaction:** Instead of the consumer committing its offset separately, the consumer offset is committed **inside the same transaction** as the output message.
2.  **`isolation.level=read_committed`:** Consumers must be configured with this setting. They will buffer messages and only make them visible to the application once the "Commit Marker" is seen in the topic.

*   **Result:** If the process crashes halfway, the transaction is aborted. No output message is visible, and the consumer offset is not updated. When the process reboots, it re-reads the same input message and tries again.
*   **Performance Cost:** Significant. Transactions increase latency and decrease throughput because of the extra coordination and the fact that consumers must wait for commit markers.
