package day22

import Day
import util.Point2
import util.by

class Day22 : Day {
    data class Point3(val x: Int, val y: Int, val z: Int)
    data class Brick(val start: Point3, val end: Point3) {
//        fun downOne()
    }

    private val input = inputFile().readLines().map { line ->
        val (first, second) = line.split("~").map {
            val (x, y, z) = it.split(",").map(String::toInt)
            Point3(x, y, z)
        }
        Brick(first, second)
    }

    override fun problemOne(): Number {
        return 1
    }

    override fun problemTwo(): Number {
        return 1
    }
}

