package treebuilders

import Cursor
import Entry
import IteratorReturnValue
import Node
import Range

class UkkonenBuilder : TreeBuilder {
    override fun build(dataset: List<Entry>): Node {

        val root = Node(Range(0, 0), null, mutableMapOf(), null)
        val inputString = dataset.joinToString("") { it.protein }
        val cursor = Cursor(root, 0, inputString, root)
        var numleaves = 0
        for (j in 1..inputString.length) {

            var prevInternalNode: Node? = null

            // skip the first numLeaves leaves since this is rule 1 and can be skipped
            for (i in numleaves..<j) {
                if (prevInternalNode != null && cursor.atNode()) {
                    prevInternalNode.link = cursor.currentNode
                    prevInternalNode = null
                }

                if (cursor.next(inputString[j - 1]) == IteratorReturnValue.OK) {
                    break // rule 3 : do nothing + show stopper
                }

                // rule 2: split edge if needed and add leaf
                if (!cursor.atNode()) {
                    val newInternalNode = cursor.splitEdge()

                    if (prevInternalNode != null) {
                        prevInternalNode.link = newInternalNode
                    }
                    prevInternalNode = newInternalNode
                }
                cursor.addLeafFromPosition(j-1)
                numleaves++

                // follow the suffix link since the extension is complete
                cursor.followLink(i + 1)
            }
        }

        return root
    }


}