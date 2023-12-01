package day15

import Day
import util.Point2
import util.by
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs


class Day15 : Day {
    //Sensor coords to Beacon Coords to distance
    private val input = inputFile().readLines().map {
        val nums = it.split("Sensor at x=", ", y=", ": closest beacon is at x=")
            .filter(String::isNotEmpty)
            .map(String::toInt)
        val sensor = nums[0] by nums[1]
        val beacon = nums[2] by nums[3]
        val distance = sensor.dist(beacon)
        Triple(sensor, beacon, distance)
    }

    private val notY = if (input.size < 20) 10 else 2000000 // Part 1 Y value
    private val range = if (input.size < 20) 0..20 else 0..4000000 // Part 2 range

    //Calculates manhattan distance
    private fun Point2.dist(other: Point2) = abs(this.x - other.x) + abs(this.y - other.y)
    private fun Point2.freq() = this.x.toLong() * 4000000 + this.y.toLong()

    override fun problemOne(): Number {
        //Calculate all positions with notY value that are definitely not sensors
        val beacons = input.map { it.second }.toSet()
        val s = input.flatMap {
            val sensor = it.first
            val distance = it.third
            val totalWidth = (distance * 2) + 1
            val widthAtY = totalWidth - (2 * (max(sensor.y, notY) - min(sensor.y, notY)))
            val widthAtYHalved = (widthAtY - 1) / 2
            (sensor.x - widthAtYHalved..sensor.x + widthAtYHalved).mapNotNull { x ->
                val p = x by notY
                if (p !in beacons) p else null
            }
        }.toSet()
        return s.size
    }

    override fun problemTwo(): Number {
        //Find beacon in range that isn't in any other sensors range
        input.forEach {
            val sensor = it.first
            val dist = it.third
            val topY = sensor.y - dist - 1
            val bottomY = sensor.y + dist + 1
            for (y in topY..bottomY) {
                val distanceToCenter = abs(sensor.y - y)
                val leftX = sensor.x - dist - 1 + distanceToCenter
                val rightX = sensor.x + dist + 1 - distanceToCenter
                val leftPoint = leftX by y
                val rightPoint = rightX by y
                if (leftX !in range || rightX !in range || y !in range) {
                    continue
                }
                val ans = listOf(leftPoint, rightPoint).filter {
                    input.none { i ->
                        i.third >= i.first.dist(leftPoint)
                    }
                }
                if (ans.isNotEmpty()) return ans.first().freq()
            }
        }
        return -1
    }
}
