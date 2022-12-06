package day6

import Day
import java.io.File

class Day6 : Day {

    private val input = File("src/day6/input").readText()

    override fun problemOne(): Int {
        return input.findFirstUnique(4)
    }

    override fun problemTwo(): Int {
        return input.findFirstUnique(14)
    }

    private fun String.findFirstUnique(len: Int): Int {
        for (i in 0 until this.length - len) {
            if (this.subSequence(i, i + len).toSet().size == len)
                return i + len
        }
        return -1
    }
}
