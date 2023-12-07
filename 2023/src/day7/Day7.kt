package day7

import Day

class Day7 : Day {

    open class Hand(
        open val card: String,
        open val bid: Int,
        countsTransformer: (List<Pair<Char, Int>>) -> List<Int>,
        val order: List<Char>
    ) : Comparable<Hand> {

        private val counts = order
            .map { it to card.count { c -> c == it } }
            .let(countsTransformer)
            .filterNot { it == 0 }
            .sortedByDescending { it }
        private val thisMax = counts[0]
        private val house = thisMax == 3 && this.counts[1] == 2
        private val twoPair = thisMax == 2 && this.counts[1] == 2
        override fun compareTo(that: Hand): Int {
            val thatMax = that.thisMax

            fun comparedSuits() = this.card.mapIndexed { i, c ->
                order.indexOf(c).compareTo(order.indexOf(that.card[i]))
            }.firstOrNull { it != 0 } ?: 0

            return when (thisMax) {
                5 -> {
                    if (thatMax == 5) comparedSuits()
                    else 1
                }

                4 -> {
                    when (thatMax) {
                        5 -> -1
                        4 -> comparedSuits()
                        else -> 1
                    }
                }

                3 -> {
                    when (thatMax) {
                        5, 4 -> -1
                        3 -> {
                            if (this.house && !that.house) 1
                            else if (!this.house && that.house) -1
                            else comparedSuits()
                        }

                        else -> 1
                    }
                }

                2 -> {
                    when (thatMax) {
                        5, 4, 3 -> -1
                        2 -> {
                            if (this.twoPair && !that.twoPair) 1
                            else if (!this.twoPair && that.twoPair) -1
                            else comparedSuits()
                        }

                        else -> 1
                    }
                }

                1 -> {
                    when (thatMax) {
                        5, 4, 3, 2 -> -1
                        else -> {
                            comparedSuits()
                        }
                    }
                }

                else -> throw Exception()
            }
        }
    }

    data class Hand1(val c: String, val b: Int) : Hand(
        c, b,
        { it.map { pair -> pair.second } },
        listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()
    )

    data class Hand2(val c: String, val b: Int) : Hand(
        c, b, { it ->
            val jNum = it.find { it.first == 'J' }?.second ?: 0
            if (jNum == 5) {
                listOf(5)
            } else if (jNum > 0) {
                val maxP = it.filter { it.first != 'J' }.maxBy { it.second }!!
                val p = maxP.first to maxP.second + jNum
                listOf(p.second) + it.filterNot { it == maxP || it.first == 'J' }.map { it.second }
            } else it.map { it.second }
        }, listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed()
    )

    private val input = inputFile().readLines()
        .map { line ->
            line.split(" ")
                .let { it[0] to it[1].toInt() }
        }

    override fun problemOne(): Number {
        return input.map { Hand1(it.first, it.second) }
            .sorted()
            .mapIndexed { i, p -> p.bid * (i + 1) }
            .sum()
//        252295678
    }

    override fun problemTwo(): Number {
        return input.map { Hand2(it.first, it.second) }
            .sorted()
            .mapIndexed { i, p -> p.bid * (i + 1) }
            .sum()
//        250577259
    }
}
