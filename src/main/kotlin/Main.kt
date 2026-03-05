/**
 * @author i.chernyshov
 * @date 15.02.2026
 */
fun main() {
    testReverseString()
    testSquares()
    testSlidingWindow()
    testTwoSum()
    testRemoveDuplicates()
    testRemoveElement()
    testMaxProfit()
    testMajorityElement()
    testMoveZeroes()
    testLongestSubstring()
    testMaxArea()
    testThreeSum()
    testMergeTwoLists()
    testDeleteDuplicates()
    testAddTwoNumbers()
    testIsValid()
    testNextGreaterElement()
    testInorderTraversal()
}


fun testSquares() {
    val solve = TwoPointers.squares(intArrayOf(-4, -1, 0, 3, 10))
    val e = solve.contentEquals(intArrayOf(0, 1, 9, 16, 100))
    println(solve.map { it.toString() }.reduce { acc, s -> "$acc, $s" })
    if (!e) throw IllegalStateException("Expected squares")
}

fun testSlidingWindow() {
    val solve = SlidingWindow().maxAvgSubArray(intArrayOf(1, 12, -5, -6, 50, 3), 4)
    val e = solve == 12.75
    println(solve)
    if (!e) throw IllegalStateException("Expected max avg sub array")
}

/**
 * `nums = [2, 7, 11, 15], target = 9` -> `[0, 1]`
 * `nums = [3, 2, 4], target = 6` -> `[1, 2]`
 * `nums = [3, 3], target = 6` -> `[0, 1]`
 * `nums = [0, 4, 3, 0], target = 0` -> `[0, 3]`
 * `nums = [-1, -2, -3, -4, -5], target = -8` -> `[2, 4]`
 */
fun testTwoSum() {
    val arraysAndStrings = ArraysAndStrings()
    val twoSumTests: MutableList<Triple<IntArray, Int, IntArray>> = mutableListOf()
    with(twoSumTests) {
        add(Triple(intArrayOf(2, 7, 11, 15), 9, intArrayOf(0, 1)))
        add(Triple(intArrayOf(3, 2, 4), 6, intArrayOf(1, 2)))
        add(Triple(intArrayOf(3, 3), 6, intArrayOf(0, 1)))
        add(Triple(intArrayOf(0, 4, 3, 0), 0, intArrayOf(0, 3)))
        add(Triple(intArrayOf(-1, -2, -3, -4, -5), -8, intArrayOf(2, 4)))
    }
    println("TwoSum")
    twoSumTests.onEach {
        val twoSum = arraysAndStrings.twoSum(it.first, it.second)
        assert(twoSum.contentEquals(it.third))
    }
}

/**
 * nums = [1, 1, 2] -> 2, nums = [1, 2, _]
 * nums = [0, 0, 1, 1, 1, 2, 2, 3, 3, 4] -> 5, nums = [0, 1, 2, 3, 4, _, _, _, _, _]
 * nums = [] -> 0, nums = []
 * nums = [1, 2, 3] -> 3, nums = [1, 2, 3]
 * nums = [1, 1, 1, 1] -> 1, nums = [1, _, _, _]
 */
fun testRemoveDuplicates() {
    val arraysAndStrings = ArraysAndStrings()
    println("Remove Duplicates")
    mutableListOf<Pair<IntArray, Int>>().apply {
        add(Pair(intArrayOf(1, 1, 2), 2))
        add(Pair(intArrayOf(0, 0, 1, 1, 1, 2, 2, 3, 3, 4), 5))
        add(Pair(intArrayOf(), 0))
        add(Pair(intArrayOf(1, 2, 3), 3))
        add(Pair(intArrayOf(1, 1, 1, 1), 1))
    }.onEach {
        val removeDuplicates = arraysAndStrings.removeDuplicates(it.first)
        println(removeDuplicates)
        assert(removeDuplicates == it.second)
    }
}

