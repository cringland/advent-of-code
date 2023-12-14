package day14

import Day
import util.Point2
import util.by
import kotlin.math.max
import kotlin.math.min


class Day14 : Day {
    private val input = inputFile().readLines()
    private val reversed = input.rotated()

    private fun List<String>.rotated(): List<String> =
        this.first().indices.map { x ->
            this.map { it[x] }.joinToString("")
        }.reversed()

    private fun List<String>.rotatedOtherWay(): List<String> =
        this.first().indices.map { x ->
            this.reversed().map { it[x] }.joinToString("")
        }

    override fun problemOne(): Number {
        return reversed.slideWest().count()
    }

    override fun problemTwo(): Number {
        var grid = input.rotated()
        val counts = mutableListOf<Long>()

        repeat(1000) {
            repeat(4) { _ ->
                grid = grid.slideWest().rotatedOtherWay()
            }
            counts.add(grid.count())
        }

        val start = 500
        val target = 1000000000
        val repeatingPattern = findRepeatingSeq(counts.drop(start))
        val indexInPattern = ((target - start) % repeatingPattern.size)-1
        return repeatingPattern[indexInPattern]
    }

//  https://stackoverflow.com/questions/11385718/python-finding-repeating-sequence-in-list-of-integers
    private fun findRepeatingSeq(seq: List<Long>): List<Long> {
        var guess = 1
        val maxLen = seq.size/2
        for (x in 2..maxLen) {
            if (seq.subList(0, x) == seq.subList(x, 2 * x))
                guess = x
        }
        return seq.subList(0, guess)
    }

    private fun List<String>.count(): Long {
        var count = 0L
        this.forEach { line ->
            line.reversed().forEachIndexed { index, c ->
                if (c == 'O') count += (index + 1)
            }
        }
        return count
    }

    private fun List<String>.slideWest(): List<String> {
        return this.map { line ->
            var newStr = ""
            while (newStr.length != line.length) {
                val y = newStr.length
                val c = line[y]
                newStr += if (c == '#') line.substring(y).takeWhile { it == '#' }
                else line.substring(y).takeWhile { it != '#' }.toList().sorted().reversed().joinToString("")
            }
            newStr
        }
    }

    fun List<String>.print() = this.forEach { line -> println(line) }
}
