import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("/Users/pawegio/Desktop/input").readText()
    val crabs = input.split(',').map { Crab(it.toInt()) }
    val minFuel = getMinFuel(crabs)
    println("Min fuel: $minFuel")
}

fun getMinFuel(crabs: List<Crab>): Int {
    var minFuel = Int.MAX_VALUE
    val groupedCrabs = crabs.groupBy { it.position }
    val min = groupedCrabs.minOf { it.key }
    val max = groupedCrabs.maxOf { it.key }
    (min..max).forEach { alignPosition ->
        var fuel = 0
        groupedCrabs.forEach { (position, values) ->
            val delta = abs(position - alignPosition)
            fuel += cost(delta) * values.count()
        }
        if (fuel < minFuel) minFuel = fuel
    }
    return minFuel
}

private fun cost(n: Int): Int = if (n == 0) 0 else cost(n - 1) + n

@JvmInline
value class Crab(val position: Int)