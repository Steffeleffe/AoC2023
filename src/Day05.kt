fun main() {
    data class AlmanacRangeMap(val source: LongRange, val offSet: Long)

    fun List<AlmanacRangeMap>.lookup(sourceNumber: Long): Long {
        this.forEach {
            if (sourceNumber in it.source) {
                return sourceNumber + it.offSet
            }
        }
        return sourceNumber
    }

    data class Input(
        val maps: List<List<AlmanacRangeMap>>
    )

    fun Input.findMin(seeds: Sequence<Long>): Long {
        return seeds
            .map { maps[0].lookup(it) }
            .map { maps[1].lookup(it) }
            .map { maps[2].lookup(it) }
            .map { maps[3].lookup(it) }
            .map { maps[4].lookup(it) }
            .map { maps[5].lookup(it) }
            .map { maps[6].lookup(it) }
            .min()
    }

    fun Input.findMin(seedRanges: List<LongRange>): Long {
        return seedRanges.map { it.asSequence() }.minOf { this.findMin(it) }
    }

    fun String.seeds(): List<Long> = this.substringAfter("seeds: ").split(" ").map { it.toLong() }

    fun String.seedsRanges(): List<LongRange> =
        this.substringAfter("seeds: ").split(" ").map { it.toLong() }.zipWithNext()
            .map { pair -> pair.first until pair.first + pair.second }.filterIndexed { index, _ -> index % 2 == 0 }

    fun String.range(): AlmanacRangeMap {
        val split = this.split(" ").map { it.toLong() }
        return AlmanacRangeMap(split[1] until split[1] + split[2], split[0] - split[1])
    }

    fun parseMap(subList: List<String>): List<AlmanacRangeMap> = subList.map { it.range() }

    fun parseInput(input: List<String>): Input {
        val maps = input.drop(2)
            .filterNot { it.endsWith("map:") }
            .fold(mutableListOf(mutableListOf<AlmanacRangeMap>())) { acc, s ->
            if (s.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(s.range())
            }
            acc
        }
        return Input(maps).also { it.println() }
    }

    fun part1(input: List<String>): Int = parseInput(input).findMin(input[0].seeds().asSequence()).toInt()

    fun part2(input: List<String>): Int =
        parseInput(input).findMin(input[0].seedsRanges()).toInt()


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    checkResult(part1(testInput), 35)
    checkResult(part2(testInput), 46)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()

}
