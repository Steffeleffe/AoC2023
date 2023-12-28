import java.util.*

enum class Pipe(val char: Char) {

    Vertical('|'), // is a vertical pipe connecting north and south.
    Horizontal('-'), //is a horizontal pipe connecting east and west.
    BendNE('L'), // is a 90-degree bend connecting north and east.
    BendNW('J'), //  is a 90-degree bend connecting north and west.
    BendSW('7'), //  is a 90-degree bend connecting south and west.
    BendSE('F'), //  is a 90-degree bend connecting south and east.
    Ground('.'), // is ground; there is no pipe in this tile.
    Start('S'); // s the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.

    companion object {
        fun from(value: Char): Pipe {
            return Pipe.entries.firstOrNull { it.char == value }!!
        }
    }
}

fun main() {

    data class Grid(val pipes: List<List<Pipe>>)

    fun parseInput(input: List<String>) = Grid(input.map { row -> row.map { Pipe.from(it) } })


    fun Grid.findStartPosition(): Pair<Int, Int> {
        for (x in pipes.indices) {
            for (y in pipes.indices) {
                if (pipes[y][x] == Pipe.Start) {
                    return Pair(x, y)
                }
            }
        }
        error("No start position was found")
    }

    fun Grid.findPathCandidatesFromStart(
        startPipe: Pair<Int, Int>
    ): MutableList<Pair<Int, Int>> {
        val candidates = mutableListOf<Pair<Int, Int>>()
        val (x, y) = startPipe
        if (x > 0 && EnumSet.of(Pipe.Horizontal, Pipe.BendNE, Pipe.BendSE).contains(pipes[y][x - 1])) candidates.add(
            Pair(x - 1, y)
        )
        if (x < pipes.size && EnumSet.of(Pipe.Horizontal, Pipe.BendNW, Pipe.BendSW)
                .contains(pipes[y][x + 1])
        ) candidates.add(
            Pair(
                x + 1, y
            )
        )

        if (y > 0 && EnumSet.of(Pipe.Vertical, Pipe.BendSW, Pipe.BendSE).contains(pipes[y - 1][x])) candidates.add(
            Pair(
                x, y - 1
            )
        )
        if (y < pipes.size && EnumSet.of(Pipe.Vertical, Pipe.BendNW, Pipe.BendNE)
                .contains(pipes[y + 1][x])
        ) candidates.add(Pair(x, y + 1))
        if (candidates.size != 2) error("Found incorrect number of candidates from position $startPipe... $candidates")
        return candidates
    }

    fun Grid.findPathCandidatesFromNonStart(
        pipe: Pair<Int, Int>
    ): List<Pair<Int, Int>> {
        val (x, y) = pipe
        return when (pipes[y][x]) {
            Pipe.Vertical -> listOf(Pair(x, y - 1), Pair(x, y + 1))
            Pipe.Horizontal -> listOf(Pair(x - 1, y), Pair(x + 1, y))
            Pipe.BendNE -> listOf(Pair(x, y - 1), Pair(x + 1, y))
            Pipe.BendNW -> listOf(Pair(x, y - 1), Pair(x - 1, y))
            Pipe.BendSW -> listOf(Pair(x, y + 1), Pair(x - 1, y))
            Pipe.BendSE -> listOf(Pair(x, y + 1), Pair(x + 1, y))
            else -> error("Error")
        }
    }


    fun part1(input: List<String>): Int {
        val grid = parseInput(input)
        val startPosition = grid.findStartPosition().also { it.println() }
        val firstPipe = grid.findPathCandidatesFromStart(startPosition).first()

        val pipePath = mutableListOf(startPosition, firstPipe)
        pipePath.println()
        while (true) {

            val (previousPipe, currentPipe) = pipePath.takeLast(2)
            val nextPipe = grid.findPathCandidatesFromNonStart(currentPipe).filterNot { it == previousPipe }.first()
            if (nextPipe == startPosition) break
            pipePath.add(nextPipe)
        }

        pipePath.println()
        return pipePath.size / 2
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    checkResult(part1(testInput), 4)
    checkResult(part1(readInput("Day10_test2")), 8)
    //   checkResult(part2(testInput), 2)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()

}
