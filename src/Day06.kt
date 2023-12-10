fun main() {

    data class Race(val time: Long, val distance: Long)

    fun Race.winningOptions() : Int {
        var wins = 0
        for (i in 1 until time) {
            val dist = i * (time - i)
            if (dist > distance) {
                wins++
            }
        }
        return wins
    }

    fun part1(input: List<Race>): Int = input.map { it.winningOptions() }.reduce { acc, i ->  acc * i }

    fun part2(input: Race): Int = input.winningOptions()


// test if implementation meets criteria from the description, like:
    val testInput = listOf(Race(7, 9), Race(15, 40), Race(30, 200))
    checkResult(part1(testInput), 288)
    checkResult(part2(Race(71530, 940200)), 71503)

    val input = listOf(Race(51, 222), Race(92, 2031), Race(68, 1126), Race(90, 1225))
    part1(input).println()
    part2(Race(51926890, 222203111261225)).println()

}
