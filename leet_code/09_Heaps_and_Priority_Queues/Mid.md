# Mid

### 1. 347. Top K Frequent Elements

Given an integer array `nums` and an integer `k`, return the `k` most frequent elements. You may return the answer in any order.

**Test Cases:**

1. `nums = [1,1,1,2,2,3], k = 2` -> `[1,2]`
2. `nums = [1], k = 1` -> `[1]`
3. `nums = [1,2], k = 2` -> `[1,2]`
4. `nums = [4,1,4,1,4,2], k = 1` -> `[4]`
5. `nums = [1,1,2,2,3], k = 2` -> `[1,2]`

### 2. 973. K Closest Points to Origin

Given an array of `points` where `points[i] = [xi, yi]` and an integer `k`, return the `k` closest points to the origin.

**Test Cases:**

1. `points = [[1,3],[-2,2]], k = 1` -> `[[-2,2]]`
2. `points = [[3,3],[5,-1],[-2,4]], k = 2` -> `[[3,3],[-2,4]]`
3. `points = [[0,1],[1,0]], k = 2` -> `[[0,1],[1,0]]`
4. `points = [[1,1],[2,2],[3,3]], k = 1` -> `[[1,1]]`
5. `points = [[0,0],[1,1]], k = 1` -> `[[0,0]]`

### 3. 621. Task Scheduler

Given a list of tasks `tasks` and a non-negative integer `n` (cooldown period), return the minimum number of intervals needed to finish all tasks.

**Test Cases:**

1. `tasks = ["A","A","A","B","B","B"], n = 2` -> `8`
2. `tasks = ["A","A","A","B","B","B"], n = 0` -> `6`
3. `tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2` -> `16`
4. `tasks = ["A","B","C"], n = 2` -> `3`
5. `tasks = ["A","A","A"], n = 1` -> `5`

### 4. 378. Kth Smallest Element in a Sorted Matrix

Given an `n x n` matrix where each row and column is sorted in ascending order, return the `kth` smallest element.

**Test Cases:**

1. `matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8` -> `13`
2. `matrix = [[-5]], k = 1` -> `-5`
3. `matrix = [[1,2],[1,3]], k = 2` -> `1`
4. `matrix = [[1,3,5],[6,7,12],[11,14,14]], k = 6` -> `11`
5. `matrix = [[1,2,3],[4,5,6],[7,8,9]], k = 5` -> `5`

### 5. 23. Merge K Sorted Lists (K-way merge)

You are given an array of `k` linked-lists lists, each sorted in ascending order. Merge all linked-lists into one sorted linked-list.

**Test Cases:**

1. `lists = [[1,4,5],[1,3,4],[2,6]]` -> `[1,1,2,3,4,4,5,6]`
2. `lists = []` -> `[]`
3. `lists = [[]]` -> `[]`
4. `lists = [[1],[0]]` -> `[0,1]`
5. `lists = [[1,2,3],[4,5,6],[7,8,9]]` -> `[1,2,3,4,5,6,7,8,9]`
