# Easy

### 1. 70. Climbing Stairs
You are climbing a staircase. It takes `n` steps to reach the top. Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?

**Test Cases:**
1. `n = 2` -> `2`
2. `n = 3` -> `3`
3. `n = 1` -> `1`
4. `n = 4` -> `5`
5. `n = 5` -> `8`

### 2. 88. Merge Sorted Array
You are given two integer arrays `nums1` and `nums2`, sorted in non-decreasing order. Merge `nums2` into `nums1` as one sorted array.

**Test Cases:**
1. `nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3` -> `[1,2,2,3,5,6]`
2. `nums1 = [1], m = 1, nums2 = [], n = 0` -> `[1]`
3. `nums1 = [0], m = 0, nums2 = [1], n = 1` -> `[1]`
4. `nums1 = [2,0], m = 1, nums2 = [1], n = 1` -> `[1,2]`
5. `nums1 = [4,5,6,0,0,0], m = 3, nums2 = [1,2,3], n = 3` -> `[1,2,3,4,5,6]`

### 3. 268. Missing Number
Given an array `nums` containing `n` distinct numbers in the range `[0, n]`, return the only number in the range that is missing from the array.

**Test Cases:**
1. `nums = [3,0,1]` -> `2`
2. `nums = [0,1]` -> `2`
3. `nums = [9,6,4,2,3,5,7,0,1]` -> `8`
4. `nums = [0]` -> `1`
5. `nums = [1]` -> `0`

### 4. 278. First Bad Version
You have `n` versions `[1, 2, ..., n]` and you want to find out the first bad one, which causes all the following ones to be bad.

**Test Cases:**
1. `n = 5, bad = 4` -> `4`
2. `n = 1, bad = 1` -> `1`
3. `n = 3, bad = 2` -> `2`
4. `n = 10, bad = 10` -> `10`
5. `n = 2, bad = 1` -> `1`

### 5. 374. Guess Number Higher or Lower
We are playing the Guess Game. The game is as follows: I pick a number from 1 to `n`. You have to guess which number I picked.

**Test Cases:**
1. `n = 10, pick = 6` -> `6`
2. `n = 1, pick = 1` -> `1`
3. `n = 2, pick = 1` -> `1`
4. `n = 2, pick = 2` -> `2`
5. `n = 100, pick = 50` -> `50`

### 6. 509. Fibonacci Number
The Fibonacci numbers form a sequence where each number is the sum of the two preceding ones. Calculate `F(n)`.

**Test Cases:**
1. `n = 2` -> `1`
2. `n = 3` -> `2`
3. `n = 4` -> `3`
4. `n = 0` -> `0`
5. `n = 1` -> `1`

### 7. 704. Binary Search
Given an array of integers `nums` which is sorted in ascending order, and an integer `target`, write a function to search `target` in `nums`.

**Test Cases:**
1. `nums = [-1,0,3,5,9,12], target = 9` -> `4`
2. `nums = [-1,0,3,5,9,12], target = 2` -> `-1`
3. `nums = [5], target = 5` -> `0`
4. `nums = [5], target = -5` -> `-1`
5. `nums = [-5,-3,0,1,5], target = 5` -> `4`
