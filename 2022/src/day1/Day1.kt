package day1

import Day

class Day1 : Day {
    private val input = inputFile().readText()
            .split("\n\n").map {
                it.lines()
                        .filter(String::isNotEmpty)
                        .sumBy(String::toInt)
            }
            .sortedDescending()

    override fun problemOne(): Int {
        return input.first()
    }

    override fun problemTwo(): Int {
        return input.take(3).sum()
    }
}
