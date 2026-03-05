# Arrays and Strings Deep Dive

Arrays and Strings form the absolute foundation of all data structures. In technical interviews, problems involving them usually test your ability to optimize time and space complexity using specific traversal patterns.

## 1. Arrays in Memory
*   **Contiguous Memory:** Arrays are stored in contiguous blocks of memory. This is their superpower. Given the starting address, accessing any index `i` is a simple mathematical operation: `Address = Start + (i * element_size)`. This results in **O(1) time complexity for random access**.
*   **Cache Locality:** Because they are contiguous, iterating through an array is incredibly fast on modern CPUs due to spatial locality. The CPU cache pre-fetches surrounding elements, minimizing slow RAM access.
*   **Static vs. Dynamic Arrays:** 
    *   *Static Array (`int[] arr`):* Fixed size allocated at creation. You cannot add elements beyond its capacity.
    *   *Dynamic Array (`ArrayList` in Java):* Uses a static array under the hood. When it reaches capacity, it creates a new, larger array (usually 1.5x or 2x the size), copies the old elements over, and discards the old array. The amortized time complexity for appending is O(1), but the worst-case (when resizing occurs) is O(N).

## 2. Strings: The Immutable Array
A String is essentially an array of characters (or bytes, depending on the encoding).
*   **Immutability (Java):** In Java, `String` objects are immutable. `String s = "a" + "b"` does not modify the original `"a"`; it creates an entirely new String `"ab"` and abandons the old ones.
*   **The String Pool:** Java optimizes memory by storing string literals in a special pool in the heap. If you create `String a = "hello"` and `String b = "hello"`, both point to the *exact same object* in the pool.
*   **`StringBuilder` vs. `StringBuffer`:**
    *   If you are concatenating strings in a loop, doing `s += "a"` creates O(N^2) time complexity because a new array is copied every time.
    *   **`StringBuilder`:** Uses a dynamic `char[]` under the hood. It allows O(1) amortized appends. *Not thread-safe.*
    *   **`StringBuffer`:** Same as `StringBuilder` but its methods are `synchronized`. It is thread-safe but slower. (Rarely used in modern applications).

## 3. Crucial Algorithmic Patterns
When facing an Array or String problem, immediately consider these patterns:

### A. The Sliding Window
*   **Use Case:** Finding a contiguous subarray or substring that satisfies a certain condition (e.g., "Longest substring without repeating characters", "Maximum sum subarray of size K").
*   **Mechanism:** Maintain two pointers (`left` and `right`) forming a "window". Expand `right` to include elements until a condition is broken, then shrink `left` until the condition is valid again.
*   **Optimization:** Reduces O(N^2) nested loops into a single O(N) pass.

### B. Two Pointers
*   **Use Case:** Searching for pairs in a sorted array, or comparing elements from both ends (e.g., "Two Sum in sorted array", "Valid Palindrome").
*   **Mechanism:** Place `left` at index 0 and `right` at index N-1. Move them towards the center based on the condition.

### C. Prefix Sums
*   **Use Case:** Rapidly answering multiple sum queries for contiguous subarrays (e.g., "Range Sum Query", "Subarray Sum Equals K").
*   **Mechanism:** Pre-calculate the running sum of the array. The sum of any subarray from index `i` to `j` can be instantly calculated as `prefixSum[j] - prefixSum[i-1]`.
*   **Optimization:** Reduces O(N) repeated range queries to O(1) time.

## 4. Key Terminology: Subarray vs Subsequence
Interviewers frequently test if you understand the difference between these terms.
*   **Subarray / Substring:** A *contiguous* sequence of elements within the array/string. Order is preserved. (e.g., `[2, 3]` is a subarray of `[1, 2, 3, 4]`). Solved using Sliding Window or Prefix Sums.
*   **Subsequence:** A sequence derived by deleting zero or more elements without changing the order of the remaining elements. *Non-contiguous*. (e.g., `[1, 3, 4]` is a subsequence of `[1, 2, 3, 4]`). Solved using Dynamic Programming or Two Pointers.
*   **Subset:** Any possible combination of elements. Order does not matter. Solved using Backtracking.

## 5. Multi-dimensional Arrays (Matrices)
A matrix is simply an array of arrays (e.g., `int[][] matrix = new int[rows][cols]`).
*   **Traversal:** Always involves nested loops. Outer loop iterates through rows (`matrix.length`), inner loop iterates through columns (`matrix[0].length`).
*   **Coordinates:** Usually represented as `(row, col)`. 
*   **Direction Arrays:** When doing BFS/DFS on a grid, use direction arrays to cleanly explore neighbors (Up, Down, Left, Right): 
    `int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};`

## 6. Time Complexity Cheat Sheet (Arrays)
| Operation | Time Complexity | Notes |
| :--- | :--- | :--- |
| **Random Access** | **O(1)** | The superpower of arrays (contiguous memory math). |
| **Search (Unsorted)** | **O(N)** | Must iterate sequentially. |
| **Search (Sorted)** | **O(log N)** | Can use Binary Search. |
| **Append (End)** | **O(1)** | Amortized for dynamic arrays. |
| **Insert / Delete (Middle)** | **O(N)** | Requires shifting all subsequent elements. |
