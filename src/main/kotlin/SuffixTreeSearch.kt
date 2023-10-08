class SuffixTreeSearch(private val tree: Node, private val originalInputString: String) {


        private fun findEndNode(searchString: String): Pair<Boolean, Node> {
            val cursor = Cursor(tree, 0, originalInputString, tree)
            if (originalInputString.isEmpty()) {
                return true to tree
            }
            var indexInString = 0
            var retValue = cursor.next(searchString[0])

            while (retValue == IteratorReturnValue.OK) {
                indexInString++
                retValue = cursor.next(searchString[indexInString])
            }

            if (indexInString == searchString.length) {
                return true to cursor.currentNode
            }
            return false to cursor.currentNode
        }

        fun search(tree: Node, searchString: String): List<String> {
            val (matchFound, endNode) = findEndNode(searchString)
            val solutionList = mutableListOf<String>()
            if (!matchFound) {
                return solutionList
            }

            val stack = ArrayDeque(listOf(endNode))
            val addedToStringsStack = ArrayDeque<Int>()
            var currentString = searchString
            while (stack.isNotEmpty()) {
                val currentNode = stack.removeFirst()
                currentString += originalInputString.substring(currentNode.range.start, currentNode.range.end)
                addedToStringsStack.addFirst(currentNode.size)
                if (currentNode.children.isEmpty()) {
                    solutionList.add(currentString)
                    currentString = currentString.substring(0, currentString.length - addedToStringsStack.removeFirst())
                } else {
                    stack.addAll(0, currentNode.children.map { it.value })
                }
            }
            return solutionList

        }
}