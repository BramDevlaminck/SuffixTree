package treebuilders

import Entry
import Node

interface TreeBuilder {

    fun build(dataset: List<Entry>): Node

}