/**
 * @author i.chernyshov
 * @date 16.02.2026
 */
object TwoPointers {
    /**
     * Reverse String
     * Difficulty: Easy Problem: Write a function that reverses a string. The input string is given as an array of characters s. You must do this by modifying the input array in-place with O(1) extra memory. Example:
     *
     * Input: s = ["h","e","l","l","o"]
     * Output: ["o","l","l","e","h"]
     */
    fun reverseString(chars: CharArray): CharArray {
        var start = 0
        var end = chars.size - 1
        while (start < end) {
            val temp = chars[start]
            chars[start] = chars[end]
            chars[end] = temp
            start++
            end--
        }
        return chars
    }

    /**
     * Squares of a Sorted Array
     * Difficulty: Easy Problem: Given an integer array nums sorted in non-decreasing order, return an array of the squares of each number sorted in non-decreasing order. Example:
     *
     * Input: nums = [-4,-1,0,3,10]
     * Output: [0,1,9,16,100] Note: Squaring the numbers gives [16,1,0,9,100]. Sorting them gives the result.
     */
    fun squares(nums: IntArray): IntArray {
        val size = nums.size
        val result = IntArray(size)
        var left = 0
        var right = size - 1

        for (i in size - 1 downTo 0) {
            val leftSquare = nums[left] * nums[left]
            val rightSquare = nums[right] * nums[right]

            if (leftSquare > rightSquare) {
                result[i] = leftSquare
                left++
            } else {
                result[i] = rightSquare
                right--
            }
        }
        return result
    }
}