fun main() {

    data class Draw(val blueCount: Int, val redCount: Int, val greenCount: Int)

    data class Game(val id: Int, val draws: List<Draw>)

    fun Game.power(): Int {
        val maxReds = draws.maxOf { it.redCount }
        val maxBlues = draws.maxOf { it.blueCount }
        val maxGreens = draws.maxOf { it.greenCount }
        return maxReds * maxBlues * maxGreens
    }

    fun possibleGame(game: Game): Boolean = game.draws.none { it.redCount > 12 || it.greenCount > 13 || it.blueCount > 14 }

    fun parseDrawString(drawString: String): Draw {
        val drawList = drawString.split(",")
        var reds = 0
        var blues = 0
        var greens = 0
        drawList.forEach {
            val count = it.trim().substringBefore(" ").toInt()
            when {
                it.contains("blue") -> blues += count
                it.contains("green") -> greens += count
                it.contains("red") -> reds += count
                else -> error("unknown color")
            }
        }
        return Draw(redCount = reds, blueCount = blues, greenCount = greens)
    }

    fun parseGameString(gameString: String): Game {
        val id = gameString.substringAfter("Game ").substringBefore(":").toInt()
        val drawList = gameString.substringAfter(":").split(";").map { parseDrawString(it) }
        return Game(id, drawList)
    }

    fun part1(input: List<String>) = input.map { parseGameString(it) }.filter { possibleGame(it) }.sumOf { it.id }

    fun part2(input: List<String>) = input.map { parseGameString(it) }.sumOf { it.power() }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    checkResult(part1(testInput), 8)
    val testInput2 = readInput("Day02_test")
    checkResult(part2(testInput2), 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()

}
