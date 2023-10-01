enum class IteratorReturnValue {
    OK, AT_END, IN_WORD
}

data class Cursor(var currentNode: Node, var index: Int) {

    /**
     * Moves the cursor to the next node in the trie if possible where we follow "nextCharacter"
     */
    fun next(nextCharacter: Char): IteratorReturnValue {
        // progress inside the string of current node
        if (index < currentNode.text.length) {
            if (currentNode.text[index] == nextCharacter) {
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

}