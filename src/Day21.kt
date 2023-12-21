enum class GardenType(val char: Char) {
    START('S'), PLOT('.'), ROCK('#');

    companion object {
        fun from(value: Char): GardenType {
            return GardenType.entries.firstOrNull { it.char == value }!!
        }
    }
}


fun main() {

    data class StepPosition(val x: Int, val y: Int)
    data class Garden(val positions: List<List<GardenType>>)

    fun parseInput(input: List<String>) = Garden(input.map { row -> row.map { GardenType.from(it) } })

    fun Garden.initialStep(): StepPosition {
        for (y in positions.indices) {
            for (x in positions.indices) {
                if (positions[y][x] == GardenType.START) {
                    return StepPosition(x, y)
                }
            }
        }
        error("No start position found")
    }

    fun steppable(gardenType: GardenType): Boolean = gardenType != GardenType.ROCK

    fun Garden.getSteppableNeighbor(step: StepPosition): MutableList<StepPosition> {
        val newSteps = mutableListOf<StepPosition>()
        with(newSteps) {
            if ((step.x > 0 && steppable(positions[step.y][step.x - 1]))) add(StepPosition(step.x - 1, step.y))
            if ((step.y > 0 && steppable(positions[step.y - 1][step.x]))) add(StepPosition(step.x, step.y - 1))
            if ((step.x < positions.size - 1 && steppable(positions[step.y][step.x + 1]))) add(
                StepPosition(
                    step.x + 1, step.y
                )
            )

            if ((step.y < positions.size - 1 && steppable(positions[step.y + 1][step.x]))) add(
                StepPosition(
                    step.x, step.y + 1
                )
            )
        }
        return newSteps
    }

    fun Garden.nextStep(currentSteps: List<StepPosition>): List<StepPosition> =
        currentSteps.map { getSteppableNeighbor(it) }.flatten().distinct()

    fun part1(input: List<String>, numberOfSteps: Int): Int {
        val garden = parseInput(input)
        var steppedPositions = listOf(garden.initialStep())

        for (i in 1..numberOfSteps) {
            steppedPositions = garden.nextStep(steppedPositions)
            steppedPositions.println()
        }
        return steppedPositions.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    checkResult(part1(testInput, 6), 16)
//  checkResult(part2(testInput), 51)

    val input = readInput("Day21")
    part1(input, 64).println()
    part2(input).println()

}
