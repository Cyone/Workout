# Heaps and Priority Queues Deep Dive

A Heap is a specialized, complete binary tree that satisfies the **Heap Property**: in a max-heap, every parent is greater than or equal to its children; in a min-heap, every parent is less than or equal to its children. In practice, heaps are almost always implemented as arrays — not explicit tree nodes — making them cache-friendly and memory efficient.

## 1. Core Mechanics: The Array Representation

For a node at index `i` (0-based):
- **Left child:** index `2i + 1`
- **Right child:** index `2i + 2`
- **Parent:** index `(i - 1) / 2`

This array mapping is exact: a "complete binary tree" always fills from left to right, so there are never any gaps in the array.

### Insertion: Sift Up
Add the new element at the end of the array (next available position). Then compare it with its parent and swap if it violates the heap property. Repeat until the property is restored. **O(log N)**.

### Removal (Poll/Extract): Sift Down
Swap the root (the extreme value) with the last element, then remove the last. The new root likely violates the heap property, so compare it with its children and swap with the smaller (min-heap) or larger (max-heap) child. Repeat downward. **O(log N)**.

### Heapify (Build Heap from Array)
Start from the last non-leaf node `(n/2 - 1)` and sift down each element. **O(N)** — counter-intuitively faster than N insertions (O(N log N)).

---

## 2. Java `PriorityQueue`

Java's `PriorityQueue` is a **min-heap** by default.

```java
// Min-heap (default)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Max-heap using reverse comparator
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

// Custom comparator: sort by second element of int[]
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);

minHeap.offer(val);   // Insert O(log N)
minHeap.poll();       // Remove & return min O(log N)
minHeap.peek();       // View min O(1)
minHeap.size();       // O(1)
```

> ⚠️ `PriorityQueue` does **not** guarantee iteration order. Only `poll()` returns elements in sorted order.

---

## 3. Key Interview Patterns

### A. Top K Elements (Min-Heap of size K)
To find the K *largest* elements, maintain a min-heap of size K. For each new element, if it's larger than the heap's minimum (`peek()`), pop the minimum and push the new element. After processing all N elements, the heap contains the K largest.

- **Time:** O(N log K) — much faster than sorting when K << N.
- **Space:** O(K)

```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
for (int num : nums) {
    minHeap.offer(num);
    if (minHeap.size() > k) minHeap.poll(); // evict the smallest
}
// minHeap now holds the K largest elements
```

### B. K-way Merge
Given K sorted lists, use a min-heap seeded with the head of each list. Poll the minimum, then push the next element from that list. Repeat.

- **Time:** O(N log K) where N is total elements.

### C. Two Heaps (Median of Stream)
Maintain a **max-heap** for the lower half and a **min-heap** for the upper half. After every insertion, balance so sizes differ by at most 1. The median is either the top of one heap or the average of both tops.

---

## 4. Time Complexity Cheat Sheet

| Operation | Time Complexity | Space |
|:---|:---|:---|
| **Insert (`offer`)** | **O(log N)** | - |
| **Remove min/max (`poll`)** | **O(log N)** | - |
| **Peek min/max** | **O(1)** | - |
| **Build heap from N elements** | **O(N)** | O(N) |
| **Top K elements** | **O(N log K)** | O(K) |
| **K-way merge (N total)** | **O(N log K)** | O(K) |
