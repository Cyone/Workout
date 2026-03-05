# Deep Dive: Log Compaction

Most topics work on a "Retention Policy" (delete data older than 7 days). Log Compaction is different. It is an "Update Semantics" policy.

## 1. The Concept
In a compacted topic, Kafka ensures that for every message **Key**, it retains at least the **last known value**.
*   It acts like a database table (Upsert).
*   If you send `Key: A, Value: 1`, then later `Key: A, Value: 2`, Kafka will eventually delete the message with `Value: 1`.

## 2. Internal Mechanics: Head vs. Tail
The partition log is conceptualized in two parts:
1.  **The Tail (Clean):** This part of the log has been compacted. Keys are unique here.
2.  **The Head (Dirty):** This is where new writes land. It contains duplicates.

## 3. The Cleaner Thread
*   A background thread on the broker (Log Cleaner) wakes up.
*   It reads the "Dirty" section and builds a map of the latest offsets for every key.
*   It then copies the log, keeping only the messages with the highest offsets found in the map.
*   It swaps the old log segment for the new, smaller segment.

## 4. Tombstones (Deleting Data)
How do you delete a key from a compacted topic if Kafka keeps the "latest value"?
*   **The Tombstone:** You send a message with `Key: A, Value: null`.
*   The Cleaner thread sees the `null`. It keeps this "tombstone" for a configurable time (`delete.retention.ms`) to ensure all consumers see that the data was deleted.
*   After that time, the key is removed entirely.

## 5. Use Cases
1.  **Kafka Streams Tables (KTable):** Maintaining the current state of an entity.
2.  **Database Change Data Capture (CDC):** Mirroring a database like PostgreSQL in Kafka. You only care about the *current* row value, not the history of changes from 3 years ago.
3.  **Restoring Application State:** When a microservice boots up, it reads the compacted topic to populate its internal cache from 0 to Now. This is much faster than reading a full history.
