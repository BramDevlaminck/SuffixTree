data class Range(var start: Int, var end: Int) {

    val length: Int get() = end - start

}