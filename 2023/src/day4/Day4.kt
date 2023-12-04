package day4

import Day
import kotlin.math.pow

class Day4 : Day {
    private val input = inputFile().readLines().map { line ->
        line.substringAfter(": ").split(" | ")
            .map { nums ->
                nums.split(" ")
                    .filter { it.isNotEmpty() }
                    .map { n -> n.toInt() }
            }.let { card ->
                val winners = card[0].toSet()
                val numbers = card[1].toSet()
                winners.intersect(numbers).size
            }
    }

    override fun problemOne(): Number {
        return input.sumBy{ (2.0.pow(it - 1)).toInt() }
    }

    override fun problemTwo(): Number {
        val cardAmts = (input.indices).map { 1 }.toMutableList()
        p2(input, cardAmts, 0)
        return cardAmts.sum()
    }

    fun p2(list: List<Int>, cardAmts: MutableList<Int>, currentIndex: Int) {
        val currentVal = list[currentIndex]
        (currentIndex+1..currentIndex+currentVal).forEach{
            if (it < cardAmts.size)
                cardAmts[it] = cardAmts[it] + cardAmts[currentIndex]
        }
        val newIdx = currentIndex+1
        if (newIdx < cardAmts.size)
            p2(list, cardAmts, newIdx)
    }
}
