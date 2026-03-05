# Linked Lists Deep Dive

Linked Lists are the foundational dynamic data structure. While arrays prioritize fast access via contiguous memory, Linked Lists prioritize fast insertion and deletion by sacrificing contiguous memory.

## 1. Core Mechanics
A Linked List is a collection of independent objects called **Nodes**. Each node contains:
1.  **Data:** The actual value being stored.
2.  **Pointer(s):** A memory reference pointing to the next (and optionally, previous) node in the sequence.

### Types of Linked Lists
*   **Singly Linked List:** Each node only points to the `next` node. Traversal can only happen in one direction (forward).
*   **Doubly Linked List:** Each node points to both the `next` and the `prev` (previous) node. This requires more memory per node but allows backward traversal and O(1) deletion if you already have the node reference.
*   **Circular Linked List:** The `next` pointer of the last node points back to the `head` (first node), forming a loop.

## 2. Time Complexity Trade-offs
Understanding when to use a Linked List over an Array is a common interview topic.

| Operation | Array (Dynamic) | Linked List | Why? |
| :--- | :--- | :--- | :--- |
| **Access (Get i-th)** | **O(1)** | O(N) | Arrays do math on contiguous memory. Lists must traverse node by node from the head. |
| **Search (Value)** | O(N) | O(N) | Both require checking every element (unless the array is sorted, allowing O(log N) binary search). |
| **Insert/Delete (End)**| O(1)* | **O(1)** | Arrays append instantly (*amortized). Lists with a `tail` pointer append instantly. |
| **Insert/Delete (Start)**| O(N) | **O(1)** | Inserting at index 0 in an array requires shifting *every* element right. In a list, you just update two pointers. |

*   **Memory Overhead:** Linked Lists have a higher memory footprint per element because every value must be bundled with a 64-bit reference pointer (or two, for doubly linked).
*   **Cache Locality:** Linked List nodes are scattered randomly across the heap. This causes frequent CPU cache misses, making them slower to iterate through than arrays in real-world benchmarks, even if the Big-O time complexity is theoretically the same.

## 3. Common Algorithmic Patterns
Linked List problems rarely require complex data manipulation; they are almost exclusively tests of **pointer manipulation**.

### A. The Fast and Slow Pointers (Floyd's Cycle Detection)
- **Use Case:** Finding the middle of a linked list, or detecting if a linked list has a cycle.
- **Mechanism:** `slow` advances 1 node per step; `fast` advances 2. If a cycle exists, they meet. If not, `slow` lands at the midpoint.

```java
ListNode slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
// slow is now at the middle
```

### B. The Dummy Node (Sentinel)
- **Use Case:** Simplifies edge cases when the `head` itself might change (merge, delete, reverse).
- **Mechanism:** A fake `ListNode dummy = new ListNode(0); dummy.next = head;` anchors the result. Return `dummy.next` at the end.

```java
// Merge two sorted lists
ListNode dummy = new ListNode(0);
ListNode cur = dummy;
while (l1 != null && l2 != null) {
    if (l1.val <= l2.val) { cur.next = l1; l1 = l1.next; }
    else                  { cur.next = l2; l2 = l2.next; }
    cur = cur.next;
}
cur.next = (l1 != null) ? l1 : l2;
return dummy.next;
```

### C. Reversing a Linked List
- **Use Case:** Palindrome check, reverse a sub-list (Reverse Nodes in k-Group), reorder list.
- **Caution:** Save `next` before breaking any pointer links.

```java
ListNode prev = null, curr = head;
while (curr != null) {
    ListNode next = curr.next; // save next
    curr.next = prev;          // reverse the pointer
    prev = curr;               // advance prev
    curr = next;               // advance curr
}
return prev; // new head
```

---

## 4. Skip Lists (Bonus — Advanced)
A Skip List is a Linked List augmented with multiple levels of "express lanes" (additional forward pointers that skip over many nodes). Each level has roughly half the nodes of the level below.

- **Average Search/Insert/Delete:** O(log N) — comparable to a balanced BST.
- **Advantage over BST:** Simpler to implement, lock-friendly for concurrent access.
- **Used in:** Redis (sorted sets), LevelDB (skiplist memtable).

```
Level 3:  1 ---------> 9 ---------> null
Level 2:  1 --> 4 ----> 9 --> 20 -> null
Level 1:  1 -> 3 -> 4 -> 7 -> 9 -> 20 -> null (base list)
```

> Skip Lists are rarely asked in LeetCode-style interviews but appear in systems design discussions when you need an ordered concurrent data structure without global locking.
