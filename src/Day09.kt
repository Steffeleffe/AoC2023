fun main() {

    fun String.diffList(): MutableList<List<Int>> {
        val diffList = mutableListOf<List<Int>>()
        var diff = split(" ").map { it.toInt() }
        diffList.add(diff)
        while (true) {
            diff = diff.zipWithNext().map { it.second - it.first }
            diffList.add(diff)
            if (diff.all { it == 0 }) {
                break
            }
        }
        return diffList
    }

    fun String.nextHistory() = diffList().map { it.last() }.reduce(Int::plus)

    fun String.firstHistory() =
        diffList().map { it.first() }.mapIndexed { index, i -> if (index % 2 == 0) i else -i }.reduce(Int::plus)

    fun part1(input: List<String>) = input.sumOf { it.nextHistory() }

    fun part2(input: List<String>) = input.sumOf { it.firstHistory() }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    checkResult(part1(testInput), 114)
    checkResult(part2(testInput), 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()

}
