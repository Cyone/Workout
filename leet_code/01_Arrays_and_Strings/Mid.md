# Mid

### 1. 3. Longest Substring Without Repeating Characters
Given a string `s`, find the length of the longest substring without repeating characters.

**Test Cases:**
1. `s = "abcabcbb"` -> `3`
2. `s = "bbbbb"` -> `1`
3. `s = "pwwkew"` -> `3`
4. `s = ""` -> `0`
5. `s = "au"` -> `2`

### 2. 11. Container With Most Water
Find two lines that together with the x-axis form a container, such that the container contains the most water.

**Test Cases:**
1. `height = [1,8,6,2,5,4,8,3,7]` -> `49`
2. `height = [1,1]` -> `1`
3. `height = [4,3,2,1,4]` -> `16`
4. `height = [1,2,1]` -> `2`
5. `height = [0,2]` -> `0`

### 3. 15. 3Sum
Given an integer array nums, return all the triplets `[nums[i], nums[j], nums[k]]` such that they add up to 0.

**Test Cases:**
1. `nums = [-1,0,1,2,-1,-4]` -> `[[-1,-1,2],[-1,0,1]]`
2. `nums = [0,1,1]` -> `[]`
3. `nums = [0,0,0]` -> `[[0,0,0]]`
4. `nums = [1,-1,-1,0]` -> `[[-1,0,1]]`
5. `nums = [-2,0,0,2,2]` -> `[[-2,0,2]]`

### 4. 49. Group Anagrams
Given an array of strings `strs`, group the anagrams together. You can return the answer in any order.

**Test Cases:**
1. `strs = ["eat","tea","tan","ate","nat","bat"]` -> `[["bat"],["nat","tan"],["ate","eat","tea"]]`
2. `strs = [""]` -> `[[""]]`
3. `strs = ["a"]` -> `[["a"]]`
4. `strs = ["abc","cba","bac","def"]` -> `[["abc","cba","bac"],["def"]]`
5. `strs = ["hello","world"]` -> `[["hello"],["world"]]`

### 5. 238. Product of Array Except Self
Given an integer array `nums`, return an array `answer` such that `answer[i]` is equal to the product of all the elements of `nums` except `nums[i]`.

**Test Cases:**
1. `nums = [1,2,3,4]` -> `[24,12,8,6]`
2. `nums = [-1,1,0,-3,3]` -> `[0,0,9,0,0]`
3. `nums = [1,1,1]` -> `[1,1,1]`
4. `nums = [0,0]` -> `[0,0]`
5. `nums = [2,3,4,5]` -> `[60,40,30,24]`

### 6. 334. Increasing Triplet Subsequence
Given an integer array `nums`, return `true` if there exists a triple of indices `(i, j, k)` such that `i < j < k` and `nums[i] < nums[j] < nums[k]`. Otherwise, return `false`.

**Test Cases:**
1. `nums = [1,2,3,4,5]` -> `true`
2. `nums = [5,4,3,2,1]` -> `false`
3. `nums = [2,1,5,0,4,6]` -> `true`
4. `nums = [1,1,1]` -> `false`
5. `nums = [20,100,10,12,5,13]` -> `true`


### 7. 209. Minimum Size Subarray Sum
Given an array of positive integers nums and a positive integer target, return the minimal length of a contiguous subarray [numsl, numsl+1, ..., numsr-1, numsr] of which the sum is greater than or equal to target. If there is no such subarray, return 0 instead.

**Test Cases:**
1. `target = 7, nums = [2,3,1,2,4,3]` -> `2`
2. `target = 4, nums = [1,4,4]` -> `1`

### 8. 560. Subarray Sum Equals K
Given an array of integers nums and an integer k, return the total number of continuous subarrays whose sum equals to k.

**Test Cases:**
1. `nums = [1,1,1], k = 2` -> `2`
2. `nums = [1,2,3], k = 3` -> `2`

### 9. 287. Find the Duplicate Number
Given an array of integers nums containing n + 1 integers where each integer is in the range [1, n] inclusive. There is only one repeated number in nums, return this repeated number. You must solve the problem without modifying the array nums and uses only constant extra space.

**Test Cases:**
1. `nums = [1,3,4,2,2]` -> `2`
2. `nums = [3,1,3,4,2]` -> `3`

### 10. 457. Circular Array Loop
You are playing a game involving a circular array of non-zero integers nums. Each nums[i] denotes the number of indices forward/backward you must move. Return true if there is a cycle, false otherwise.

**Test Cases:**
1. `nums = [2,-1,1,2,2]` -> `true`
2. `nums = [-1,2]` -> `false`
