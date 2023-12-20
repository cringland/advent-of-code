package day18

import Day
import util.*
import kotlin.math.abs


private val dirMap =
    mapOf('U' to up, 'L' to left, 'D' to down, 'R' to right, '3' to up, '2' to left, '1' to down, '0' to right)

class Day18 : Day {
    data class Instruction(private val inputString: String) {
        private val dir = inputString.first()
        private val colour = inputString.split(" ")[2].replace("(", "").replace(")", "")
        val p1Num = inputString.split(" ")[1].toInt()
        val p1PointVal = dirMap[dir]!!.let { it.x * p1Num by it.y * p1Num }
        val p2Num = colour.drop(1).dropLast(1).toInt(16)
        val p2PointVal = dirMap[colour.last()]!!.let { it.x * p2Num by it.y * p2Num }
    }

    private val input = inputFile().readLines().map { Instruction(it) }

    override fun problemOne() = solve(Instruction::p1PointVal, Instruction::p1Num)

    override fun problemTwo() = solve(Instruction::p2PointVal, Instruction::p2Num)

    private fun solve(pointGetter: (Instruction) -> Point2, numGetter: (Instruction) -> Int): Long {
        val corners = mutableListOf(0 by 0)
        input.forEach { instruction ->
            corners += corners.last() + pointGetter(instruction)
        }
        val perimiter = (input.map { numGetter(it).toLong() }.sum() / 2) + 1
        val area = polygonArea(corners.drop(1))
        return perimiter + area
    }

    //https://www.geeksforgeeks.org/area-of-a-polygon-with-given-n-ordered-vertices/
    fun polygonArea(points: List<Point2>): Long {
        var area = 0L
        var j = points.size - 1
        for (i in points.indices) {
            val pointI = points[i]
            val pointJ = points[j]
            area += (pointJ.x.toLong() + pointI.x.toLong()) * (pointJ.y.toLong() - pointI.y.toLong())
            j = i
        }
        return abs(area / 2)
    }

    fun List<Point2>.printPoints() {
        (0..this.maxBy { it.y }!!.y).forEach { y ->
            (0..this.maxBy { it.x }!!.x).forEach { x ->
                if (x by y in this) print("#") else print(".")
            }
            println()
        }
    }
}
