package day21

import Day
import util.Point2
import util.by

class Day21 : Day {
    private val input = inputFile().readLines()
    private val start = input.mapIndexedNotNull { y, str ->
        if (str.contains("S")) y by str.indexOf('S') else null
    }.first()

    override fun problemOne(): Number {
        var currentPoints = setOf(start)
        repeat(64) {
            currentPoints = currentPoints.flatMap {
                it.adjacent().filter { adj ->
                    (adj.y in input.indices) && (adj.x in input[adj.y].indices) && (input[adj.y][adj.x] != '#')
                }
            }.toSet()
        }
        return currentPoints.size
    }


    private fun Point2.remapPoint(): Char {
        val ysize = input.size
        val xsize = input.first().length
        val xrem = x % xsize
        val yrem = y % ysize
        val newY = if (yrem < 0) ysize + yrem else if (yrem == 0) 0 else yrem
        val newX = if (xrem < 0) xsize + xrem else if (xrem == 0) 0 else xrem
        return input[newY][newX]
    }

    override fun problemTwo(): Number {
        var currentPoints = setOf(start)
        val sizes = mutableListOf(currentPoints.size.toLong())
        val diffs = mutableListOf(currentPoints.size.toLong())
        repeat(500) {
            currentPoints = currentPoints.flatMap {point ->
                point.adjacent().filter { adj ->
                    adj.remapPoint() != '#'
                }
            }.toSet()
            diffs.add(currentPoints.size.toLong() - sizes.last())
            sizes.add(currentPoints.size.toLong())
            if (it > 495)  {
                currentPoints.printSteps()
                println()
                println()
                println()
            }
        }
//        println(sizes)
//        println(diffs)

        return currentPoints.size
    }

    fun Set<Point2>.printSteps() {
        val ymin = this.minBy { it.y }!!.y
        val xmin = this.minBy { it.x }!!.x
        val ymax = this.maxBy { it.y }!!.y
        val xmax = this.maxBy { it.x }!!.x
        (ymin..ymax).forEach { y ->
            (xmin..xmax).forEach { x ->
                if (x by y in this) print("0") else print((x by y).let { it.remapPoint() })
            }
            println()
        }
        println()
    }
}
