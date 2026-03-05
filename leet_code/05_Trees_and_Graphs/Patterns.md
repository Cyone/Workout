# Problem Solving Patterns: Trees and Graphs

## Common Patterns
1. **Breadth-First Search (BFS)**
2. **Depth-First Search (DFS)**
3. **Topological Sort**
4. **Union Find (Disjoint Set)**

## How to Detect These Patterns

### 1. Breadth-First Search (BFS)
- **When to use**: You need to traverse a tree/graph level by level, or find the shortest path in an unweighted graph.
- **Keywords**: "level order", "shortest path", "minimum depth", "word ladder".
- **Example**: Binary Tree Level Order Traversal, Number of Islands, Word Ladder.

### 2. Depth-First Search (DFS)
- **When to use**: You need to traverse to the leaves of a tree before exploring siblings, exhaustive search, or backtrack on a graph.
- **Keywords**: "all paths", "maximum depth", "validate BST", "connected components", "backtracking".
- **Example**: Maximum Depth of Binary Tree, Validate Binary Search Tree, Clone Graph.

### 3. Topological Sort
- **When to use**: You need to find a linear ordering of elements that have dependencies on each other (directed acyclic graph).
- **Keywords**: "prerequisites", "course schedule", "order of tasks", "alien dictionary".
- **Example**: Course Schedule, Course Schedule II, Alien Dictionary.

### 4. Union Find (Disjoint Set)
- **When to use**: You need to efficiently detect cycle in an undirected graph or find the number of connected components as edges are added.
- **Keywords**: "connected components", "redundant connection", "kruskal's algorithm", "dynamic connectivity".
- **Example**: Number of Connected Components in an Undirected Graph, Redundant Connection.
