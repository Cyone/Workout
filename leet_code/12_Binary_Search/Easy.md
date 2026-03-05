# Easy

### 1. 704. Binary Search

Given an array of integers `nums` sorted in ascending order and an integer `target`, write a function to search for `target`. Return the index if found, otherwise return -1.

**Test Cases:**

1. `nums = [-1,0,3,5,9,12], target = 9` -> `4`
2. `nums = [-1,0,3,5,9,12], target = 2` -> `-1`
3. `nums = [5], target = 5` -> `0`
4. `nums = [1,2,3,4,5], target = 1` -> `0`
5. `nums = [2,5], target = 5` -> `1`

### 2. 278. First Bad Version

You are a product manager and currently leading a team. There are `n` versions `[1, 2, ..., n]`. A version is bad if it fails a `bool isBadVersion(version)` check. Find the first bad version minimizing API calls.

**Test Cases:**

1. `n = 5, bad = 4` -> `4`
2. `n = 1, bad = 1` -> `1`
3. `n = 3, bad = 2` -> `2`
4. `n = 10, bad = 7` -> `7`
5. `n = 100, bad = 1` -> `1`

### 3. 35. Search Insert Position

Given a sorted array of distinct integers `nums` and a `target`, return the index if found, otherwise the index where it would be if inserted in order.

**Test Cases:**

1. `nums = [1,3,5,6], target = 5` -> `2`
2. `nums = [1,3,5,6], target = 2` -> `1`
3. `nums = [1,3,5,6], target = 7` -> `4`
4. `nums = [1,3,5,6], target = 0` -> `0`
5. `nums = [1], target = 0` -> `0`
