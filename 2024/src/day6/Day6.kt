package day6

import Day
import util.Point2
import util.by

class Day6 : Day {
    private val input = inputFile().readLines()
    private val guardStart = input.indexOfFirst { it.contains("^") }.let { input[it].indexOf('^') by it }
    private fun inBounds(pt: Point2): Boolean = pt.y >= 0 && pt.y < input.size && pt.x < input[pt.y].length && pt.x >= 0

    private val U = 'U'
    private val R = 'R'
    private val D = 'D'
    private val L = 'L'


    override fun problemOne(): Number {
        val visited = mutableSetOf(guardStart)
        var dir = U
        var currentPoint = guardStart
        fun nextPoint(): Point2 {
            val nextPoint = when (dir) {
                U -> currentPoint + (0 by -1)
                R -> currentPoint + (1 by 0)
                L -> currentPoint + (-1 by 0)
                else -> currentPoint + (0 by 1)
            }
            if (!inBounds(nextPoint))
                return nextPoint
            if (input[nextPoint.y][nextPoint.x] == '#') {
                dir = when (dir) {
                    U -> R
                    R -> D
                    D -> L
                    else -> U
                }
                return nextPoint()
            }
            return nextPoint
        }

        while (inBounds(currentPoint)) {
            visited.add(currentPoint)
            currentPoint = nextPoint()
        }
        return visited.size
    }

    override fun problemTwo(): Number {
        var validLoopSpots = mutableSetOf<Point2>()
        val inputPermutations = input.mapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                if (cell == '.')
                    x by y
                else -1 by -1
            }
        }.flatten().filter { it != -1 by -1 }.map {
            it to input.mapIndexed { y, row ->
                row.mapIndexed { x, cell ->
                    if (x by y == it) '#' else cell
                }
            }
        }

        inputPermutations.count {
            val currentLoopSpot = it.first
            var dir = U
            var currentPoint = guardStart
            val visited = mutableSetOf((-1 by -1) to 'C')
            fun nextPoint(): Point2 {
                val nextPoint = when (dir) {
                    U -> currentPoint + (0 by -1)
                    R -> currentPoint + (1 by 0)
                    L -> currentPoint + (-1 by 0)
                    else -> currentPoint + (0 by 1)
                }
                if (!inBounds(nextPoint))
                    return nextPoint
                if (it.second[nextPoint.y][nextPoint.x] == '#') {
                    dir = when (dir) {
                        U -> R
                        R -> D
                        D -> L
                        else -> U
                    }
                    return nextPoint()
                }
                return nextPoint
            }
            while (inBounds(currentPoint)) {
                if (currentPoint to dir in visited) {
                    validLoopSpots.add(currentLoopSpot)
                    return@count true
                }
                visited.add(currentPoint to dir)
                currentPoint = nextPoint()
            }
            false
        }
        return validLoopSpots.size
    }


}
