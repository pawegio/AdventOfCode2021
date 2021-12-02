
fun main(args: Array<String>) {
    val commands = getCommands(args)
    var horizontalPosition = 0
    var depth = 0
    var aim = 0
    commands.forEach { (direction, units) ->
        horizontalPosition += direction.x * units
        depth += when (direction) {
            Direction.Forward -> direction.y * units * aim
            else -> direction.y * units
        }
        aim += direction.aim * units
        println("depth: $depth - aim: $aim")
    }
    val result = horizontalPosition * depth
    println("Commands: $commands")
    println("Horizontal position: $horizontalPosition, depth: $depth")
    println("Result: $result")
}

fun getCommands(args: Array<String>) = args.toList().windowed(2, step = 2).map {
    val direction = when(it.first().first()) {
        'f' -> Direction.Forward
        'd' -> Direction.Down
        'u' -> Direction.Up
        else -> throw IllegalArgumentException("Unknown direction: ${it.first()}")
    }
    val units = it.last().toInt()
    Command(direction, units)
}

data class Command(val direction: Direction, val units: Int)

enum class Direction(val x: Int, val y: Int, val aim: Int) {
    Forward(1, 1, 0),
    Down(0, 0, 1),
    Up(0, 0, -1);
}