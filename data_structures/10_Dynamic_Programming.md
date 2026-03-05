# Dynamic Programming Deep Dive

Dynamic Programming (DP) is an algorithm design paradigm that solves complex problems by breaking them into overlapping subproblems and storing the results to avoid redundant computation. It applies when a problem has two properties: **Optimal Substructure** (the optimal solution contains optimal solutions to subproblems) and **Overlapping Subproblems** (the same subproblems are solved many times).

## 1. Two Implementation Styles

### Top-Down (Memoization)
Write a recursive solution, but cache results in a map/array (the "memo"). On every call, check the cache first; compute only if absent.

```java
Map<Integer, Long> memo = new HashMap<>();
long fib(int n) {
    if (n <= 1) return n;
    if (memo.containsKey(n)) return memo.get(n);
    long result = fib(n - 1) + fib(n - 2);
    memo.put(n, result);
    return result;
}
```
- **Pros:** Only computes subproblems actually needed. Easier to reason about.
- **Cons:** Recursive call stack overhead; risk of stack overflow for large N.

### Bottom-Up (Tabulation)
Iteratively fill a table from the smallest subproblem up to the answer. No recursion.
```java
long[] dp = new long[n + 1];
dp[0] = 0; dp[1] = 1;
for (int i = 2; i <= n; i++) dp[i] = dp[i-1] + dp[i-2];
```
- **Pros:** Better constants, no stack overhead, often space-optimizable.
- **Cons:** Must compute all subproblems (even unused ones).

---

## 2. Core DP Patterns

### A. Fibonacci / 1D Linear DP
State: `dp[i]` = answer for problem of size `i`.
Recurrence typically involves 1–2 previous states.
- **Climbing Stairs:** `dp[i] = dp[i-1] + dp[i-2]`
- **House Robber:** `dp[i] = max(dp[i-1], dp[i-2] + nums[i])`
- **Space optimization:** If you only look back 2 steps, use two variables instead of an array.

### B. 0/1 Knapsack
Each item is either included or excluded (used at most once). Capacity constraint.

```java
// dp[i][w] = max value using first i items with capacity w
for (int i = 1; i <= n; i++)
    for (int w = capacity; w >= weight[i]; w--)  // iterate BACKWARDS for 0/1
        dp[w] = Math.max(dp[w], dp[w - weight[i]] + value[i]);
```
- **Why backwards?** Prevents using the same item twice in the same pass.
- **Examples:** Partition Equal Subset Sum, Target Sum, Last Stone Weight II.

### C. Unbounded Knapsack
Items can be used unlimited times. Iterate capacity **forwards**.

```java
for (int coin : coins)
    for (int amount = coin; amount <= target; amount++)  // FORWARDS = reuse allowed
        dp[amount] = Math.min(dp[amount], dp[amount - coin] + 1);
```
- **Examples:** Coin Change, Coin Change II, Integer Break.

### D. 2D Sequence DP (LCS-style)
Compare two sequences. `dp[i][j]` = result for first `i` elements of `s1` and first `j` elements of `s2`.

```
if s1[i] == s2[j]: dp[i][j] = dp[i-1][j-1] + 1  (LCS)
else:               dp[i][j] = max(dp[i-1][j], dp[i][j-1])
```
- **Examples:** Longest Common Subsequence, Edit Distance, Distinct Subsequences.

### E. Grid / Matrix DP
`dp[i][j]` depends on `dp[i-1][j]` and/or `dp[i][j-1]`.
- **Examples:** Unique Paths, Minimum Path Sum, Maximal Square, Dungeon Game.

### F. Interval DP
`dp[i][j]` = answer for the subarray from index `i` to `j`. Fill in increasing interval lengths.
- **Examples:** Burst Balloons, Palindrome Partitioning, Matrix Chain Multiplication.

---

## 3. How to Identify DP Problems

| Signal | Example |
|:---|:---|
| "Number of ways to…" | Climbing stairs, Unique Paths |
| "Minimum/Maximum cost to…" | Coin Change, Dungeon Game |
| "Can you…?" (feasibility) | Word Break, Partition Equal Subset |
| "Longest/Shortest subsequence" | LCS, LIS, Edit Distance |
| Overlapping recursive calls evident | Any Fibonacci-like recursion |

---

## 4. Time Complexity Cheat Sheet

| Pattern | Time | Space | Optimizable Space? |
|:---|:---|:---|:---|
| **1D Fibonacci** | O(N) | O(N) | ✅ O(1) (two vars) |
| **0/1 Knapsack** | O(N × W) | O(N × W) | ✅ O(W) (1D array) |
| **Unbounded Knapsack** | O(N × W) | O(W) | Already optimal |
| **LCS / Edit Distance** | O(M × N) | O(M × N) | ✅ O(min(M,N)) |
| **Grid DP** | O(M × N) | O(M × N) | ✅ O(N) (one row) |
| **Interval DP** | O(N³) | O(N²) | Rarely |
