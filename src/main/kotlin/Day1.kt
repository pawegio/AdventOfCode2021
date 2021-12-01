
fun main(args: Array<String>) {
    val depths = args.toList().map { it.toInt() }
    // Part 1
    var increases = getIncreases(depths)
    println("Depths: $depths")
    println("Increases: $increases")
    // Part 2
    val depthsWithNoise = getDepthsWithNoise(depths)
    increases = getIncreases(depthsWithNoise)
    println("Depths with noise: $depthsWithNoise")
    println("Increases: $increases")
}

private fun getDepthsWithNoise(depths: List<Int>) =
    List(depths.dropLast(2).size) { index -> depths[index] + depths[index + 1] + depths[index + 2] }

private fun getIncreases(depths: List<Int>) =
    depths.windowed(2).count { it.last() > it.first() }