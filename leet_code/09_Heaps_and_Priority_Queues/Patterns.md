# Problem Solving Patterns: Heaps and Priority Queues

## Common Patterns
1. **Top K Elements**
2. **K-way Merge**
3. **Two Heaps**
4. **Sliding Window Maximum**

## How to Detect These Patterns

### 1. Top K Elements
- **When to use**: You need to find the top/bottom K elements from a collection or stream, without sorting the entire thing.
- **Keywords**: "K largest", "K smallest", "K most frequent", "K closest", "top K".
- **Approach**: Use a min-heap of size K (for top-K largest) or max-heap (for top-K smallest). Add each element and pop if size > K.
- **Example**: Kth Largest Element in an Array, Top K Frequent Elements, K Closest Points to Origin.

### 2. K-way Merge
- **When to use**: You're given K sorted arrays/lists and need to merge them, or find the smallest/largest element across all.
- **Keywords**: "merge K sorted", "smallest among K lists", "K sorted files".
- **Approach**: Put the head of each list into a min-heap. Poll the minimum, advance that list's pointer, and push the next element.
- **Example**: Merge K Sorted Lists, Find K Pairs with Smallest Sums, Kth Smallest Element in a Sorted Matrix.

### 3. Two Heaps
- **When to use**: You need to partition a dataset into two halves and repeatedly query the median or boundary between them.
- **Keywords**: "median of a stream", "find middle", "balance two groups".
- **Approach**: Maintain a max-heap for the lower half and a min-heap for the upper half. Balance sizes after each insertion.
- **Example**: Find Median from Data Stream, Sliding Window Median.

### 4. Sliding Window Maximum
- **When to use**: You need the maximum (or minimum) within each sliding window of a fixed size.
- **Keywords**: "sliding window max", "maximum in window of size K".
- **Approach**: Use a max-heap or a monotonic deque for O(N) solution.
- **Example**: Sliding Window Maximum.
