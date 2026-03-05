import kotlin.math.max
import kotlin.math.min

class ArraysAndStrings {
    /**
    Given an array of integers `nums` and an integer `target`, 
    return indices of the two numbers such that they add up to `target`.
     */
    fun twoSum(nums: IntArray, target: Int): IntArray {
        val map = HashMap<Int, Int>()
        for (i in nums.indices) {
            val diff = target - nums[i]
            if (map.containsKey(diff)) {
                return intArrayOf(map[diff]!!, i)
            } else {
                map[nums[i]] = i
            }
        }
        return intArrayOf(-1, -1)
    }

    /**
     * Given an integer array nums sorted in non-decreasing order,
     * remove the duplicates in-place such that each unique element appears only once.
     */
    fun removeDuplicates(nums: IntArray): Int {
        if (nums.isEmpty()) return 0
        var left = 1
        for (i in 1 until nums.size) {
            if (nums[i] != nums[i - 1]) {
                nums[left] = nums[i]
                left++
            }
        }
        return left
    }

    /**
     * Given an integer array nums and an integer val, remove all occurrences of val in nums in-place.
     */
    fun removeElement(nums: IntArray, v: Int): Int {
        var k = 0

        for (i in nums.indices) {
            if (nums[i] != v) {
                nums[k] = nums[i]
                k++
            }
        }

        return k
    }

    /**
     * You are given an array prices where prices is the price of a given stock on the ith day.
     * Return the maximum profit you can achieve.
     */
    fun maxProfit(prices: IntArray): Int {
        var min = 0
        var profit = 0
        for (i in prices.indices) {
            if (prices[i] < prices[min]) min = i
            val diff = prices[i] - prices[min]
            if (prices[i] - prices[min] > profit) {
                profit = diff

            }
        }
        return profit
    }

    /**
     * Given an array nums of size n, return the majority element (appears more than ⌊n / 2⌋ times).
     */
    fun majorityElement(nums: IntArray): Int {
        var candidate = 0
        var count = 0
        for (num in nums) {
            if (count == 0) candidate = num
            if (num == candidate) count++ else count--
        }
        return candidate
    }

    /**
     * Given an integer array nums, move all 0s to the end of it while maintaining the relative order of the non-zero elements.
    nums = [0, 0, 1] -> [1, 0, 0]
    nums = [0, 1, 0, 3, 12] -> [1, 3, 12, 0, 0]
     */

    fun moveZeroes(nums: IntArray) {
        var left = 0
        for (i in nums.indices) {
            if (nums[i] != 0) {
                val temp = nums[left]
                nums[left] = nums[i]
                nums[i] = temp

                left++
            }
        }
    }

    /**
     * Write a function that reverses a string.
     * The input string is given as an array of characters s. Do this in-place.
     *
     */
    fun reverseString(s: CharArray): Unit {
        if (s.isEmpty()) return
        var left = 0
        var right = s.size - 1
        while (left < right) {
            val temp = s[left]
            s[left] = s[right]
            s[right] = temp
            left++; right--
        }
    }

    /**
     * Given a string s, find the length of the longest substring without repeating characters.
     */
    fun lengthOfLongestSubstring(s: String): Int {
        if (s.isEmpty()) return 0
        var length = 0
        var j = 0
        val map = mutableMapOf<Char, Int>()
        for (i in 0 until s.length) {
            val elem = s[i]
            if (map.contains(elem) && map[elem]!! >= j) {
                j = map[elem]!! + 1
            }
            map[elem] = i
            val tmp = i - j + 1
            if (tmp > length) length = tmp
        }
        return length
    }

    /**
     * Find two lines that together with the x-axis form a container,
     * such that the container contains the most water.
     */
    fun maxArea(height: IntArray): Int {
        if (height.isEmpty()) return 0
        var left = 0
        var right = height.size - 1
        var result = 0
        while (left < right) {
            val area = min(height[left], height[right]) * (right - left)
            result = max(area, result)
            if (height[left] < height[right]) left++ else right--
        }
        return result
    }

    /**
     * Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]]
     * such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
     *
     * Notice that the solution set must not contain duplicate triplets.
     */

    fun threeSum(nums: IntArray): List<List<Int>> {
        val result = mutableListOf<List<Int>>()

        // 1. Sort the array
        nums.sort()
        // 2. Iterate and fix the first number
        for (i in 0 until nums.size - 2) {
            // Skip duplicate values for the first number to avoid duplicate triplets
            if (i > 0 && nums[i] == nums[i - 1]) continue
            // If the smallest number is greater than 0, we can't ever sum to 0
            if (nums[i] > 0) break
            // 3. Two pointers for the remaining array
            var left = i + 1
            var right = nums.size - 1
            while (left < right) {
                val sum = nums[i] + nums[left] + nums[right]
                when {
                    sum == 0 -> {
                        result.add(listOf(nums[i], nums[left], nums[right]))
                        // Move pointers and skip duplicates for the second and third numbers
                        left++
                        right--
                        while (left < right && nums[left] == nums[left - 1]) left++
                        while (left < right && nums[right] == nums[right + 1]) right--
                    }
                    sum < 0 -> {
                        // Sum is too small, move left pointer to increase it
                        left++
                    }
                    else -> {
                        // Sum is too large, move right pointer to decrease it
                        right--
                    }
                }
            }
        }

        return result
    }
}