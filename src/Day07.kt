enum class Type { // Natural ordering by strength
    HighCard, OnePair, TwoPair, ThreeOfAKind, FullHouse, FourOfAKind, FiveOfAKind,
}

fun main() {

    fun Char.labelValue(): Int {
        return when (this) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 11
            'T' -> 10
            else -> this.digitToInt()
        }
    }

    fun Char.labelValuePart2(): Int {
        return when (this) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> 0
            'T' -> 10
            else -> this.digitToInt()
        }
    }

    data class Hand(val cards: String, val bid: Int) : Comparable<Hand> {

        override fun compareTo(other: Hand): Int {
            val compareType = this.type() compareTo other.type()
            if (compareType != 0) return compareType
            for (i in 0..4) {
                val compareLabel = this.cards[i].labelValue() compareTo other.cards[i].labelValue()
                if (compareLabel != 0) return compareLabel
            }
            return 0
        }

        fun type(): Type {
            val grouping = cards.groupingBy { it }.eachCount()
            return when {
                grouping.values.any { it == 5 } -> Type.FiveOfAKind
                grouping.values.any { it == 4 } -> Type.FourOfAKind
                grouping.values.any { it == 3 } && grouping.values.any { it == 2 } -> Type.FullHouse
                grouping.values.any { it == 3 } -> Type.ThreeOfAKind
                grouping.values.count { it == 2 } == 2 -> Type.TwoPair
                grouping.values.count { it == 2 } == 1 -> Type.OnePair
                else -> Type.HighCard
            }
        }

    }

    data class HandPart2(val cards: String, val bid: Int) : Comparable<HandPart2> {

        override fun compareTo(other: HandPart2): Int {
            val compareType = this.type() compareTo other.type()
            if (compareType != 0) return compareType
            for (i in 0..4) {
                val compareLabel = this.cards[i].labelValuePart2() compareTo other.cards[i].labelValuePart2()
                if (compareLabel != 0) return compareLabel
            }
            return 0
        }

        fun type(): Type {
            val grouping = cards.filterNot { it == 'J' }.groupingBy { it }.eachCount()
            val baseType = when {
                grouping.values.any { it == 5 } -> Type.FiveOfAKind
                grouping.values.any { it == 4 } -> Type.FourOfAKind
                grouping.values.any { it == 3 } && grouping.values.any { it == 2 } -> Type.FullHouse
                grouping.values.any { it == 3 } -> Type.ThreeOfAKind
                grouping.values.count { it == 2 } == 2 -> Type.TwoPair
                grouping.values.count { it == 2 } == 1 -> Type.OnePair
                else -> Type.HighCard
            }
            val jokers = cards.count { it == 'J' }
            if (jokers == 5) return Type.FiveOfAKind

            return (1..jokers).fold(baseType) { type, _ ->
                when (type) {
                    Type.HighCard -> Type.OnePair
                    Type.OnePair -> Type.ThreeOfAKind
                    Type.TwoPair -> Type.FullHouse
                    Type.ThreeOfAKind -> Type.FourOfAKind
                    Type.FullHouse -> error("Too many cards")
                    Type.FourOfAKind -> Type.FiveOfAKind
                    Type.FiveOfAKind -> error("Too many cards")
                }
            }

        }

    }

    fun parseInput(input: List<String>) = input.map { it.split(" ") }.map { Hand(it[0], it[1].toInt()) }


    fun part1(input: List<String>): Int =
        parseInput(input).sorted().mapIndexed { rank, hand -> (rank + 1) * hand.bid }.sum()


    fun part2(input: List<String>): Int =
        parseInput(input).map { HandPart2(it.cards, it.bid) }.sorted().also { it.println() }
            .onEach { println("${it.cards} - ${it.type()}") }.mapIndexed { rank, hand -> (rank + 1) * hand.bid }.sum()


    part2(
        listOf("AA77J 1")
    )
// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    checkResult(part1(testInput), 6440)
    checkResult(part2(testInput), 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()

}
