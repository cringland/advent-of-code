package day14

import Day
import util.Point2
import util.by

class Day14 : Day {

    data class Robot(val pos: Point2, val vel: Point2)

    private val input = inputFile().readLines().map {
        val matches = "(\\d+)".toRegex().findAll(it).map { it.groupValues[0].toInt() }.toList()
        Robot(matches[0] by matches[1], matches[2] by matches[3])
    }

    override fun problemOne(): Number {
        return 1
    }

    override fun problemTwo(): Number {
        return 1
    }
}
