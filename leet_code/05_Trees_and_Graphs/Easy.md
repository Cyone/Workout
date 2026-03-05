# Easy

### 1. 94. Binary Tree Inorder Traversal
Given the `root` of a binary tree, return the inorder traversal of its nodes' values.

**Test Cases:**
1. `root = [1,null,2,3]` -> `[1,3,2]`
2. `root = []` -> `[]`
3. `root = [1]` -> `[1]`
4. `root = [1,2]` -> `[2,1]`
5. `root = [1,null,2]` -> `[1,2]`

### 2. 100. Same Tree
Given the roots of two binary trees `p` and `q`, write a function to check if they are the same or not.

**Test Cases:**
1. `p = [1,2,3], q = [1,2,3]` -> `true`
2. `p = [1,2], q = [1,null,2]` -> `false`
3. `p = [1,2,1], q = [1,1,2]` -> `false`
4. `p = [], q = []` -> `true`
5. `p = [1], q = []` -> `false`

### 3. 101. Symmetric Tree
Given the `root` of a binary tree, check whether it is a mirror of itself.

**Test Cases:**
1. `root = [1,2,2,3,4,4,3]` -> `true`
2. `root = [1,2,2,null,3,null,3]` -> `false`
3. `root = [1]` -> `true`
4. `root = []` -> `true`
5. `root = [1,2,3]` -> `false`

### 4. 104. Maximum Depth of Binary Tree
Given the `root` of a binary tree, return its maximum depth.

**Test Cases:**
1. `root = [3,9,20,null,null,15,7]` -> `3`
2. `root = [1,null,2]` -> `2`
3. `root = []` -> `0`
4. `root = [0]` -> `1`
5. `root = [1,2,3,4]` -> `3`

### 5. 226. Invert Binary Tree
Given the `root` of a binary tree, invert the tree, and return its root.

**Test Cases:**
1. `root = [4,2,7,1,3,6,9]` -> `[4,7,2,9,6,3,1]`
2. `root = [2,1,3]` -> `[2,3,1]`
3. `root = []` -> `[]`
4. `root = [1]` -> `[1]`
5. `root = [1,2]` -> `[1,null,2]`

### 6. 235. Lowest Common Ancestor of a Binary Search Tree
Given a binary search tree (BST), find the lowest common ancestor (LCA) node of two given nodes in the BST.

**Test Cases:**
1. `root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8` -> `6`
2. `root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4` -> `2`
3. `root = [2,1], p = 2, q = 1` -> `2`
4. `root = [3,1,4,null,2], p = 2, q = 4` -> `3`
5. `root = [5,3,6,2,4,null,null,1], p = 1, q = 4` -> `3`

### 7. 733. Flood Fill
An image is represented by an `m x n` integer grid `image`. Perform a flood fill starting from coordinate `(sr, sc)`.

**Test Cases:**
1. `image = [[1,1,1],[1,1,0],[1,0,1]], sr = 1, sc = 1, color = 2` -> `[[2,2,2],[2,2,0],[2,0,1]]`
2. `image = [[0,0,0],[0,0,0]], sr = 0, sc = 0, color = 0` -> `[[0,0,0],[0,0,0]]`
3. `image = [[0,0,0],[0,1,1]], sr = 1, sc = 1, color = 1` -> `[[0,0,0],[0,1,1]]`
4. `image = [[1]], sr = 0, sc = 0, color = 2` -> `[[2]]`
5. `image = [[1,0],[0,1]], sr = 0, sc = 0, color = 2` -> `[[2,0],[0,1]]`
