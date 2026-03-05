import java.util.*

/**
 * @author i.chernyshov
 * @date 02.03.2026
 */
class LinkedLists {

    /**
     * Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.
     */
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        val dummy = ListNode(-1)
        var current = dummy

        var l1 = list1
        var l2 = list2

        while (l1 != null && l2 != null) {
            if (l1.`val` <= l2.`val`) {
                current.next = l1
                l1 = l1.next
            } else {
                current.next = l2
                l2 = l2.next
            }
            current = current.next!!
        }

        // Attach whichever list still has elements left
        current.next = l1 ?: l2

        return dummy.next
    }

    /**
     * Given the head of a sorted linked list, delete all duplicates such that each element appears only once.
     */
    fun deleteDuplicates(head: ListNode?): ListNode? {
        var cur = head

        while (cur?.next != null) {
            if (cur.`val` == cur.next!!.`val`) {
                cur.next = cur.next!!.next
            } else {
                cur = cur.next
            }
        }

        return head
    }

    /**
     * You are given two non-empty linked lists representing two non-negative integers.
     * The digits are stored in reverse order, and each of their nodes contains a single digit.
     * Add the two numbers and return the sum as a linked list.

     * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
     */
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        var carried = 0
        var dummy = ListNode(0)
        var result = dummy
        var list1 = l1
        var list2 = l2
        while (list1 != null || list2 != null || carried > 0) {
            val sum = (list1?.`val` ?: 0) + (list2?.`val` ?: 0) + carried

            carried = sum / 10
            val listNode = ListNode(sum % 10)
            result.next = listNode
            result = listNode

            list1 = list1?.next
            list2 = list2?.next
        }

        return dummy.next
    }

    /**
     * Given a string s containing just the characters (, ), {, }, [ and ], determine if the input string is valid.
     * An input string is valid if:
     *
     * Open brackets must be closed by the same type of brackets.
     * Open brackets must be closed in the correct order.
     * Every close bracket has a corresponding open bracket of the same type.
     *
     */
    fun isValid(s: String): Boolean {
        val queue = LinkedList<Char>()
        val opposite = listOf(')', '}', ']')
        for (c in s) {
            val flip = when (c) {
                ')' -> '('
                '}' -> '{'
                ']' -> '['
                else -> ' '
            }
            if (opposite.contains(c)) {
                if (queue.lastOrNull() == flip) queue.removeLast() else return false
            } else queue.add(c)
        }
        return queue.isEmpty()
    }

    /**
    The next greater element of some element x in an array is the first greater element that is to the right of x in the same array.
    You are given two distinct 0-indexed integer arrays nums1 and nums2, where nums1 is a subset of nums2.
    For each 0 <= i < nums1.length, find the index j such that nums1[i] == nums2[j] and
    determine the next greater element of nums2[j] in nums2. If there is no next greater element, then the answer for this query is -1.
    Return an array ans of length nums1.length such that ans[i] is the next greater element as described above.
     */
    fun nextGreaterElement(nums1: IntArray, nums2: IntArray): IntArray {
        val nextGreaterMap = HashMap<Int, Int>()
        val stack = ArrayDeque<Int>()
        for (num in nums2) {
            while (stack.isNotEmpty() && num > stack.last()) {
                nextGreaterMap[stack.removeLast()] = num
            }
            stack.addLast(num)
        }

        val ans = IntArray(nums1.size)
        for (i in nums1.indices) {
            ans[i] = nextGreaterMap.getOrDefault(nums1[i], -1)
        }
        return ans
    }

    fun test(head: ListNode?): ListNode? {
        var prev: ListNode? = null
        var curr = head
        while (curr != null) {
            val next = curr.next // save next
            curr.next = prev          // reverse the pointer
            prev = curr               // advance prev
            curr = next               // advance curr
        }
        return prev // new head
    }
}


class ListNode(var `val`: Int) {
    var next: ListNode? = null

    override fun toString(): String {
        return listToString(this)
    }
}

fun createList(values: IntArray): ListNode? {
    if (values.isEmpty()) return null
    val head = ListNode(values[0])
    var current = head
    for (i in 1 until values.size) {
        current.next = ListNode(values[i])
        current = current.next!!
    }
    return head
}

fun listToString(head: ListNode?): String {
    if (head == null) return "[]"
    val sb = StringBuilder("[")
    var current = head
    while (current != null) {
        sb.append(current.`val`)
        if (current.next != null) sb.append(",")
        current = current.next
    }
    sb.append("]")
    return sb.toString()
}

fun listsEqual(list1: ListNode?, list2: ListNode?): Boolean {
    var current1 = list1
    var current2 = list2
    while (current1 != null && current2 != null) {
        if (current1.`val` != current2.`val`) return false
        current1 = current1.next
        current2 = current2.next
    }
    return current1 == null && current2 == null
}
