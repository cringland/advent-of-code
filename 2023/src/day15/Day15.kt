package day15

import Day
import util.Point2
import util.by
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs


class Day15 : Day {
    private val input = inputFile().readText().split(",")

    private fun String.hash() = this.fold(0) { currentValue, c ->
        val ascii = c.toByte().toInt()
        ((currentValue + ascii) * 17) % 256
    }

    override fun problemOne(): Number {
        return input.sumBy { it.hash() }
    }

    override fun problemTwo(): Number {
        val list = MutableList<MutableList<Pair<String, Int>>>(256) { mutableListOf() }
        input.forEach { str ->
            val label = str.takeWhile { it != '-' && it != '=' }
            val labelHash = label.hash()
            val indexInBox = list[labelHash].indexOfFirst { it.first == label }
            if (str.last() == '-') {
                if (indexInBox != -1) {
                    list[labelHash].removeAt(indexInBox)
                }
            } else {
                val focalLength = str.substringAfter('=').toInt()
                if (indexInBox == -1) {
                    list[labelHash].add(label to focalLength)
                } else {
                    list[labelHash][indexInBox] = (label to focalLength)
                }
            }
        }
        return list.mapIndexed { boxNum, box ->
            box.mapIndexed { slotNum, lens ->
                (1 + boxNum) * (1 + slotNum) * lens.second
            }.sum()
        }.sum()
    }
}
