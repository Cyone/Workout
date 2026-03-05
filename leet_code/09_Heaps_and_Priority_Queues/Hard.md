# Hard

### 1. 295. Find Median from Data Stream

Design a data structure that supports adding integers and finding the median. Implement `MedianFinder` with `addNum(int num)` and `findMedian()`.

**Test Cases:**

1. `addNum(1), addNum(2), findMedian()` -> `1.5`
2. `addNum(1), addNum(2), addNum(3), findMedian()` -> `2.0`
3. `addNum(5), findMedian(), addNum(3), findMedian()` -> `5.0` then `4.0`
4. `addNum(6), addNum(10), addNum(2), addNum(9), findMedian()` -> `7.5`
5. `addNum(-1), addNum(-2), addNum(-3), findMedian()` -> `-2.0`

### 2. 239. Sliding Window Maximum

Given an integer array `nums` and a sliding window of size `k`, return an array of the maximum values in each window position.

**Test Cases:**

1. `nums = [1,3,-1,-3,5,3,6,7], k = 3` -> `[3,3,5,5,6,7]`
2. `nums = [1], k = 1` -> `[1]`
3. `nums = [1,-1], k = 1` -> `[1,-1]`
4. `nums = [9,11], k = 2` -> `[11]`
5. `nums = [4,-2,0,5,3,2,1], k = 3` -> `[4,5,5,5,3]`

### 3. 480. Sliding Window Median

Given an integer array `nums` and an integer `k`, return the median array for each window of size `k`.

**Test Cases:**

1. `nums = [1,3,-1,-3,5,3,6,7], k = 3` -> `[1.0,-1.0,-1.0,3.0,5.0,6.0]`
2. `nums = [1,2,3,4,2,3,1,4,2], k = 3` -> `[2.0,3.0,3.0,3.0,2.0,3.0,2.0]`
3. `nums = [1], k = 1` -> `[1.0]`
4. `nums = [5,5,5], k = 2` -> `[5.0,5.0]`
5. `nums = [2,1,5,3,4], k = 2` -> `[1.5,3.0,4.0,3.5]`

### 4. 632. Smallest Range Covering Elements from K Lists

You have `k` lists of sorted integers. Find the smallest range `[a, b]` such that there is at least one number from each list within the range.

**Test Cases:**

1. `nums = [[4,10,15,24,26],[0,9,12,20],[5,18,22,30]]` -> `[20,24]`
2. `nums = [[1,2,3],[1,2,3],[1,2,3]]` -> `[1,1]`
3. `nums = [[1],[1]]` -> `[1,1]`
4. `nums = [[1,7],[2,8],[3,9]]` -> `[7,9]`
5. `nums = [[1,2],[3,4],[5,6]]` -> `[2,6]` (one from each)
