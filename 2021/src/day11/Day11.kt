package day11

import Day
import java.io.File

class Day11 : Day {
    private val matrix = inputFile().readLines().map { it.toCharArray().map(Character::getNumericValue) }

    override fun problemOne(): Int {
        val state = matrix.map { it.toMutableList() }.toMutableList()
        var flashes = 0
        repeat(100) {
            flashes += state.mutate()
        }
        return flashes
    }

    override fun problemTwo(): Int {
        val state = matrix.map { it.toMutableList() }.toMutableList()
        var i = 0
        while (true) {
            i++
            state.mutate()
            if (state.all { column -> column.all { it == 0 } })
                return i
        }
    }

    private fun MutableList<MutableList<Int>>.mutate(): Int {
        for (x in this.indices) {
            for (y in this[x].indices) {
                this[x][y] += 1
            }
        }
        val flashed: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var toFlash = this.mapIndexed { x, column ->
            column.mapIndexedNotNull { y, value ->
                if (value > 9) x to y else null
            }
        }.flatten()

        while (toFlash.isNotEmpty()) {
            flashed.addAll(toFlash)

            toFlash.flatMap { this.adjacentValues(it.first, it.second) }
                    .forEach { this[it.first][it.second]++ }

            toFlash = this.mapIndexed { x, column ->
                column.mapIndexedNotNull { y, value ->
                    if (value > 9) x to y else null
                }
            }.flatten().filterNot(flashed::contains)
        }
        flashed.forEach { this[it.first][it.second] = 0 }
        return flashed.size
    }


    private fun List<List<Int>>.adjacentValues(startX: Int, startY: Int): List<Triple<Int, Int, Int>> {
        return (startX - 1..startX + 1).flatMap { x ->
            (startY - 1..startY + 1).mapNotNull { y ->
                val value = getOrNull(x)?.getOrNull(y)
                if (value == null) null else Triple(x, y, value)
            }
        }
    }

    private fun List<List<Int>>.print() {
        for (row in this) {
            for (col in row) {
                print("$col ")
            }
            println()
        }
        println()
    }
}
