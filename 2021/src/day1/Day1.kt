package day1

import java.io.File
import Day

class Day1 : Day {
    private val input = File("src/day1/input").readLines().map(String::toLong)

    override fun problemOne(): Int {
        return countIncrements(input)
    }

    override fun problemTwo(): Int {
        val slidingWindowSums = input.dropLast(2).mapIndexed { i, value -> value + input[i + 1] + input[i + 2] }
        return countIncrements(slidingWindowSums)
    }

    private fun countIncrements(array: List<Long>): Int {
        var count = 0;
        for (i in 0 until array.size - 1) {
            if (array[i] < array[i + 1]) {
                count++
            }
        }
        return count;
    }
}
