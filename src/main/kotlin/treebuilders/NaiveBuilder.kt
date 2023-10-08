package treebuilders

import Cursor
import Entry
import IteratorReturnValue
import Node
import Range

class NaiveBuilder: TreeBuilder {

    override fun build(dataset: List<Entry>): Node {

        val root = Node(Range(0, 0), null, mutableMapOf(), null)
        val inputString = dataset.joinToString("") { it.protein }
        val cursor = Cursor(root, 0, inputString, root)
        val endIndex = inputString.length
        for (i in inputString.indices) {
            // progress through the tree
            var indexInEntry = i
            var retValue = cursor.next(inputString[i])

            while (retValue == IteratorReturnValue.OK) {
                indexInEntry++
                // first check case where we have a second duplicate identical string that also ends on same character
                if (indexInEntry == inputString.length) {
                    retValue = IteratorReturnValue.AT_END
                    break
                }
                retValue = cursor.next(inputString[indexInEntry])
            }

            // add this entry to the tree in a different way depending on if we are in the middle of an edge or not
            if (retValue == IteratorReturnValue.IN_WORD) {
                // split the edge
                val newNode = Node(Range(indexInEntry, endIndex), cursor.currentNode, mutableMapOf(), null)
                val nodeToInsertInEdge = Node(Range(cursor.currentNode.range.start + cursor.index, cursor.currentNode.range.end), cursor.currentNode, cursor.currentNode.children, null)
                cursor.currentNode.children = mutableMapOf(
                    inputString[nodeToInsertInEdge.range.start] to nodeToInsertInEdge,
                    inputString[newNode.range.start] to newNode
                )
                cursor.currentNode.range = Range(cursor.currentNode.range.start, cursor.currentNode.range.start + cursor.index)
            } else if (retValue == IteratorReturnValue.AT_END && indexInEntry != endIndex) { // only create a new node if we don't already have an identical node (last check fails in that case)
                // add new node as child
                val newNode = Node(Range(indexInEntry, endIndex), cursor.currentNode, mutableMapOf(), null)
                cursor.currentNode.children[inputString[indexInEntry]] = newNode
            }

            // reset the cursor
            cursor.currentNode = root
            cursor.index = 0
        }

        return root
    }
}