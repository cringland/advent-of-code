package day13

import Day
import util.Point2
import util.by
import java.awt.Point

class Day13 : Day {
    //Button A: X+94, Y+34
    //Button B: X+22, Y+67
    //Prize: X=8400, Y=5400
    data class Result(val aPresses: Int, val bPresses: Int) {

    }

    data class Machine(private val s: String) {
        val matches = "(\\d+)".toRegex().findAll(s).map { it.groupValues[0].toInt() }.toList()
        val a = matches[0] by matches[1]
        val b = matches[2] by matches[3]
        val prize = matches[4] by matches[5]
        val ax = matches[0]
        val ay = matches[1]
        val bx = matches[2]
        val by = matches[3]
        val prizeX = matches[4]
        val prizeY = matches[5]

        fun canWinOne(): Boolean {
            fun check(currentPoint: Point2, aPress: Int, bPress: Int): Boolean {
                val withA = currentPoint + a
                if (withA == prize) return
            }
        }
    }

    private val input = inputFile().readText().split("\n\n")

    override fun problemOne(): Number {
        return 1
    }

    override fun problemTwo(): Number {
        return 1
    }
}
