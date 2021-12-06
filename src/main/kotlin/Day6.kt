import kotlinx.coroutines.*
import java.io.File

private const val DAYS = 256
private const val GESTATION_CYCLE = 7
private const val BREEDING_DELAY = 2
private val answers = mutableMapOf<Int, Long>()

fun main() = runBlocking {
    val input = File("/Users/pawegio/Desktop/input").readText()
    val initialFish = input.split(',').map { it.toInt() }.toMutableList()
    println("Initial state: ${initialFish.joinToString(",")}")
    val count = initialFish.sumOf { simulate(DAYS - it) }
    println("Count: $count")
}

fun simulate(days: Int): Long = answers[days] ?: run {
    var count = 0L
    var remainingDays = days
    while (remainingDays > 0) {
        remainingDays -= GESTATION_CYCLE
        count += simulate(remainingDays - BREEDING_DELAY)
    }
    count + 1
}.also { answers[days] = it }
