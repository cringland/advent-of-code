package day5

import java.io.File
import Day

class Day5 : Day {
    private val input = File("src/day5/input").readLines().filter(String::isNotEmpty).map(::Line)

    override fun problemOne(): Int {
        return input.filter { !it.isDiagonal }.greaterThan2Count()
    }

    override fun problemTwo(): Int {
        return input.greaterThan2Count()
    }

    private fun List<Line>.greaterThan2Count(): Int  {
        return flatMap { it.points }.groupingBy { it }.eachCount().filter { it.value >= 2 }.count()
    }

    class Line(str: String) {
        private val nums = str.split(" -> ").flatMap { it.split(",").map { it.toInt() } }
        val x1 = nums[0]
        val y1 = nums[1]
        val x2 = nums[2]
        val y2 = nums[3]

        private val isVertical = x1 == x2
        private val isHorizontal = y1 == y2
        val isDiagonal = !isVertical && !isHorizontal
        val points = if (!isDiagonal) {
            val xRange = if (x1 > x2) (x1 downTo x2) else (x1..x2)
            val yRange = if (y1 > y2) (y1 downTo y2) else (y1..y2)
            xRange.flatMap { x ->
                yRange.map { y ->
                    x to y
                }
            }
        } else {
            if (x1 > x2) {
                val rise = (y1 - y2) / (x1 - x2)
                (x1 downTo x2).mapIndexed { i, it -> it to y1 - (rise * i) }
            } else {
                val rise = (y2 - y1) / (x2 - x1)
                (x1..x2).mapIndexed { i, it -> it to y1 + (rise * i) }
            }
        }
    }
}
