package day25

import Day
import kotlin.math.pow

class Day25 : Day {
    private val input = inputFile().readLines().map { it.fromSnafu() }

    private fun String.fromSnafu(): Long {
        var n = 1L
        return this.reversed().fold(0L) { acc, c ->
            acc + when (c) {
                '-' -> -1
                '=' -> -2
                else -> c.toString().toLong()
            }.let { it * n }.also { n *= 5 }
        }
    }

    private fun Long.toSnafu(): String {
        var rem = this
        var place = 0L
        var str = ""
        while (rem > 0) {
            val unit = (5L).pow(place)
            val mod = (5L).pow(place + 1)
            val num = (((rem + 2 * unit) % mod) / unit) - 2
            val c = when (num.toInt()) {
                -2 -> "="
                -1 -> "-"
                else -> num.toString()
            }
            rem -= num * unit
            str = c + str
            place++
        }
        return str
    }

    private fun Long.pow(that: Long) = this.toDouble().pow(that.toDouble()).toLong()


    override fun problemOne(): String {
        return input.sum().toSnafu()
    }

    override fun problemTwo(): Number {
        return 1
    }
}
