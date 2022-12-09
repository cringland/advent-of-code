package day9

import Day

class Day9 : Day {
    private val input = inputFile().readLines().filter(String::isNotEmpty).flatMap {
        val l = it.split(" ")
        val num = l[1].toInt()
        List(num) {
            when (l[0]) {
                "R" -> 1 by 0
                "U" -> 0 by 1
                "L" -> -1 by 0
                else -> 0 by 0 - 1
            }
        }

    }

    override fun problemOne(): Int = solve(2)
    override fun problemTwo(): Number = solve(10)

    private fun solve(ropeSize: Int): Int {
        var rope = List(ropeSize) { 0 by 0 }
        var lastHead = rope.first()
        return input.fold(mutableSetOf(0 by 0)) { acc, change ->
            rope = rope.mapIndexed { i, p ->
                val isFirst = i == 0
                val isLast = i == rope.size - 1
                if (isFirst) {
                    lastHead = p + change
                    lastHead
                } else {
                    val diff = lastHead - p
                    // There must be a formula here somewhere
                    val x = if (diff.x == 2 || (diff.x == 1 && (diff.y > 1 || diff.y < -1))) 1
                    else if (diff.x == -2 || (diff.x == -1 && (diff.y > 1 || diff.y < -1))) -1
                    else 0

                    val y = if (diff.y == 2 || (diff.y == 1 && (diff.x > 1 || diff.x < -1))) 1
                    else if (diff.y == -2 || (diff.y == -1 && (diff.x > 1 || diff.x < -1))) -1
                    else 0
                    val new = p + (x by y)
                    if (isLast) {
                        acc.add(new)
                    }
                    lastHead = new
                    new
                }
            }
            acc
        }.size
    }
}

data class Point2(val x: Int, val y: Int) {
    operator fun plus(that: Point2): Point2 = (this.x + that.x) by (this.y + that.y)
    operator fun minus(that: Point2): Point2 = (this.x - that.x) by (this.y - that.y)
}

infix fun Int.by(that: Int): Point2 = Point2(this, that)
