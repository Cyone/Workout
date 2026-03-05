## Most common interview data structures + the algorithms/patterns used on them (and how to spot them)

### 1) **Arrays / Strings**
**Popular algorithms/patterns**
- Two pointers (including fast/slow)
- Sliding window (fixed/variable)
- Prefix sums / difference arrays
- Sorting + scan
- Binary search on index / on answer
- Hashing for frequency/counting

**How to spot**
- “subarray/substring”, “contiguous”, “longest/shortest”, “at most/at least k” → **sliding window**
- “pair/triplet”, “sorted array”, “remove duplicates”, “palindrome” → **two pointers**
- “range sum”, “sum between i..j”, “many queries”, “count subarrays with sum …” → **prefix sums**
- “minimum/maximum value that satisfies condition” (monotonic feasibility) → **binary search on answer**
- “find first/last occurrence”, “rotated sorted”, “peak”, “kth” → **binary search**
- “anagram/permutation”, “frequency”, “distinct” → **hash map counting**

---

### 2) **Hash Map / Hash Set**
**Popular algorithms/patterns**
- Frequency counting
- Complement lookup (two-sum style)
- Dedup + seen-before tracking
- Grouping by key (anagrams, buckets)

**How to spot**
- “check if exists”, “unique”, “first time seen”, “count occurrences” → **hash set/map**
- “find pair that sums to k”, “difference equals k” → **complement lookup**
- “group by”, “categorize” → **map from key → list/count**

---

### 3) **Linked List**
**Popular algorithms/patterns**
- Fast/slow pointers (cycle detection, middle)
- Reverse (whole list, sublist)
- Merge two lists (often after sorting)
- Pointer manipulation for deletion/partition

**How to spot**
- “cycle”, “loop”, “find middle”, “kth from end” → **fast/slow**
- “reverse”, “in-place”, “between m and n” → **reverse sublist**
- “merge”, “sorted lists”, “reorder” → **merge/relink pointers**

---

### 4) **Stack**
**Popular algorithms/patterns**
- Monotonic stack (next greater/smaller, span)
- Parsing/validation (parentheses)
- Expression evaluation (RPN/infix)
- “Undo/backtracking” style using stack

**How to spot**
- “next greater element”, “daily temperatures”, “largest rectangle” → **monotonic stack**
- “valid parentheses”, “balanced”, “remove outer/adjacent” → **stack**
- “evaluate expression”, “calculator”, “RPN” → **stack**

---

### 5) **Queue / Deque**
**Popular algorithms/patterns**
- BFS (level order)
- Sliding window maximum/minimum (monotonic deque)
- Producer-consumer simulation

**How to spot**
- “minimum steps/moves”, “shortest path in unweighted graph/grid” → **BFS with queue**
- “max in each window”, “min in each window” → **monotonic deque**
- “process in arrival order”, “round robin” → **queue**

---

### 6) **Heap / Priority Queue**
**Popular algorithms/patterns**
- Top-K / Kth largest/smallest
- Merge K sorted lists/arrays
- Streaming median (two heaps)
- Dijkstra (shortest path weighted)
- Scheduling by earliest finishing / best candidate

**How to spot**
- “top k”, “k largest”, “k closest”, “k most frequent” → **heap**
- “merge k sorted …” → **min-heap**
- “continuously add numbers, get median” → **two heaps**
- “minimum cost/path with weights” → **Dijkstra (heap)**
- “always pick next best / earliest deadline” → **priority queue**

---

### 7) **Binary Tree / Binary Search Tree**
**Popular algorithms/patterns**
- DFS traversals (pre/in/post), recursion
- BFS level order
- BST property exploitation (inorder sorted, bounds)
- Lowest common ancestor (LCA)
- Tree DP (compute values from children)

**How to spot**
- “ancestor”, “path”, “from root to leaf”, “max depth”, “diameter” → **DFS**
- “level”, “zigzag”, “minimum depth” → **BFS**
- “BST”, “kth smallest”, “validate BST” → **inorder / bounds**
- “common ancestor of two nodes” → **LCA**
- “best at node depends on children” (robber on tree, max path) → **tree DP**

---

### 8) **Graph**
**Popular algorithms/patterns**
- BFS/DFS for traversal, connected components
- Topological sort (Kahn/DFS) for DAG dependencies
- Union-Find (DSU) for connectivity/cycle detection
- Dijkstra / Bellman-Ford / Floyd-Warshall (shortest paths)
- Bipartite check (2-coloring)

**How to spot**
- “connected”, “islands”, “components”, “can reach” → **BFS/DFS**
- “prerequisites”, “ordering”, “depends on”, “course schedule” → **topo sort**
- “dynamic connectivity”, “merge groups”, “cycle in undirected graph” → **DSU**
- “shortest path”:
    - unweighted → **BFS**
    - weighted nonnegative → **Dijkstra**
    - negative edges → **Bellman-Ford**
- “two groups / conflict”, “possible to split” → **bipartite**

---

### 9) **Trie**
**Popular algorithms/patterns**
- Prefix queries
- Word search / dictionary matching
- Autocomplete

**How to spot**
- “prefix”, “startsWith”, “many words, many queries” → **trie**
- “replace words with roots”, “word break with prefixes” → **trie often helps**

---

### 10) **Dynamic Programming (DP)**
**Popular algorithms/patterns**
- 1D DP (house robber, coin change)
- 2D DP (grid paths, edit distance)
- Knapsack-style (subset sum, partition)
- Interval DP (palindrome partition, burst balloons)
- DP on strings (LCS, LIS variant with patience sorting)

**How to spot**
- “number of ways”, “min/max cost”, “optimal”, “choose or not choose” with overlapping subproblems → **DP**
- “constraints small enough for O(n·m)” (e.g., two strings lengths) → **2D DP**
- “subsequence” (not contiguous) → often **DP**
- “split into segments”, “between i and j” → **interval DP**
- If state can be described as `dp[i]` or `dp[i][j]` and transitions reuse earlier results → **DP**

---

### 11) **Greedy**
**Popular algorithms/patterns**
- Interval scheduling (sort by end time)
- Minimum jumps / reachability (often greedy)
- Cost minimization with sorting
- Huffman-like combining (use heap)

**How to spot**
- “maximize number of intervals”, “non-overlapping”, “meeting rooms” → **sort + greedy**
- “can you reach end”, “minimum jumps” → **greedy**
- “locally optimal choice seems safe” and you can argue an exchange proof → **greedy**

---

### 12) **Bit Manipulation**
**Popular algorithms/patterns**
- XOR tricks (single number, odd occurrences)
- Bitmask DP / subsets
- Checking power-of-two, counting bits

**How to spot**
- “appears once, others twice”, “missing number”, “toggle” → **XOR/bit ops**
- “n ≤ 20–25” and “consider all subsets” → **bitmasking**

---

## A quick “spotting” checklist (fast mapping from prompt → technique)
- **Contiguous subarray/substring** → sliding window / prefix sums
- **Sorted + pair/triplet** → two pointers
- **First/last/peak/min feasible** → binary search (index or answer)
- **“Next greater/smaller”** → monotonic stack
- **“Top K / Kth / stream”** → heap
- **“Minimum steps in grid / unweighted shortest path”** → BFS
- **Dependencies / ordering / prerequisites** → topological sort
- **Repeated optimal choices / min cost / #ways** → DP
- **Connectivity with unions** → DSU
- **Prefix queries** → trie

If you share 2–3 example interview questions you’re practicing (or the company level), I can map each to the most likely pattern and the telltale phrases.