class SuffixTreeSearch(private val tree: Node, private val originalInputString: String) {


    private val cursor = Cursor(tree, 0, originalInputString, tree)

    private fun findEndNode(searchString: String): Pair<Boolean, Node> {
        if (originalInputString.isEmpty()) {
            return true to tree
        }
        var indexInString = 0
        var retValue = cursor.next(searchString[0])

        while (retValue == IteratorReturnValue.OK && indexInString + 1 < searchString.length) {
            indexInString++
            retValue = cursor.next(searchString[indexInString])
        }

        val endNode = cursor.currentNode
        cursor.reset()
        if (indexInString == searchString.length - 1 && retValue == IteratorReturnValue.OK) {
            return true to endNode
        }
        return false to endNode
    }

    fun searchProtein(searchString: String): List<String> {
        val (matchFound, endNode) = findEndNode(searchString)
        cursor.reset() // prepare cursor for next search
        val suffixIndicesList = mutableListOf<Int>()
        if (!matchFound) {
            return emptyList()
        }

        // find all the indices in the array that the leaves belong to
        val stack = ArrayDeque(listOf(endNode))
        while (stack.isNotEmpty()) {
            val currentNode = stack.removeFirst()
            if (currentNode.suffixIndex != null) {
                suffixIndicesList.add(currentNode.suffixIndex)
            } else {
                stack.addAll(0, currentNode.children.map { it.value })
            }
        }

        val solutionList = mutableListOf<String>()
        // retrieve the proteins
        suffixIndicesList.forEach {
            var begin = it
            var end = it
            while (begin > 0 && originalInputString[begin - 1] != '#') {
                begin--
            }

            while (originalInputString[end] != '$' && originalInputString[end] != '#') {
                end++
            }
            solutionList.add(originalInputString.substring(begin, end))
        }

        return solutionList
    }
}