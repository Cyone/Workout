# Problem Solving Patterns: Dynamic Programming

## Common Patterns
1. **Fibonacci / 1D DP**
2. **0/1 Knapsack**
3. **Unbounded Knapsack**
4. **Longest Common Subsequence (2D DP)**
5. **Matrix / Grid DP**
6. **Palindrome DP**

## How to Detect These Patterns

### 1. Fibonacci / 1D DP
- **When to use**: The answer for problem of size N depends on previous 1–2 subproblems. Classic linear recurrence.
- **Keywords**: "number of ways", "minimum steps", "climbing stairs", "how many paths".
- **Approach**: Build bottom-up array `dp[i]` from base cases upward.
- **Example**: Climbing Stairs, House Robber, Min Cost Climbing Stairs, Decode Ways.

### 2. 0/1 Knapsack
- **When to use**: You must choose whether to include or exclude each item (each item used at most once) to maximize/minimize a value subject to a weight/capacity constraint.
- **Keywords**: "subset sum", "partition equal", "target sum", "0/1 choice".
- **Approach**: 2D `dp[i][w]` or optimized 1D. For each item, iterate capacity backwards.
- **Example**: Partition Equal Subset Sum, Target Sum, Last Stone Weight II.

### 3. Unbounded Knapsack
- **When to use**: Items can be used multiple times (unlimited). Optimise sum/count.
- **Keywords**: "coin change", "minimum coins", "unlimited supply", "cutting rod".
- **Approach**: 1D dp, iterate capacity forwards (allows reuse of same item).
- **Example**: Coin Change, Coin Change II, Integer Break.

### 4. Longest Common Subsequence (2D DP)
- **When to use**: Comparing two sequences (strings/arrays) and finding optimal alignment.
- **Keywords**: "longest common", "edit distance", "minimum operations to convert", "longest palindromic subsequence".
- **Approach**: `dp[i][j]` represents result for first i chars of s1 and j chars of s2.
- **Example**: Longest Common Subsequence, Edit Distance, Longest Palindromic Subsequence.

### 5. Matrix / Grid DP
- **When to use**: You traverse a 2D grid and the cost or count at each cell depends on adjacent cells.
- **Keywords**: "unique paths", "minimum path sum", "dungeon game", "triangle".
- **Approach**: Fill `dp[i][j]` from top-left to bottom-right (or vice-versa).
- **Example**: Unique Paths, Minimum Path Sum, Dungeon Game, Maximal Square.

### 6. Palindrome DP
- **When to use**: Questions about palindromic substrings/subsequences in a string.
- **Keywords**: "palindromic substring", "minimum cuts palindrome", "expand around center".
- **Approach**: `dp[i][j]` is true if substring s[i..j] is a palindrome; build from smaller intervals.
- **Example**: Palindromic Substrings, Longest Palindromic Substring, Palindrome Partitioning II.
