package day9

import Day
import java.io.File

class Day9 : Day {
    private val dirs = listOf<(Int, Int) -> Pair<Int, Int>>(
            { x, y -> Pair(x - 1, y) },
            { x, y -> Pair(x + 1, y) },
            { x, y -> Pair(x, y - 1) },
            { x, y -> Pair(x, y + 1) })

    private val input = File("src/day9/input").readLines()
            .map { line -> line.toCharArray().map(Character::getNumericValue) }

    private val mins = input.mapIndexed { x, it ->
        it.mapIndexedNotNull { y, value ->
            val adj = input.getAdjacentValues(x, y).map { it.third }
            if (value < adj.min()!!) Triple(x, y, input[x][y]) else null
        }
    }.flatten()

    override fun problemOne(): Int {
        return mins.map { it.third }.sum() + mins.size
    }

    override fun problemTwo(): Int {
        return mins.map { input.getBasinSize(it.first, it.second) }.sortedDescending().subList(0, 3).fold(1) { acc, i -> i * acc }
    }

    private fun List<List<Int>>.getBasinSize(x: Int, y: Int): Int {
        return this.getBasin(x, y, mutableListOf(x to y)).size
    }

    private fun List<List<Int>>.getBasin(x: Int, y: Int, currentBasin: MutableList<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val currentValue = input[x][y]
        dirs.map { it(x, y) }
                .filter { this.getOrNull(it.first, it.second) != null }
                .filterNot { currentBasin.contains(it) || this[it.first][it.second] == 9 }
                .filter { currentValue < this[it.first][it.second] }
                .onEach { currentBasin.add(it) }
                .forEach {
                    this.getBasin(it.first, it.second, currentBasin)
                }
        return currentBasin.toList()
    }

    private fun List<List<Int>>.getAdjacentValues(x: Int, y: Int): List<Triple<Int, Int, Int>> {
        return dirs.map { it(x, y) }
                .mapNotNull { this.getOrNull(it.first, it.second) }.map { Triple(x, y, it) }
    }

    private fun List<List<Int>>.getOrNull(x: Int, y: Int) =
            this.getOrNull(x)?.getOrNull(y)
}

