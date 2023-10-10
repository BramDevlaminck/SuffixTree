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

        fun searchProtein(searchString: String): List<String> {
            val (matchFound, endNode) = findEndNode(searchString)
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

            // prepare the cursor for next use
            cursor.reset()

            return solutionList
        }

        fun searchSuffix(searchString: String): List<String> {
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