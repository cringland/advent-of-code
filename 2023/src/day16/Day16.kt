package day16

import Day
import util.*

class Day16 : Day {
    private val dirMap = mapOf('U' to up, 'L' to left, 'D' to down, 'R' to right)


    private val input = inputFile().readLines()
    private fun List<String>.get(p: Point2): Char? = this.getOrNull(p.y)?.getOrNull(p.x)

    private fun energise(
        dir: Char,
        start: Point2,
        seenCombos: MutableSet<Pair<Point2, Char>>
    ): Int {
        val currentChar = input.get(start) ?: return 0
        if (seenCombos.contains(start to dir)) return 0
        seenCombos.add(start to dir)

        val nextPoints = when (dir) {
            'U' -> when (currentChar) {
                '/' -> listOf('R' to dirMap['R']!! + start)
                '\\' -> listOf('L' to dirMap['L']!! + start)
                '-' -> listOf('L' to dirMap['L']!! + start, 'R' to dirMap['R']!! + start)
                else -> listOf(dir to dirMap[dir]!! + start)
            }

            'D' -> when (currentChar) {
                '/' -> listOf('L' to dirMap['L']!! + start)
                '\\' -> listOf('R' to dirMap['R']!! + start)
                '-' -> listOf('L' to dirMap['L']!! + start, 'R' to dirMap['R']!! + start)
                else -> listOf(dir to dirMap[dir]!! + start)
            }

            'R' -> when (currentChar) {
                '/' -> listOf('U' to dirMap['U']!! + start)
                '\\' -> listOf('D' to dirMap['D']!! + start)
                '|' -> listOf('U' to dirMap['U']!! + start, 'D' to dirMap['D']!! + start)
                else -> listOf(dir to dirMap[dir]!! + start)
            }

            else -> when (currentChar) {
                '/' -> listOf('D' to dirMap['D']!! + start)
                '\\' -> listOf('U' to dirMap['U']!! + start)
                '|' -> listOf('U' to dirMap['U']!! + start, 'D' to dirMap['D']!! + start)
                else -> listOf(dir to dirMap[dir]!! + start)
            }
        }
        return 1 + nextPoints.sumBy { energise(it.first, it.second, seenCombos) }
    }

    override fun problemOne(): Number {
        val seenCombos = mutableSetOf<Pair<Point2, Char>>()
        energise('R', 0 by 0, seenCombos)
        return seenCombos.map { it.first }.toSet().size
    }

    override fun problemTwo(): Number {
        return (input.indices.flatMap { y ->
            listOf((0 by y) to 'R', (input.first().length - 1 by y) to 'L')
        } + input.first().indices.flatMap { x ->
            listOf((x by 0) to 'D', (x by input.size - 1) to 'U')
        }).map { pair ->
            val seenCombos = mutableSetOf<Pair<Point2, Char>>()
            energise(pair.second, pair.first, seenCombos)
            seenCombos.map { it.first }.toSet().size
        }.max()!!
    }

    fun printPoints(set: Set<Point2>) {
        input.forEachIndexed { y, s ->
            s.indices.forEach { x ->
                if (x by y in set) print("#") else print('.')
            }
            println()
        }
        println()
    }

}
