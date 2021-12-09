import java.io.File

fun main() {
    val input = File("/Users/pawegio/Desktop/input").readLines()
    val heightmap = getHeightmap(input)
    // Part 1
    println("heightmap: ${heightmap.toList().map { it.toList() }}")
    val lowPoints = getLowPoints(heightmap)
    println("low points: $lowPoints")
    val risk = lowPoints.sumOf { it.second + 1 }
    println("risk: $risk")
    // Part 2
    val result = lowPoints.map { (location, _) ->
        val visited = mutableListOf<Location>()
        visited.add(location)
        val basin = getBasin(heightmap, visited, location) + location
        basin.size
    }.sortedDescending().take(3).reduce { acc, i -> acc * i }
    println("Largest basins: $result")
}

private fun getHeightmap(input: List<String>): Array<IntArray> =
    input.map { line -> line.map { it.digitToInt() }.toIntArray() }.toTypedArray()

private fun getLowPoints(map: Array<IntArray>): List<Pair<Location, Int>> {
    val lowPoints = mutableListOf<Pair<Location, Int>>()
    val n = map.indices.last
    val m = map[0].indices.last
    for (x in map.indices) {
        for (y in map[0].indices) {
            val height = map[x][y]
            if ((x == 0 || map[x - 1][y] > height) && (y == 0 || map[x][y - 1] > height) &&
                (x == n || map[x + 1][y] > height) && (y == m || map[x][y + 1] > height)
            ) {
                lowPoints.add(Location(x, y) to height)
            }
        }
    }
    return lowPoints
}

private fun getBasin(map: Array<IntArray>, visited: MutableList<Location>, location: Location): Set<Location> {
    val n = map.indices.last
    val m = map[0].indices.last
    val (x, y) = location
    val left = Location(x - 1, y)
    val right = Location(x + 1, y)
    val top = Location(x, y - 1)
    val bottom = Location(x, y + 1)
    val leftBasin = when {
        x > 0 && !visited.contains(left) -> {
            visited.add(left)
            when {
                map[left.x][left.y] < 9 -> getBasin(map, visited, left)
                else -> emptySet()
            }
        }
        else -> emptySet()
    }
    val rightBasin = when {
        x < n && !visited.contains(right) -> {
            visited.add(right)
            when {
                map[right.x][right.y] < 9 -> getBasin(map, visited, right)
                else -> emptySet()
            }
        }
        else -> emptySet()
    }
    val topBasin = when {
        y > 0 && !visited.contains(top) -> {
            visited.add(top)
            when {
                map[top.x][top.y] < 9 -> getBasin(map, visited, top)
                else -> emptySet()
            }
        }
        else -> emptySet()
    }
    val bottomBasin = when {
        y < m && !visited.contains(bottom) -> {
            visited.add(bottom)
            when {
                map[bottom.x][bottom.y] < 9 -> getBasin(map, visited, bottom)
                else -> emptySet()
            }
        }
        else -> emptySet()
    }
    return leftBasin + rightBasin + topBasin + bottomBasin + location
}

private data class Location(val x: Int, val y: Int)
