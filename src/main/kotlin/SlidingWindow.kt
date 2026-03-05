/**
 * @author i.chernyshov
 * @date 17.02.2026
 */
class SlidingWindow {

    /**
     * Difficulty: Easy Problem: You are given an integer array nums consisting of n elements, and an integer k. Find a contiguous subarray whose length is equal to k that has the maximum average value and return this value. Example:
     *
     * Input: nums = [1,12,-5,-6,50,3], k = 4
     * Output: 12.75000 Explanation: Maximum average is (12 - 5 - 6 + 50) / 4 = 51 / 4 = 12.75.
     */
    fun maxAvgSubArray(nums: IntArray, k: Int): Double {
        var lastSum = 0
        for (i in 0 until k) lastSum += nums[i]
        var maxSum = lastSum
        for (i in k until nums.size) {
            val sum = lastSum - nums[i - k] + nums[i]
            if (maxSum < sum) maxSum = sum
            lastSum = sum
        }
        return maxSum.div(k.toDouble())

    }

    fun maxConsecutiveOnes(nums: IntArray, k: Int): Int {
        var count = 0
        for (i in 0 until k) {
            if (nums[i] == 1) count++
        }
        var maxCount = count
        for (i in k until nums.size) {
            if (nums[i] == 1) count++
            if (nums[i - k] == 1) count--
            if (maxCount < count) maxCount = count
        }
        return maxCount
    }
}