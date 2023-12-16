import ContraptionType.*
import Direction.*

enum class ContraptionType(val symbol: Char) {
    EMPTY('.'), MIRROR_FORWARD('/'), MIRROR_BACKWARD('\\'), SPLITTER_VERTICAL('|'), SPLITTER_HORIZONTAL('-');

    companion object {
        fun from(value: Char): ContraptionType {
            return ContraptionType.entries.firstOrNull { it.symbol == value }!!
        }
    }

}

enum class Direction { UP, DOWN, LEFT, RIGHT }

fun main() {

    data class GridContents(
        val symbol: ContraptionType,
        var energized: Boolean = false,
        val energizedDirection: MutableList<Direction> = mutableListOf(),
    )

    data class Light(var x: Int, var y: Int, var direction: Direction)

    fun Light.move() {
        when (direction) {
            UP -> y--
            DOWN -> y++
            LEFT -> x--
            RIGHT -> x++
        }
    }

    fun parseInput(input: List<String>) = input.map { it.map { GridContents(ContraptionType.from(it)) } }


    fun part1(input: List<String>, startBeam: Light = Light(0, 0, RIGHT)): Int {
        val energizedCount = mutableListOf(0)
        val currentLights = mutableListOf(startBeam)
        val contraption = parseInput(input)

        while (true) {
            val newLights = mutableListOf<Light>()
            val removeLights = mutableListOf<Light>()
            currentLights.forEach { light ->
                if (light.x in contraption.indices && light.y in contraption.indices) {
                    val gridContents = contraption[light.y][light.x]
                    if (gridContents.energized && gridContents.energizedDirection.contains(light.direction)) {
                        removeLights.add(light)
                    }

                    gridContents.energized = true
                    gridContents.energizedDirection.add(light.direction)
                    when (gridContents.symbol) {
                        EMPTY -> {}
                        MIRROR_FORWARD -> {
                            light.direction = when (light.direction) {
                                UP -> RIGHT
                                DOWN -> LEFT
                                LEFT -> DOWN
                                RIGHT -> UP
                            }
                        }

                        MIRROR_BACKWARD -> {
                            light.direction = when (light.direction) {
                                UP -> LEFT
                                DOWN -> RIGHT
                                LEFT -> UP
                                RIGHT -> DOWN
                            }
                        }

                        SPLITTER_VERTICAL -> {
                            when (light.direction) {
                                UP, DOWN -> {}
                                LEFT, RIGHT -> {
                                    light.direction = UP
                                    newLights.add(Light(light.x, light.y, DOWN))
                                }
                            }
                        }

                        SPLITTER_HORIZONTAL -> when (light.direction) {
                            UP, DOWN -> {
                                light.direction = LEFT
                                newLights.add(Light(light.x, light.y, RIGHT))
                            }

                            LEFT, RIGHT -> {}
                        }
                    }
                } else {
                    removeLights.add(light)
                }
            }
            currentLights.removeAll(removeLights)
            currentLights.addAll(newLights)
            newLights.clear()
            removeLights.clear()
            currentLights.forEach { it.move() }

            energizedCount.add(contraption.flatten().count { it.energized })
            if (energizedCount.takeLast(25).distinct().size == 1) {
                return energizedCount.last()
            }
        }

    }

    fun part2(input: List<String>): Int {
        val startBeams = mutableListOf<Light>()
        input.indices.forEach {
            startBeams.add(Light(it, 0, DOWN))
            startBeams.add(Light(input.size - 1, it, UP))
            startBeams.add(Light(0, it, RIGHT))
            startBeams.add(Light(input.size - 1, it, LEFT))
        }
        return startBeams.maxOf { part1(input, it) }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    checkResult(part1(testInput), 46)
    checkResult(part2(testInput), 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()

}


