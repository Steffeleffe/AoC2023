enum class LeftRight(val value: Char) {
    LEFT('L'), RIGHT('R');

    companion object {
        fun from(value: Char): LeftRight {
            return LeftRight.entries.firstOrNull { it.value == value }!!
        }
    }
}

fun main() {

    data class Node(val id: String, val leftNodeId: String, val rightNodeId: String)

    fun parseNode(string: String): Pair<String, Node> {
        // SGR = (JLL, VRV)
        val regex = "(...) = \\((...), (...)\\)".toRegex()
        val (nodeId, leftNodeId, rightNodeId) = regex.find(string)!!.destructured
        return nodeId to Node(nodeId, leftNodeId, rightNodeId)
    }

    data class Map(val instructions: List<LeftRight>, val nodes: kotlin.collections.Map<String, Node>)

    fun parseInput(input: List<String>): Map {
        val instructions = input.first().map { LeftRight.from(it) }
        val nodes = input.drop(2).associate { parseNode(it) }
        return Map(instructions, nodes)
    }


    fun part1(input: List<String>): Int {
        val (instructions, nodes) = parseInput(input)
        var steps = 0
        var currentNode = nodes["AAA"]!!
        while (true) {
            for (i in instructions) {
                currentNode = when (i) {
                    LeftRight.LEFT -> nodes[currentNode.leftNodeId]!!
                    LeftRight.RIGHT -> nodes[currentNode.rightNodeId]!!
                }
                steps++
                if (currentNode.id == "ZZZ") return steps
            }
        }
    }


    fun part2(input: List<String>): Long {
        val (instructions, nodes) = parseInput(input)
        val stepsNeeded = nodes.filter { it.value.id.endsWith('A') }.values.map { startingNode ->
                var steps = 0L
                var currentNode = startingNode
                outer@ while (true) {
                    for (i in instructions) {
                        currentNode = when (i) {
                            LeftRight.LEFT -> nodes[currentNode.leftNodeId]!!
                            LeftRight.RIGHT -> nodes[currentNode.rightNodeId]!!
                        }
                        steps++
                        if (currentNode.id.endsWith('Z')) break@outer
                    }
                }
                steps
            }
        val minStep = stepsNeeded.min()
        var currentSteps = minStep
        while (true) {
            if (stepsNeeded.all { currentSteps % it == 0L }) return currentSteps
            currentSteps += minStep
        }
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    checkResult(part1(testInput), 2)
    checkResult(part1(readInput("Day08_test1")), 6)
    checkResult(part2(readInput("Day08_test2")), 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
