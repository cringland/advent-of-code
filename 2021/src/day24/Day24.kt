package day24

import Day
import java.io.File
import java.util.*

class Day24 : Day {
    val input = File("src/day24/input").readLines().filter { it.isNotEmpty() }.chunked(18)

    fun run(part2: Boolean): Long {
        val result = MutableList(14) { -1 }
        val buffer = ArrayDeque<Pair<Int, Int>>()
        fun List<String>.lastOf(command: String) = last { it.startsWith(command) }.split(" ").last().toInt()
        val best = if (part2) 1 else 9
        input.forEachIndexed { index, instructions ->
            if ("div z 26" in instructions) {
                val offset = instructions.lastOf("add x")
                val (lastIndex, lastOffset) = buffer.removeFirst()
                val difference = offset + lastOffset
                if (difference >= 0) {
                    result[lastIndex] = if (part2) best else best - difference
                    result[index] = if (part2) best + difference else best
                } else {
                    result[lastIndex] = if (part2) best - difference else best
                    result[index] = if (part2) best else best + difference
                }
            } else buffer.addFirst(index to instructions.lastOf("add y"))
        }

        return result.joinToString("").toLong()
    }

    override fun problemOne(): Long {
        return run(false)
    }


    override fun problemTwo(): Long {
        return run(true)
    }

}

