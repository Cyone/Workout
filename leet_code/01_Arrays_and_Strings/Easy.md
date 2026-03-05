# Easy

### 1. 1. Two Sum

Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.

**Test Cases:**

1. `nums = [2, 7, 11, 15], target = 9` -> `[0, 1]`
2. `nums = [3, 2, 4], target = 6` -> `[1, 2]`
3. `nums = [3, 3], target = 6` -> `[0, 1]`
4. `nums = [0, 4, 3, 0], target = 0` -> `[0, 3]`
5. `nums = [-1, -2, -3, -4, -5], target = -8` -> `[2, 4]`

### 2. 26. Remove Duplicates from Sorted Array

Given an integer array `nums` sorted in non-decreasing order, remove the duplicates in-place such that each unique element appears only once.

**Test Cases:**

1. `nums = [1, 1, 2]` -> `2, nums = [1, 2, _]`
2. `nums = [0, 0, 1, 1, 1, 2, 2, 3, 3, 4]` -> `5, nums = [0, 1, 2, 3, 4, _, _, _, _, _]`
3. `nums = []` -> `0, nums = []`
4. `nums = [1, 2, 3]` -> `3, nums = [1, 2, 3]`
5. `nums = [1, 1, 1, 1]` -> `1, nums = [1, _, _, _]`

### 3. 27. Remove Element

Given an integer array `nums` and an integer `val`, remove all occurrences of `val` in `nums` in-place.

**Test Cases:**

1. `nums = [3, 2, 2, 3], val = 3` -> `2, nums = [2, 2, _, _]`
2. `nums = [0, 1, 2, 2, 3, 0, 4, 2], val = 2` -> `5, nums = [0, 1, 3, 0, 4, _, _, _]`
3. `nums = [1], val = 1` -> `0, nums = [_]`
4. `nums = [1, 2, 3], val = 4` -> `3, nums = [1, 2, 3]`
5. `nums = [], val = 0` -> `0, nums = []`

### 4. 121. Best Time to Buy and Sell Stock

You are given an array `prices` where `prices[i]` is the price of a given stock on the `ith` day. Return the maximum profit you can achieve.

**Test Cases:**

1. `prices = [7, 1, 5, 3, 6, 4]` -> `5`
2. `prices = [7, 6, 4, 3, 1]` -> `0`
3. `prices = [1, 2]` -> `1`
4. `prices = [2, 4, 1]` -> `2`
5. `prices = [3, 2, 6, 5, 0, 3]` -> `4`

### 5. 169. Majority Element

Given an array `nums` of size `n`, return the majority element (appears more than `⌊n / 2⌋` times).

**Test Cases:**

1. `nums = [3, 2, 3]` -> `3`
2. `nums = [2, 2, 1, 1, 1, 2, 2]` -> `2`
3. `nums = [1]` -> `1`
4. `nums = [6, 5, 5]` -> `5`
5. `nums = [10, 9, 9, 9, 10]` -> `9`

### 6. 283. Move Zeroes

Given an integer array `nums`, move all `0`s to the end of it while maintaining the relative order of the non-zero elements.

**Test Cases:**

1. `nums = [0, 1, 0, 3, 12]` -> `[1, 3, 12, 0, 0]`
2. `nums = [0]` -> `[0]`
3. `nums = [1, 2, 3]` -> `[1, 2, 3]`
4. `nums = [0, 0, 1]` -> `[1, 0, 0]`
5. `nums = [4, 2, 4, 0, 0, 3, 0, 5, 1, 0]` -> `[4, 2, 4, 3, 5, 1, 0, 0, 0, 0]`

### 7. 344. Reverse String

Write a function that reverses a string. The input string is given as an array of characters `s`. Do this in-place.

**Test Cases:**

1. `s = ["h","e","l","l","o"]` -> `["o","l","l","e","h"]`
2. `s = ["H","a","n","n","a","h"]` -> `["h","a","n","n","a","H"]`
3. `s = ["a"]` -> `["a"]`
4. `s = ["a","b"]` -> `["b","a"]`
5. `s = []` -> `[]`
