# Problem Solving Patterns: Stacks and Queues

## Common Patterns
1. **Monotonic Stack**
2. **LIFO/FIFO Ordering**
3. **Two Stacks for a Queue**
4. **String/Path Parsing with Stack**

## How to Detect These Patterns

### 1. Monotonic Stack
- **When to use**: You need to find the "next greater" or "next smaller" element for items in an array.
- **Keywords**: "next greater element", "next smaller element", "daily temperatures", "largest rectangle".
- **Example**: Next Greater Element I, Daily Temperatures, Largest Rectangle in Histogram.

### 2. LIFO/FIFO Ordering
- **When to use**: The problem requires you to process elements in a Last-In-First-Out (Stack) or First-In-First-Out (Queue) manner.
- **Keywords**: "valid parentheses", "undo operation", "recent calls", "moving average".
- **Example**: Valid Parentheses, Min Stack, Number of Recent Calls.

### 3. Two Stacks for a Queue
- **When to use**: You are explicitly asked to implement a queue using stacks, or need amortized O(1) queue operations in functional languages.
- **Keywords**: "implement queue using stacks".
- **Example**: Implement Queue using Stacks.

### 4. String/Path Parsing with Stack
- **When to use**: You are traversing a string representing a path or evaluating an expression where you need to go "back" a step.
- **Keywords**: "simplify path", "evaluate reverse polish notation", "calculator".
- **Example**: Simplify Path, Evaluate Reverse Polish Notation.
