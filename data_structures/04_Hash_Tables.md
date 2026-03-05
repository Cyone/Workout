# Hash Tables Deep Dive

Hash Tables (often called Hash Maps or Dictionaries) are the most frequently used data structure in enterprise software. They provide astonishing speed for lookups, making them the default solution for caching and relationship mapping.

## 1. Core Mechanics: How O(1) Works
A Hash Table maps **Keys** to **Values**. It achieves O(1) average time complexity through a clever combination of an array and a hashing function.

1.  **The Array:** Under the hood, a Hash Table is just a standard Array of "Buckets."
2.  **The Hash Function:** When you call `put(key, value)`, the Hash Table passes the `key` into a Hash Function (e.g., Java's `hashCode()`). This function deterministically scrambles the key into a massive integer.
3.  **The Index Calculation:** The massive integer is compressed to fit within the bounds of the internal array using the modulo operator: `index = hashCode(key) % array_length`.
4.  **Storage:** The `(key, value)` pair is stored in the array at that specific `index`.
5.  **Retrieval:** When you call `get(key)`, the same math occurs. The system calculates the exact array index instantly and retrieves the value without searching. O(1) time.

## 2. The Collision Problem
Because the internal array is smaller than the infinite number of possible keys, two different keys will eventually hash to the exact same array index. This is a **Collision**.

### Collision Resolution Strategies
*   **Open Addressing (Linear Probing):** If the target index is occupied, simply look at the *next* index (`index + 1`). If that's full, check the next, until you find an empty bucket. *Drawback:* Causes clustering, severely degrading performance.
*   **Separate Chaining (Java's Default):** The bucket at each index doesn't hold a single value; it holds a Linked List. If a collision occurs, the new `(key, value)` pair is simply appended to the Linked List at that bucket.

## 3. Java `HashMap` Internal Optimizations
Interviewers frequently ask how `HashMap` handles worst-case scenarios. If you have terrible luck (or a bad hash function), 1000 keys might all collide at `index = 5`. The linked list at that index becomes 1000 nodes long, reducing `get()` performance from O(1) to O(N).

**The Treeify Threshold (Java 8+):**
To prevent this vulnerability (which can be exploited in DoS attacks), Java monitors the size of the linked lists in the buckets.
*   If a bucket's linked list exceeds **8 elements** (`TREEIFY_THRESHOLD`), Java automatically converts that Linked List into a **Red-Black Tree** (a self-balancing binary search tree).
*   This drops the worst-case search time from O(N) to **O(log N)**.

## 4. Load Factor and Rehashing
If a Hash Table gets too full, collisions happen constantly.
*   **Load Factor:** The ratio of elements to bucket capacity. Java's default is `0.75`. If the map is 75% full, it triggers a resize.
*   **Rehashing:** The `HashMap` creates a new internal array double the size of the old one. It then recalculates the modulo index for *every single element* and moves them to the new array. This is an expensive **O(N)** operation.
*   *Optimization:* If you know you are adding 10,000 items, initialize the map with an adequate capacity (`new HashMap<>(14000)`) to prevent multiple O(N) rehashing pauses.

## 5. HashSet vs. HashMap
A `HashSet` in Java is actually just a `HashMap` under the hood. When you call `set.add(value)`, it calls `map.put(value, DUMMY_OBJECT)`. It just uses the keys of a HashMap to ensure uniqueness.

---

## 6. Time Complexity Cheat Sheet (Hash Tables)

| Operation | Average Time | Worst-Case Time (No Treeify) | Worst-Case Time (Java 8+ Treeify) |
| :--- | :--- | :--- | :--- |
| **Insert (`put`)** | **O(1)** | O(N) | O(log N) |
| **Lookup (`get`)** | **O(1)** | O(N) | O(log N) |
| **Delete (`remove`)**| **O(1)** | O(N) | O(log N) |

## 7. Common Algorithmic Patterns
Hash Tables are frequently combined with other patterns to optimize lookups from O(N) to O(1).

### A. Frequency Map + Sliding Window
*   **Use Case:** Finding substring anagrams, or "Longest Substring with At Most K Distinct Characters".
*   **Mechanism:** Use a HashMap to keep a running count of characters currently inside the window. As the `right` pointer expands, increment the count. As the `left` pointer shrinks, decrement the count.
*   **Optimization:** Often an array `int[] charCounts = new int[256]` is much faster than a `HashMap<Character, Integer>` if the character set is known (e.g., ASCII) because it avoids object boxing and hashing overhead.

### B. Two Sum Complement Lookup
*   **Use Case:** Finding a pair of numbers that satisfy a condition without nested loops.
*   **Mechanism:** As you iterate through the array, calculate the "complement" you need. Check if the complement is already in the map. If not, add the current number to the map so future numbers can find it.
