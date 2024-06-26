data class Node(var range: Range, var parent: Node?, var children: MutableMap<Char, Node>, var link: Node?, val suffixIndex: Int?) {
    val size: Int get() = range.end - range.start
}