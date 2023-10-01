import java.io.File

class TsvParser {

    companion object {

        fun parse(filename: String): List<Entry> {
            val reader = File(filename).bufferedReader()
            val headers = reader.readLine().split("\t")
            return reader.readLines().map { line ->
                val fields = line.split("\t")
                Entry(fields[1] + "$", fields[8].toInt())
            }
        }

    }

}