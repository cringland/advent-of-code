package day3

import Day

class Day3 : Day {
    private val input = inputFile().readLines()

    private val priority: (Char) -> Int = { if (it.isLowerCase()) it.toInt() - 96 else it.toInt() - 3 }

    override fun problemOne(): Int {
        return input.sumBy {
            val first = it.substring(0, it.length / 2).toSet()
            val second = it.substring(it.length / 2).toSet()
            first.intersect(second).sumBy(priority)
        }
    }

    override fun problemTwo(): Int {
        return input.chunked(3)
                .sumBy {
                    it.map(CharSequence::toSet).reduce { acc, group ->
                        acc.intersect(group)
                    }.sumBy(priority)
                }
    }
}
