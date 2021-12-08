import java.io.File

fun main() {
    val input = File("/Users/pawegio/Desktop/input").readLines()
    val entries = getEntries(input)
    // Part 1
    val simpleCount = countSimpleDigits(entries)
    println("Simple count: $simpleCount")
    // Part 2
    val sum = entries.sumOf { entry ->
        val digits = getDigits(entry.patterns)
        getOutputValue(entry.output, digits)
    }
    println("Sum: $sum")
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

private fun getDigits(patterns: Set<Digit>): Map<Digit, Int> {
    val remaining = mutableSetOf<Digit>().apply { addAll(patterns) }
    lateinit var six: Digit
    return mapOf(
        remaining.removeFirst { it.segments.size == 2 } to 1,
        remaining.removeFirst { it.segments.size == 3 } to 7,
        remaining.removeFirst { it.segments.size == 4 } to 4,
        remaining.removeFirst { it.segments.size == 7 } to 8,
        remaining.removeFirst { it.segments.size == 6 && it.segments.intersect(patterns.first { it.segments.size == 4 }.segments).size == 4 } to 9,
        remaining.removeFirst { it.segments.size == 6 && it.segments.intersect(patterns.first { it.segments.size == 3 }.segments).size == 3 } to 0,
        remaining.removeFirst { it.segments.size == 6 }.also { six = it } to 6,
        remaining.removeFirst { it.segments.size == 5 && it.segments.intersect(patterns.first { it.segments.size == 2 }.segments).size == 2 } to 3,
        remaining.removeFirst { it.segments.size == 5 && it.segments.intersect(six.segments).size == 5 } to 5,
        remaining.removeFirst { it.segments.size == 5 }.also { remaining.remove(it) } to 2
    )
}

private fun <T> MutableSet<T>.removeFirst(predicate: (T) -> Boolean): T = first(predicate).also(::remove)

private fun getOutputValue(output: List<Digit>, digits: Map<Digit, Int>) =
    output.joinToString("") { digits[it].toString() }.toInt()

private data class Entry(val patterns: Set<Digit>, val output: List<Digit>)

private data class Digit(val segments: Set<Char>)