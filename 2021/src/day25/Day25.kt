package day25

import Day
import java.io.File
import java.util.*

class Day25 : Day {

    val input = File("src/day25/input").readLines().filter { it.isNotEmpty() }
//            .map { it.toCharArray().toList() }

    override fun problemOne(): Int {
        var old: List<String>
        var new = input
        var i = 0
        do {
            i++
            old = new
            new = old.mutate()
        } while (old != new)
        return i
    }

    override fun problemTwo(): Long {
        return Random().nextLong()
    }

    private fun String.replace(i: Int, c: Char): String = this.replaceRange(i, i + 1, c.toString())

    private fun List<String>.mutate(): List<String> {
        val eastsMoved = this.map {
            var newStr = it
            for ((index, c) in it.withIndex()) {
                if (c == '>') {
                    val nextIndex = if (index + 1 == it.length) 0 else index + 1
                    if (it[nextIndex] == '.') {
                        newStr = newStr.replace(index, '.').replace(nextIndex, '>')
                    }
                }
            }
            newStr
        }

        val southsMoved = eastsMoved.toMutableList()
        eastsMoved.forEachIndexed { x, str ->
            str.forEachIndexed { y, c ->
                if (c == 'v') {
                    val nextIndex = if (x + 1 == eastsMoved.size) 0 else x + 1
                    if (eastsMoved[nextIndex][y] == '.') {
                        southsMoved[x] = southsMoved[x].replace(y, '.')
                        southsMoved[nextIndex] = southsMoved[nextIndex].replace(y, 'v')
                    }
                }
            }
        }
        return southsMoved
    }

    private fun List<String>.print() {
        for (row in this) {
            println(row)
        }
        println()
    }
}
