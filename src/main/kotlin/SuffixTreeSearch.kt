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

            if (indexInString == searchString.length - 1 && retValue == IteratorReturnValue.OK) {
                return true to cursor.currentNode
            }
            return false to cursor.currentNode
        }

        fun search(searchString: String): List<String> {
            val (matchFound, endNode) = findEndNode(searchString)
            val solutionList = mutableListOf<String>()
            if (!matchFound) {
                return solutionList
            }

            dfsTreeTraversal(searchString.substring(0, searchString.length - cursor.index), endNode, solutionList)

            // prepare the cursor for next use
            cursor.reset()

            return solutionList

        }

    /**
     * recursively go through the tree starting from currentNode and add the strings that correspond with the traversed paths
     */
    private fun dfsTreeTraversal(prefix: String, currentNode: Node, resultList: MutableList<String>) {
        val substringCurrentNode = originalInputString.substring(currentNode.range.start, currentNode.range.end)
        if (currentNode.children.isEmpty()) {
            resultList.add(prefix + originalInputString.substring(currentNode.range.start, currentNode.range.end))
            return
        }

        val newPrefix = prefix + substringCurrentNode
        currentNode.children.forEach {
            dfsTreeTraversal(newPrefix, it.value, resultList)
        }
    }
}