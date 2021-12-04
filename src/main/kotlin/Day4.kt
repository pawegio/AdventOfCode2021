import java.io.File

fun main() {
    val input = File("/Users/pawegio/Desktop/input").readLines()

    val drawNumbers = getDrawNumbers(input)
    val boards = getBoards(input).toMutableList()
    boards.forEach { println(it) }

    drawNumbers.forEach { number ->
        markCells(boards, number)
        val winner = getWinner(boards)
        if (winner != null) {
            println("number: $number, board: ${boards.indexOf(winner)}")
            val score = winner.cells.filter { !it.isMarked }.sumOf { it.number }
            val finalScore = score * number
            println("final score: $finalScore")
            boards.remove(winner)
        }
    }
}

fun getDrawNumbers(input: List<String>) = input.first().split(',').map { it.toInt() }

fun getBoards(input: List<String>): List<Board> = input.asSequence()
    .drop(1)
    .filter { it.isNotEmpty() }
    .windowed(ROWS, ROWS)
    .map { board ->
        board.mapIndexed { row, line ->
            line.split(' ').filter { it.isNotEmpty() }.mapIndexed { column, number ->
                Cell(number.trim().toInt(), row, column)
            }
        }.flatten().let(::Board)
    }.toList()

private fun markCells(boards: List<Board>, number: Int) {
    boards.forEach { board ->
        board.cells.forEach { cell ->
            if (cell.number == number && !cell.isMarked) {
                cell.isMarked = true
            }
        }
    }
}

fun getWinner(boards: List<Board>): Board? {
    boards.forEach { board ->
        repeat(ROWS) { row ->
            if (board.cells.filter { it.row == row }.all { it.isMarked }) return board
        }
        repeat(COLUMNS) { column ->
            if (board.cells.filter { it.column == column }.all { it.isMarked }) return board
        }
    }
    return null
}

private const val ROWS = 5
private const val COLUMNS = 5

data class Board(
    val cells: List<Cell>
)

data class Cell(
    val number: Int,
    val row: Int,
    val column: Int,
    var isMarked: Boolean = false
)