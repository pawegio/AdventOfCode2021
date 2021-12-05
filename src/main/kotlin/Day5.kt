import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = File("/Users/pawegio/Desktop/input").readLines()

    val (lines, size) = getLines(input)
    val diagram = getDiagram(lines, size)
    var result = 0
    for (y in 0 until size) {
        for (x in 0 until size) {
            val position = diagram.positions[x][y]
            print(position.takeIf { it > 0 } ?: ".")
            if (position >= 2) result++
        }
        println()
    }
    println("result: $result")
}

fun getLines(input: List<String>): Pair<List<Line>, Int> {
    var size = 0
    return input.map {
        val numbers = it.replace(" -> ", ",").split(",").map(String::toInt)
        numbers.maxOrNull()?.let { max -> if (max > size) size = max }
        Line(from = Coords(numbers[0], numbers[1]), to = Coords(numbers[2], numbers[3]))
    } to size + 1
}

fun getDiagram(lines: List<Line>, size: Int): Diagram {
    val positions = Array(size + 5) { Array(size + 5) { 0 } }
    lines
        .filter { it.from.x == it.to.x || it.from.y == it.to.y }
        .forEach { (from, to) ->
            val fromX = min(from.x, to.x)
            val toX = max(from.x, to.x)
            val fromY = min(from.y, to.y)
            val toY = max(from.y, to.y)
            for (x in fromX..toX) {
                for (y in fromY..toY) {
                    positions[x][y] += 1
                }
            }
        }
    lines
        .filter { it.from.x != it.to.x && it.from.y != it.to.y }
        .forEach { (from, to) ->
            val direction = when {
                from.x < to.x && from.y < to.y -> DiagonalDirection.DownRight
                from.x < to.x && from.y > to.y -> DiagonalDirection.UpRight
                from.x > to.x && from.y < to.y -> DiagonalDirection.DownLeft
                else -> DiagonalDirection.UpLeft
            }
            when (direction) {
                DiagonalDirection.DownRight, DiagonalDirection.UpLeft -> {
                    val fromX = min(from.x, to.x)
                    val toX = max(from.x, to.x)
                    val fromY = min(from.y, to.y)
                    for ((index, x) in (fromX..toX).withIndex()) {
                        positions[x][fromY + index] += 1
                    }
                }
                DiagonalDirection.UpRight, DiagonalDirection.DownLeft -> {
                    val fromX = min(from.x, to.x)
                    val toX = max(from.x, to.x)
                    val toY = max(from.y, to.y)
                    for ((index, x) in (fromX..toX).withIndex()) {
                        positions[x][toY - index] += 1
                    }
                }
            }
        }
    return Diagram(positions)
}

class Diagram(val positions: Array<Array<Int>>)

data class Line(val from: Coords, val to: Coords)

data class Coords(val x: Int, val y: Int)

enum class DiagonalDirection { UpRight, UpLeft, DownRight, DownLeft }
