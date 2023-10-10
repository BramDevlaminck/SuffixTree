package treebuilders

import Cursor
import Entry
import IteratorReturnValue
import Node
import OutputHandler.customPrint
import OutputHandler.customPrintln
import Range

class UkkonenBuilder : TreeBuilder {
    override fun build(dataset: List<Entry>): Node {

        val root = Node(Range(0, 0), null, mutableMapOf(), null, null)
        val inputString = dataset.joinToString("") { it.protein }
        val cursor = Cursor(root, 0, inputString, root)
        var numleaves = 0
        for (j in 1..inputString.length) {
            if (j % 1000 == 0) {
                val percent = j/inputString.length.toFloat()
                customPrint("Building tree progress: [${"#".repeat((50*percent).toInt())}${"-".repeat((50*(1-percent)).toInt())}] ${"%,.2f".format(percent*100)}%\r")
            }
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
                cursor.addLeafFromPosition(j - 1, i)
                numleaves++

                // follow the suffix link since the extension is complete
                cursor.followLink()
            }
        }
        customPrintln("")

        return root
    }


}