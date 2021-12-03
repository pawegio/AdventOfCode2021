import java.lang.IllegalStateException

fun main(args: Array<String>) {
    val diagnosticReport = args.toList()

    // Part 1
    val gammaRate = getGammaRate(diagnosticReport)
    val epsilonRate = getEpsilonRate(gammaRate)
    val power = gammaRate.toInt(2) * epsilonRate.toInt(2)
    println("gamma rate: $gammaRate\nepsilon rate: $epsilonRate\npower: $power")

    // Part 2
    val oxygenGeneratorRating = getOxygenGeneratorRating(diagnosticReport)
    val co2ScrubberRating = getCo2ScrubberRating(diagnosticReport)
    val lifeSupportRating = oxygenGeneratorRating.toInt(2) * co2ScrubberRating.toInt(2)
    println("oxygen gemerator rating: $oxygenGeneratorRating\nco2 scrubber rating: $co2ScrubberRating")
    println("life support rating: $lifeSupportRating")
}

fun getGammaRate(diagnosticReport: List<String>): String = buildString {
    repeat(diagnosticReport.first().length) { index ->
        val zeroBits = diagnosticReport.countZeroBits(index)
        val oneBits = diagnosticReport.countOneBits(index)
        append(if (zeroBits > oneBits) '0' else '1')
    }
}

fun getEpsilonRate(gammaRate: String): String =
    gammaRate.map { if (it == '0') '1' else '0' }.joinToString("")

fun getOxygenGeneratorRating(diagnosticReport: List<String>): String {
    val numbers = diagnosticReport.toMutableList()
    repeat(diagnosticReport.first().length) { index ->
        val zeroBits = numbers.countZeroBits(index)
        val oneBits = numbers.countOneBits(index)
        when {
            zeroBits > oneBits -> numbers.removeAll { it[index] == '1' }
            else -> numbers.removeAll { it[index] == '0' }
        }
        if (numbers.size == 1) return numbers.first()
    }
    throw IllegalStateException()
}

fun getCo2ScrubberRating(diagnosticReport: List<String>): String {
    val numbers = diagnosticReport.toMutableList()
    repeat(diagnosticReport.first().length) { index ->
        val zeroBits = numbers.countZeroBits(index)
        val oneBits = numbers.countOneBits(index)
        when {
            oneBits < zeroBits -> numbers.removeAll { it[index] == '0' }
            else -> numbers.removeAll { it[index] == '1' }
        }
        if (numbers.size == 1) return numbers.first()
    }
    throw IllegalStateException()
}

fun List<String>.countZeroBits(index: Int) = count { it[index] == '0' }

fun List<String>.countOneBits(index: Int) = count { it[index] == '1' }
