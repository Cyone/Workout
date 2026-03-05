# Mid

### 1. 167. Two Sum II - Input Array Is Sorted
Given a 1-indexed array of integers `numbers` that is already sorted in non-decreasing order, find two numbers such that they add up to a specific `target` number.

**Test Cases:**
1. `numbers = [2,7,11,15], target = 9` -> `[1,2]`
2. `numbers = [2,3,4], target = 6` -> `[1,3]`
3. `numbers = [-1,0], target = -1` -> `[1,2]`
4. `numbers = [0,0,3,4], target = 0` -> `[1,2]`
5. `numbers = [-5,-3,-1,0], target = -4` -> `[2,3]`

### 2. 424. Longest Repeating Character Replacement
You can choose any character of the string and change it to any other uppercase English character at most `k` times. Return the length of the longest substring containing the same letter.

**Test Cases:**
1. `s = "ABAB", k = 2` -> `4`
2. `s = "AABABBA", k = 1` -> `4`
3. `s = "A", k = 0` -> `1`
4. `s = "ABAA", k = 0` -> `2`
5. `s = "ABAA", k = 1` -> `4`

### 3. 438. Find All Anagrams in a String
Given two strings `s` and `p`, return an array of all the start indices of `p`'s anagrams in `s`.

**Test Cases:**
1. `s = "cbaebabacd", p = "abc"` -> `[0,6]`
2. `s = "abab", p = "ab"` -> `[0,1,2]`
3. `s = "a", p = "a"` -> `[0]`
4. `s = "a", p = "ab"` -> `[]`
5. `s = "abacbabc", p = "abc"` -> `[1,2,3,5]`


### 4. 215. Kth Largest Element in an Array
Given an integer array nums and an integer k, return the kth largest element in the array. Note that it is the kth largest element in the sorted order, not the kth distinct element.

**Test Cases:**
1. `nums = [3,2,1,5,6,4], k = 2` -> `5`
2. `nums = [3,2,3,1,2,4,5,5,6], k = 4` -> `4`

### 5. 347. Top K Frequent Elements
Given an integer array nums and an integer k, return the k most frequent elements. You may return the answer in any order.

**Test Cases:**
1. `nums = [1,1,1,2,2,3], k = 2` -> `[1,2]`
2. `nums = [1], k = 1` -> `[1]`

### 6. 378. Kth Smallest Element in a Sorted Matrix
Given an n x n matrix where each of the rows and columns is sorted in ascending order, return the kth smallest element in the matrix. Note that it is the kth smallest element in the sorted order, not the kth distinct element.

**Test Cases:**
1. `matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8` -> `13`
2. `matrix = [[-5]], k = 1` -> `-5`

### 7. 373. Find K Pairs with Smallest Sums
You are given two integer arrays nums1 and nums2 sorted in ascending order and an integer k. Define a pair (u, v) which consists of one element from the first array and one element from the second array. Return the k pairs (u1, v1), (u2, v2), ..., (uk, vk) with the smallest sums.

**Test Cases:**
1. `nums1 = [1,7,11], nums2 = [2,4,6], k = 3` -> `[[1,2],[1,4],[1,6]]`
2. `nums1 = [1,1,2], nums2 = [1,2,3], k = 2` -> `[[1,1],[1,1]]`

### 8. 55. Jump Game
You are given an integer array nums. You are initially positioned at the array's first index, and each element in the array represents your maximum jump length at that position. Return true if you can reach the last index, or false otherwise.

**Test Cases:**
1. `nums = [2,3,1,1,4]` -> `true`
2. `nums = [3,2,1,0,4]` -> `false`

### 9. 134. Gas Station
There are n gas stations along a circular route, where the amount of gas at the ith station is gas[i]. You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from the ith station to its next (i + 1)th station. You begin the journey with an empty tank at one of the gas stations. Return the starting gas station's index if you can travel around the circuit once in the clockwise direction, otherwise return -1.

**Test Cases:**
1. `gas = [1,2,3,4,5], cost = [3,4,5,1,2]` -> `3`
2. `gas = [2,3,4], cost = [3,4,3]` -> `-1`

### 10. 322. Coin Change
You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money. Return the fewest number of coins that you need to make up that amount.

**Test Cases:**
1. `coins = [1,2,5], amount = 11` -> `3`
2. `coins = [2], amount = 3` -> `-1`

### 11. 300. Longest Increasing Subsequence
Given an integer array nums, return the length of the longest strictly increasing subsequence.

**Test Cases:**
1. `nums = [10,9,2,5,3,7,101,18]` -> `4`
2. `nums = [0,1,0,3,2,3]` -> `4`
