import treebuilders.BruteForceBuilder

fun main(args: Array<String>) {
    val data = TsvParser.parse("/Users/brdvlami/Documents/Ugent/MA2/Thesis/Dataset/swissprot_2023_3.tsv")

    val testData = listOf(
        Entry("xabxa$", 0),
        Entry("xabxa$", 0),
    )
    val builder = BruteForceBuilder()
    val res = builder.build(testData)
    print("")

}