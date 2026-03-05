# Complexity-Based Pattern Detection

## Common Patterns for Big O Inference
Sometimes, you can deduce the expected algorithm based purely on the input constraints. The size of N (the input) is a massive hint:

## 1. N ≤ 10 or 20 -> O(N!) or O(2^N)
- **Pattern**: Backtracking, recursion, permutations, subsets.
- **Keywords/Hints**: "Find all...", "Combinations", "Permutations".
- **Why**: 20! is huge, but 2^20 is ~1 million, easily fitting in the 1-second limit (~10^8 operations).

## 2. N ≤ 100 or 500 -> O(N^3)
- **Pattern**: Dynamic Programming (3D/2D), Matrix multiplication, sometimes combinations like Floyd-Warshall.
- **Why**: 500^3 is 1.2 * 10^8, right passing the 1-second limit.

## 3. N ≤ 1,000 to 2,500 -> O(N^2)
- **Pattern**: Nested loops, 2D Dynamic Programming, basic Graph traversal (if completely dense), Insertion/Selection Sort.
- **Keywords/Hints**: "Longest Common Subsequence", "Edit Distance".

## 4. N ≤ 10^5 (100,000) -> O(N log N) or O(N)
- **Pattern**: 
  - **O(N log N)**: Sorting Arrays, Heap/Priority Queue, Binary Search on Answer, Divide and Conquer.
  - **O(N)**: Hash Maps, Sliding Window, Two Pointers, Linear Scans, Prefix Sums, Monotonic Stack.
- **Keywords/Hints**: Most LeetCode Mediums fall here. "Find in Array", "Subarray size", "Distinct elements".

## 5. N ≥ 10^9 (1 Billion) -> O(log N) or O(1)
- **Pattern**: Binary Search, Math formulas (combinatorics/geometry), Bit Manipulation.
- **Why**: Iterating over 10^9 takes too long (10 seconds), so you must skip portions of the input.
