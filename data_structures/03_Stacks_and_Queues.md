# Stacks and Queues Deep Dive

Stacks and Queues are abstract data types (ADTs). They don't dictate *how* data is stored in memory (you can build them using Arrays or Linked Lists); instead, they dictate the strict *rules* for adding and removing data.

## 1. Stacks (LIFO - Last In, First Out)
Think of a stack of plates. You can only put a new plate on top, and you can only take a plate off the top. The last plate you added is the first one you remove.

### Core Operations:
*   `push(val)`: Adds an item to the top. O(1).
*   `pop()`: Removes and returns the top item. O(1).
*   `peek()`: Returns the top item without removing it. O(1).

### Java Implementations:
*   **Never use `java.util.Stack`:** It extends `Vector`, meaning every single method is `synchronized`. This causes massive thread-contention overhead even in single-threaded apps.
*   **The Standard:** Use `Deque<Integer> stack = new ArrayDeque<>();`. It is an array-backed, highly optimized, non-thread-safe stack implementation.

### Common Use Cases:
*   **The Call Stack:** How the JVM keeps track of method executions and local variables.
*   **Expression Parsing:** Validating matching parentheses (e.g., `{[()]}`).
*   **Backtracking / DFS:** Stacks are the iterative equivalent of Depth-First Search recursion.

### Advanced Pattern: The Monotonic Stack
*   A stack whose elements are strictly increasing or strictly decreasing.
*   **Use Case:** "Next Greater Element" problems. Example: Given an array of daily temperatures, find how many days you have to wait until a warmer day. A monotonic stack solves this in O(N) time instead of O(N^2).

```java
// Next Greater Element — O(N)
int[] result = new int[nums.length];
Deque<Integer> stack = new ArrayDeque<>(); // stores indices
for (int i = 0; i < nums.length; i++) {
    while (!stack.isEmpty() && nums[i] > nums[stack.peek()])
        result[stack.pop()] = nums[i]; // found next greater
    stack.push(i);
}
// remaining indices have no next greater element (result stays 0)
```

### Advanced Pattern: The Monotonic Deque (Sliding Window Maximum)
*   A `Deque` (double-ended queue) whose indices are kept in **decreasing order** of their array values.
*   **Use Case:** Finding the maximum (or minimum) value in every sliding window of a fixed size K. This runs in **O(N)** — better than the O(N log K) heap approach.
*   **Mechanism:** For each new element, pop from the *back* of the deque all indices with values ≤ current (they can never be the window's max). Pop from the *front* any index that has fallen outside the window. The front always holds the index of the current maximum.

```java
// Sliding Window Maximum — O(N)
int[] result = new int[nums.length - k + 1];
Deque<Integer> deque = new ArrayDeque<>(); // stores indices, decreasing by value
for (int i = 0; i < nums.length; i++) {
    // Remove indices outside the window
    if (!deque.isEmpty() && deque.peekFirst() < i - k + 1)
        deque.pollFirst();
    // Remove smaller elements from the back (they'll never be the max)
    while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i])
        deque.pollLast();
    deque.offerLast(i);
    if (i >= k - 1)
        result[i - k + 1] = nums[deque.peekFirst()];
}
```

---

## 2. Queues (FIFO - First In, First Out)
Think of a line at a grocery store. The first person to join the line is the first person to be served.

### Core Operations:
*   `offer(val)` (or `enqueue`): Adds an item to the back (tail). O(1).
*   `poll()` (or `dequeue`): Removes and returns the front item (head). O(1).
*   `peek()`: Returns the front item without removing it. O(1).

### Java Implementations:
*   **General Purpose:** `Queue<Integer> q = new LinkedList<>();` or `new ArrayDeque<>();` (ArrayDeque is faster but cannot hold nulls).
*   **Thread-Safe (Concurrent):** `ConcurrentLinkedQueue` (lock-free) or `ArrayBlockingQueue` (bounded, uses locks).

### Common Use Cases:
*   **Task Scheduling / Buffering:** Print spoolers, Thread Pools (`ThreadPoolExecutor` uses a BlockingQueue).
*   **Message Brokers:** The fundamental concept behind Kafka or RabbitMQ topics.
*   **Breadth-First Search (BFS):** Queues are the backbone of traversing trees or graphs level by level.

---

## 3. Priority Queues (Heaps)
A specialized queue where elements are not dequeued in FIFO order, but according to their **Priority** (e.g., smallest value first, or based on a custom `Comparator`).

*   **Implementation:** Backed by a Binary Heap data structure (an array visualized as a complete binary tree).
*   **Time Complexity:**
    *   `offer()`: O(log N) - Needs to bubble up the tree to maintain heap properties.
    *   `poll()`: O(log N) - Needs to extract the root and bubble down.
    *   `peek()`: O(1) - The highest priority element is always at the root (index 0).
*   **Use Cases:** Dijkstra's Shortest Path algorithm, scheduling time-critical jobs, finding the "Top K" largest elements in a massive dataset.

---

## 4. Time Complexity Cheat Sheet (Stacks & Queues)

| Data Structure | Operation | Amortized Time | Worst-Case Time |
| :--- | :--- | :--- | :--- |
| **Stack** | `push()`, `pop()`, `peek()` | **O(1)** | O(N) (if array-backed and resizing) |
| **Queue** | `offer()`, `poll()`, `peek()` | **O(1)** | O(N) (if array-backed and resizing) |
| **Priority Queue** | `offer()`, `poll()` | **O(log N)** | O(log N) |
| **Priority Queue** | `peek()` | **O(1)** | O(1) |
