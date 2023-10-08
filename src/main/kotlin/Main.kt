import treebuilders.NaiveBuilder
import treebuilders.UkkonenBuilder

fun main(args: Array<String>) {
    val data = TsvParser.parse("/Users/brdvlami/Documents/Ugent/MA2/Thesis/Dataset/swissprot_2023_3.tsv")

    val testData = listOf(
        Entry("xabxa#", 0),
         Entry("acabx$", 0),
    )
    val builder = UkkonenBuilder()
    val res = builder.build(testData)
    val searched = SuffixTreeSearch(res, testData.joinToString("") { it.protein }).search("a")
    searched.forEach { println(it) }
    print("")

}