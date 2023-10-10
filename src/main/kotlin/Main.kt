import treebuilders.NaiveBuilder
import treebuilders.UkkonenBuilder
import java.io.File

fun main(args: Array<String>) {
//    val data = TsvParser.parse("/Users/brdvlami/Documents/Ugent/MA2/Thesis/Dataset/swissprot_2023_3.tsv")
//
//    val testData = listOf(
//        Entry("xabxa#", 0),
//         Entry("acabx$", 0),
//    )

    if (args.size == 0) {
        println("Only 1 argument expected: the input filename")
        return
    }
    val filename = args[0]
    var data = File(filename).readText()
    data += "$"

    val builder = UkkonenBuilder()
    println("building suffix tree...")
    val res = builder.build(listOf(Entry(data, 0)))
    val searcher = SuffixTreeSearch(res, data)
    while (true) {
        print("Input your search string: ")
        val inputWord = readlnOrNull()
        if (inputWord != null) {
            val searched = searcher.searchProtein(inputWord)
            println("found ${searched.size} matches")
            searched.forEach { println(it) }
        }

    }

}