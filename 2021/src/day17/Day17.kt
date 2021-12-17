package day17

import Day
import util.Point2
import util.by


class Day17 : Day {

    private val targetArea = (57..116) to (-148 downTo -198)
    //    private val targetArea = (20..30) to (-5 downTo -10)
    private val allPaths = (1..targetArea.first.last).flatMap { x ->
        (targetArea.second.last..Math.abs(targetArea.second.last)).mapNotNull { y ->
            (x by y).calculatePath(targetArea)
        }
    }

    override fun problemOne(): Int {
        return allPaths.maxBy { it.maxBy { it.y }!!.y }!!.maxBy { it.y }!!.y
    }

    override fun problemTwo(): Int {
        return allPaths.size
    }

    private fun Point2.calculatePath(target: Pair<IntProgression, IntProgression>): List<Point2>? {
        var velocity = this
        val points = mutableListOf(0 by 0)
        while (true) {
            points.add(points.last() + velocity)
            if (points.last().inRange(target))
                return points
            if ((velocity.x == 0 && points.last().x !in target.first)
                    || (points.last().y <= target.second.last))
                return null

            val newX = Math.max(velocity.x - 1, 0)
            val newY = velocity.y - 1
            velocity = newX by newY
        }
    }

    private fun Point2.inRange(target: Pair<IntProgression, IntProgression>): Boolean {
        return this.x in target.first && this.y in target.second
    }
}

