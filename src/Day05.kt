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
        val seedToSoil: List<AlmanacRangeMap>,
        val soilToFertilizer: List<AlmanacRangeMap>,
        val fertilizerToWater: List<AlmanacRangeMap>,
        val waterToLight: List<AlmanacRangeMap>,
        val lightToTemperature: List<AlmanacRangeMap>,
        val temperatureToHumidity: List<AlmanacRangeMap>,
        val humidityToLocation: List<AlmanacRangeMap>,
    )

    fun Input.findMin(seeds: Sequence<Long>): Long {
        return seeds.map { seedToSoil.lookup(it) }.map { soilToFertilizer.lookup(it) }
            .map { fertilizerToWater.lookup(it) }.map { waterToLight.lookup(it) }.map { lightToTemperature.lookup(it) }
            .map { temperatureToHumidity.lookup(it) }.map { humidityToLocation.lookup(it) }.min()
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
        val inputIndexes = mutableListOf<Int>()

        for (i in input.indices) {
            if (input[i].endsWith("map:")) {
                inputIndexes.add(i)
            }
        }

        return Input(
            parseMap(input.subList(inputIndexes[0] + 1, inputIndexes[1] - 1)),
            parseMap(input.subList(inputIndexes[1] + 1, inputIndexes[2] - 1)),
            parseMap(input.subList(inputIndexes[2] + 1, inputIndexes[3] - 1)),
            parseMap(input.subList(inputIndexes[3] + 1, inputIndexes[4] - 1)),
            parseMap(input.subList(inputIndexes[4] + 1, inputIndexes[5] - 1)),
            parseMap(input.subList(inputIndexes[5] + 1, inputIndexes[6] - 1)),
            parseMap(input.subList(inputIndexes[6] + 1, input.size - 1))
        )
    }

    fun part1(input: List<String>): Int = parseInput(input).findMin(input[0].seeds().asSequence()).toInt()

    fun part2(input: List<String>): Int =
        parseInput(input).findMin(input[0].seedsRanges()).also { it.println() }.toInt()


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    checkResult(part1(testInput), 35)
    checkResult(part2(testInput), 46)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()

}
