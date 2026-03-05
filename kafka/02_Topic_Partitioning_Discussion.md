# Deep Dive: Topics vs. Partitions

In a Kafka interview, understanding the relationship between Topics and Partitions is the litmus test for understanding Kafka's scalability.

## 1. The Core Distinction

*   **Topic (Logical):** A specific stream of data (e.g., `user-logs`, `transactions`). To the producer and consumer, this looks like a single queue.
*   **Partition (Physical):** The fundamental unit of storage and parallelism. A topic is broken down into one or more partitions.

## 2. Partitions: The Unit of Parallelism
This is the most critical concept. A topic with 1 partition can only be consumed by 1 consumer instance in a group effectively.
*   **Scaling Rule:** If you want to consume a topic with 10 consumers in parallel, you need at least 10 partitions.
*   **Physical Layout:** On the broker's disk, a topic doesn't exist as a single file. It exists as a directory of partition logs (e.g., `/tmp/kafka-logs/my-topic-0`, `/tmp/kafka-logs/my-topic-1`).

## 3. Ordering Guarantees (The "Key" to understanding)
Kafka **does NOT** guarantee global ordering across a whole topic.
*   **Within a Partition:** Messages are strictly ordered by offset (0, 1, 2...).
*   **Across Partitions:** There is no ordering guarantee. Message A in Partition 0 and Message B in Partition 1 have no temporal relationship enforced by Kafka.

## 4. Message Placement Strategies
How does a message decide which partition to go to?

### A. Round Robin (No Key)
If the producer sends a message with `key=null`:
*   Kafka will distribute messages evenly across all available partitions.
*   **Pro:** Even load balancing.
*   **Con:** No ordering guarantees for related data.

### B. Semantic Partitioning (With Key)
If the producer sends a message with `key=user_123`:
*   Kafka runs a hashing algorithm: `hash(key) % num_partitions`.
*   **Guarantee:** All messages with the same key (e.g., `user_123`) will ALWAYS go to the same partition.
*   **Why this matters:** This ensures strict ordering for that specific user. You will process "User Created" before "User Updated" because they land in the same partition.

## 5. Interview Gotcha: Changing Partition Count
*   **Question:** "What happens if I resize a topic from 10 to 20 partitions?"
*   **Answer:** You break the ordering guarantee for keyed messages. The formula `hash(key) % 10` produces a different result than `hash(key) % 20`. Old data for `user_123` might be in Partition 4, but new data might land in Partition 14.
*   **Fix:** Avoid resizing keyed topics, or perform a migration/re-keying strategy.
