enum class IteratorReturnValue {
    OK, AT_END, IN_WORD
}

data class Cursor(var currentNode: Node, var index: Int, val inputString: String, val root: Node) {

    /**
     * Moves the cursor to the next node in the trie if possible where we follow "nextCharacter"
     */
    fun next(nextCharacter: Char): IteratorReturnValue {
        // progress inside the string of current node
        if (index < currentNode.range.length) {
            if (inputString[currentNode.range.start + index] == nextCharacter) {
                index++
                return IteratorReturnValue.OK
            }
            return IteratorReturnValue.IN_WORD
        }
        // go to child node if possible
        if (nextCharacter in currentNode.children) {
            currentNode = currentNode.children[nextCharacter]!!
            // set to one since the key in currentNode.children is the same as the first character of the text,
            // so by "consuming the key", we also consume that first character
            index = 1
            return IteratorReturnValue.OK
        }
        return IteratorReturnValue.AT_END
    }

    fun atNode(): Boolean = index == 0 || index == currentNode.size

    fun followLink(indexInString: Int) {
        var currentIndexInString = indexInString
        // root
        if (currentNode == root) {
            return
        }

        // save how far we have to walk
        var distanceToTraverse: Int
        if (currentNode.parent == root) {
            currentNode = root
            distanceToTraverse = index - 1
            if (distanceToTraverse > 0) {
                currentNode = currentNode.children[inputString[currentIndexInString]]!!
            } else {
                index = 0
            }
        } else {
            // follow link
            currentNode = currentNode.parent!!.link!!
            distanceToTraverse = index
        }


        while (distanceToTraverse > 0) {
            if (currentNode.size < distanceToTraverse) {
                val traversed = currentNode.size
                currentNode = currentNode.children[inputString[currentIndexInString + traversed]]!!
                currentIndexInString += traversed
                distanceToTraverse -= traversed
            } else {
                index = distanceToTraverse
                distanceToTraverse = 0
            }
        }
    }

    fun splitEdge(): Node {
        val newInternalNode = Node(
            Range(currentNode.range.start, currentNode.range.start + index),
            currentNode.parent,
            mutableMapOf(
                inputString[currentNode.range.start + index] to currentNode,
            ), null
        )
        currentNode.parent!!.children[inputString[newInternalNode.range.start]] = newInternalNode

        currentNode.range = Range(currentNode.range.start + index, currentNode.range.end)
        currentNode.parent = newInternalNode

        // move the cursor to the just created node, since we are technically completely at the end of that new node
        // this is also used to be able to use addLeafAtPosition since that function uses the new currentNode
        currentNode = newInternalNode
        return newInternalNode
    }

    fun addLeafFromPosition(i: Int) {
        val newLeaf = Node(Range(i, inputString.length), currentNode, mutableMapOf(), null)
        currentNode.children[inputString[newLeaf.range.start]] = newLeaf
    }

}