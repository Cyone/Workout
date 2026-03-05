# Hard

### 1. 23. Merge k Sorted Lists
You are given an array of `k` linked-lists `lists`, each linked-list is sorted in ascending order. Merge all the linked-lists into one sorted linked-list and return it.

**Test Cases:**
1. `lists = [[1,4,5],[1,3,4],[2,6]]` -> `[1,1,2,3,4,4,5,6]`
2. `lists = []` -> `[]`
3. `lists = [[]]` -> `[]`
4. `lists = [[], [1]]` -> `[1]`
5. `lists = [[1,2], [3,4], [5,6]]` -> `[1,2,3,4,5,6]`

### 2. 25. Reverse Nodes in k-Group
Given the `head` of a linked list, reverse the nodes of the list `k` at a time, and return the modified list.

**Test Cases:**
1. `head = [1,2,3,4,5], k = 2` -> `[2,1,4,3,5]`
2. `head = [1,2,3,4,5], k = 3` -> `[3,2,1,4,5]`
3. `head = [1,2,3,4,5], k = 1` -> `[1,2,3,4,5]`
4. `head = [1], k = 1` -> `[1]`
5. `head = [1,2], k = 2` -> `[2,1]`
