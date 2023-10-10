
object OutputHandler {

    var suppressOutput = false

    fun customPrint(input: String) {
        if (!suppressOutput)
            print(input)
    }

    fun customPrintln(input: String) {
        if (!suppressOutput)
            println(input)
    }
}