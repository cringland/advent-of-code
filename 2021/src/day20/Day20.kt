package day20

import Day
import util.by
import java.io.File

class Day20 : Day {


    private val input = File("src/day20/input").readLines().filter { it.isNotEmpty() }
    private val algorithm = input.first().map { c -> c == '#' }
    private val image = input.drop(1).map { it.map { c -> c == '#' } }
    private var infinite = false

    override fun problemOne(): Int {
        return image.mutate().mutate().flatten().count { it }
    }

    private fun List<List<Boolean>>.mutate(): List<List<Boolean>> {
        return (-3..this.size + 3).map { x ->
            (-3..this.first().size + 3).map { y ->
                val index = (x by y).diagAdjacentWithSelf().map { point ->
                    this.getOrNull(point.x)?.getOrNull(point.y) ?: infinite
                }.joinToString("") { if (it) "1" else "0" }.toInt(2)
                algorithm[index]
            }
        }.also { infinite = if (infinite) algorithm.last() else algorithm.first() }
    }

    override fun problemTwo(): Int {
        var current = image
        repeat(50) {
            current = current.mutate()
        }
        return current.flatten().count { it }
    }
}

