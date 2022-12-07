package day1

import Day

class Day1 : Day {
    private val input = inputFile().readLines().map(String::toLong)

    override fun problemOne(): Int {
        return input.countIncrements()
    }

    override fun problemTwo(): Int {
        return input.dropLast(2).mapIndexed { i, value -> value + input[i + 1] + input[i + 2] }.countIncrements()
    }

    private fun List<Long>.countIncrements(): Int = this.zipWithNext()
            .fold(0) { acc, pair -> if (pair.first < pair.second) acc + 1 else acc }
}