/**
 * nums = [3, 2, 2, 3], val = 3 -> 2, nums = [2, 2, _, _]
 * nums = [0, 1, 2, 2, 3, 0, 4, 2], val = 2 -> 5, nums = [0, 1, 3, 0, 4, _, _, _]
 * nums = [1], val = 1 -> 0, nums = [_]
 * nums = [1, 2, 3], val = 4 -> 3, nums = [1, 2, 3]
 * nums = [], val = 0 -> 0, nums = []
 */
fun testRemoveElement() {
    val arraysAndStrings = ArraysAndStrings()
    println("Remove Element")
    mutableListOf<Triple<IntArray, Int, Int>>().apply {
        add(Triple(intArrayOf(3, 2, 2, 3), 3, 2))
        add(Triple(intArrayOf(0, 1, 2, 2, 3, 0, 4, 2), 2, 5))
        add(Triple(intArrayOf(1, 2, 3), 4, 3))
        add(Triple(intArrayOf(), 0, 0))
        add(Triple(intArrayOf(1), 1, 0))
        add(Triple(intArrayOf(4, 5), 4, 1))
        add(Triple(intArrayOf(4, 5), 5, 1))
    }.onEach {
        val removeElement = arraysAndStrings.removeElement(it.first, it.second)
        println(removeElement)
        assert(removeElement == it.third)
    }

}

/**
 * prices = [7, 1, 5, 3, 6, 4] -> 5
 * prices = [7, 6, 4, 3, 1] -> 0
 * prices = [1, 2] -> 1
 * prices = [2, 4, 1] -> 2
 * prices = [3, 2, 6, 5, 0, 3] -> 4
 */
fun testMaxProfit() {
    val arraysAndStrings = ArraysAndStrings()
    println("Max Profit")
    mutableListOf<Pair<IntArray, Int>>().apply {
        add(Pair(intArrayOf(7, 1, 5, 3, 6, 4), 5))
        add(Pair(intArrayOf(7, 6, 4, 3, 1), 0))
        add(Pair(intArrayOf(1, 2), 1))
        add(Pair(intArrayOf(2, 4, 1), 2))
        add(Pair(intArrayOf(3, 2, 6, 5, 0, 3), 4))
    }.onEach {
        val maxProfit = arraysAndStrings.maxProfit(it.first)
        println(maxProfit)
        assert(maxProfit == it.second)
    }
}

/**
 * nums = [3, 2, 3] -> 3
 * nums = [2, 2, 1, 1, 1, 2, 2] -> 2
 * nums = [1] -> 1
 * nums = [6, 5, 5] -> 5
 * nums = [10, 9, 9, 9, 10] -> 9
 * 
 */
fun testMajorityElement() {
    val arraysAndStrings = ArraysAndStrings()
    println("Majority Element")
    mutableListOf<Pair<IntArray, Int>>().apply {
        add(Pair(intArrayOf(3, 2, 3), 3))
        add(Pair(intArrayOf(2, 2, 1, 1, 1, 2, 2), 2))
        add(Pair(intArrayOf(1), 1))
        add(Pair(intArrayOf(6, 5, 5), 5))
        add(Pair(intArrayOf(10, 9, 9, 9, 10), 9))
    }.onEach {
        val majorityElement = arraysAndStrings.majorityElement(it.first)
        println(majorityElement)
        assert(majorityElement == it.second)
    }
}

/**
 * nums = [0, 1, 0, 3, 12] -> [1, 3, 12, 0, 0]
 *nums = [0] -> [0]
 *nums = [1, 2, 3] -> [1, 2, 3]
 *nums = [0, 0, 1] -> [1, 0, 0]
 *nums = [4, 2, 4, 0, 0, 3, 0, 5, 1, 0] -> [4, 2, 4, 3, 5, 1, 0, 0, 0, 0]
 */
