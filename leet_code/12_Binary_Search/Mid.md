# Mid

### 1. 33. Search in Rotated Sorted Array

There is an integer array `nums` sorted in ascending order, possibly rotated. Given the array and a `target`, return the index of `target`, or -1 if not present. Must be O(log N).

**Test Cases:**

1. `nums = [4,5,6,7,0,1,2], target = 0` -> `4`
2. `nums = [4,5,6,7,0,1,2], target = 3` -> `-1`
3. `nums = [1], target = 0` -> `-1`
4. `nums = [1,3], target = 3` -> `1`
5. `nums = [3,1], target = 1` -> `1`

### 2. 153. Find Minimum in Rotated Sorted Array

Given a sorted and rotated array `nums` with unique elements, return the minimum element. Must be O(log N).

**Test Cases:**

1. `nums = [3,4,5,1,2]` -> `1`
2. `nums = [4,5,6,7,0,1,2]` -> `0`
3. `nums = [11,13,15,17]` -> `11`
4. `nums = [2,1]` -> `1`
5. `nums = [5]` -> `5`

### 3. 74. Search a 2D Matrix

Given an `m x n` matrix where each row is sorted and the first integer of each row is greater than the last of the previous row, search for `target` in O(log(m*n)).

**Test Cases:**

1. `matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3` -> `true`
2. `matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13` -> `false`
3. `matrix = [[1]], target = 1` -> `true`
4. `matrix = [[1,3]], target = 3` -> `true`
5. `matrix = [[1],[3]], target = 2` -> `false`

### 4. 875. Koko Eating Bananas

Koko can eat `k` bananas per hour. Given `piles` and `h` hours, find the minimum `k` such that she finishes all bananas in `h` hours.

**Test Cases:**

1. `piles = [3,6,7,11], h = 8` -> `4`
2. `piles = [30,11,23,4,20], h = 5` -> `30`
3. `piles = [30,11,23,4,20], h = 6` -> `23`
4. `piles = [1,1,1,999999999], h = 10` -> `142857143`
5. `piles = [312884470], h = 312884469` -> `2`

### 5. 34. Find First and Last Position of Element in Sorted Array

Given a sorted array `nums` and a `target`, return the starting and ending position. Return `[-1,-1]` if not found. Must be O(log N).

**Test Cases:**

1. `nums = [5,7,7,8,8,10], target = 8` -> `[3,4]`
2. `nums = [5,7,7,8,8,10], target = 6` -> `[-1,-1]`
3. `nums = [], target = 0` -> `[-1,-1]`
4. `nums = [1,2,3], target = 2` -> `[1,1]`
5. `nums = [1,1,1,1,1], target = 1` -> `[0,4]`
