import java.io.File

fun main() {
    val input = File("/Users/pawegio/Desktop/input").readLines()
    // Part 1
    val syntaxErrorScore = getSyntaxErrorScore(input)
    println("Syntax error score: $syntaxErrorScore")
    // Part 2
    val scores = getAutocompleteScores(input)
    val middleScore = scores.sorted()[scores.size / 2]
    println("Autocomplete middle score: $middleScore")
}

private fun getAutocompleteScores(input: List<String>) =
    input.filter { getIllegalCharacter(it) == null }.map { line ->
        val completion = autocomplete(line)
        completion.map { it.autocompletePoints }.reduce { acc, c -> acc * 5 + c }
    }

private fun getSyntaxErrorScore(input: List<String>) =
    input.sumOf { line ->
        val illegalChar = getIllegalCharacter(line)
        illegalChar?.syntaxErrorPoints ?: 0
    }

private fun getIllegalCharacter(line: String): Char? {
    val checked = Array(line.length) { false }
    chars@ for (i in line.indices) {
        if (line[i].isClosing && !checked[i]) {
            checked[i] = true
            for (j in i downTo 0) {
                if (!checked[j] && line[i] == line[j].closing) {
                    checked[j] = true
                    continue@chars
                }
                if (line[j].isOpening && !checked[j]) {
                    return line[i]
                }
            }
            return line[i]
        }
    }
    return null
}

private fun autocomplete(line: String): String {
    var tail = ""
    for (i in line.indices) {
        val c = line[i]
        if (c.isOpening) {
            tail += c
        } else {
            tail = tail.reversed().replaceFirst(c.opening.toString(), "").reversed()
        }
    }
    return tail.map { it.closing }.joinToString("").reversed()
}

private val Char.isOpening
    get() = when (this) {
        '(', '[', '{', '<' -> true
        else -> false
    }

private val Char.isClosing
    get() = when (this) {
        ')', ']', '}', '>' -> true
        else -> false
    }

private val Char.opening
    get() = when (this) {
        ')' -> '('
        ']' -> '['
        '}' -> '{'
        '>' -> '<'
        else -> throw IllegalArgumentException()
    }

private val Char.closing
    get() = when (this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw IllegalArgumentException()
    }

private val Char.syntaxErrorPoints
    get() = when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException()
    }

private val Char.autocompletePoints
    get() = when (this) {
        ')' -> 1L
        ']' -> 2L
        '}' -> 3L
        '>' -> 4L
        else -> throw IllegalArgumentException()
    }