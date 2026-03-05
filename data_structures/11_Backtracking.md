# Backtracking Deep Dive

Backtracking is a systematic method for exploring all possible solutions by incrementally building candidates and **abandoning** (backtracking from) a candidate the moment it is determined it cannot lead to a valid solution. It is essentially a DFS on a decision tree, with pruning.

## 1. The Core Template

Every backtracking solution follows this pattern:

```java
void backtrack(int start, List<Integer> current, /* ...params... */) {
    // 1. Base case: a complete solution has been found
    if (isSolution(current)) {
        result.add(new ArrayList<>(current)); // add a COPY
        return;
    }

    // 2. Explore all choices
    for (int i = start; i < choices.length; i++) {
        // 3. Pruning: skip invalid choices early
        if (!isValid(choices[i], current)) continue;

        // 4. Make a choice
        current.add(choices[i]);

        // 5. Recurse
        backtrack(i + 1, current, ...); // or (i, ...) for reuse

        // 6. Undo the choice (BACKTRACK)
        current.remove(current.size() - 1);
    }
}
```

> 🔑 **The critical insight:** `current.remove(current.size() - 1)` at step 6 is the "backtrack" step. It undoes the choice made in step 4, restoring state for the next loop iteration.

---

## 2. Four Core Problem Types

### A. Subsets (Power Set)
**Goal:** Generate all 2^N subsets of N elements.
**Key:** At each index, you decide to **include** or **exclude** the element. No sorting trick needed unless there are duplicates.

```java
// Subsets II (with duplicates): sort first, skip duplicate siblings
Arrays.sort(nums);
if (i > start && nums[i] == nums[i - 1]) continue;
```

### B. Permutations
**Goal:** Generate all N! orderings of N elements.
**Key:** Unlike subsets, order matters. Use a `boolean[] used` array (or swap-based approach) instead of a `start` index — each position can use any unused element.

```java
if (used[i]) continue;
used[i] = true;
backtrack(current, used);
used[i] = false;
```

### C. Combinations / Combination Sum
**Goal:** Pick K elements from N, or find all subsets that sum to a target.
**Key:** Pass a `start` index to avoid revisiting elements. For "unlimited use" (Combination Sum I), recurse with `i` not `i+1`.

### D. Board / Constraint-based (N-Queens, Sudoku)
**Goal:** Place elements on a board satisfying strict constraints.
**Key:** Use sets/arrays to track occupied rows, columns, and diagonals. Prune immediately when a position is invalid.

```java
// N-Queens: O(1) constraint checking
Set<Integer> cols = new HashSet<>(), diag1 = new HashSet<>(), diag2 = new HashSet<>();
// For row r, col c: diag1 key = r-c, diag2 key = r+c
if (cols.contains(c) || diag1.contains(r-c) || diag2.contains(r+c)) continue;
```

---

## 3. Pruning: The Performance Multiplier

Backtracking visits every candidate; pruning cuts branches early so they're never visited.

| Problem | Pruning Condition |
|:---|:---|
| Combination Sum | `remaining < 0` → stop recursing |
| N-Queens | Column/diagonal already occupied |
| Palindrome Partitioning | Current substring is not a palindrome |
| Word Search | Cell out of bounds or already visited |

Without pruning, backtracking degrades to brute-force enumeration. **Effective pruning is what makes backtracking practical.**

---

## 4. Time Complexity Cheat Sheet

| Problem Type | Time Complexity | Notes |
|:---|:---|:---|
| **Subsets** | O(N × 2^N) | 2^N subsets, O(N) to copy each |
| **Permutations** | O(N × N!)  | N! permutations, O(N) to copy each |
| **Combinations (K of N)** | O(K × C(N,K)) | Binomial coefficient |
| **Combination Sum** | O(N^(T/M)) | T=target, M=min element (exponential) |
| **N-Queens** | O(N!)       | Strong pruning makes it tractable in practice |
| **Sudoku** | O(9^M)      | M = empty cells (at most 81) |

> **Space:** Always O(N) for the recursion depth (call stack) + O(N) for the current path, so O(N) auxiliary space. The result list can be O(2^N × N).
