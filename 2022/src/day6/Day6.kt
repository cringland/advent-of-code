package day6

import Day

class Day6 : Day {

    private val input = inputFile().readText()

    override fun problemOne(): Int = input.findFirstUnique(4)
    override fun problemTwo(): Int = input.findFirstUnique(14)

    private fun String.findFirstUnique(len: Int): Int {
        for (i in 0 until this.length - len) {
            if (this.subSequence(i, i + len).toSet().size == len)
                return i + len
        }
        return -1
    }
}
