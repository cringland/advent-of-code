package day13

import Day
import java.io.File

class Day13 : Day {
    private val input = File("src/day13/input").readText().split("\n\n")
    private val coords = input[0].split("\n").map { val (x, y) = it.split(","); x.toInt() to y.toInt() }.toSet()
    private val folds = input[1].split("\n").filter { it.isNotEmpty() }.map {
        val (xy, value) = it.split(" ")[2].split("=")
        if (xy == "y") 0 to value.toInt() else value.toInt() to 0
    }

    override fun problemOne(): Int {
        return coords.fold(folds.first()).size
    }


    override fun problemTwo(): String {
        val foldedPaper = folds.fold(coords) { acc, pair -> acc.fold(pair) }
        return foldedPaper.asString()
    }
}


private fun Set<Pair<Int, Int>>.asString(): String {
    var str = "\n"
    val minX = this.minBy { it.first }!!.first
    val maxX = this.maxBy { it.first }!!.first
    val minY = this.minBy { it.second }!!.second
    val maxY = this.maxBy { it.second }!!.second
    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            if (this.contains(x to y)) str += "#" else str += "."
        }
        str += "\n"
    }
    return str
}

private fun Set<Pair<Int, Int>>.fold(fold: Pair<Int, Int>): Set<Pair<Int, Int>> {
    return if (fold.second > 0) {
        this.mapNotNull {
            val newY = newValue(it.second, fold.second)
            if (newY == null) null else it.first to newY
        }.toSet()
    } else {
        this.mapNotNull {
            val newX = newValue(it.first, fold.first)
            if (newX == null) null else newX to it.second
        }.toSet()
    }
}

private fun newValue(j: Int, fold: Int): Int? {
    return when {
        j < fold -> j
        j == fold -> null
        else -> {
            j - (2 * (j - fold))
        }
    }
}


