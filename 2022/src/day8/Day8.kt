package day8

import Day
import kotlin.math.max

class Day8 : Day {

    private val input = inputFile().readLines().map { it.map(Char::toString).map(String::toInt) }
    private val HEIGHT = input.size
    private val WIDTH = input[0].size

    override fun problemOne(): Int {
        fun getPoints(
            range1: IntProgression,
            range2: IntProgression,
            func: (Int, Int) -> Pair<Int, Int>
        ): Set<Pair<Int, Int>> {
            val points = mutableSetOf<Pair<Int, Int>>()
            for (i in range1) {
                val startP = func(i, range2.first)
                val others = mutableListOf(input[startP])
                points.add(startP)
                for (j in range2) {
                    val p = func(i, j)
                    if (input[p.first][p.second] > (others.max() ?: 0)) {
                        points.add(p.first to p.second)
                        others.add(input[p.first][p.second])
                    }
                }
            }
            return points
        }

        val allPoints = getPoints(0 until HEIGHT, 0 until WIDTH - 1) { y, x -> y to x } +
                getPoints(0 until HEIGHT, WIDTH - 1 downTo 1) { y, x -> y to x } +
                getPoints(0 until WIDTH, 0 until HEIGHT - 1) { x, y -> y to x } +
                getPoints(0 until WIDTH, HEIGHT - 1 downTo 1) { x, y -> y to x }
        return allPoints.size
    }


    override fun problemTwo(): Number {
        var bestScore = 0
        for (x in 1 until WIDTH - 1) {
            for (y in 1 until HEIGHT - 1) {
                val current = input[y][x]
                fun getDistance(range: IntProgression, func: (Int) -> (Int)): Int =
                    range.takeWhileInclusive { i -> current > func(i) }.size

                val score = listOf(
                    getDistance((x - 1 downTo 0)) { x2 -> input[y][x2] },// left
                    getDistance((x + 1 until WIDTH)) { x2 -> input[y][x2] }, // right
                    getDistance((y - 1 downTo 0)) { y2 -> input[y2][x] }, // up
                    getDistance((y + 1 until HEIGHT)) { y2 -> input[y2][x] } // down
                ).fold(1) { acc, i -> acc * i }
                if (bestScore < score)
                    bestScore = score
            }
        }
        return bestScore
    }

    operator fun List<List<Int>>.get(p: Pair<Int, Int>): Int = this[p.first][p.second]

    private fun <T> Iterable<T>.takeWhileInclusive(pred: (T) -> Boolean): List<T> {
        var shouldContinue = true
        return takeWhile {
            val result = shouldContinue
            shouldContinue = pred(it)
            result
        }
    }
}
