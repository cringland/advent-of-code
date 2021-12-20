package day20

import Day
import util.by
import java.io.File

class Day20 : Day {


    private val input = File("src/day20/input").readLines().filter { it.isNotEmpty() }
    private val algorithm = input.first().map { c -> c == '#' }
    private val image = input.drop(1).map { it.map { c -> c == '#' } }

    override fun problemOne(): Int {
        return image.mutate().mutate().flatten().count { it }
    }

    private fun List<List<Boolean>>.mutate(): List<List<Boolean>> {
        return (-1..this.size + 1).map { x ->
            (-1..this.first().size + 1).map { y ->
                val index = (x by y).diagAdjacentWithSelf().map { point ->
                    this.getOrNull(point.x)?.getOrNull(point.y) ?: false
                }.joinToString("") { if (it) "1" else "0" }.toInt(2)
                algorithm[index]
            }
        }
    }

    override fun problemTwo(): Int {
        return 1
    }
}

