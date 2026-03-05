/**
 * @author i.chernyshov
 * @date 03.03.2026
 */
class TreesAndGraphs {

    /**
     * Given the root of a binary tree, return the inorder traversal of its nodes' values.
     */
    fun inorderTraversal(root: TreeNode?): List<Int> {
        val result = mutableListOf<Int>()
        val stack = ArrayDeque<TreeNode>()
        var current = root

        while (current != null || stack.isNotEmpty()) {
            // Traverse to the leftmost node
            while (current != null) {
                stack.addLast(current)
                current = current.left
            }
            // Process the node
            current = stack.removeLast()
            result.add(current.`val`)
            // Move to the right subtree
            current = current.right
        }

        return result
    }

}

class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null

    override fun toString(): String {
        val result = mutableListOf<String>()
        val queue = ArrayDeque<TreeNode?>()
        queue.add(this)

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            if (node == null) {
                result.add("null")
            } else {
                result.add(node.`val`.toString())
                queue.add(node.left)
                queue.add(node.right)
            }
        }

        // Remove trailing nulls
        while (result.isNotEmpty() && result.last() == "null") {
            result.removeLast()
        }

        return "[${result.joinToString(",")}]"
    }

}

fun createTreeNode(values: Array<Int?>): TreeNode? {
    if (values.isEmpty() || values[0] == null) return null

    val root = TreeNode(values[0]!!)
    val queue = mutableListOf(root)
    var i = 1

    while (i < values.size && queue.isNotEmpty()) {
        val node = queue.removeAt(0)

        if (values[i] != null) {
            node.left = TreeNode(values[i]!!)
            queue.add(node.left!!)
        }
        i++

        if (i < values.size && values[i] != null) {
            node.right = TreeNode(values[i]!!)
            queue.add(node.right!!)
        }
        i++
    }

    return root
}