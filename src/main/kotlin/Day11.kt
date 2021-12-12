import java.io.File

fun main() {
    val input = File("/Users/pawegio/Desktop/input").readLines()
    val octopuses = getOctopuses(input)
    val (flashes, firstStepAllFlash) = simulate(octopuses)
    println("flashes: $flashes, first step all flash: $firstStepAllFlash")
}

private fun getOctopuses(input: List<String>): List<List<Octopus>> =
    input.mapIndexed { y, line -> line.mapIndexed { x, char -> Octopus(x, y, char.digitToInt()) } }

private fun simulate(octopuses: List<List<Octopus>>): Pair<Int, Int?> {
    var flashes = 0
    var firstStepAllFlash: Int? = null
    printStep(0, octopuses)
    var step = 1
    while (firstStepAllFlash == null) {
        octopuses.flatten().forEach { octopus -> octopus.brighten() }
        val beforeFlash = mutableListOf<Octopus>()
        val flashed = mutableListOf<Octopus>()
        beforeFlash.addAll(octopuses.flatten().filter { it.canFlash })
        while (beforeFlash.isNotEmpty()) {
            flashed.addAll(beforeFlash)
            beforeFlash.forEach { octopus -> octopus.getNeighbours(octopuses).forEach { it.brighten() } }
            beforeFlash.clear()
            beforeFlash.addAll(octopuses.flatten().filter { it.canFlash && it !in flashed })
        }
        if (flashed.size == octopuses.flatten().size) {
            firstStepAllFlash = step
        }
        octopuses.flatten().filter { it.canFlash }.forEach { it.energy = 0 }
        flashes += flashed.size
        printStep(step + 1, octopuses)
        step++
    }
    return flashes to firstStepAllFlash
}

private fun Octopus.getNeighbours(octopuses: List<List<Octopus>>): List<Octopus> {
    val (x, y) = this
    val n = octopuses.indices.last
    val m = octopuses[0].indices.last
    fun octopusAt(x: Int, y: Int): Octopus? =
        if (x < 0 || x > n || y < 0 || y > m) null else octopuses[y][x]
    return listOfNotNull(
        octopusAt(x - 1, y - 1),
        octopusAt(x, y - 1),
        octopusAt(x + 1, y - 1),
        octopusAt(x - 1, y),
        octopusAt(x + 1, y),
        octopusAt(x - 1, y + 1),
        octopusAt(x, y + 1),
        octopusAt(x + 1, y + 1)
    )
}

private fun printStep(step: Int, octopuses: List<List<Octopus>>) {
    println("After step: $step")
    octopuses.forEach {
        it.forEach { octopus -> print(octopus.energy) }
        println()
    }
    println()
}

private data class Octopus(val x: Int, val y: Int, var energy: Int) {
    val canFlash get() = energy > 9
    fun brighten() {
        energy++
    }
}