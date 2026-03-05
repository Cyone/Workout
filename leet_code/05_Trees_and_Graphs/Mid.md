# Mid

### 1. 102. Binary Tree Level Order Traversal
Given the `root` of a binary tree, return the level order traversal of its nodes' values.

**Test Cases:**
1. `root = [3,9,20,null,null,15,7]` -> `[[3],[9,20],[15,7]]`
2. `root = [1]` -> `[[1]]`
3. `root = []` -> `[]`
4. `root = [1,2,null,3,null,4,null,5]` -> `[[1],[2],[3],[4],[5]]`
5. `root = [1,2,3,4,5,6,7]` -> `[[1],[2,3],[4,5,6,7]]`

### 2. 200. Number of Islands
Given an `m x n` grid of `'1'`s (land) and `'0'`s (water), return the number of islands.

**Test Cases:**
1. `grid = [["1","1","1","1","0"],["1","1","0","1","0"],["1","1","0","0","0"],["0","0","0","0","0"]]` -> `1`
2. `grid = [["1","1","0","0","0"],["1","1","0","0","0"],["0","0","1","0","0"],["0","0","0","1","1"]]` -> `3`
3. `grid = [["0"]]` -> `0`
4. `grid = [["1"]]` -> `1`
5. `grid = [["1","0","1"],["0","1","0"],["1","0","1"]]` -> `5`

### 3. 207. Course Schedule
Given `numCourses` and a list of `prerequisites`, return `true` if you can finish all courses. Otherwise, return `false`.

**Test Cases:**
1. `numCourses = 2, prerequisites = [[1,0]]` -> `true`
2. `numCourses = 2, prerequisites = [[1,0],[0,1]]` -> `false`
3. `numCourses = 1, prerequisites = []` -> `true`
4. `numCourses = 3, prerequisites = [[1,0],[2,1]]` -> `true`
5. `numCourses = 3, prerequisites = [[1,0],[2,1],[0,2]]` -> `false`


### 4. 133. Clone Graph
Given a reference of a node in a connected undirected graph. Return a deep copy (clone) of the graph.

**Test Cases:**
1. `adjList = [[2,4],[1,3],[2,4],[1,3]]` -> `[[2,4],[1,3],[2,4],[1,3]]`

### 5. 199. Binary Tree Right Side View
Given the root of a binary tree, imagine yourself standing on the right side of it, return the values of the nodes you can see ordered from top to bottom.

**Test Cases:**
1. `root = [1,2,3,null,5,null,4]` -> `[1,3,4]`
2. `root = [1,null,3]` -> `[1,3]`

### 6. 236. Lowest Common Ancestor of a Binary Tree
Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.

**Test Cases:**
1. `root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1` -> `3`
2. `root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4` -> `5`

### 7. 210. Course Schedule II
There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites. Return the ordering of courses you should take to finish all courses.

**Test Cases:**
1. `numCourses = 2, prerequisites = [[1,0]]` -> `[0,1]`
2. `numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]` -> `[0,2,1,3]`

### 8. 323. Number of Connected Components in an Undirected Graph
You have a graph of n nodes. You are given an integer n and an array edges. Return the number of connected components in the graph.

**Test Cases:**
1. `n = 5, edges = [[0,1],[1,2],[3,4]]` -> `2`
2. `n = 5, edges = [[0,1],[1,2],[2,3],[3,4]]` -> `1`

### 9. 684. Redundant Connection
In this problem, a tree is an undirected graph that is connected and has no cycles. You are given a graph that started as a tree with n nodes. Return an edge that can be removed so that the resulting graph is a tree of n nodes.

**Test Cases:**
1. `edges = [[1,2],[1,3],[2,3]]` -> `[2,3]`
2. `edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]` -> `[1,4]`
