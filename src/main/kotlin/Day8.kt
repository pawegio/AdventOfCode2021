import java.io.File

fun main() {
    val input = File("/Users/pawegio/Desktop/input").readLines()
    val entries = getEntries(input)
    val count = countSimpleDigits(entries)
    println("Count: $count")
}

private fun getEntries(input: List<String>): List<Entry> =
    input.map { line ->
        val parts = line.split('|').map { it.trim() }
        val patterns = parts[0].split(' ').map { Digit(it.toSet()) }.toSet()
        val output = parts[1].split(' ').map { Digit(it.toSet()) }
        Entry(patterns, output)
    }

private fun countSimpleDigits(entries: List<Entry>): Int =
    entries.sumOf { entry -> entry.output.count { output -> (output.segments.size in setOf(2, 3, 4, 7)) } }

private data class Entry(val patterns: Set<Digit>, val output: List<Digit>)

private data class Digit(val segments: Set<Char>)