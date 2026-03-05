# Big O Notation: Space and Time Complexity

As a Senior Developer, "it works" is not enough. You must be able to mathematically prove *why* your solution is efficient and how it will behave as the dataset grows from 1,000 to 1,000,000 elements.

## 1. What is Big O?
Big O notation describes the **Upper Bound** of an algorithm's growth rate in the **Worst-Case Scenario**. It ignores constants and lower-order terms (e.g., `O(2N + 5)` simplifies to `O(N)`).

## 2. Common Time Complexities (Ordered from Best to Worst)

### O(1) - Constant Time
*   The execution time does not change regardless of the input size.
*   *Examples:* Accessing an array index, pushing/popping from a stack, getting a value from a HashMap (average).

### O(log N) - Logarithmic Time
*   The input size is halved at every step.
*   *Examples:* Binary Search, searching/inserting in a balanced Binary Search Tree (Red-Black Tree).
*   *Efficiency:* Incredibly fast. Searching 1,000,000 items only takes ~20 steps.

### O(N) - Linear Time
*   The time grows proportionally with the input size.
*   *Examples:* Iterating through an array, searching a Linked List, a single loop.

### O(V + E) - Vertices and Edges (Graph Linear Time)
*   The standard complexity for basic Graph algorithms like DFS, BFS, and Topological Sort.
*   It means you visit every Vertex (V) once, and traverse every Edge (E) once. In a sparse graph, this is very fast.

### O(N log N) - Linearithmic Time
*   Usually the result of a "Divide and Conquer" algorithm where each step is linear.
*   *Examples:* Efficient sorting algorithms like Merge Sort, Quick Sort (average), and Heap Sort. This is the fastest possible time complexity for comparison-based sorting.

### O(N^2) - Quadratic Time
*   Nested loops. For every element, you iterate through every other element.
*   *Examples:* Bubble Sort, Insertion Sort, finding duplicates with two loops.
*   *Danger:* This is acceptable for small inputs but becomes unuseable as N grows large.

### O(2^N) - Exponential Time
*   Growth doubles with every addition to the input.
*   *Examples:* Recursive calculation of Fibonacci numbers without memoization, or solving the "Towers of Hanoi."

### O(N!) - Factorial Time
*   The most disastrous complexity.
*   *Examples:* Generating all permutations of a string, or the brute-force "Traveling Salesperson Problem."

---

## 3. Space Complexity
This measures the amount of **extra memory** an algorithm requires as the input grows.
*   **O(1) Space:** You only use a few extra variables (e.g., a counter or two pointers). This is called "In-Place."
*   **O(N) Space:** You create a new array, list, or hash set to store results or track visited elements.
*   **The Trade-off:** Often, you can reduce **Time Complexity** by increasing **Space Complexity** (e.g., using a HashMap to turn an O(N^2) search into an O(N) search).

## 4. Tips for the Interview
1.  **Iterate and Improve:** Start with a "Brute Force" O(N^2) solution. Explicitly state its complexity. Then, ask yourself: "Can I use a Hash Table to make this O(N)? Or a Sort/Binary Search to make it O(log N)?"
2.  **State your assumptions:** "Assuming the input array is already sorted, I can solve this in O(N) time with O(1) space."
3.  **Recursive Complexity:** If you see a recursive function, the time complexity is generally `O(Branches ^ Depth)`. Use memoization to drop this back down to O(N).

---

## 5. Amortized Analysis

Amortized analysis gives the **average cost per operation over a sequence of operations**, even when individual operations are occasionally expensive.

| Data Structure | Operation | Amortized | Worst-Case | Why |
|:---|:---|:---|:---|:---|
| **Dynamic Array (ArrayList)** | `add(element)` | **O(1)** | O(N) | Doubling resize is rare; cost spread over N appends |
| **StringBuilder** | `append(char)` | **O(1)** | O(N) | Same doubling strategy as ArrayList |
| **HashMap** | `put(k, v)` | **O(1)** | O(N) | Rehashing at 75% load; cost amortized over all inserts |
| **Stack (ArrayDeque)** | `push/pop` | **O(1)** | O(N) | Resize only; practically constant |

> "Amortized O(1)" means the **average** across many operations is O(1), even if rare operations take O(N). When an interviewer asks "what is the complexity of ArrayList.add()?", the correct answer is **O(1) amortized**.

---

## 6. Recursive Complexity: The Master Theorem

For divide-and-conquer recursions of the form `T(N) = a * T(N/b) + O(N^d)`:

| Condition | Result | Example |
|:---|:---|:---|
| `d > log_b(a)` | **O(N^d)** | — |
| `d == log_b(a)` | **O(N^d log N)** | Merge Sort: a=2, b=2, d=1 → O(N log N) |
| `d < log_b(a)` | **O(N^(log_b(a)))** | — |

**Quick rules to remember:**
- **Binary Search:** `T(N) = T(N/2) + O(1)` → **O(log N)**
- **Merge Sort:** `T(N) = 2T(N/2) + O(N)` → **O(N log N)**
- **Fibonacci (naive):** `T(N) = T(N-1) + T(N-2) + O(1)` → **O(2^N)** — fix with memoization → **O(N)**
- **Tree DFS/BFS:** Always O(V + E) — you visit each node and each edge exactly once.
