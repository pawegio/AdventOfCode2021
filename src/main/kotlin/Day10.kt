import java.io.File

fun main() {
    val input = File("/Users/pawegio/Desktop/input").readLines()
    val syntaxErrorScore = getSyntaxErrorScore(input)
    println("Score: $syntaxErrorScore")
}

private fun getSyntaxErrorScore(input: List<String>) =
    input.sumOf { line ->
        val illegalChar = getIllegalCharacter(line)
        if (illegalChar != null) {
            println("Illegal char: $illegalChar")
        }
        illegalChar?.points ?: 0
    }

private fun getIllegalCharacter(line: String): Char? {
    val checked = Array(line.length) { false }
    chars@ for (i in line.indices) {
        if (line[i].isClosing && !checked[i]) {
            checked[i] = true
            for (j in i downTo 0) {
                if (!checked[j]) {
                    if (line[j].opens(line[i])) {
                        checked[j] = true
                        continue@chars
                    }
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

private fun Char.opens(closingChar: Char) =
    closingChar == when (this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> ' '
    }

private val Char.points
    get() = when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException()
    }