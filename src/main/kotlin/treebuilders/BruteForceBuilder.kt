package treebuilders

import Cursor
import Entry
import IteratorReturnValue
import Node

class BruteForceBuilder: TreeBuilder {

    override fun build(dataset: List<Entry>): Node {

        val root = Node("", mutableMapOf(), null)
        val cursor = Cursor(root, 0)
        for (entry in dataset) {
            for (i in 0..<entry.protein.length) {
                val stringToInsert = entry.protein.substring(i)
                // progress through the tree
                var indexInEntry = 0
                var retValue = cursor.next(stringToInsert[indexInEntry])

                while (retValue == IteratorReturnValue.OK) {
                    indexInEntry++
                    // first check case where we have a second duplicate identical string that also ends on same character
                    if (indexInEntry == stringToInsert.length) {
                        retValue = IteratorReturnValue.AT_END
                        break
                    }
                    retValue = cursor.next(stringToInsert[indexInEntry])
                }

                // add this entry to the tree in a different way depending on if we are in the middle of an edge or not
                if (retValue == IteratorReturnValue.IN_WORD) {
                    val newNode = Node(stringToInsert.substring(indexInEntry), mutableMapOf(), null)
                    val nodeToInsertInEdge = Node(cursor.currentNode.text.substring(cursor.index), cursor.currentNode.children, null)
                    cursor.currentNode.children = mutableMapOf(
                        nodeToInsertInEdge.text[0] to nodeToInsertInEdge,
                        newNode.text[0] to newNode
                    )
                    cursor.currentNode.text = cursor.currentNode.text.substring(0, cursor.index)
                } else if (retValue == IteratorReturnValue.AT_END && indexInEntry != stringToInsert.length) { // only create a new node if we don't already have an identical node (last check fails in that case)
                    val newNode = Node(stringToInsert.substring(indexInEntry), mutableMapOf(), null)
                    cursor.currentNode.children[newNode.text[0]] = newNode
                }

                // reset the cursor
                cursor.currentNode = root
                cursor.index = 0
            }
        }

        return root
    }
}