import OutputHandler.customPrintln
import treebuilders.UkkonenBuilder
import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("First argument should be the location of the input filename")
        return
    }

    val options = args.asList().subList(1, args.size).toSet()

    if ("--no-progress" in options) {
        OutputHandler.suppressOutput = true
    }

    customPrintln("reading input...")
    val filename = args[0]
    var data = File(filename).readText()
    data += "$"

    val builder = UkkonenBuilder()
    val res = builder.build(listOf(Entry(data, 0)))
    val searcher = SuffixTreeSearch(res, data)
    if ("--build-only" !in options) {
        while (true) {
            print("Input your search string: ")
            val inputWord = readlnOrNull()
            if (inputWord != null) {
                val searched = searcher.searchProtein(inputWord)
                println("found ${searched.size} matches")
                searched.forEach { println("* $it") }
            }

        }
    }

}