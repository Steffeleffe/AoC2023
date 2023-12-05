import kotlin.math.min
import kotlin.math.pow

fun main() {

    data class Card(val id: Int, val winningNumbers: List<Int>, val ownedNumbers: List<Int>, var count: Int = 1)

    fun String.toCard(): Card {
        val id = this.substringAfter("Card").substringBefore(':').trim().toInt()
        val winningNumbers =
            this.substringAfter(":").substringBefore("|").trim().split("\\s+".toRegex()).map { it.trim() }
                .map { it.toInt() }
        val ownedNumbers = this.substringAfter("|").trim().split("\\s+".toRegex()).map { it.trim() }.map { it.toInt() }
        return Card(id, winningNumbers, ownedNumbers)
    }

    fun Card.points(): Int {
        val matchCount = winningNumbers.intersect(ownedNumbers).size
        return 2.0.pow(matchCount - 1).toInt()
    }

    fun Card.extraCards(): Int = winningNumbers.intersect(ownedNumbers).size

    fun part1(input: List<String>) = input.map { it.toCard() }.sumOf { it.points() }

    fun part2(input: List<String>): Int {

        val list = input.map { it.toCard() }.sortedBy { it.id }

        list.forEachIndexed { index, card ->
            val cardCount = card.count
            val extraCards = card.extraCards()
            for (i in min(list.size, index + 1) until min(list.size, index + 1 + extraCards)) {
                list[i].count += cardCount
            }
        }
        return list.sumOf { it.count }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    checkResult(part1(testInput), 13)
    checkResult(part2(testInput), 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()

}



