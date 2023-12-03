fun main() {

    data class Coordinate(val x: Int, val y: Int)

    data class NumberCoordinate(val number: Int, val coordinates: List<Coordinate>)

    data class SymbolCoordinate(val symbol: Char, val coordinate: Coordinate)

    data class Schematic(val symbols: List<SymbolCoordinate>, val numbers: List<NumberCoordinate>)

    fun parseSchematic(input: List<String>): Schematic {
        val symbols = mutableListOf<SymbolCoordinate>()
        val numbers = mutableListOf<NumberCoordinate>()
        input.forEachIndexed { y, line ->
            var currentNumber = 0
            var currentNumberCoordinates = mutableListOf<Coordinate>()
            line.forEachIndexed { x, char ->
                when {
                    char.isDigit() -> {
                        currentNumber = currentNumber * 10 + char.digitToInt()
                        currentNumberCoordinates.add(Coordinate(x, y))
                    }

                    char == '.' -> {
                        if (currentNumber != 0) {
                            numbers.add(NumberCoordinate(currentNumber, currentNumberCoordinates))
                            currentNumber = 0
                            currentNumberCoordinates = mutableListOf()
                        }
                    }

                    else -> {
                        if (currentNumber != 0) {
                            numbers.add(NumberCoordinate(currentNumber, currentNumberCoordinates))
                            currentNumber = 0
                            currentNumberCoordinates = mutableListOf()
                        }
                        symbols.add(SymbolCoordinate(char, Coordinate(x, y)))
                    }
                }

                if (x == line.length - 1) {
                    numbers.add(NumberCoordinate(currentNumber, currentNumberCoordinates))
                }
            }
        }
        return Schematic(symbols, numbers)
    }

    fun Coordinate.adjacent(other: Coordinate): Boolean {
        return other.x in this.x - 1..this.x + 1 && other.y in this.y - 1..this.y + 1
    }


    fun NumberCoordinate.adjacent(symbols: List<SymbolCoordinate>): Boolean {
        coordinates.forEach { numberCoordinate ->
            symbols.forEach { symbolCoordinate ->
                if (numberCoordinate.adjacent(symbolCoordinate.coordinate)) {
                    return true
                }
            }
        }
        return false
    }

    fun Schematic.partNumbers(): List<Int> = numbers.filter { it.adjacent(symbols) }.map { it.number }

    fun Schematic.gearNumbers(): List<Int> {
        val gearProducts = mutableListOf<Int>()
        symbols.filter { it.symbol == '*' }.forEach { engineCoordinate ->
                val adjacentParts = mutableListOf<Int>()
                numbers.forEach { numberCoordinate ->
                    if (numberCoordinate.adjacent(listOf(engineCoordinate))) {
                        adjacentParts.add(numberCoordinate.number)
                    }
                }
                if (adjacentParts.size == 2) {
                    gearProducts.add(adjacentParts[0] * adjacentParts[1])
                }
            }
        return gearProducts
    }

    fun part1(input: List<String>) = parseSchematic(input).partNumbers().sum()

    fun part2(input: List<String>) = parseSchematic(input).gearNumbers().sum()


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    checkResult(part1(testInput), 4361)
    checkResult(part2(testInput), 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()

}



