# Trees and Graphs Deep Dive

While Arrays and Linked Lists represent linear data, Trees and Graphs represent hierarchical and networked data. They are crucial for representing real-world systems (file systems, social networks, routing).

## 1. Trees Fundamentals
A Tree is a restricted Graph. It consists of Nodes connected by edges, with a strict parent-child hierarchy.
*   **Root:** The single top-most node.
*   **Leaf:** A node with no children.
*   **Constraint:** A tree cannot contain cycles (loops). There is exactly one path from the root to any specific node.

### Binary Trees
The most common variant. Each node has at most two children (`left` and `right`).

### Tree Traversal Algorithms
How do you print every node in a tree?
*   **Breadth-First Search (BFS) / Level-Order:** Uses a **Queue**. Explores level 1, then level 2, etc. Good for finding the shortest path in unweighted graphs.
*   **Depth-First Search (DFS):** Uses a **Stack** (or recursion). Plunges as deep as possible down one branch before backtracking.
    *   **Pre-order:** `Root -> Left -> Right` (Used for copying/serializing a tree).
    *   **In-order:** `Left -> Root -> Right` (Returns sorted values in a BST).
    *   **Post-order:** `Left -> Right -> Root` (Used for deleting a tree, processing children before parents).

## 2. Binary Search Trees (BST)
A Binary Tree with a strict mathematical property:
For any node, all values in its `left` subtree are **less than** the node's value, and all values in its `right` subtree are **greater than** the node's value.

*   **Time Complexity:** Searching for a value takes **O(log N)** time on average, as you eliminate half the tree at every step (like binary search on an array).
*   **The Worst-Case Trap:** If you insert pre-sorted data (1, 2, 3, 4, 5) into a basic BST, it degenerates into a straight Linked List. Search time degrades to O(N).
*   **Self-Balancing Trees:** To fix the worst-case, enterprise systems use self-balancing BSTs. They automatically rotate nodes during insertion/deletion to guarantee the tree remains balanced, strictly enforcing **O(log N)** operations.
    *   **AVL Trees:** Named after Adelson-Velsky and Landis, AVL trees are *strictly* balanced. For any node, the heights of its left and right subtrees differ by at most 1. Because of this strict balancing, **lookups are extremely fast** (faster on average than Red-Black trees). However, this strictness means insertions and deletions may require more rotations, making them slightly slower for write-heavy workloads.
    *   **Red-Black Trees:** A self-balancing structure where nodes are colored "red" or "black". The rules of coloring ensure that the longest path from root to leaf is no more than twice as long as the shortest path. Because it’s less strictly balanced than an AVL tree, **insertions and deletions are faster** (fewer tree rotations are needed). This excellent general-purpose performance makes Red-Black trees the standard choice for most language libraries (e.g., Java's `TreeMap` and `TreeSet`, C++'s `std::map`).

### How Balancing is Achieved: Tree Rotations
Balancing is primarily achieved using **Tree Rotations**—O(1) pointer-swapping operations that change the structure of the tree while preserving the BST property (left < parent < right).
*   **Left Rotation:** Moves a node down to the left and brings its right child up to take its place.
*   **Right Rotation:** Moves a node down to the right and brings its left child up to take its place.

#### AVL Tree Balancing Strategy
1.  **Balance Factor:** Every node tracks its "Balance Factor" (`Height of Left Subtree - Height of Right Subtree`). Valid values are -1, 0, or 1.
2.  **Detection:** After a node is inserted or deleted, the tree traces back up to the root, recalculating balance factors.
3.  **Correction:** If a node's balance factor hits -2 or 2, an imbalance is detected and fixed immediately using exactly one or two rotations. There are four cases based on where the new node was added relative to the unbalanced node:
    *   **Left-Left (LL) Heavy:** Fixed with a single **Right Rotation**.
    *   **Right-Right (RR) Heavy:** Fixed with a single **Left Rotation**.
    *   **Left-Right (LR) Heavy:** The left child leans right. Fixed with a **Left Rotation** on the child, then a **Right Rotation** on the parent.
    *   **Right-Left (RL) Heavy:** The right child leans left. Fixed with a **Right Rotation** on the child, then a **Left Rotation** on the parent.

