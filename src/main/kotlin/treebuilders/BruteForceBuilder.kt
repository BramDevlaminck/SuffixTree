package treebuilders

import Cursor
import Entry
import IteratorReturnValue
import Node
import Range

class BruteForceBuilder: TreeBuilder {

    override fun build(dataset: List<Entry>): Node {

        val root = Node(Range(0, 0), mutableMapOf(), null)
        val inputString = dataset.map { it.protein }.joinToString("")
        val cursor = Cursor(root, 0, inputString)
        for (i in inputString.indices) {
            val endIndex = inputString.length
            // progress through the tree
            var indexInEntry = 0
            var retValue = cursor.next(inputString[i])

            while (retValue == IteratorReturnValue.OK) {
                indexInEntry++
                // first check case where we have a second duplicate identical string that also ends on same character
                if (indexInEntry == inputString.length - i) {
                    retValue = IteratorReturnValue.AT_END
                    break
                }
                retValue = cursor.next(inputString[i + indexInEntry])
            }

            // add this entry to the tree in a different way depending on if we are in the middle of an edge or not
            if (retValue == IteratorReturnValue.IN_WORD) {
                // split the edge
                val newNode = Node(Range(i + indexInEntry, endIndex), mutableMapOf(), null)
                val nodeToInsertInEdge = Node(Range(cursor.currentNode.range.start + cursor.index, cursor.currentNode.range.end), cursor.currentNode.children, null)
                cursor.currentNode.children = mutableMapOf(
                    inputString[nodeToInsertInEdge.range.start] to nodeToInsertInEdge,
                    inputString[newNode.range.start] to newNode
                )
                cursor.currentNode.range = Range(cursor.currentNode.range.start, cursor.currentNode.range.start + cursor.index)
            } else if (retValue == IteratorReturnValue.AT_END && indexInEntry != endIndex - i) { // only create a new node if we don't already have an identical node (last check fails in that case)
                // add new node as child
                val newNode = Node(Range(i + indexInEntry, endIndex), mutableMapOf(), null)
                cursor.currentNode.children[inputString[i + indexInEntry]] = newNode
            }

            // reset the cursor
            cursor.currentNode = root
            cursor.index = 0
        }

        return root
    }
}