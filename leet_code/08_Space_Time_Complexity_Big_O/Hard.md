# Hard

### 1. 41. First Missing Positive

Given an unsorted integer array `nums`, return the smallest positive integer that is not present in `nums`. Must run in O(N) time and use O(1) auxiliary space.

**Test Cases:**

1. `nums = [1,2,0]` -> `3`
2. `nums = [3,4,-1,1]` -> `2`
3. `nums = [7,8,9,11,12]` -> `1`
4. `nums = [1]` -> `2`
5. `nums = [-1,-2,-3]` -> `1`

### 2. 146. LRU Cache

Design a data structure that follows the constraints of a Least Recently Used (LRU) cache. The functions `get` and `put` must each run in O(1) average time complexity.

**Test Cases:**

1. `["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]`, `[[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]` -> `[null, null, null, 1, null, -1, null, -1, 3, 4]`
2. `["LRUCache", "put", "get"]`, `[[1], [1, 1], [1]]` -> `[null, null, 1]`
3. `["LRUCache", "put", "put", "get"]`, `[[1], [1, 1], [2, 2], [1]]` -> `[null, null, null, -1]`
4. `["LRUCache", "put", "put", "put"]`, `[[2], [1,1], [2,2], [3,3]]` -> `[null, null, null, null]`
5. `["LRUCache", "get"]`, `[[1], [1]]` -> `[null, -1]`