#### Red-Black Tree Balancing Strategy
1.  **Color Rules:** 
    * Every node is Red or Black.
    * The Root is Black.
    * Red nodes cannot have Red children (no two consecutive Red nodes on any path).
    * Every path from a node to its descendant `null` pointers must contain the same number of Black nodes.
2.  **Detection:** New nodes are always inserted as **Red**. If its parent is also Red, a rule is violated (the "Red-Red" clash).
3.  **Correction:** To fix a Red-Red clash, the tree looks at the newly inserted node's **Uncle** (the parent's sibling):
    *   **Case 1 (Uncle is Red):** We fix it via **Recoloring**. We change both the Parent and the Uncle to Black, and change the Grandparent to Red. We then restart checking the rules up at the Grandparent. (No rotations needed here, but the issue can propagate up the tree).
    *   **Case 2 (Uncle is Black or Null):** We fix it via **Rotations**. We perform a Left or Right Rotation on the Parent/Grandparent (similar to AVL's LL/RR/LR/RL cases), and immediately swap the colors of the rotated nodes. This guarantees the tree is fixed locally without propagating further up.

## 3. Graphs Fundamentals
A Graph is a collection of Nodes (Vertices) and Edges connecting them. Unlike trees, any node can connect to any other node, and cycles are allowed.

### Types of Graphs
*   **Directed vs. Undirected:** Are the edges one-way streets (Twitter follows) or two-way streets (Facebook friends)?
*   **Weighted vs. Unweighted:** Do the edges have a cost associated with them (e.g., mileage between cities)?

### Representing a Graph in Memory
1.  **Adjacency Matrix:** A 2D array `matrix[V][V]`. `matrix[i][j] = 1` if an edge exists.
    *   *Pros:* O(1) edge lookup.
    *   *Cons:* Terrible O(V^2) memory footprint. Horrible for sparse graphs.
2.  **Adjacency List (The Standard):** An array or Map where every Vertex points to a List of its neighbors: `Map<Node, List<Node>>`.
    *   *Pros:* Highly memory efficient O(V + E).
    *   *Cons:* Checking if a specific edge exists takes O(Neighbors) time.

### Essential Graph Algorithms
*   **Dijkstra's Algorithm:** Finds the shortest path in a weighted graph (no negative weights). Uses a Priority Queue.
*   **Topological Sort:** Used on Directed Acyclic Graphs (DAGs) to linearly order nodes based on dependencies (e.g., resolving package/build dependencies in Gradle/Maven, or scheduling college courses).

---

## 4. Time Complexity Cheat Sheet (Trees & Graphs)

| Algorithm / Traversal | Time Complexity | Space Complexity | Use Cases |
| :--- | :--- | :--- | :--- |
| **DFS (Pre/In/Post Order)** | **O(V + E)** | **O(V)** (Worst case call stack if tree is skewed) | Tree traversals, exploring all paths, cycle detection. |
| **BFS (Level Order)** | **O(V + E)** | **O(V)** (Queue can hold up to V/2 nodes at leaf level) | Shortest path on unweighted graphs, level-by-level ops. |
| **BST Search/Insert/Delete** | **O(log V)** (Avg) | **O(1)** (Iterative) or **O(log V)** (Recursive stack) | Fast ordered lookups in balanced trees (AVL, Red-Black). |
| **Dijkstra's (Adj List + Min-Heap)** | **O((V+E) log V)**| **O(V)** (For distances array and Priority Queue) | Shortest path on weighted graphs with non-negative edges. |

*(Note: V = Vertices/Nodes, E = Edges)*