fun testMoveZeroes() {
    val arraysAndStrings = ArraysAndStrings()
    println("Move Zeroes")
    mutableListOf<Pair<IntArray, IntArray>>().apply {
        add(Pair(intArrayOf(0, 1, 0, 3, 12), intArrayOf(1, 3, 12, 0, 0)))
        add(Pair(intArrayOf(0), intArrayOf(0)))
        add(Pair(intArrayOf(1, 2, 3), intArrayOf(1, 2, 3)))
        add(Pair(intArrayOf(0, 0, 1), intArrayOf(1, 0, 0)))
        add(Pair(intArrayOf(4, 2, 4, 0, 0, 3, 0, 5, 1, 0), intArrayOf(4, 2, 4, 3, 5, 1, 0, 0, 0, 0)))
        add(Pair(intArrayOf(0, 1), intArrayOf(1, 0)))
    }.onEach {
        arraysAndStrings.moveZeroes(it.first)
        println(it.first.contentToString())
        assert(it.first.contentEquals(it.second))
    }
}

/**
 * 
 * s = ["h","e","l","l","o"] -> ["o","l","l","e","h"]
 * s = ["H","a","n","n","a","h"] -> ["h","a","n","n","a","H"]
 * s = ["a"] -> ["a"]
 * s = ["a","b"] -> ["b","a"]
 * s = [] -> []
 */
fun testReverseString() {
    val arraysAndStrings = ArraysAndStrings()
    println("Reverse String")
    mutableListOf<Pair<CharArray, CharArray>>().apply {
        add(Pair(charArrayOf('h', 'e', 'l', 'l', 'o'), charArrayOf('o', 'l', 'l', 'e', 'h')))
        add(Pair(charArrayOf('H', 'a', 'n', 'n', 'a', 'h'), charArrayOf('h', 'a', 'n', 'n', 'a', 'H')))
        add(Pair(charArrayOf('a'), charArrayOf('a')))
        add(Pair(charArrayOf('a', 'b'), charArrayOf('b', 'a')))
        add(Pair(charArrayOf(), charArrayOf()))
    }.onEach {
        arraysAndStrings.reverseString(it.first)
        println(it.first.contentToString())
        assert(it.first.contentEquals(it.second))
    }
}

/**
 * s = "abcabcbb" -> 3
 * s = "bbbbb" -> 1
 * s = "pwwkew" -> 3
 * s = "" -> 0
 * s = "au" -> 2
 * 
 */
fun testLongestSubstring() {
    val arraysAndStrings = ArraysAndStrings()
    println("Longest Substring")
    mutableListOf<Pair<String, Int>>().apply {
        add(Pair("abcabcbb", 3))
        add(Pair("bbbbb", 1))
        add(Pair("pwwkew", 3))
        add(Pair("", 0))
        add(Pair("au", 2))
        add(Pair("aab", 2))

    }.onEach {
        val lengthOfLongestSubstring = arraysAndStrings.lengthOfLongestSubstring(it.first)
        println(lengthOfLongestSubstring)
        assert(lengthOfLongestSubstring == it.second)
    }
}

/**
 * height = [1,8,6,2,5,4,8,3,7] -> 49
 * height = [1,1] -> 1
 * height = [4,3,2,1,4] -> 16
 * height = [1,2,1] -> 2
 * height = [0,2] -> 0
 */
fun testMaxArea() {
    val arraysAndStrings = ArraysAndStrings()
    println("Max Area")
    mutableListOf<Pair<IntArray, Int>>().apply {
        add(Pair(intArrayOf(1, 8, 6, 2, 5, 4, 8, 3, 7), 49))
        add(Pair(intArrayOf(1, 1), 1))
        add(Pair(intArrayOf(4, 3, 2, 1, 4), 16))
        add(Pair(intArrayOf(1, 2, 1), 2))
        add(Pair(intArrayOf(0, 2), 0))
    }.onEach {
        val maxArea = arraysAndStrings.maxArea(it.first)
        println(maxArea)
        assert(maxArea == it.second)
    }
}

/**
 *nums = [-1,0,1,2,-1,-4] -> [[-1,-1,2],[-1,0,1]]
 * nums = [0,1,1] -> []
 * nums = [0,0,0] -> [[0,0,0]]
 * nums = [1,-1,-1,0] -> [[-1,0,1]]
 * nums = [-2,0,0,2,2] -> [[-2,0,2]]
 */
