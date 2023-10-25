import kotlin.math.min

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

    fun atNode(): Boolean = index == currentNode.size

    fun followLink() {
        // root
        if (currentNode == root) {
            return
        }

        var begin = currentNode.range.start

        // save how far we have to walk
        var distanceLeftToWalk: Int
        if (currentNode.parent == root) {
            currentNode = root
            begin += 1
            distanceLeftToWalk = index - 1
        } else {
            // follow link
            distanceLeftToWalk = index // distance before following link
            currentNode = currentNode.parent!!.link!!
        }
        index = currentNode.size

        while (distanceLeftToWalk > 0) {
            // move to child
            currentNode = currentNode.children[inputString[begin]]!!

            // walk as far as possible on current edge
            val currentAdvance = min(currentNode.size, distanceLeftToWalk)
            distanceLeftToWalk -= currentAdvance
            begin += currentAdvance
            index = currentAdvance
        }

    }

    fun splitEdge(): Node {
        val newInternalNode = Node(
            Range(currentNode.range.start, currentNode.range.start + index),
            currentNode.parent,
            mutableMapOf(
                inputString[currentNode.range.start + index] to currentNode,
            ),
            null,
            null
        )
        currentNode.parent!!.children[inputString[newInternalNode.range.start]] = newInternalNode

        currentNode.range = Range(currentNode.range.start + index, currentNode.range.end)
        currentNode.parent = newInternalNode

        // move the cursor to the just created node, since we are technically completely at the end of that new node
        // this is also used to be able to use addLeafAtPosition since that function uses the new currentNode
        currentNode = newInternalNode
        return newInternalNode
    }

    fun addLeafFromPosition(j: Int, suffixIndex: Int) {
        val newLeaf = Node(Range(j, inputString.length), currentNode, mutableMapOf(), null, suffixIndex)
        currentNode.children[inputString[newLeaf.range.start]] = newLeaf
    }

    fun reset() {
        index = 0
        currentNode = root
    }

}