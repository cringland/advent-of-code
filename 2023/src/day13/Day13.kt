package day13

import Day


class Day13 : Day {
    private val input = inputFile().readText().split("\n\n").map { it.lines() }
    private val rotatedInputs = input.map { it.rotated() }

    override fun problemOne(): Number {
        return solve { left, right -> left == right }
    }

    override fun problemTwo(): Number {
        return solve { left, right -> left.offByOne(right) }
    }

    private fun solve(comparer: ((List<String>, List<String>) -> Boolean)): Number {
        val rowVals = input.mapNotNull { grid ->
            grid.getSymmetric(comparer)
        }.sumBy { it.times(100) }
        val colVals = rotatedInputs.mapNotNull { grid ->
            grid.getSymmetric(comparer)
        }.sum()
        return colVals + rowVals
    }

    private fun List<String>.rotated(): List<String> =
        this.first().indices.map { x ->
            this.map { it[x] }.toString()
        }

    private fun List<String>.getSymmetric(comparer: ((List<String>, List<String>) -> Boolean)) =
        this.indices.drop(1).find { y ->
            val size = y.coerceAtMost(this.size - y)
            val left = this.subList(y - size, y)
            val right = this.drop(y).take(size).reversed()
            comparer(left, right)
        }


    private fun List<String>.offByOne(other: List<String>): Boolean {
        var oneOff = false
        this.forEachIndexed { y, thisLine ->
            thisLine.forEachIndexed { x, thisC ->
                if (thisC != other[y][x]) {
                    if (oneOff) return false else oneOff = true
                }
            }
        }
        return oneOff
    }
}

