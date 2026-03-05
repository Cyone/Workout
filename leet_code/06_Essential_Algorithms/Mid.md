# Mid

### 1. 33. Search in Rotated Sorted Array
Given an array `nums` after a possible rotation and an integer `target`, return the index of `target` if it is in `nums`, or `-1` if it is not in `nums`.

**Test Cases:**
1. `nums = [4,5,6,7,0,1,2], target = 0` -> `4`
2. `nums = [4,5,6,7,0,1,2], target = 3` -> `-1`
3. `nums = [1], target = 0` -> `-1`
4. `nums = [1,3], target = 3` -> `1`
5. `nums = [3,1], target = 1` -> `1`

### 2. 56. Merge Intervals
Given an array of `intervals`, merge all overlapping intervals, and return an array of the non-overlapping intervals.

**Test Cases:**
1. `intervals = [[1,3],[2,6],[8,10],[15,18]]` -> `[[1,6],[8,10],[15,18]]`
2. `intervals = [[1,4],[4,5]]` -> `[[1,5]]`
3. `intervals = [[1,4],[2,3]]` -> `[[1,4]]`
4. `intervals = [[1,1]]` -> `[[1,1]]`
5. `intervals = [[1,4],[0,4]]` -> `[[0,4]]`

### 3. 74. Search a 2D Matrix
Write an efficient algorithm that searches for a value `target` in an `m x n` integer matrix `matrix` which is sorted.

**Test Cases:**
1. `matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3` -> `true`
2. `matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13` -> `false`
3. `matrix = [[1]], target = 1` -> `true`
4. `matrix = [[1]], target = 0` -> `false`
5. `matrix = [[1,3]], target = 3` -> `true`


### 4. 57. Insert Interval
You are given an array of non-overlapping intervals sorted in ascending order by starti. Insert a new interval into the intervals (merge if necessary).

**Test Cases:**
1. `intervals = [[1,3],[6,9]], newInterval = [2,5]` -> `[[1,5],[6,9]]`
2. `intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]` -> `[[1,2],[3,10],[12,16]]`

### 5. 435. Non-overlapping Intervals
Given an array of intervals, return the minimum number of intervals you need to remove to make the rest of the intervals non-overlapping.

**Test Cases:**
1. `intervals = [[1,2],[2,3],[3,4],[1,3]]` -> `1`
2. `intervals = [[1,2],[1,2],[1,2]]` -> `2`

### 6. 287. Find the Duplicate Number
Given an array of integers nums containing n + 1 integers where each integer is in the range [1, n] inclusive. There is only one repeated number in nums, return this repeated number. (Cyclic Sort approach)

**Test Cases:**
1. `nums = [1,3,4,2,2]` -> `2`

### 7. 442. Find All Duplicates in an Array
Given an integer array nums of length n where all the integers of nums are in the range [1, n] and each integer appears once or twice, return an array of all the integers that appears twice. You must write an algorithm that runs in O(n) time and uses only constant extra space.

**Test Cases:**
1. `nums = [4,3,2,7,8,2,3,1]` -> `[2,3]`
2. `nums = [1,1,2]` -> `[1]`

### 8. 46. Permutations
Given an array nums of distinct integers, return all the possible permutations. You can return the answer in any order.

**Test Cases:**
1. `nums = [1,2,3]` -> `[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]`
2. `nums = [0,1]` -> `[[0,1],[1,0]]`

### 9. 78. Subsets
Given an integer array nums of unique elements, return all possible subsets (the power set). The solution set must not contain duplicate subsets.

**Test Cases:**
1. `nums = [1,2,3]` -> `[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]`
2. `nums = [0]` -> `[[],[0]]`
