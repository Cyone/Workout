# Mid

### 1. 53. Maximum Subarray
Given an integer array `nums`, find the contiguous subarray with the largest sum. Must be O(N) time (Kadane's Algorithm).

**Test Cases:**
1. `nums = [-2,1,-3,4,-1,2,1,-5,4]` -> `6`
2. `nums = [1]` -> `1`
3. `nums = [5,4,-1,7,8]` -> `23`
4. `nums = [-1]` -> `-1`
5. `nums = [-2,-1,-3]` -> `-1`

### 2. 238. Product of Array Except Self
Return an array `answer` such that `answer[i]` is equal to the product of all the elements of `nums` except `nums[i]`. Must be O(N) time and O(1) space without division.

**Test Cases:**
1. `nums = [1,2,3,4]` -> `[24,12,8,6]`
2. `nums = [-1,1,0,-3,3]` -> `[0,0,9,0,0]`
3. `nums = [0,0]` -> `[0,0]`
4. `nums = [1,0]` -> `[0,1]`
5. `nums = [2,3]` -> `[3,2]`

### 3. 287. Find the Duplicate Number
There is only one repeated number in `nums`. Do this without modifying `nums` and uses only constant extra space O(1).

**Test Cases:**
1. `nums = [1,3,4,2,2]` -> `2`
2. `nums = [3,1,3,4,2]` -> `3`
3. `nums = [1,1]` -> `1`
4. `nums = [1,1,2]` -> `1`
5. `nums = [2,2,2,2,2]` -> `2`


### 4. 5. Longest Palindromic Substring
Given a string s, return the longest palindromic substring in s. (O(N^2) Expand Around Center)

**Test Cases:**
1. `s = "babad"` -> `"bab"`
2. `s = "cbbd"` -> `"bb"`

### 5. 56. Merge Intervals
Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input. (O(N log N) Sorting algorithm)

**Test Cases:**
1. `intervals = [[1,3],[2,6],[8,10],[15,18]]` -> `[[1,6],[8,10],[15,18]]`

### 6. 15. 3Sum
Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0. (O(N^2) Two Pointers)

**Test Cases:**
1. `nums = [-1,0,1,2,-1,-4]` -> `[[-1,-1,2],[-1,0,1]]`
