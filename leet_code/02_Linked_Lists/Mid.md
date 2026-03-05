# Mid

### 1. 2. Add Two Numbers
You are given two non-empty linked lists representing two non-negative integers. Add the two numbers and return the sum as a linked list.

**Test Cases:**
1. `l1 = [2,4,3], l2 = [5,6,4]` -> `[7,0,8]`
2. `l1 = [0], l2 = [0]` -> `[0]`
3. `l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]` -> `[8,9,9,9,0,0,0,1]`
4. `l1 = [2,4], l2 = [5,6,4]` -> `[7,0,5]`
5. `l1 = [5], l2 = [5]` -> `[0,1]`

### 2. 19. Remove Nth Node From End of List
Given the `head` of a linked list, remove the `nth` node from the end of the list and return its head.

**Test Cases:**
1. `head = [1,2,3,4,5], n = 2` -> `[1,2,3,5]`
2. `head = [1], n = 1` -> `[]`
3. `head = [1,2], n = 1` -> `[1]`
4. `head = [1,2], n = 2` -> `[2]`
5. `head = [1,2,3], n = 3` -> `[2,3]`

### 3. 138. Copy List with Random Pointer
Construct a deep copy of a linked list where nodes also have a `random` pointer that points to any node or null.

**Test Cases:**
1. `head = [[7,null],[13,0],[11,4],[10,2],[1,0]]` -> `[[7,null],[13,0],[11,4],[10,2],[1,0]]`
2. `head = [[1,1],[2,1]]` -> `[[1,1],[2,1]]`
3. `head = [[3,null],[3,0],[3,null]]` -> `[[3,null],[3,0],[3,null]]`
4. `head = []` -> `[]`
5. `head = [[1,null]]` -> `[[1,null]]`


### 4. 142. Linked List Cycle II
Given the head of a linked list, return the node where the cycle begins. If there is no cycle, return null.

**Test Cases:**
1. `head = [3,2,0,-4], pos = 1` -> `idx 1`
2. `head = [1,2], pos = 0` -> `idx 0`

### 5. 143. Reorder List
You are given the head of a singly linked-list. The list can be represented as: L0 → L1 → … → Ln-1 → Ln. Reorder the list to be on the following form: L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → …

**Test Cases:**
1. `head = [1,2,3,4]` -> `[1,4,2,3]`
2. `head = [1,2,3,4,5]` -> `[1,5,2,4,3]`

### 6. 92. Reverse Linked List II
Given the head of a singly linked list and two integers left and right where left <= right, reverse the nodes of the list from position left to position right, and return the reversed list.

**Test Cases:**
1. `head = [1,2,3,4,5], left = 2, right = 4` -> `[1,4,3,2,5]`
2. `head = [5], left = 1, right = 1` -> `[5]`

### 7. 24. Swap Nodes in Pairs
Given a linked list, swap every two adjacent nodes and return its head. You must solve the problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)

**Test Cases:**
1. `head = [1,2,3,4]` -> `[2,1,4,3]`
2. `head = []` -> `[]`

### 8. 61. Rotate List
Given the head of a linked list, rotate the list to the right by k places.

**Test Cases:**
1. `head = [1,2,3,4,5], k = 2` -> `[4,5,1,2,3]`
2. `head = [0,1,2], k = 4` -> `[2,0,1]`

### 9. 86. Partition List
Given the head of a linked list and a value x, partition it such that all nodes less than x come before nodes greater than or equal to x.

**Test Cases:**
1. `head = [1,4,3,2,5,2], x = 3` -> `[1,2,2,4,3,5]`
2. `head = [2,1], x = 2` -> `[1,2]`
