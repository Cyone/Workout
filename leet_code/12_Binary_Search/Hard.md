# Hard

### 1. 4. Median of Two Sorted Arrays

Given two sorted arrays `nums1` and `nums2` of size `m` and `n`, return the median of the two sorted arrays. The overall run time complexity should be O(log(m+n)).

**Test Cases:**

1. `nums1 = [1,3], nums2 = [2]` -> `2.0`
2. `nums1 = [1,2], nums2 = [3,4]` -> `2.5`
3. `nums1 = [0,0], nums2 = [0,0]` -> `0.0`
4. `nums1 = [], nums2 = [1]` -> `1.0`
5. `nums1 = [2], nums2 = []` -> `2.0`

### 2. 410. Split Array Largest Sum

Given an integer array `nums` and an integer `k`, split `nums` into `k` non-empty subarrays such that the largest sum of any subarray is minimized. Return this minimized largest sum.

**Test Cases:**

1. `nums = [7,2,5,10,8], k = 2` -> `18`
2. `nums = [1,2,3,4,5], k = 2` -> `9`
3. `nums = [1,4,4], k = 3` -> `4`
4. `nums = [1,2,3,4,5], k = 5` -> `5`
5. `nums = [10,5,13,4,8,4,5,11,14,9,16,10,20,8], k = 8` -> `25`

### 3. 1095. Find in Mountain Array

You may call a `MountainArray.get(index)` API. Given a mountain array (strictly increases then strictly decreases), find the minimum index of `target` or return -1.

**Test Cases:**

1. `array = [1,2,3,4,5,3,1], target = 3` -> `2`
2. `array = [0,1,2,4,2,1], target = 3` -> `-1`
3. `array = [1,5,2], target = 1` -> `0`
4. `array = [1,2,3,4,5], target = 5` -> `4`
5. `array = [3,5,3,2,0], target = 3` -> `0`
