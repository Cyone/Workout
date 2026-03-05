# LeetCode: Arrays and Strings Patterns

*Reference: See `Algos.md` in your project for a deep dive into these patterns (Two Pointers, Sliding Window, Prefix Sum).*

## 1. Two Pointers

### Reverse String
**Difficulty:** Easy
**Problem:** Write a function that reverses a string. The input string is given as an array of characters `s`. You must do this by modifying the input array **in-place** with `O(1)` extra memory.
**Example:**
- Input: `s = ["h","e","l","l","o"]`
- Output: `["o","l","l","e","h"]`

### Squares of a Sorted Array
**Difficulty:** Easy
**Problem:** Given an integer array `nums` sorted in **non-decreasing** order, return an array of the *squares of each number* sorted in non-decreasing order.
**Example:**
- Input: `nums = [-4,-1,0,3,10]`
- Output: `[0,1,9,16,100]`
**Note:** Squaring the numbers gives `[16,1,0,9,100]`. Sorting them gives the result.

---

## 2. Sliding Window

### Maximum Average Subarray I
**Difficulty:** Easy
**Problem:** You are given an integer array `nums` consisting of `n` elements, and an integer `k`. Find a contiguous subarray whose length is equal to `k` that has the maximum average value and return this value.
**Example:**
- Input: `nums = [1,12,-5,-6,50,3]`, `k = 4`
- Output: `12.75000`
**Explanation:** Maximum average is `(12 - 5 - 6 + 50) / 4 = 51 / 4 = 12.75`.

### Max Consecutive Ones III
**Difficulty:** Medium
**Problem:** Given a binary array `nums` and an integer `k`, return the maximum number of consecutive `1`'s in the array if you can flip at most `k` `0`'s.
**Example:**
- Input: `nums = [1,1,1,0,0,0,1,1,1,1,0]`, `k = 2`
- Output: `6`
**Explanation:** [1,1,1,0,0,**1**,1,1,1,1,**1**] (Bolded numbers were flipped from 0 to 1).

---

## 3. Prefix Sum

### Running Sum of 1d Array
**Difficulty:** Easy
**Problem:** Given an array `nums`. We define a running sum of an array as `runningSum[i] = sum(nums[0]…nums[i])`. Return the running sum of `nums`.
**Example:**
- Input: `nums = [1,2,3,4]`
- Output: `[1,3,6,10]`

### Minimum Value to Get Positive Step by Step Sum
**Difficulty:** Easy
**Problem:** Given an array of integers `nums`, you start with an initial positive value `startValue`. In each iteration, you calculate the step by step sum of `startValue` plus elements in `nums` (from left to right). Return the minimum positive value of `startValue` such that the step by step sum is never less than `1`.
**Example:**
- Input: `nums = [-3,2,-3,4,2]`
- Output: `5`

### K Radius Subarray Averages
**Difficulty:** Medium
**Problem:** You are given a 0-indexed array `nums` of `n` integers, and an integer `k`. The **k-radius average** for the subarray centered at index `i` with the radius `k` is the average of all elements in `nums` between indices `i - k` and `i + k` (inclusive). If there are less than `k` elements before or after the index `i`, then the k-radius average is `-1`.
**Example:**
- Input: `nums = [7,4,3,9,1,8,5,2,6]`, `k = 3`
- Output: `[-1,-1,-1,5,4,4,-1,-1,-1]`

---

## 4. Kadane's Algorithm (Maximum Subarray)

### Maximum Subarray
**Difficulty:** Easy/Medium
**Pattern:** Dynamic Programming / Greedy — O(N) time, O(1) space
**Problem:** Given an integer array `nums`, find the contiguous subarray (at least one element) which has the largest sum and return its sum.
**Example:**
- Input: `nums = [-2,1,-3,4,-1,2,1,-5,4]`
- Output: `6`
- Explanation: The subarray `[4,-1,2,1]` has the largest sum = 6.

**Key Insight:** At each position, decide: is it better to extend the previous subarray, or start fresh from the current element?

```java
int maxSum = nums[0], currentSum = nums[0];
for (int i = 1; i < nums.length; i++) {
    currentSum = Math.max(nums[i], currentSum + nums[i]); // extend or restart
    maxSum = Math.max(maxSum, currentSum);
}
```

**How to spot Kadane's:** "maximum sum subarray", "largest sum contiguous", "best time to buy and sell" variants — all use the same extend-or-restart greedy reasoning.

---

## 4. More Common Patterns (Simulation & Traversal)

### Spiral Matrix
**Difficulty:** Medium
**Pattern:** Matrix Simulation
**Problem:** Given an `m x n` matrix, return all elements of the matrix in spiral order.
**Example:**
- Input: `matrix = [[1,2,3],[4,5,6],[7,8,9]]`
- Output: `[1,2,3,6,9,8,7,4,5]`

### Pascal's Triangle
**Difficulty:** Easy
**Pattern:** Dynamic Construction / Array
**Problem:** Given an integer `numRows`, return the first numRows of Pascal's triangle. In Pascal's triangle, each number is the sum of the two numbers directly above it.
**Example:**
- Input: `numRows = 5`
- Output: `[[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]`

---

## 5. Bonus Problems (Arrays & Strings)

### Move Zeroes
**Difficulty:** Easy
**Pattern:** Two Pointers (In-place)
**Problem:** Given an integer array `nums`, move all `0`'s to the end of it while maintaining the relative order of the non-zero elements. Note that you must do this in-place without making a copy of the array.

### Find Pivot Index
**Difficulty:** Easy
**Pattern:** Prefix Sum
**Problem:** Given an array of integers `nums`, calculate the pivot index of this array. The pivot index is the index where the sum of all the numbers strictly to the left of the index is equal to the sum of all the numbers strictly to the index's right.

### Reverse Words in a String III
**Difficulty:** Easy
**Pattern:** Two Pointers / String Manipulation
**Problem:** Given a string `s`, reverse the order of characters in each word within a sentence while still preserving whitespace and initial word order.
**Example:**
- Input: `s = "Let's take LeetCode contest"`
- Output: `"s'teL ekat edoCteeL tsetnoc"`

### Rotate Image
**Difficulty:** Medium
**Pattern:** Matrix / In-place
**Problem:** You are given an `n x n` 2D matrix representing an image, rotate the image by 90 degrees (clockwise). You have to rotate the image in-place.