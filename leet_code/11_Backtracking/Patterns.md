# Problem Solving Patterns: Backtracking

## Common Patterns
1. **Subsets**
2. **Permutations**
3. **Combinations**
4. **Constraint-based Pruning (N-Queens, Sudoku)**

## How to Detect These Patterns

### 1. Subsets
- **When to use**: Generate all possible subsets (power set) of a given collection.
- **Keywords**: "all subsets", "power set", "all possible groups", "choose any elements".
- **Approach**: At each step decide to include or exclude the current element. Recurse through all indices.
- **Example**: Subsets, Subsets II (with duplicates), Sum of all Subsets.

### 2. Permutations
- **When to use**: Generate all possible orderings of a collection.
- **Keywords**: "all permutations", "all arrangements", "rearrange", "next permutation".
- **Approach**: At each position, try every unused element. Swap-based or boolean-visited array.
- **Example**: Permutations, Permutations II (duplicates), Next Permutation.

### 3. Combinations
- **When to use**: Select K elements from N without repetition (order doesn't matter). Or find all combinations that sum to a target.
- **Keywords**: "combination sum", "choose K from N", "all combinations that add up to".
- **Approach**: Recurse with a start index, add to current path, backtrack by removing the last element.
- **Example**: Combination Sum, Combination Sum II, Combinations, Letter Combinations of a Phone Number.

### 4. Constraint-based Pruning
- **When to use**: Place elements under strict constraints (rows, columns, diagonals, boxes). Prune branches that violate constraints early.
- **Keywords**: "N-Queens", "Sudoku", "valid placement", "no two in same row/column".
- **Approach**: Track which positions are invalid using sets or arrays; skip invalid choices immediately.
- **Example**: N-Queens, Sudoku Solver, Word Search.