fun testThreeSum() {
    val arraysAndStrings = ArraysAndStrings()
    println("Three Sum")
    mutableListOf<Pair<IntArray, List<List<Int>>>>().apply {
        add(Pair(intArrayOf(-1, 0, 1, 2, -1, -4), listOf(listOf(-1, -1, 2), listOf(-1, 0, 1))))
        add(Pair(intArrayOf(0, 1, 1), listOf()))
        add(Pair(intArrayOf(0, 0, 0), listOf(listOf(0, 0, 0))))
        add(Pair(intArrayOf(1, -1, -1, 0), listOf(listOf(-1, 0, 1))))
        add(Pair(intArrayOf(-2, 0, 0, 2, 2), listOf(listOf(-2, 0, 2))))
    }.onEach {
        val threeSum = arraysAndStrings.threeSum(it.first)
        println(threeSum)
        assert(threeSum.toSet() == it.second.toSet())
    }
}

/**
 * list1 = [1,2,4], list2 = [1,3,4] -> [1,1,2,3,4,4]
 * list1 = [], list2 = [] -> []
 * list1 = [], list2 = [0] -> [0]
 * list1 = [2], list2 = [1] -> [1,2]
 * list1 = [1,1], list2 = [2,2] -> [1,1,2,2]
 */
fun testMergeTwoLists() {
    val linkedLists = LinkedLists()
    println("Merge Two Lists")
    mutableListOf<Triple<ListNode?, ListNode?, ListNode?>>().apply {
        add(
            Triple(
                createList(intArrayOf(1, 2, 4)),
                createList(intArrayOf(1, 3, 4)),
                createList(intArrayOf(1, 1, 2, 3, 4, 4))
            )
        )
        add(Triple(null, null, null))
        add(Triple(null, createList(intArrayOf(0)), createList(intArrayOf(0))))
        add(Triple(createList(intArrayOf(2)), createList(intArrayOf(1)), createList(intArrayOf(1, 2))))
        add(Triple(createList(intArrayOf(1, 1)), createList(intArrayOf(2, 2)), createList(intArrayOf(1, 1, 2, 2))))
    }.onEach {
        val merged = linkedLists.mergeTwoLists(it.first, it.second)
        println(listToString(merged))
        assert(listsEqual(merged, it.third))
    }
}

/**
 * head = [1,1,2] -> [1,2]
 * head = [1,1,2,3,3] -> [1,2,3]
 * head = [] -> []
 * head = [1,1,1] -> [1]
 * head = [1,2,3] -> [1,2,3]
 */
fun testDeleteDuplicates() {
    val linkedLists = LinkedLists()
    println("Delete Duplicates")
    mutableListOf<Pair<ListNode?, ListNode?>>().apply {
        add(Pair(createList(intArrayOf(1, 1, 2)), createList(intArrayOf(1, 2))))
        add(Pair(createList(intArrayOf(1, 1, 2, 3, 3)), createList(intArrayOf(1, 2, 3))))
        add(Pair(null, null))
        add(Pair(createList(intArrayOf(1, 1, 1)), createList(intArrayOf(1))))
        add(Pair(createList(intArrayOf(1, 2, 3)), createList(intArrayOf(1, 2, 3))))
    }.onEach {
        val result = linkedLists.deleteDuplicates(it.first)
        println(result.toString())
        assert(listsEqual(result, it.second))
        println(linkedLists.test(it.first))
    }
}
/**
 * l1 = [2,4,3], l2 = [5,6,4] -> [7,0,8]
 * l1 = [0], l2 = [0] -> [0]
 * l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9] -> [8,9,9,9,0,0,0,1]
 * l1 = [2,4], l2 = [5,6,4] -> [7,0,5]
 * l1 = [5], l2 = [5] -> [0,1]
 */
