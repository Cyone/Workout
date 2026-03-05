# Mid

### 1. 55. Jump Game

You are given an integer array `nums`. You are initially at index 0. Each element represents the maximum jump length at that position. Return `true` if you can reach the last index.

**Test Cases:**

1. `nums = [2,3,1,1,4]` -> `true`
2. `nums = [3,2,1,0,4]` -> `false`
3. `nums = [0]` -> `true`
4. `nums = [1,0,0]` -> `false`
5. `nums = [2,0,0]` -> `true`

### 2. 45. Jump Game II

Given an array `nums`, return the minimum number of jumps to reach the last index. You can always reach the last index.

**Test Cases:**

1. `nums = [2,3,1,1,4]` -> `2`
2. `nums = [2,3,0,1,4]` -> `2`
3. `nums = [1,1,1,1]` -> `3`
4. `nums = [6,2,6,1,7,9,3,5,3,7,2,8,9,4,7,8,1,4,5,6,4]` -> `2`
5. `nums = [0]` -> `0`

### 3. 134. Gas Station

There are `n` gas stations in a circle with `gas[i]` supply and `cost[i]` to travel to the next. Return the starting gas station index if you can complete the circuit once; otherwise -1.

**Test Cases:**

1. `gas = [1,2,3,4,5], cost = [3,4,5,1,2]` -> `3`
2. `gas = [2,3,4], cost = [3,4,3]` -> `-1`
3. `gas = [5,1,2,3,4], cost = [4,4,1,5,1]` -> `4`
4. `gas = [1,2], cost = [2,1]` -> `1`
5. `gas = [2], cost = [2]` -> `0`

### 4. 767. Reorganize String

Given a string `s`, rearrange its characters such that no two adjacent characters are the same. Return any valid rearrangement, or empty string if impossible.

**Test Cases:**

1. `s = "aab"` -> `"aba"`
2. `s = "aaab"` -> `""`
3. `s = "vvvlo"` -> `"vlvov"` (one valid answer)
4. `s = "a"` -> `"a"`
5. `s = "aabb"` -> `"abab"` or `"baba"`
