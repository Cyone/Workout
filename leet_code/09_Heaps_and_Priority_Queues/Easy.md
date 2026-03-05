# Easy

### 1. 703. Kth Largest Element in a Stream

Design a class to find the `kth` largest element in a stream. Note that it is the `kth` largest element in sorted order, not the `kth` distinct element. Implement the `KthLargest` class with `add(int val)` returning the current kth largest.

**Test Cases:**

1. `k = 3, nums = [4,5,8,2], add(3)` -> `4`
2. `k = 3, nums = [4,5,8,2], add(5)` -> `5`
3. `k = 1, nums = [], add(3), add(-1)` -> `3` then `3`
4. `k = 2, nums = [0], add(-1), add(1)` -> `0` then `1`
5. `k = 3, nums = [1,2,3,4], add(5)` -> `3`

### 2. 1046. Last Stone Weight

You are given an array of integers `stones` where `stones[i]` is the weight of the `i`th stone. Each turn, pick the two heaviest stones and smash them. Return the weight of the last remaining stone, or 0 if none.

**Test Cases:**

1. `stones = [2,7,4,1,8,1]` -> `1`
2. `stones = [1]` -> `1`
3. `stones = [2,2]` -> `0`
4. `stones = [10,4,2,10]` -> `2`
5. `stones = [3,3,3,3]` -> `0`

### 3. 215. Kth Largest Element in an Array (Easy variant)

Find the `kth` largest element in an integer array `nums`. Note: you need the kth largest in sorted order.

**Test Cases:**

1. `nums = [3,2,1,5,6,4], k = 2` -> `5`
2. `nums = [3,2,3,1,2,4,5,5,6], k = 4` -> `4`
3. `nums = [1], k = 1` -> `1`
4. `nums = [2,1], k = 1` -> `2`
5. `nums = [5,2,4,1,3,6,0], k = 3` -> `4`