fun testAddTwoNumbers() {
    val linkedLists = LinkedLists()
    println("Add Two Numbers")
    mutableListOf<Triple<ListNode?, ListNode?, ListNode?>>().apply {
        add(
            Triple(
                createList(intArrayOf(2, 4, 3)),
                createList(intArrayOf(5, 6, 4)),
                createList(intArrayOf(7, 0, 8))
            )
        )
        add(
            Triple(
                createList(intArrayOf(0)),
                createList(intArrayOf(0)),
                createList(intArrayOf(0))
            )
        )
        add(
            Triple(
                createList(intArrayOf(9, 9, 9, 9, 9, 9, 9)),
                createList(intArrayOf(9, 9, 9, 9)),
                createList(intArrayOf(8, 9, 9, 9, 0, 0, 0, 1))
            )
        )
        add(
            Triple(
                createList(intArrayOf(2, 4)),
                createList(intArrayOf(5, 6, 4)),
                createList(intArrayOf(7, 0, 5))
            )
        )
        add(
            Triple(
                createList(intArrayOf(5)),
                createList(intArrayOf(5)),
                createList(intArrayOf(0, 1))
            )
        )
    }.onEach {
        val result = linkedLists.addTwoNumbers(it.first, it.second)
        println(listToString(result))
        assert(listsEqual(result, it.third))
    }
}

/**
 * s = "()" -> true
 * s = "()[]{}" -> true
 * s = "(]" -> false
 * s = "([)]" -> false
 * s = "{[]}" -> true
 */
fun testIsValid(){
    val linkedLists = LinkedLists()
    println("Is Valid")
    mutableListOf<Pair<String, Boolean>>().apply {
        add(Pair("]", false))
        add(Pair("()", true))
        add(Pair("()[]{}", true))
        add(Pair("(]", false))
        add(Pair("([)]", false))
        add(Pair("{[]}", true))

    }.onEach {
        val result = linkedLists.isValid(it.first)
        println(result)
        assert(result == it.second)
    }
}

/**
 *nums1 = [4,1,2], nums2 = [1,3,4,2] -> [-1,3,-1]
 * nums1 = [2,4], nums2 = [1,2,3,4] -> [3,-1]
 * nums1 = [1], nums2 = [1,2] -> [2]
 * nums1 = [2], nums2 = [2,1] -> [-1]
 * nums1 = [1,3], nums2 = [1,2,3,4] -> [2,4]
 * 
 */
fun testNextGreaterElement() {
    val linkedLists = LinkedLists()
    println("Next Greater Element")
    mutableListOf<Triple<IntArray, IntArray, IntArray>>().apply {
        add(Triple(intArrayOf(4, 1, 2), intArrayOf(1, 3, 4, 2), intArrayOf(-1, 3, -1)))
        add(Triple(intArrayOf(2, 4), intArrayOf(1, 2, 3, 4), intArrayOf(3, -1)))
        add(Triple(intArrayOf(1), intArrayOf(1, 2), intArrayOf(2)))
        add(Triple(intArrayOf(2), intArrayOf(2, 1), intArrayOf(-1)))
        add(Triple(intArrayOf(1, 3), intArrayOf(1, 2, 3, 4), intArrayOf(2, 4)))
    }.onEach {
        val result = linkedLists.nextGreaterElement(it.first, it.second)
        println(result.contentToString())
        assert(result.contentEquals(it.third))
    }
}

/**
 * root = [1,null,2,3] -> [1,3,2]
 * root = [] -> []
 * root = [1] -> [1]
 * root = [1,2] -> [2,1]
 * root = [1,null,2] -> [1,2]
 */
fun testInorderTraversal() {
    val treesAndGraphs = TreesAndGraphs()
    println("Inorder Traversal")
    mutableListOf<Pair<TreeNode?, List<Int>>>().apply {
        add(Pair(createTreeNode(arrayOf(1, null, 2, 3)), listOf(1, 3, 2)))
        add(Pair(null, listOf()))
        add(Pair(createTreeNode(arrayOf(1)), listOf(1)))
        add(Pair(createTreeNode(arrayOf(1, 2)), listOf(2, 1)))
        add(Pair(createTreeNode(arrayOf(1, null, 2)), listOf(1, 2)))
    }.onEach {
        val result = treesAndGraphs.inorderTraversal(it.first)
        println(result)
        assert(result == it.second)
    }
}




