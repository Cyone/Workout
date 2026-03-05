# Problem Solving Patterns: Binary Search

## Common Patterns
1. **Classic Binary Search**
2. **Modified Binary Search (Rotated / Duplicates)**
3. **Binary Search on Answer (Search Space)**
4. **Search in 2D Matrix**

## How to Detect These Patterns

### 1. Classic Binary Search
- **When to use**: You have a sorted array and need to find a target, or find the insertion position.
- **Keywords**: "sorted array", "find target", "first/last occurrence", "search".
- **Approach**: `lo = 0, hi = n-1`. While `lo <= hi`, compute `mid = lo + (hi-lo)/2`. Narrow to left or right half.
- **Example**: Binary Search, First Bad Version, Search Insert Position, Find First and Last Position.

### 2. Modified Binary Search (Rotated / Duplicates)
- **When to use**: The array is "almost sorted" — rotated, or has duplicates — but you still need O(log N) search.
- **Keywords**: "rotated sorted array", "find minimum in rotated", "search with duplicates".
- **Approach**: Determine which half is sorted; use that to decide which half to search next.
- **Example**: Search in Rotated Sorted Array, Find Minimum in Rotated Sorted Array, Search in Rotated Sorted Array II.

### 3. Binary Search on Answer (Search Space)
- **When to use**: The answer lies within a range (not array indices), and you can verify a candidate answer in O(N). Converts an optimisation problem to a decision problem.
- **Keywords**: "minimum maximum", "maximum minimum", "capacity to ship", "split array largest sum", "minimize the maximum".
- **Approach**: Binary search over the answer range; for each `mid`, check feasibility with a greedy/linear scan.
- **Example**: Koko Eating Bananas, Capacity to Ship Packages, Split Array Largest Sum, Minimize Max Distance.

### 4. Search in 2D Matrix
- **When to use**: A 2D matrix with row/column sorted properties. Can be flattened to 1D or traversed from corner.
- **Keywords**: "sorted matrix", "2D search", "find in matrix".
- **Approach**: Flatten and binary search, or start from top-right corner and move left/down.
- **Example**: Search a 2D Matrix, Search a 2D Matrix II.
