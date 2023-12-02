fun main() {

    fun getCalibrationValue(line: String): Int {
        val firstDigit = line.first { it.isDigit() }.digitToInt()
        val lastDigit = line.last { it.isDigit() }.digitToInt()
        return firstDigit * 10 + lastDigit
    }

    fun part1(input: List<String>) = input.sumOf { getCalibrationValue(it) }

    fun getCalibrationValueAdvanced(line: String): Int {
        val digits = line.indices.mapNotNull {
            when {
                line[it].isDigit() -> line[it].digitToInt()
                line.substring(it).startsWith("one") -> 1
                line.substring(it).startsWith("two") -> 2
                line.substring(it).startsWith("three") -> 3
                line.substring(it).startsWith("four") -> 4
                line.substring(it).startsWith("five") -> 5
                line.substring(it).startsWith("six") -> 6
                line.substring(it).startsWith("seven") -> 7
                line.substring(it).startsWith("eight") -> 8
                line.substring(it).startsWith("nine") -> 9
                else -> null
            }
        }
        return digits.first() * 10 + digits.last()
    }

    fun part2(input: List<String>) = input.sumOf { getCalibrationValueAdvanced(it) }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    checkResult(part1(testInput), 142)
    checkResult(part2(testInput), 142)
    val testInput2 = readInput("Day01_test2")
    checkResult(part2(testInput2), 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()

}
