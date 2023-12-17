package day16

import Day
import util.*
class Day16 : Day {
    private val UP = Point2(0, - 1)
    private val DOWN = Point2(0, 1)
    private val LEFT = Point2(-1, 0)
    private val RIGHT = Point2(1, 0)


    private val input = inputFile().readLines()
    private fun List<String>.get(p :Point2): Char? = this.getOrNull(p.y)?.getOrNull(p.x)
    private val DIRMAP = mapOf('U' to UP, 'L' to LEFT, 'D' to DOWN, 'R' to RIGHT)

    private fun energise(dir: Char, start: Point2, seenPoints: MutableSet<Point2>, seenCombos: MutableSet<Pair<Point2, Char>>) {
//        printPoints(seenPoints)
        val currentChar = input.get(start) ?: return
        if (seenCombos.contains(start to dir)) return
        seenCombos.add(start to dir)
        seenPoints.add(start)
        when (dir) {
            'U' -> when(currentChar) {
                '/' -> energise('R', DIRMAP['R']!! + start, seenPoints, seenCombos)
                '\\' -> energise('L', DIRMAP['L']!! + start, seenPoints, seenCombos)
                '-' -> {
                    energise('L', DIRMAP['L']!! + start, seenPoints, seenCombos)
                    energise('R', DIRMAP['R']!! + start, seenPoints, seenCombos)
                }
                else -> energise(dir, DIRMAP[dir]!! + start, seenPoints, seenCombos)
            }
            'D' -> when(currentChar) {
                '/' -> energise('L', DIRMAP['L']!! + start, seenPoints, seenCombos)
                '\\' -> energise('R', DIRMAP['R']!! + start, seenPoints, seenCombos)
                '-' -> {
                    energise('L', DIRMAP['L']!! + start, seenPoints, seenCombos)
                    energise('R', DIRMAP['R']!! + start, seenPoints, seenCombos)
                }
                else -> energise(dir, DIRMAP[dir]!! + start, seenPoints, seenCombos)
            }
            'R' -> when(currentChar) {
                '/' -> energise('U', DIRMAP['U']!! + start, seenPoints, seenCombos)
                '\\' -> energise('D', DIRMAP['D']!! + start, seenPoints, seenCombos)
                '|' -> {
                    energise('U', DIRMAP['U']!! + start, seenPoints, seenCombos)
                    energise('D', DIRMAP['D']!! + start, seenPoints, seenCombos)
                }
                else -> energise(dir, DIRMAP[dir]!! + start, seenPoints, seenCombos)
            }
            'L' -> when(currentChar) {
                '/' -> energise('D', DIRMAP['D']!! + start, seenPoints, seenCombos)
                '\\' -> energise('U', DIRMAP['U']!! + start, seenPoints, seenCombos)
                '|' -> {
                    energise('U', DIRMAP['U']!! + start, seenPoints, seenCombos)
                    energise('D', DIRMAP['D']!! + start, seenPoints, seenCombos)
                }
                else -> energise(dir, DIRMAP[dir]!! + start, seenPoints, seenCombos)
            }
        }
    }

    override fun problemOne(): Number {
        val seenPoints = mutableSetOf<Point2>()
        energise('R', 0 by 0, seenPoints, mutableSetOf())
        return seenPoints.size
    }

    override fun problemTwo(): Number {
        return (input.indices.flatMap {y ->
            listOf((0 by y) to 'R', (input.first().length-1 by y) to 'L')
        } + input.first().indices.flatMap { x ->
            listOf((x by 0) to 'D', (x by input.size-1) to 'U')
        }).map {pair ->
            val seenPoints = mutableSetOf<Point2>()
            energise(pair.second, pair.first, seenPoints, mutableSetOf())
            seenPoints.size
        }.max()!!
    }

    fun printPoints(set: Set<Point2>) {
        input.forEachIndexed { y, s ->
            s.indices.forEach{ x ->
                if (x by y in set) print("#") else print('.')
            }
            println()
        }
        println()
    }

}
