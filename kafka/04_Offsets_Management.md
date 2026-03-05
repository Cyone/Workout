# Deep Dive: Offsets

Offsets are the mechanism by which Kafka tracks "what has been done." They act as a bookmark in the book (Partition).

## 1. What is an Offset?
*   A monotonically increasing integer (long) assigned to every message as it is written to a partition.
*   **Immutable:** Once assigned, it never changes.

## 2. Types of Offsets
1.  **Log End Offset (LEO):** The offset of the last message written to the partition leader.
2.  **High Watermark:** The offset of the last message that has been successfully replicated to all In-Sync Replicas (ISR). Consumers generally cannot read past the High Watermark (prevents reading "uncommitted" data).
3.  **Consumer Offset:** The offset the consumer group has acknowledged processing.

## 3. Storing Offsets: `__consumer_offsets`
Where does Kafka store the bookmark?
*   **Old Kafka (Zookeeper):** Stored in Zookeeper (Slow, bottleneck).
*   **Modern Kafka:** Stored in a special internal Kafka topic called `__consumer_offsets`.
*   When a consumer "commits" an offset, it is actually sending a message to this internal topic (`Key: GroupID+Topic+Partition, Value: Offset`).

## 4. Committing Strategies
This determines your delivery semantics.

### A. Auto-Commit (`enable.auto.commit=true`)
*   The consumer library automatically commits offsets every `auto.commit.interval.ms` (default 5s).
*   **Risk:** If your app crashes *after* processing a message but *before* the 5s timer hits, you will re-process the message (At-Least-Once).
*   **Risk 2:** If the timer hits *before* you finish processing, and then you crash, the message is lost (At-Most-Once).

### B. Manual Commit
*   You turn off auto-commit and call `commitSync()` or `commitAsync()` in your code.
*   **When:** Usually performed *after* the business logic is successfully completed.
*   **Pros:** Exact control over when a message is considered "done."

## 5. Offset Reset (`auto.offset.reset`)
What happens if a new consumer group starts, or an old group asks for an offset that has been deleted (aged out)?
*   `latest` (Default): Start reading from the end of the log (miss old data, only see new data).
*   `earliest`: Start reading from the beginning of the available log (process all history).
*   `none`: Throw an exception if no previous offset is found.
